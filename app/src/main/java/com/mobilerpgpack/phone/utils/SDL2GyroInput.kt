package com.mobilerpgpack.phone.utils

import android.content.Context
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.sun.jna.Native
import kotlin.math.roundToInt

class SDL2GyroInput (ctx: Context, engineInfo: IEngineInfo) : GyroInput(ctx, engineInfo) {
    private var wasRegistered = false

    private external fun nativeGyroMouse(dx: Int, dy: Int)

    override fun onNativeGyroMouse(dx: Float, dy: Float) {
        if (!wasRegistered){
            wasRegistered = true
            Native.register(SDL2GyroInput::class.java, "SDL2GyroInput")
        }
        nativeGyroMouse(dx.roundToInt(), dy.roundToInt())
    }
}