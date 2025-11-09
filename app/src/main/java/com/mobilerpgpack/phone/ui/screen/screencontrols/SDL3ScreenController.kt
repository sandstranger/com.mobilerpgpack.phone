package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
import org.libsdl3.app.SDLActivity
import org.libsdl3.app.SDLSurface
import org.libsdl3.app.SDLSurface.getNormalizedX
import org.libsdl3.app.SDLSurface.getNormalizedY

class SDL3ScreenController : SDLScreenController() {

    override val viewWidth: Int get() = SDLSurface.fixedWidth

    override val viewHeight: Int get() = SDLSurface.fixedHeight

    override fun handlePointerAtIndex(i: Int, pointerId: Int, viewWidth : Float,
                                      viewHeight : Float, event: MotionEvent) {

        if (i < 0 || i >= event.pointerCount) {
            return
        }

        val eventAction = event.actionMasked

        when (event.getToolType(i)) {
            MotionEvent.TOOL_TYPE_MOUSE -> {
                val motionListener = SDLActivity.getMotionListener()
                val x = motionListener.getEventX(event, i)
                val y = motionListener.getEventY(event, i)
                val relative = motionListener.inRelativeMode()
                SDLActivity.onNativeMouse(event.buttonState, eventAction, x, y, relative)
            }

            MotionEvent.TOOL_TYPE_STYLUS, MotionEvent.TOOL_TYPE_ERASER -> {
                val p = event.getPressure(i).coerceAtMost(1.0f)
                val toolType = event.getToolType(i)
                val buttonState =
                    (event.buttonState shr 4) or (1 shl (if (toolType == MotionEvent.TOOL_TYPE_STYLUS) 0 else 30))
                SDLActivity.onNativePen(
                    pointerId,
                    SDLActivity.getMotionListener().getPenDeviceType(event.device),
                    buttonState,
                    eventAction,
                    event.getX(i),
                    event.getY(i),
                    p)
            }

            else -> {
                val x = getNormalizedX(event.getX(i))
                val y = getNormalizedY(event.getY(i))
                val p = event.getPressure(i).coerceAtMost(1.0f)

                SDLActivity.onNativeTouch(event.deviceId, pointerId,
                    eventAction, x, y, p)
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