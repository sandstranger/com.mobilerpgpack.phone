package com.mobilerpgpack.phone.utils

import android.opengl.EGL14
import android.util.Log

private const val TAG = "SDL_EGL_FIX"

fun forceDestroyCurrentEgl() {
    try {
        val display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (display == EGL14.EGL_NO_DISPLAY) {
            Log.w(TAG, "No EGL display")
            return
        }
        val major = IntArray(1)
        val minor = IntArray(1)
        EGL14.eglInitialize(display, major, 0, minor, 0)

        val currentContext = EGL14.eglGetCurrentContext()
        val currentSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW)

        Log.i(TAG, "eglGetCurrentContext=$currentContext eglGetCurrentSurface=$currentSurface")

        // Unbind current
        EGL14.eglMakeCurrent(
            display,
            EGL14.EGL_NO_SURFACE,
            EGL14.EGL_NO_SURFACE,
            EGL14.EGL_NO_CONTEXT
        )

        if (currentSurface != null && currentSurface !== EGL14.EGL_NO_SURFACE) {
            try {
                EGL14.eglDestroySurface(display, currentSurface)
                Log.i(TAG, "Destroyed EGL surface")
            } catch (t: Throwable) {
                Log.w(TAG, "Failed destroySurface: $t")
            }
        }

        if (currentContext != null && currentContext !== EGL14.EGL_NO_CONTEXT) {
            try {
                EGL14.eglDestroyContext(display, currentContext)
                Log.i(TAG, "Destroyed EGL context")
            } catch (t: Throwable) {
                Log.w(TAG, "Failed destroyContext: $t")
            }
        }

        try {
            EGL14.eglTerminate(display)
            Log.i(TAG, "eglTerminate done")
        } catch (t: Throwable) {
            Log.w(TAG, "eglTerminate failed: $t")
        }
    } catch (t: Throwable) {
        Log.e("SDL_EGL_FIX", "forceDestroyCurrentEgl error", t)
    }
}
