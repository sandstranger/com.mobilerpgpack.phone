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

using PFNEGLGETCURRENTSURFACEPROC = EGLSurface (*)(EGLint);

static bool swappyWasEnabled = false;
static PFNEGLGETCURRENTSURFACEPROC p_eglGetCurrentSurface = nullptr;

extern "C" {
__attribute__((used)) __attribute__((visibility("default")))
JNIEXPORT void JNICALL
Java_com_mobilerpgpack_phone_utils_SwappyJNILayer_initSwappyGL(JNIEnv *env,jobject thiz,jobject activity,
                                                             jboolean enableAutoPipelineMode,jboolean enableAutoSwap,
                                                             jint targetFPS) {
    if (SwappyGL_init(env, activity)) {
        const auto ns = (targetFPS == 0) ? (SWAPPY_SWAP_60FPS) : (uint64_t) (1000000000L / targetFPS);
        swappyWasEnabled = true;
        SwappyGL_setAutoSwapInterval(enableAutoSwap);
        SwappyGL_setUseAffinity(true);
        SwappyGL_enableStats(false);
        SwappyGL_setAutoPipelineMode(enableAutoPipelineMode);
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
    if (p_eglGetCurrentSurface == nullptr){
        p_eglGetCurrentSurface = reinterpret_cast<PFNEGLGETCURRENTSURFACEPROC>(SDL_EGL_GetProcAddress("eglGetCurrentSurface"));
    }
    const auto display = SDL_EGL_GetCurrentDisplay();
    const auto surface = p_eglGetCurrentSurface(EGL_DRAW);
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