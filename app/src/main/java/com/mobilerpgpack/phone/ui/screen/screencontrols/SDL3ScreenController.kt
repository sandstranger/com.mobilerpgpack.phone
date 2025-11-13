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
        val eventAction = event.actionMasked
        val pointerIndex = event.actionIndex

        when (event.getToolType(pointerIndex)) {
            MotionEvent.TOOL_TYPE_MOUSE -> {
                val motionListener = SDLActivity.getMotionListener()
                val relative = motionListener.inRelativeMode()
                SDLActivity.onNativeMouse(event.buttonState, eventAction, x, y, relative)
            }

            MotionEvent.TOOL_TYPE_STYLUS, MotionEvent.TOOL_TYPE_ERASER -> {
                val toolType = event.getToolType(pointerIndex)
                val buttonState =
                    (event.buttonState shr 4) or (1 shl (if (toolType == MotionEvent.TOOL_TYPE_STYLUS) 0 else 30))
                SDLActivity.onNativePen(
                    pointerId,
                    SDLActivity.getMotionListener().getPenDeviceType(event.device),
                    buttonState,
                    eventAction,x,y,pressure)
            }

            else -> {
                val normalizedX = getNormalizedX(x)
                val normalizedY = getNormalizedY(y)

                SDLActivity.onNativeTouch(event.deviceId, pointerId,
                    eventAction, normalizedX, normalizedY, pressure)
            }
        }

    }

    override fun onMotionEventFinished(event: MotionEvent) {
        super.onMotionEventFinished(event)
        SDLSurface.getScaleGestureDetector().onTouchEvent(event)
    }

    companion object {
        const val SDL3_SCREEN_CONTROLLER_NAME = "SDL3_SCREEN_CONTROLLER"
    }
}