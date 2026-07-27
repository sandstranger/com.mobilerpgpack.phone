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
#include <atomic>
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

static inline PFNEGLGETERRORPROC GetEGLGetError()
{
    static auto p_eglGetError =
            reinterpret_cast<PFNEGLGETERRORPROC>(
                    SDL_EGL_GetProcAddress("eglGetError")
            );

    return p_eglGetError;
}

static inline SDL_Window* GetSDLWindow()
{
    static SDL_Window* window = []() -> SDL_Window*
    {
        int count = 0;
        SDL_Window** windows = SDL_GetWindows(&count);

        SDL_Window* result = nullptr;

        if (count > 0 && windows)
        {
            result = windows[0];
        }

        SDL_free(windows);

        return result;
    }();

    return window;
}

extern "C" {
__attribute__((used)) __attribute__((visibility("default")))
JNIEXPORT void JNICALL
Java_com_mobilerpgpack_phone_utils_SwappyJNILayer_initSwappyGL(JNIEnv *env,jobject thiz,jobject activity,
                                                             jboolean enableAutoPipelineMode,
                                                             jboolean enableAutoSwap,
                                                             jboolean enableFramePacing,
                                                             jboolean enableBlockingWait,
                                                             jint bufferStuffingFixWait,
                                                             jlong fenceTimeoutMS,
                                                             jint targetFPS) {
    std::lock_guard<std::mutex> lock(g_swappyMutex);
    if (!swappyWasEnabled.load() && SwappyGL_init(env, activity)){
        const uint64_t fenceTimeout = fenceTimeoutMS <= 0 ? 0 : fenceTimeoutMS * NS_PER_MS;
        const auto ns = (targetFPS <= 0) ? (SWAPPY_SWAP_60FPS) : (uint64_t) (1000000000L / targetFPS);
        swappyWasEnabled = true;
        SwappyGL_setAutoSwapInterval(enableAutoSwap);
        SwappyGL_setUseAffinity(true);
        SwappyGL_enableStats(false);
        SwappyGL_setAutoPipelineMode(enableAutoPipelineMode);
        SwappyGL_enableFramePacing(enableFramePacing);
        SwappyGL_setBufferStuffingFixWait(bufferStuffingFixWait);
        SwappyGL_enableBlockingWait(enableBlockingWait);
        SwappyGL_setFenceTimeoutNS(fenceTimeout);
        SwappyGL_setSwapIntervalNS(ns);
        SwappyGL_setMaxAutoSwapIntervalNS(ns);
    }
}

__attribute__((used)) __attribute__((visibility("default")))
JNIEXPORT void JNICALL Java_com_mobilerpgpack_phone_utils_SwappyJNILayer_destroySwappyGL(JNIEnv *env,jobject thiz){
    DestroySwappy();
}

__attribute__((used)) __attribute__((visibility("default")))
bool SwappySwapBuffers(){
    if (g_swappyDestroyed.load(std::memory_order_relaxed) || !swappyWasEnabled.load(std::memory_order_relaxed)) {
        return false;
    }
    const auto eglGetCurrentSurfacePTR = GetEGLGetCurrentSurface();
    const auto display = SDL_EGL_GetCurrentDisplay();
    const auto surface = eglGetCurrentSurfacePTR(EGL_DRAW);
    if (display != EGL_NO_DISPLAY && surface != EGL_NO_SURFACE)
    {
        if (!SwappyGL_swap(display, surface))
        {
            const auto eglGetErrorPTR = GetEGLGetError();
            if (eglGetErrorPTR() != EGL_BAD_SURFACE)
            {
                return true;
            }
            auto window = GetSDLWindow();
            const auto context = SDL_GL_GetCurrentContext();
            if (window != nullptr && context != nullptr)
            {
                SDL_GL_MakeCurrent(window, nullptr);
                SDL_GL_MakeCurrent(window, context);
            }
        }
        return true;
    }
    return false;
}

__attribute__((used)) __attribute__((visibility("default")))
void DestroySwappy() {
    return;
    std::lock_guard<std::mutex> lock(g_swappyMutex);
    if (g_swappyDestroyed.load(std::memory_order_acquire)) {
        return;
    }
    const auto wasEnabled = swappyWasEnabled.exchange(false, std::memory_order_acq_rel);
    if (wasEnabled) {
        SwappyGL_destroy();
    }
    swappyWasEnabled.store(false, std::memory_order_release);
    g_swappyDestroyed.store(true, std::memory_order_release);
}
}