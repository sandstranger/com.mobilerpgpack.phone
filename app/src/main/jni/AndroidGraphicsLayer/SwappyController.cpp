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

#define NS_PER_MS 1000000ULL

using PFNEGLGETCURRENTSURFACEPROC = EGLSurface (*)(EGLint);
static bool swappyWasEnabled = false;

static inline PFNEGLGETCURRENTSURFACEPROC GetEGLGetCurrentSurface() {
    static PFNEGLGETCURRENTSURFACEPROC p_eglGetCurrentSurface = []() -> PFNEGLGETCURRENTSURFACEPROC {
        return reinterpret_cast<PFNEGLGETCURRENTSURFACEPROC>(
                SDL_EGL_GetProcAddress("eglGetCurrentSurface")
        );
    }();
    return p_eglGetCurrentSurface;
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
    if (!swappyWasEnabled && SwappyGL_init(env, activity)) {
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
    if (!swappyWasEnabled){
        return false;
    }
    const auto eglGetCurrentSurfacePTR = GetEGLGetCurrentSurface();
    const auto display = SDL_EGL_GetCurrentDisplay();
    const auto surface = eglGetCurrentSurfacePTR(EGL_DRAW);
    if (display != EGL_NO_DISPLAY && surface != EGL_NO_SURFACE)
    {
        SwappyGL_swap(display, surface);
        return true;
    }
    return false;
}

__attribute__((used)) __attribute__((visibility("default")))
void DestroySwappy(){
    if (swappyWasEnabled && SwappyGL_isEnabled()){
        swappyWasEnabled = false;
        SwappyGL_destroy();
    }
}
}