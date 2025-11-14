package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
import org.libsdl3.app.SDLActivity
import org.libsdl3.app.SDLSurface
import org.libsdl3.app.SDLSurface.getNormalizedX
import org.libsdl3.app.SDLSurface.getNormalizedY

class SDL3ScreenController : SDLScreenController() {

    override val viewWidth: Int get() = SDLSurface.fixedWidth

    override val viewHeight: Int get() = SDLSurface.fixedHeight

    override fun handlePointer(
        pointerId: Int,
        pressure: Float,
        x: Float,
        y: Float,
        viewWidth: Float,
        viewHeight: Float,
        eventAction: Int,
        event: MotionEvent
    ) {
        val normalizedX = getNormalizedX(x)
        val normalizedY = getNormalizedY(y)
        SDLActivity.onNativeTouch(event.deviceId, pointerId,
            eventAction, normalizedX, normalizedY, pressure,
            engineInfo.mouseButtonsEventsCanBeInvoked)
    }

    override fun onMotionEventFinished(event: MotionEvent) {
        super.onMotionEventFinished(event)
        SDLSurface.getScaleGestureDetector().onTouchEvent(event)
    }

    companion object {
        const val SDL3_SCREEN_CONTROLLER_NAME = "SDL3_SCREEN_CONTROLLER"
    }
}