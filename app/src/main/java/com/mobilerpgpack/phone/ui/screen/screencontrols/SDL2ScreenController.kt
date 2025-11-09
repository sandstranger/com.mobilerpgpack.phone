package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface

class SDL2ScreenController : SDLScreenController() {

    override val viewWidth: Int get() = SDLSurface.fixedWidth

    override val viewHeight: Int get() = SDLSurface.fixedHeight

    override fun handlePointerAtIndex(i: Int, pointerId: Int, actionForSDL: Int,
                                      viewWidth : Float, viewHeight : Float, event: MotionEvent) {

        if (i < 0 || i >= event.pointerCount) {
            return
        }

        val x = event.getX(i) / viewWidth
        val y = event.getY(i) / viewHeight
        val p = event.getPressure(i).coerceAtMost(1.0f)
        SDLActivity.onNativeTouch(event.deviceId, pointerId, actionForSDL, x, y, p)
    }

    companion object{
        const val SDL2_SCREEN_CONTROLLER_NAME = "SDL2_SCREEN_CONTROLLER"
    }
}
