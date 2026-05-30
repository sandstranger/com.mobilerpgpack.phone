package com.mobilerpgpack.phone.utils

import android.opengl.EGL14
import android.opengl.GLES20
import android.os.Build

class GpuProbe {
    data class Result(
        val renderer: String?,
        val vendor: String?,
        val isAdreno: Boolean,
        val isQualcomm: Boolean
    )

    fun probe(): Result {
        val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            return fallbackResult()
        }

        val version = IntArray(2)
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            return fallbackResult()
        }

        val attribList = intArrayOf(
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_NONE
        )

        val configs = arrayOfNulls<android.opengl.EGLConfig>(1)
        val numConfigs = IntArray(1)
        if (!EGL14.eglChooseConfig(eglDisplay, attribList, 0, configs, 0, 1, numConfigs, 0) || numConfigs[0] <= 0) {
            EGL14.eglTerminate(eglDisplay)
            return fallbackResult()
        }

        val config = configs[0] ?: run {
            EGL14.eglTerminate(eglDisplay)
            return fallbackResult()
        }

        val pbufferAttribs = intArrayOf(
            EGL14.EGL_WIDTH, 1,
            EGL14.EGL_HEIGHT, 1,
            EGL14.EGL_NONE
        )

        val eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, config, pbufferAttribs, 0)
        if (eglSurface == EGL14.EGL_NO_SURFACE) {
            EGL14.eglTerminate(eglDisplay)
            return fallbackResult()
        }

        val contextAttribs = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL14.EGL_NONE
        )

        val eglContext = EGL14.eglCreateContext(
            eglDisplay,
            config,
            EGL14.EGL_NO_CONTEXT,
            contextAttribs,
            0
        )

        if (eglContext == EGL14.EGL_NO_CONTEXT) {
            EGL14.eglDestroySurface(eglDisplay, eglSurface)
            EGL14.eglTerminate(eglDisplay)
            return fallbackResult()
        }

        val madeCurrent = EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
        if (!madeCurrent) {
            EGL14.eglDestroyContext(eglDisplay, eglContext)
            EGL14.eglDestroySurface(eglDisplay, eglSurface)
            EGL14.eglTerminate(eglDisplay)
            return fallbackResult()
        }

        val renderer = GLES20.glGetString(GLES20.GL_RENDERER)
        val vendor = GLES20.glGetString(GLES20.GL_VENDOR)

        EGL14.eglMakeCurrent(
            eglDisplay,
            EGL14.EGL_NO_SURFACE,
            EGL14.EGL_NO_SURFACE,
            EGL14.EGL_NO_CONTEXT
        )
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
        EGL14.eglTerminate(eglDisplay)

        return Result(
            renderer = renderer,
            vendor = vendor,
            isAdreno = renderer?.contains("adreno", ignoreCase = true) == true ||
                    vendor?.contains("qualcomm", ignoreCase = true) == true,
            isQualcomm = isLikelyQualcomm()
        )
    }

    private fun fallbackResult(): Result {
        return Result(
            renderer = null,
            vendor = null,
            isAdreno = false,
            isQualcomm = isLikelyQualcomm()
        )
    }

    private fun isLikelyQualcomm(): Boolean {
        fun match(s: String?): Boolean {
            val v = s?.lowercase().orEmpty()
            return v.contains("qcom") ||
                    v.contains("qualcomm") ||
                    v.startsWith("msm") ||
                    v.startsWith("sdm") ||
                    v.startsWith("sm")
        }

        if (match(Build.HARDWARE)) return true
        if (match(Build.BOARD)) return true
        if (match(Build.DEVICE)) return true
        if (match(Build.PRODUCT)) return true
        if (match(Build.BRAND)) return true

        val props = listOf(
            getProp("ro.board.platform"),
            getProp("ro.hardware"),
            getProp("ro.product.board"),
            getProp("ro.vendor.product.device")
        )
        return props.any(::match)
    }

    private fun getProp(name: String): String? {
        return try {
            val c = Class.forName("android.os.SystemProperties")
            val m = c.getMethod("get", String::class.java)
            m.invoke(null, name) as String
        } catch (_: Throwable) {
            null
        }
    }
}