//
// Created by sandstranger on 21.06.2026.
//
#include "SwappyController.h"
#include "swappy/swappyGL.h"
#include <swappy/swappyGL_extra.h>
#include <swappy/swappy_common.h>
#include "SDL3/SDL.h"
#include "SDL3/SDL_egl.h"
#include "jni.h"
#include <android/log.h>
#include <atomic>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <mutex>

#define NS_PER_MS 1000000ULL

using PFNEGLGETCURRENTSURFACEPROC = EGLSurface (*)(EGLint);
using PFNEGLGETERRORPROC = EGLint (*)();

static std::atomic<bool> swappyWasEnabled = false;
static std::atomic<bool> g_swappyDestroyed = false;
static std::mutex g_swappyMutex;

static inline PFNEGLGETCURRENTSURFACEPROC GetEGLGetCurrentSurface() {
    static auto p_eglGetCurrentSurface = []() -> PFNEGLGETCURRENTSURFACEPROC {
        return reinterpret_cast<PFNEGLGETCURRENTSURFACEPROC>(
                SDL_EGL_GetProcAddress("eglGetCurrentSurface")
        );
    }();
    return p_eglGetCurrentSurface;
}

static inline PFNEGLGETERRORPROC GetEGLGetError() {
    static auto p_eglGetError = []() -> PFNEGLGETERRORPROC {
        return reinterpret_cast<PFNEGLGETERRORPROC>(
                SDL_EGL_GetProcAddress("eglGetError")
        );
    }();
    return p_eglGetError;
}

// Do not cache SDL_Window*: SDL may recreate its native surface during lifecycle
// and orientation changes. Resolve the current SDL window when recovery is needed.
static inline SDL_Window* GetSDLWindow() {
    int count = 0;
    SDL_Window** windows = SDL_GetWindows(&count);

    SDL_Window* result = nullptr;
    if (windows != nullptr && count > 0) {
        result = windows[0];
    }

    SDL_free(windows);
    return result;
}

// Graphics debuggers and capture tools are GLES layers or interceptors loaded into the process;
// their libraries show up in its memory map.
static bool DetectGraphicsDebugger() {
    static const char* const markers[] = {
        "renderdoc",        // RenderDoc: libVkLayer_GLES_RenderDoc.so, librenderdoc.so
        "libgapii",         // Android GPU Inspector / GAPID
        "/libaga.so",       // Arm Graphics Analyzer (Arm Mobile Studio)
        "/libmgd.so",       // Mali Graphics Debugger
    };

    FILE* maps = fopen("/proc/self/maps", "re");
    if (maps == nullptr) {
        return false;
    }

    char line[1024];
    const char* found = nullptr;
    while (found == nullptr && fgets(line, sizeof(line), maps) != nullptr) {
        for (const char* marker : markers) {
            if (strcasestr(line, marker) != nullptr) {
                found = marker;
                break;
            }
        }
    }
    fclose(maps);

    if (found != nullptr) {
        __android_log_print(ANDROID_LOG_INFO, "AndroidGraphicsLayer",
                            "Graphics debugger in the process (%s): Swappy is bypassed, plain eglSwapBuffers", found);
    }
    return found != nullptr;
}

// Engines that quit with exit() straight from their game loop (fteqw's Sys_Quit/Sys_Error and others)
// never reach their DestroySwappy() call. exit() then runs static destructors, Swappy's trace singleton
// among them, while its choreographer filter threads keep running: the next vsync tick dereferences the
// destroyed singleton (SIGSEGV in ChoreographerFilter::threadMain). Exit handlers run in reverse order of
// registration, so registering this after Swappy is configured (which creates that singleton) stops
// Swappy's threads before anything they use is torn down.
static void DestroySwappyAtExit() {
    DestroySwappy();
}

extern "C" {
__attribute__((used)) __attribute__((visibility("default")))
JNIEXPORT void JNICALL
Java_com_mobilerpgpack_phone_utils_SwappyJNILayer_initSwappyGL(
        JNIEnv *env,
        jobject thiz,
        jobject activity,
        jboolean enableAutoPipelineMode,
        jboolean enableAutoSwap,
        jboolean enableFramePacing,
        jboolean enableBlockingWait,
        jint bufferStuffingFixWait,
        jlong fenceTimeoutMS,
        jint targetFPS) {
    std::lock_guard<std::mutex> lock(g_swappyMutex);

    // Swappy is a process-lifetime singleton in this controller. Do not race
    // initialization with teardown or attempt to revive a destroyed instance.
    if (g_swappyDestroyed.load(std::memory_order_acquire) ||
        swappyWasEnabled.load(std::memory_order_acquire)) {
        return;
    }

    if (!SwappyGL_init(env, activity)) {
        return;
    }

    const uint64_t fenceTimeout =
            fenceTimeoutMS <= 0 ? 0 : static_cast<uint64_t>(fenceTimeoutMS) * NS_PER_MS;
    const uint64_t swapInterval =
            targetFPS <= 0 ? SWAPPY_SWAP_60FPS
                           : static_cast<uint64_t>(1000000000ULL / targetFPS);

    SwappyGL_setAutoSwapInterval(enableAutoSwap);
    SwappyGL_setUseAffinity(true);
    SwappyGL_enableStats(false);
    SwappyGL_setAutoPipelineMode(enableAutoPipelineMode);
    SwappyGL_enableFramePacing(enableFramePacing);
    SwappyGL_setBufferStuffingFixWait(bufferStuffingFixWait);
    SwappyGL_enableBlockingWait(enableBlockingWait);
    SwappyGL_setFenceTimeoutNS(fenceTimeout);
    SwappyGL_setSwapIntervalNS(swapInterval);
    SwappyGL_setMaxAutoSwapIntervalNS(swapInterval);

    std::atexit(DestroySwappyAtExit);

    // Publish readiness only after the configuration is complete. Swaps use
    // the same mutex, so no frame can enter Swappy during initialization.
    swappyWasEnabled.store(true, std::memory_order_release);
}

__attribute__((used)) __attribute__((visibility("default")))
JNIEXPORT void JNICALL
Java_com_mobilerpgpack_phone_utils_SwappyJNILayer_destroySwappyGL(JNIEnv *env, jobject thiz) {
    DestroySwappy();
}

__attribute__((used)) __attribute__((visibility("default")))
bool AndroidGraphicsDebuggerAttached(void) {
    static const bool attached = DetectGraphicsDebugger();
    return attached;
}

__attribute__((used)) __attribute__((visibility("default")))
bool SwappySwapBuffers() {
    // Under a capture tool the swap is where the captured frame ends and the tool reads back all
    // resources; Swappy's own EGL fences and presentation timestamps around it only get in the way
    // (and frame pacing a captured frame is pointless): the engine does a plain swap instead.
    if (AndroidGraphicsDebuggerAttached()) {
        return false;
    }

    // SwappyGL_swap() runs against Swappy's shared state and background
    // Choreographer/filter threads. Serialize it with init/destroy to prevent
    // teardown from freeing that state while a render thread is still swapping.
    std::lock_guard<std::mutex> lock(g_swappyMutex);

    if (g_swappyDestroyed.load(std::memory_order_acquire) ||
        !swappyWasEnabled.load(std::memory_order_acquire)) {
        return false;
    }

    const auto eglGetCurrentSurfacePTR = GetEGLGetCurrentSurface();
    if (eglGetCurrentSurfacePTR == nullptr) {
        return false;
    }

    const auto display = SDL_EGL_GetCurrentDisplay();
    if (display == EGL_NO_DISPLAY) {
        return false;
    }

    const auto surface = eglGetCurrentSurfacePTR(EGL_DRAW);
    if (surface == EGL_NO_SURFACE) {
        return false;
    }

    if (!SwappyGL_swap(display, surface)) {
        const auto eglGetErrorPTR = GetEGLGetError();

        // Swappy has already attempted the swap. Do not fall back to a raw
        // swap on an unknown error; that could issue another swap against an
        // invalid EGL surface.
        if (eglGetErrorPTR == nullptr || eglGetErrorPTR() != EGL_BAD_SURFACE) {
            return true;
        }

        // Fetch the current SDL window instead of reusing a possibly stale
        // pointer after Android recreates the underlying Surface.
        auto* window = GetSDLWindow();
        const auto context = SDL_GL_GetCurrentContext();
        if (window != nullptr && context != nullptr) {
            SDL_GL_MakeCurrent(window, nullptr);
            SDL_GL_MakeCurrent(window, context);
        }
    }

    return true;
}

__attribute__((used)) __attribute__((visibility("default")))
void DestroySwappy() {
    std::lock_guard<std::mutex> lock(g_swappyMutex);

    if (g_swappyDestroyed.exchange(true, std::memory_order_acq_rel)) {
        return;
    }

    const auto wasEnabled = swappyWasEnabled.exchange(false, std::memory_order_acq_rel);
    if (wasEnabled) {
        g_swappyDestroyed = true;
        SwappyGL_destroy();
    }
}
}