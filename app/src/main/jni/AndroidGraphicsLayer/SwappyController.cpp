//
// Created by sandstranger on 21.06.2026.
//
#include "SwappyController.h"
#include "SwappyVkController.h"
#include "swappy/swappyGL.h"
#include <swappy/swappyGL_extra.h>
#include <swappy/swappyVk.h>
#include <swappy/swappy_common.h>
#include "SDL3/SDL.h"
#include "SDL3/SDL_egl.h"
#include "jni.h"
#include <android/log.h>
#include <android/native_window.h>
#include <algorithm>
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

// The launcher's frame pacing settings (initSwappyGL), kept for the Vulkan renderers (SwappyVk), with what
// SwappyVk needs from Java: the VM to get the render thread's JNIEnv and the activity.
struct SwappyVkSettings {
    bool valid = false;
    bool autoPipelineMode = true;
    bool autoSwap = false;
    bool framePacing = true;
    bool blockingWait = true;
    uint64_t fenceTimeoutNS = 0;
    uint64_t swapIntervalNS = SWAPPY_SWAP_60FPS;
};
static SwappyVkSettings g_vkSettings;
static JavaVM* g_javaVM = nullptr;
static jobject g_activity = nullptr;    // global reference

// SwappyVk state, all under g_swappyMutex: the device whose present queue Swappy knows, the swapchain it paces.
static VkDevice g_vkDevice = VK_NULL_HANDLE;
static VkSwapchainKHR g_vkSwapchain = VK_NULL_HANDLE;
static bool g_vkAtExitRegistered = false;

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

// The ANativeWindow of the SDL window, the one the Vulkan surface was created for.
static ANativeWindow* GetANativeWindow() {
    SDL_Window* window = GetSDLWindow();
    if (window == nullptr) {
        return nullptr;
    }
    return static_cast<ANativeWindow*>(SDL_GetPointerProperty(SDL_GetWindowProperties(window),
                                                              SDL_PROP_WINDOW_ANDROID_WINDOW_POINTER, nullptr));
}

// JNIEnv of the calling (render) thread. SDL's main thread is a Java thread; any other thread is attached by
// SDL, which detaches it when it exits.
static JNIEnv* GetJNIEnv() {
    JNIEnv* env = nullptr;
    if (g_javaVM != nullptr && g_javaVM->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) == JNI_OK) {
        return env;
    }
    return static_cast<JNIEnv*>(SDL_GetAndroidJNIEnv());
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

// Releases what SwappyVk has (its threads, fences and command buffers on the device), g_swappyMutex held.
static void DestroySwappyVkLocked() {
    if (g_vkSwapchain != VK_NULL_HANDLE) {
        SwappyVk_destroySwapchain(g_vkDevice, g_vkSwapchain);
        g_vkSwapchain = VK_NULL_HANDLE;
    }
    if (g_vkDevice != VK_NULL_HANDLE) {
        SwappyVk_destroyDevice(g_vkDevice);
        g_vkDevice = VK_NULL_HANDLE;
    }
}

static bool SwappyVkAvailableLocked() {
    return g_vkSettings.valid && g_activity != nullptr && !g_swappyDestroyed.load(std::memory_order_acquire);
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

    const uint64_t fenceTimeout =
            fenceTimeoutMS <= 0 ? 0 : static_cast<uint64_t>(fenceTimeoutMS) * NS_PER_MS;
    const uint64_t swapInterval =
            targetFPS <= 0 ? SWAPPY_SWAP_60FPS
                           : static_cast<uint64_t>(1000000000ULL / targetFPS);

    // The same settings for a Vulkan renderer (SwappyVk is set up per swapchain, SwappyVkController_*)
    if (g_javaVM == nullptr) {
        env->GetJavaVM(&g_javaVM);
    }
    if (g_activity == nullptr) {
        g_activity = env->NewGlobalRef(activity);
    }
    g_vkSettings.autoPipelineMode = enableAutoPipelineMode;
    g_vkSettings.autoSwap = enableAutoSwap;
    g_vkSettings.framePacing = enableFramePacing;
    g_vkSettings.blockingWait = enableBlockingWait;
    g_vkSettings.fenceTimeoutNS = fenceTimeout;
    g_vkSettings.swapIntervalNS = swapInterval;
    g_vkSettings.valid = g_javaVM != nullptr && g_activity != nullptr;

    if (!SwappyGL_init(env, activity)) {
        return;
    }

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

    DestroySwappyVkLocked();

    const auto wasEnabled = swappyWasEnabled.exchange(false, std::memory_order_acq_rel);
    if (wasEnabled) {
        g_swappyDestroyed = true;
        SwappyGL_destroy();
    }
}

__attribute__((used)) __attribute__((visibility("default")))
uint32_t SwappyVkController_GetDeviceExtensions(VkPhysicalDevice physicalDevice, uint32_t availableExtensionCount,
                                                const VkExtensionProperties* pAvailableExtensions,
                                                const char** outNames, uint32_t maxNames) {
    if (AndroidGraphicsDebuggerAttached() || pAvailableExtensions == nullptr || outNames == nullptr) {
        return 0;
    }

    std::lock_guard<std::mutex> lock(g_swappyMutex);
    if (!SwappyVkAvailableLocked()) {
        return 0;
    }

    constexpr uint32_t MAX_SWAPPY_EXTENSIONS = 4;
    static char extensionNames[MAX_SWAPPY_EXTENSIONS][VK_MAX_EXTENSION_NAME_SIZE];
    char* extensionNamePtrs[MAX_SWAPPY_EXTENSIONS];
    for (uint32_t i = 0; i < MAX_SWAPPY_EXTENSIONS; ++i) {
        extensionNamePtrs[i] = extensionNames[i];
    }

    auto* const availableExtensions = const_cast<VkExtensionProperties*>(pAvailableExtensions);
    uint32_t requiredCount = 0;
    SwappyVk_determineDeviceExtensions(physicalDevice, availableExtensionCount, availableExtensions, &requiredCount, nullptr);
    requiredCount = std::min(requiredCount, std::min(MAX_SWAPPY_EXTENSIONS, maxNames));

    // Called with the names even for none: this is where Swappy notes whether the device will have
    // VK_GOOGLE_display_timing (the caller enables what is returned).
    SwappyVk_determineDeviceExtensions(physicalDevice, availableExtensionCount, availableExtensions, &requiredCount,
                                       extensionNamePtrs);
    for (uint32_t i = 0; i < requiredCount; ++i) {
        outNames[i] = extensionNames[i];
        __android_log_print(ANDROID_LOG_INFO, "AndroidGraphicsLayer", "SwappyVk: device extension %s", extensionNames[i]);
    }
    return requiredCount;
}

__attribute__((used)) __attribute__((visibility("default")))
void SwappyVkController_SetPresentQueue(VkDevice device, VkQueue queue, uint32_t queueFamilyIndex) {
    if (AndroidGraphicsDebuggerAttached() || device == VK_NULL_HANDLE || queue == VK_NULL_HANDLE) {
        return;
    }

    std::lock_guard<std::mutex> lock(g_swappyMutex);
    if (!SwappyVkAvailableLocked()) {
        return;
    }

    if (g_vkDevice != VK_NULL_HANDLE && g_vkDevice != device) {
        DestroySwappyVkLocked();    // a device the renderer destroyed without telling
    }
    SwappyVk_setQueueFamilyIndex(device, queue, queueFamilyIndex);
    g_vkDevice = device;
}

__attribute__((used)) __attribute__((visibility("default")))
bool SwappyVkController_InitSwapchain(VkPhysicalDevice physicalDevice, VkDevice device, VkSwapchainKHR swapchain,
                                      VkPresentModeKHR presentMode) {
    if (AndroidGraphicsDebuggerAttached() || swapchain == VK_NULL_HANDLE) {
        return false;
    }

    std::lock_guard<std::mutex> lock(g_swappyMutex);

    // Swappy paces vsync'd presentation; the queue must be known (SwappyVkController_SetPresentQueue)
    if (!SwappyVkAvailableLocked() || presentMode != VK_PRESENT_MODE_FIFO_KHR || device != g_vkDevice) {
        return false;
    }

    JNIEnv* const env = GetJNIEnv();
    if (env == nullptr) {
        return false;
    }

    if (g_vkSwapchain != VK_NULL_HANDLE) {
        SwappyVk_destroySwapchain(g_vkDevice, g_vkSwapchain);   // destroyed without telling
        g_vkSwapchain = VK_NULL_HANDLE;
    }

    uint64_t refreshPeriodNS = 0;
    if (!SwappyVk_initAndGetRefreshCycleDuration(env, g_activity, physicalDevice, device, swapchain, &refreshPeriodNS)) {
        __android_log_print(ANDROID_LOG_WARN, "AndroidGraphicsLayer", "SwappyVk: initialization failed, no frame pacing");
        SwappyVk_destroySwapchain(device, swapchain);
        return false;
    }

    // the window lets Swappy ask the display for a refresh rate that fits the swap interval
    if (ANativeWindow* const window = GetANativeWindow(); window != nullptr) {
        SwappyVk_setWindow(device, swapchain, window);
    }

    // The same settings as SwappyGL (initSwappyGL). The global ones apply to the swapchains Swappy has:
    // set after the swapchain was given to it.
    SwappyVk_setAutoSwapInterval(g_vkSettings.autoSwap);
    SwappyVk_setAutoPipelineMode(g_vkSettings.autoPipelineMode);
    SwappyVk_setMaxAutoSwapIntervalNS(g_vkSettings.swapIntervalNS);
    SwappyVk_setFenceTimeoutNS(g_vkSettings.fenceTimeoutNS);
    SwappyVk_setSwapIntervalNS(device, swapchain, g_vkSettings.swapIntervalNS);
    SwappyVk_enableStats(swapchain, false);
    SwappyVk_enableFramePacing(swapchain, g_vkSettings.framePacing);
    SwappyVk_enableBlockingWait(swapchain, g_vkSettings.blockingWait);
    g_vkSwapchain = swapchain;

    // Exit handlers run in reverse order of registration: registered after SwappyVk made its singletons,
    // DestroySwappy stops its threads before those are destroyed (see DestroySwappyAtExit).
    if (!g_vkAtExitRegistered) {
        g_vkAtExitRegistered = true;
        std::atexit(DestroySwappyAtExit);
    }

    __android_log_print(ANDROID_LOG_INFO, "AndroidGraphicsLayer",
                        "SwappyVk: pacing the swapchain, refresh period %.3f ms, swap interval %.3f ms",
                        refreshPeriodNS / 1.0e6, g_vkSettings.swapIntervalNS / 1.0e6);
    return true;
}

__attribute__((used)) __attribute__((visibility("default")))
bool SwappyVkController_QueuePresent(VkQueue queue, const VkPresentInfoKHR* pPresentInfo, VkResult* pResult) {
    if (pPresentInfo == nullptr || pResult == nullptr) {
        return false;
    }

    // Serialized with init/destroy like SwappyGL_swap: teardown must not free Swappy's state mid-present.
    std::lock_guard<std::mutex> lock(g_swappyMutex);
    if (g_vkSwapchain == VK_NULL_HANDLE || g_swappyDestroyed.load(std::memory_order_acquire)) {
        return false;
    }
    if (pPresentInfo->swapchainCount != 1 || pPresentInfo->pSwapchains[0] != g_vkSwapchain) {
        return false;
    }

    *pResult = SwappyVk_queuePresent(queue, pPresentInfo);
    return true;
}

__attribute__((used)) __attribute__((visibility("default")))
void SwappyVkController_DestroySwapchain(VkDevice device, VkSwapchainKHR swapchain) {
    std::lock_guard<std::mutex> lock(g_swappyMutex);
    if (swapchain != VK_NULL_HANDLE && swapchain == g_vkSwapchain) {
        SwappyVk_destroySwapchain(device, swapchain);
        g_vkSwapchain = VK_NULL_HANDLE;
    }
}

__attribute__((used)) __attribute__((visibility("default")))
void SwappyVkController_DestroyDevice(VkDevice device) {
    std::lock_guard<std::mutex> lock(g_swappyMutex);
    if (device != VK_NULL_HANDLE && device == g_vkDevice) {
        DestroySwappyVkLocked();
    }
}
}