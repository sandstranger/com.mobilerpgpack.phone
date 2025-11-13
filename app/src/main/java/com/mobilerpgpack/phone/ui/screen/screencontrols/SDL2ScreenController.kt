package com.mobilerpgpack.phone.ui.screen.screencontrols

import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface

class SDL2ScreenController : SDLScreenController() {

    override val viewWidth: Int get() = SDLSurface.fixedWidth

    override val viewHeight: Int get() = SDLSurface.fixedHeight

    override fun handlePointer(pointerId: Int, pressure: Float, x: Float, y: Float, motionEvent : Int,
                                      viewWidth : Float, viewHeight : Float) {
        val normalizedX = x / viewWidth
        val normalizedY = y / viewHeight
        SDLActivity.onNativeTouch(DEFAULT_TOUCH_DEVICE_ID, pointerId,
            motionEvent, normalizedX, normalizedY, pressure)
    }

    companion object{
        const val SDL2_SCREEN_CONTROLLER_NAME = "SDL2_SCREEN_CONTROLLER"
    }
}
