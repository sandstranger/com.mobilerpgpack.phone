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
        SDLActivity.onNativeMouse(sdlKeyEvent, MotionEvent.ACTION_DOWN,
            DEFAULT_POSITION, DEFAULT_POSITION, false)

    override fun onTouchUp(keyCode: Int) =
        SDLActivity.onNativeMouse(sdlKeyEvent, MotionEvent.ACTION_UP,
            DEFAULT_POSITION, DEFAULT_POSITION, false)

    private companion object{
        private const val DEFAULT_POSITION = Int.MIN_VALUE.toFloat()
    }
}