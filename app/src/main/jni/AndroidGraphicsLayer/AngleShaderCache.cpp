#include "AngleShaderCache.h"
#include "SDL3/SDL.h"
#include <EGL/egl.h>
#include <EGL/eglext.h>
#include <cctype>
#include <chrono>
#include <condition_variable>
#include <cstdint>
#include <cstdio>
#include <cstring>
#include <deque>
#include <memory>
#include <mutex>
#include <string>
#include <thread>
#include <unordered_map>
#include <vector>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

static std::mutex g_mutex;
static bool g_installed = false;
static std::string g_base_dir;
static std::string g_namespace_dir;
static bool g_enable_angle = false;

static std::string sanitize_namespace(const char *name) {
    std::string out;
    if (!name || !*name) {
        return "default";
    }

    for (const unsigned char c: std::string(name)) {
        if (std::isalnum(c) || c == '_' || c == '-' || c == '.') {
            out.push_back(static_cast<char>(c));
        } else {
            out.push_back('_');
        }
    }

    if (out.empty()) {
        out = "default";
    }
    return out;
}

static bool ensure_dir(const std::string &path) {
    if (path.empty()) {
        return false;
    }

    if (access(path.c_str(), F_OK) == 0) {
        return true;
    }

    if (mkdir(path.c_str(), 0777) == 0) {
        return true;
    }

    if (errno == EEXIST) {
        return true;
    }

    return false;
}

static bool ensure_parent_tree(const std::string &dir) {
    if (dir.empty() || dir == "/") {
        return true;
    }

    std::string cur;
    cur.reserve(dir.size());

    for (char i : dir) {
        cur.push_back(i);
        if (i == '/' && cur.size() > 1) {
            if (!ensure_dir(cur)) {
                return false;
            }
        }
    }

    return ensure_dir(dir);
}

static uint64_t fnv1a64(const void *data, size_t size, uint64_t seed) {
    const uint8_t *p = static_cast<const uint8_t *>(data);
    uint64_t h = seed;

    for (size_t i = 0; i < size; ++i) {
        h ^= p[i];
        h *= 1099511628211ULL;
    }

    return h;
}

static std::string hex64(uint64_t v) {
    static const char *kHex = "0123456789abcdef";
    char buf[16];
    for (int i = 15; i >= 0; --i) {
        buf[i] = kHex[v & 0xFULL];
        v >>= 4;
    }
    return std::string(buf, 16);
}

static std::string blob_filename_for_key(const void *key, EGLsizeiANDROID keySize) {
    const uint64_t h1 = fnv1a64(key, static_cast<size_t>(keySize),
                                1469598103934665603ULL);
    const uint64_t h2 = fnv1a64(key, static_cast<size_t>(keySize),
                                1099511628211ULL ^ 0x9e3779b97f4a7c15ULL);

    std::string name;
    name.reserve(64);
    name += "b_";
    name += hex64(static_cast<uint64_t>(keySize));
    name += "_";
    name += hex64(h1);
    name += hex64(h2);
    name += ".bin";
    return name;
}

static std::string blob_path_for_key(const void *key, EGLsizeiANDROID keySize) {
    return g_namespace_dir + "/" + blob_filename_for_key(key, keySize);
}

static bool write_file_atomic(const std::string &path, const void *data, size_t size) {
    const std::string tmp = path + ".tmp";

    std::FILE *fp = std::fopen(tmp.c_str(), "wb");
    if (!fp) {
        return false;
    }

    const bool ok = (size == 0) || (std::fwrite(data, 1, size, fp) == size);
    std::fflush(fp);
    std::fclose(fp);

    if (!ok) {
        std::remove(tmp.c_str());
        return false;
    }

    std::remove(path.c_str());
    if (std::rename(tmp.c_str(), path.c_str()) != 0) {
        std::remove(tmp.c_str());
        return false;
    }

    return true;
}

static bool read_file(const std::string &path, std::vector<uint8_t> &out) {
    out.clear();

    std::FILE *fp = std::fopen(path.c_str(), "rb");
    if (!fp) {
        return false;
    }

    if (std::fseek(fp, 0, SEEK_END) != 0) {
        std::fclose(fp);
        return false;
    }

    long sz = std::ftell(fp);
    if (sz < 0) {
        std::fclose(fp);
        return false;
    }

    if (std::fseek(fp, 0, SEEK_SET) != 0) {
        std::fclose(fp);
        return false;
    }

    out.resize(static_cast<size_t>(sz));
    if (!out.empty()) {
        if (std::fread(out.data(), 1, out.size(), fp) != out.size()) {
            std::fclose(fp);
            out.clear();
            return false;
        }
    }

    std::fclose(fp);
    return true;
}

/*
 * ANGLE calls the blob callbacks on the thread that renders: when it links a program, and every
 * so often to save its Vulkan pipeline cache - all of it, megabytes split into blobs, whenever new
 * pipelines were made since the last time. Writing those files right there (open, write, flush,
 * rename each) stalled the frame for as long as the flash took: a freeze every few seconds while a
 * game kept meeting new render states. The callback only copies the blob into a queue now and one
 * background thread writes them. A blob not written yet is served from memory by get_blob_cb.
 */
using BlobData = std::shared_ptr<const std::vector<uint8_t>>;

static std::mutex g_write_mutex;
// Serializes background and overflow writes so a stale queued blob cannot overwrite a newer one.
static std::mutex g_file_write_mutex;
static std::condition_variable g_write_cv;      // the writer waits for blobs, a shutdown for the queue to empty
static std::deque<std::string> g_write_queue;   // paths, as set (a path set again may come twice)
static std::unordered_map<std::string, BlobData> g_pending;     // the newest data of each path not written yet
static size_t g_pending_bytes = 0;
static bool g_writer_busy = false;
static bool g_writer_started = false;

// More than this waiting (a slow flash and a huge pipeline cache): written right away as before.
// Every blob waiting here is a copy held in RAM - ANGLE hands over its whole compressed pipeline
// cache again each time new pipelines were made - so the queue stays small.
static const size_t kMaxPendingBytes = 64u * 1024u * 1024u;

static void blob_writer_main() {
    size_t blobs = 0;
    size_t bytes = 0;
    auto burstStart = std::chrono::steady_clock::now();

    for (;;) {
        std::string path;
        BlobData data;
        {
            std::unique_lock<std::mutex> lock(g_write_mutex);
            g_writer_busy = false;
            if (g_write_queue.empty()) {
                g_write_cv.notify_all();
                if (blobs) {
                    // one line per burst: lines in step with the freezes would say ANGLE was saving its caches then
                    const auto ms = std::chrono::duration_cast<std::chrono::milliseconds>(
                            std::chrono::steady_clock::now() - burstStart).count();
                    SDL_LogInfo(SDL_LOG_CATEGORY_APPLICATION,
                                "ANGLE blobcache: %zu blobs, %zu KiB written in the background (%lld ms)",
                                blobs, bytes / 1024, static_cast<long long>(ms));
                    blobs = 0;
                    bytes = 0;
                }
                g_write_cv.wait(lock, [] { return !g_write_queue.empty(); });
                burstStart = std::chrono::steady_clock::now();
            }
            path = std::move(g_write_queue.front());
            g_write_queue.pop_front();
            const auto it = g_pending.find(path);
            if (it == g_pending.end()) {
                continue;   // written already, by its earlier entry
            }
            data = it->second;
            g_writer_busy = true;
        }

        bool ok = true;
        bool current = false;
        {
            // A synchronous overflow write may have replaced or removed this pending blob.
            // Serialize the file operation and re-check after taking the I/O lock.
            std::lock_guard<std::mutex> fileLock(g_file_write_mutex);
            {
                std::lock_guard<std::mutex> lock(g_write_mutex);
                const auto it = g_pending.find(path);
                current = it != g_pending.end() && it->second == data;
            }
            if (current) {
                ok = write_file_atomic(path, data->data(), data->size());
            }
        }

        if (!current) {
            continue;   // superseded or written synchronously while this item was queued
        }
        if (!ok) {
            SDL_LogWarn(SDL_LOG_CATEGORY_APPLICATION,
                        "ANGLE blobcache: failed to write %s", path.c_str());
        }
        blobs++;
        bytes += data->size();

        std::lock_guard<std::mutex> lock(g_write_mutex);
        const auto it = g_pending.find(path);
        if (it != g_pending.end() && it->second == data) {
            // not set again while it was written (else its newer data is still queued)
            g_pending_bytes -= data->size();
            g_pending.erase(it);
        }
    }
}

static void set_blob_cb(const void *key, EGLsizeiANDROID keySize,
                 const void *value, EGLsizeiANDROID valueSize) {
    if (!key || keySize <= 0 || !value || valueSize <= 0) {
        return;
    }

    std::string path;
    {
        std::lock_guard<std::mutex> lock(g_mutex);
        if (g_namespace_dir.empty()) {
            return;
        }
        path = blob_path_for_key(key, keySize);
    }

    const auto *bytes = static_cast<const uint8_t *>(value);
    BlobData data = std::make_shared<const std::vector<uint8_t>>(bytes, bytes + valueSize);

    {
        std::lock_guard<std::mutex> lock(g_write_mutex);
        const auto pendingIt = g_pending.find(path);
        const size_t oldPendingSize =
                pendingIt == g_pending.end() ? 0 : pendingIt->second->size();
        const size_t bytesWithoutThisPath = g_pending_bytes - oldPendingSize;

        if (g_writer_started &&
            data->size() <= kMaxPendingBytes &&
            bytesWithoutThisPath <= kMaxPendingBytes - data->size()) {
            auto &slot = g_pending[path];
            slot = data;
            g_pending_bytes = bytesWithoutThisPath + data->size();
            g_write_queue.push_back(path);
            g_write_cv.notify_all();
            return;
        }
    }

    {
        // Remove any older queued copy before writing the newest blob synchronously.
        // The I/O mutex makes this safe even if the writer has already taken that blob.
        std::lock_guard<std::mutex> fileLock(g_file_write_mutex);
        {
            std::lock_guard<std::mutex> lock(g_write_mutex);
            const auto it = g_pending.find(path);
            if (it != g_pending.end()) {
                g_pending_bytes -= it->second->size();
                g_pending.erase(it);
            }
        }

        if (!write_file_atomic(path, data->data(), data->size())) {
            SDL_LogWarn(SDL_LOG_CATEGORY_APPLICATION,
                        "ANGLE blobcache: failed to write %s", path.c_str());
        }
    }
}

static EGLsizeiANDROID get_blob_cb(const void *key, EGLsizeiANDROID keySize,
                            void *value, EGLsizeiANDROID valueSize) {
    if (!key || keySize <= 0) {
        return 0;
    }

    std::string path;
    {
        std::lock_guard<std::mutex> lock(g_mutex);
        if (g_namespace_dir.empty()) {
            return 0;
        }
        path = blob_path_for_key(key, keySize);
    }

    // set but not written yet: from memory
    BlobData pending;
    {
        std::lock_guard<std::mutex> lock(g_write_mutex);
        const auto it = g_pending.find(path);
        if (it != g_pending.end()) {
            pending = it->second;
        }
    }

    std::vector<uint8_t> data;
    if (pending) {
        data = *pending;
    } else if (!read_file(path, data)) {
        return 0;
    }

    const EGLsizeiANDROID needed = static_cast<EGLsizeiANDROID>(data.size());

    if (!value) {
        return needed;
    }

    if (valueSize < needed) {
        return needed;
    }

    if (!data.empty()) {
        std::memcpy(value, data.data(), data.size());
    }

    return needed;
}

extern "C" {
__attribute__((used)) __attribute__((visibility("default")))
void setAngleState(bool enableAngle){
    g_enable_angle = enableAngle;
}

__attribute__((used)) __attribute__((visibility("default")))
bool angle_blobcache_install(const char *namespace_name) {
    if (!g_enable_angle){
        return false;
    }
    std::lock_guard<std::mutex> lock(g_mutex);

    if (g_installed) {
        return true;
    }

    const char *cache = SDL_GetAndroidCachePath();
    if (!cache || !*cache) {
        SDL_LogWarn(SDL_LOG_CATEGORY_APPLICATION,
                    "SDL_GetAndroidCachePath() failed: %s", SDL_GetError());
        return false;
    }

    g_base_dir = std::string(cache) + "/angle_blobcache";
    g_namespace_dir = g_base_dir + "/" + sanitize_namespace(namespace_name);

    if (!ensure_parent_tree(g_namespace_dir)) {
        SDL_LogWarn(SDL_LOG_CATEGORY_APPLICATION,
                    "ANGLE blobcache: failed to create cache dir %s",
                    g_namespace_dir.c_str());
        return false;
    }

    const SDL_EGLDisplay sdl_display = SDL_EGL_GetCurrentDisplay();
    if (!sdl_display) {
        SDL_LogWarn(SDL_LOG_CATEGORY_APPLICATION,
                    "ANGLE blobcache: no current EGL display");
        return false;
    }

    using PFN_eglSetBlobCacheFuncsANDROID =
            void (*)(EGLDisplay, EGLSetBlobFuncANDROID, EGLGetBlobFuncANDROID);

    auto func = reinterpret_cast<PFN_eglSetBlobCacheFuncsANDROID>(
            SDL_EGL_GetProcAddress("eglSetBlobCacheFuncsANDROID"));

    if (!func) {
        SDL_LogWarn(SDL_LOG_CATEGORY_APPLICATION,
                    "ANGLE blobcache: eglSetBlobCacheFuncsANDROID not found");
        return false;
    }

    {
        std::lock_guard<std::mutex> writeLock(g_write_mutex);
        if (!g_writer_started) {
            // Detached: it outlives every engine of the process, and a joinable std::thread left at
            // exit would terminate the process
            std::thread(blob_writer_main).detach();
            g_writer_started = true;
        }
    }

    func(reinterpret_cast<EGLDisplay>(sdl_display),
         &set_blob_cb,
         &get_blob_cb);

    g_installed = true;
    SDL_LogInfo(SDL_LOG_CATEGORY_APPLICATION,
                "ANGLE blobcache installed at %s", g_namespace_dir.c_str());
    return true;
}

__attribute__((used)) __attribute__((visibility("default")))
void angle_blobcache_shutdown(void) {
    if (!g_enable_angle){
        return;
    }
    {
        // the blobs still queued are written first (a cache saved for the next start), two seconds at most
        std::unique_lock<std::mutex> writeLock(g_write_mutex);
        g_write_cv.wait_for(writeLock, std::chrono::seconds(2),
                            [] { return g_write_queue.empty() && !g_writer_busy; });
    }
    std::lock_guard<std::mutex> lock(g_mutex);
    g_installed = false;
    g_namespace_dir.clear();
    g_base_dir.clear();
}
}