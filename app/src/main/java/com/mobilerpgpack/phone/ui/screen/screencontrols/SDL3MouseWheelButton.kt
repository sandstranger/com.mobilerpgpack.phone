package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import org.libsdl3.app.SDLActivity

class SDL3MouseWheelButton(
    id: String,
    engineType: EngineTypes,
    private val wheelUp : Boolean,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    override val isQuickPanel: Boolean = false) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha, Int.MIN_VALUE, buttonResId, useToggle = false) {

    init {
        show = !isQuickPanel
    }

    override fun onTouchDown(keyCode: Int) =
        SDLActivity.onNativeMouse(keyCode, MotionEvent.ACTION_SCROLL,
            0f, if (wheelUp) 500.0f else -500.0f, false)

    override fun onTouchUp(keyCode: Int) {}
    private companion object{
        private const val DEFAULT_POSITION = Int.MIN_VALUE.toFloat()
    }
}