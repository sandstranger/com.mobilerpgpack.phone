package com.mobilerpgpack.phone.utils

import android.app.Activity
import com.mobilerpgpack.phone.main.ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME

class SwappyJNILayer {
    private var swappyWasInit = false
    private var swappyWasDisposed = false
    private external fun initSwappyGL(activity: Activity, enableAutoPipelineMode : Boolean,
                                    enableAutoSwap : Boolean, targetFPS : Int)
    private external fun destroySwappyGL()

    fun initSwappy(activity: Activity, enableAutoPipelineMode : Boolean = true,
                   enableAutoSwap : Boolean = false, targetFPS : Int = 60){
        if (swappyWasInit || swappyWasDisposed){
            return
        }
        swappyWasInit = true
        System.loadLibrary(ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME)
        initSwappyGL(activity, enableAutoPipelineMode, enableAutoSwap, targetFPS)
    }

    fun destroySwappy (){
        if (swappyWasInit && !swappyWasDisposed){
            swappyWasDisposed = true
            destroySwappyGL()
        }
    }
}