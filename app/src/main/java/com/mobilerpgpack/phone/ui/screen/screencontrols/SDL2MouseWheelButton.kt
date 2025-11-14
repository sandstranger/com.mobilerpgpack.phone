package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import org.libsdl.app.SDLActivity

class SDL2MouseWheelButton(
    id: String,
    engineType: EngineTypes,
    wheelUp : Boolean,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    isQuickPanel: Boolean = false) :
    SDLMouseWheelButton(id, engineType, wheelUp,offsetXPercent, offsetYPercent, sizePercent,
        alpha,buttonResId, isQuickPanel){

    override fun onMouseWheel (keyCode: Int, x : Float, y : Float, event: Int ) =
        SDLActivity.onNativeMouse(keyCode, event, x, y, false)
}
