package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import org.libsdl3.app.SDLActivity

class SDL3MouseImageButton(
    id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    private val sdlKeyEvent: Int = 0,
    buttonResId: Int = NOT_EXISTING_RES,
    override val isQuickPanel: Boolean = false,
    useToggle: Boolean = false) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha,Int.MIN_VALUE, buttonResId, useToggle = useToggle) {

    init {
        show = !isQuickPanel
    }

    override fun onTouchDown(keyCode: Int) =
        SDLActivity.onVirtualMouse(sdlKeyEvent, MotionEvent.ACTION_DOWN)

    override fun onTouchUp(keyCode: Int) =
        SDLActivity.onVirtualMouse(sdlKeyEvent, MotionEvent.ACTION_UP)
}