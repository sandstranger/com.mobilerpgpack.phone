package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import org.libsdl3.app.SDLActivity.onNativeKeyUp
import org.libsdl3.app.SDLActivity.onNativeKeyDown

class SDL3ImageButton(
    id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = 0,
    buttonResId: Int = NOT_EXISTING_RES) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha,sdlKeyEvent, buttonResId) {

    override fun onTouchDown(keyCode: Int) = onNativeKeyDown(keyCode)

    override fun onTouchUp(keyCode: Int) = onNativeKeyUp(keyCode)
}