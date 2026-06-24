package com.mobilerpgpack.phone.utils

import android.app.Activity
import com.mobilerpgpack.phone.main.ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME

class SwappyJNILayer {
    private var swappyWasInit = false
    private var swappyWasDisposed = false

    private external fun initSwappyGL(activity: Activity, enableAutoPipelineMode: Boolean,
        enableAutoSwap: Boolean, enableFramePacing: Boolean,
        enableBlockingWait: Boolean, bufferStuffingFixWait: Int, targetFPS: Int
    )

    private external fun destroySwappyGL()
    private external fun updateAngleState(enableAngle: Boolean)

    fun initSwappy(activity: Activity, enableAngle: Boolean = false, enableAutoPipelineMode: Boolean = true,
        enableAutoSwap: Boolean = false,enableFramePacing: Boolean = true, enableBlockingWait: Boolean = true,
                   bufferStuffingFixWait: Int = 0, targetFPS: Int = 60
    ) {
        if (swappyWasInit || swappyWasDisposed) {
            return
        }
        swappyWasInit = true
        System.loadLibrary(ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME)
        updateAngleState(enableAngle)
        initSwappyGL(activity, enableAutoPipelineMode, enableAutoSwap, enableFramePacing, enableBlockingWait,
            bufferStuffingFixWait,targetFPS)
    }

    fun destroySwappy() {
        if (swappyWasInit && !swappyWasDisposed) {
            swappyWasDisposed = true
            destroySwappyGL()
        }
    }
}