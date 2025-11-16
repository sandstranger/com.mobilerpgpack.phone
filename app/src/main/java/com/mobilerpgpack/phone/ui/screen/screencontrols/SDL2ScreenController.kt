package com.mobilerpgpack.phone.ui.screen.screencontrols

import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface

class SDL2ScreenController : SDLScreenController() {

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
        touchDeviceId: Int
    ) {
        val normalizedX = x / viewWidth
        val normalizedY = y / viewHeight
        SDLActivity.onNativeTouch(touchDeviceId, pointerId,
            eventAction, normalizedX, normalizedY, pressure,
            engineInfo.mouseButtonsEventsCanBeInvoked)
    }

    companion object{
        const val SDL2_SCREEN_CONTROLLER_NAME = "SDL2_SCREEN_CONTROLLER"
    }
}
