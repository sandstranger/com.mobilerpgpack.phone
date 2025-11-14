package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
import com.mobilerpgpack.phone.engine.EngineTypes

abstract class SDLMouseWheelButton(
    id: String,
    engineType: EngineTypes,
    private val wheelUp : Boolean,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = ButtonState.NOT_EXISTING_RES,
    override val isQuickPanel: Boolean = false) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha, Int.MIN_VALUE, buttonResId, useToggle = false) {

    init {
        show = !isQuickPanel
    }

    final override fun onTouchDown(keyCode: Int) =
        onMouseWheel(keyCode,0f, if (wheelUp) DEFAULT_POSITION else -DEFAULT_POSITION, MotionEvent.ACTION_SCROLL)

    final override fun onTouchUp(keyCode: Int) {}

    protected abstract fun onMouseWheel (keyCode: Int, x : Float, y : Float, event: Int )

    private companion object{
        private const val DEFAULT_POSITION = 500.0f
    }
}