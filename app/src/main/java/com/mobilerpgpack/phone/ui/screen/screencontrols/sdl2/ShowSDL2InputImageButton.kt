package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2

import android.util.Log
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLInputImageButton

class ShowSDL2InputImageButton(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = ButtonState.NOT_EXISTING_RES)  :
    ShowSDLInputImageButton(engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha, buttonResId)
{
    override fun onKeyDown(keyCode: Int) = org.libsdl.app.onKeyDown(keyCode, delayBeforeKeyRelease = 50L)
}