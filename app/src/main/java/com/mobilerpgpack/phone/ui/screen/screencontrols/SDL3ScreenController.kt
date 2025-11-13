package com.mobilerpgpack.phone.ui.screen.screencontrols

import org.libsdl3.app.SDLActivity
import org.libsdl3.app.SDLSurface
import org.libsdl3.app.SDLSurface.getNormalizedX
import org.libsdl3.app.SDLSurface.getNormalizedY

class SDL3ScreenController : SDLScreenController() {

    override val viewWidth: Int get() = SDLSurface.fixedWidth

    override val viewHeight: Int get() = SDLSurface.fixedHeight

    override fun handlePointer(pointerId: Int, pressure: Float, x: Float, y: Float, motionEvent : Int,
                               viewWidth : Float, viewHeight : Float) {

        val normalizedX = getNormalizedX(x)
        val normalizedY = getNormalizedY(y)
        SDLActivity.onNativeTouch(DEFAULT_TOUCH_DEVICE_ID, pointerId,
                    motionEvent, normalizedX, normalizedY, pressure)
    }

    companion object {
        const val SDL3_SCREEN_CONTROLLER_NAME = "SDL3_SCREEN_CONTROLLER"
    }
}