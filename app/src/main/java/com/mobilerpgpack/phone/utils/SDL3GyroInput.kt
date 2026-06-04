package com.mobilerpgpack.phone.utils

import android.content.Context
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.main.SDL3_GYRO_HELPER_LIB_NAME
import com.sun.jna.Native

class SDL3GyroInput (ctx: Context, engineInfo: IEngineInfo) : GyroInput(ctx, engineInfo) {
    private var wasRegistered = false

    private external fun nativeGyroMouse(dx: Float, dy: Float)

    override fun onNativeGyroMouse(dx: Float, dy: Float) {
        if (!wasRegistered){
            wasRegistered = true
            Native.register(SDL3GyroInput::class.java, SDL3_GYRO_HELPER_LIB_NAME)
        }
        nativeGyroMouse(dx, dy)
    }
}