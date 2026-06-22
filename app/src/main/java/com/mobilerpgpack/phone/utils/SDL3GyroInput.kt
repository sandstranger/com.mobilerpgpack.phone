package com.mobilerpgpack.phone.utils

import android.content.Context
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.main.ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME
import com.sun.jna.Native

class SDL3GyroInput (ctx: Context, engineInfo: IEngineInfo) : GyroInput(ctx, engineInfo) {
    private var wasRegistered = false

    private external fun nativeGyroMouse(dx: Float, dy: Float)

    override fun onNativeGyroMouse(dx: Float, dy: Float) {
        if (!wasRegistered){
            wasRegistered = true
            Native.register(SDL3GyroInput::class.java, ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME)
        }
        nativeGyroMouse(dx, dy)
    }
}