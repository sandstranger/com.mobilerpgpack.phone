package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import org.libsdl3.app.SDLActivity.*

class SDL3Dpad(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f) : Dpad(engineType, offsetXPercent, offsetYPercent, sizePercent) {

    override fun onTouchDown(keyCode: Int) = onNativeKeyDown(keyCode)

    override fun onTouchUp(keyCode: Int) = onNativeKeyUp(keyCode)
}