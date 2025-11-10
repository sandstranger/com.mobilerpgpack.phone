package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import org.libsdl.app.SDLActivity.onNativeKeyDown
import org.libsdl.app.SDLActivity.onNativeKeyUp

class SDL2ImageButton(
    id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = 0,
    buttonResId: Int = NOT_EXISTING_RES,
    override val isQuickPanel: Boolean = false,
    useToggle: Boolean = false ) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha,sdlKeyEvent, buttonResId, useToggle = useToggle) {

    init {
        canBeDrawn = !isQuickPanel
    }

    override fun onTouchDown(keyCode: Int) = onNativeKeyDown(keyCode)

    override fun onTouchUp(keyCode: Int) = onNativeKeyUp(keyCode)
}