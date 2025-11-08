package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import org.libsdl3.app.SDLActivity
import org.libsdl3.app.SDLSurface
import org.libsdl3.app.SDLSurface.getNormalizedX
import org.libsdl3.app.SDLSurface.getNormalizedY
import kotlin.math.roundToInt

class SDL3ScreenController : ScreenController() {

    @Composable
    override fun DrawTouchCamera() {
        var mWidth by remember { mutableFloatStateOf(0.0f) }
        var mHeight by remember { mutableFloatStateOf(0.0f) }
        var isActionDownActive by remember { mutableStateOf(false) }
        var widthSize = 0
        var heightSize = 0

        fun onTouchEvent(event: MotionEvent): Boolean {
            /* Ref: http://developer.android.com/training/gestures/multi.html */
            val touchDevId = event.deviceId
            val pointerCount = event.pointerCount
            var action = event.actionMasked
            var pointerId: Int
            var i = 0
            var x: Float
            var y: Float
            var p: Float

            if (action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_POINTER_DOWN) i =
                event.actionIndex

            isActionDownActive = action == MotionEvent.ACTION_DOWN

            do {
                when (val toolType = event.getToolType(i)) {
                    MotionEvent.TOOL_TYPE_MOUSE -> {
                        val buttonState = event.buttonState
                        var relative = false

                        // We need to check if we're in relative mouse mode and get the axis offset rather than the x/y values
                        // if we are. We'll leverage our existing mouse motion listener
                        val motionListener = SDLActivity.getMotionListener()
                        x = motionListener.getEventX(event, i)
                        y = motionListener.getEventY(event, i)
                        relative = motionListener.inRelativeMode()

                        SDLActivity.onNativeMouse(buttonState, action, x, y, relative)
                    }

                    MotionEvent.TOOL_TYPE_STYLUS, MotionEvent.TOOL_TYPE_ERASER -> {
                        pointerId = event.getPointerId(i)
                        x = event.getX(i)
                        y = event.getY(i)
                        p = event.getPressure(i)
                        if (p > 1.0f) {
                            // may be larger than 1.0f on some devices
                            // see the documentation of getPressure(i)
                            p = 1.0f
                        }

                        // BUTTON_STYLUS_PRIMARY is 2^5, so shift by 4, and apply SDL_PEN_INPUT_DOWN/SDL_PEN_INPUT_ERASER_TIP
                        val buttonState =
                            (event.buttonState shr 4) or (1 shl (if (toolType == MotionEvent.TOOL_TYPE_STYLUS) 0 else 30))

                        SDLActivity.onNativePen(
                            pointerId,
                            SDLActivity.getMotionListener()
                                .getPenDeviceType(event.device),
                            buttonState,
                            action,
                            x,
                            y,
                            p
                        )
                    }

                    else -> { // MotionEvent.TOOL_TYPE_FINGER or MotionEvent.TOOL_TYPE_UNKNOWN
                        pointerId =
                            if (isActionDownActive) event.getPointerId(i) else (event.getPointerId(i) - 1)
                        if (pointerId < 0) pointerId = 0
                        if (!isActionDownActive && pointerId == 0) {
                            action =
                                if (action == MotionEvent.ACTION_POINTER_DOWN) MotionEvent.ACTION_DOWN else MotionEvent.ACTION_UP
                        }
                        x = getNormalizedX(event.getX(i))
                        y = getNormalizedY(event.getY(i))
                        p = event.getPressure(i)
                        if (p > 1.0f) {
                            // may be larger than 1.0f on some devices
                            // see the documentation of getPressure(i)
                            p = 1.0f
                        }

                        SDLActivity.onNativeTouch(
                            touchDevId,
                            pointerId,
                            action,
                            x,
                            y,
                            p
                        )
                    }
                }

                // Non-primary up/down
                if (action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_POINTER_DOWN) {
                    isActionDownActive = false
                    break
                }
            } while (++i < pointerCount)

            SDLSurface.getScaleGestureDetector().onTouchEvent(event)

            return true
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .layout { measurable, constraints ->
                    widthSize = constraints.maxWidth
                    heightSize = constraints.maxHeight

                    if (SDLSurface.fixedWidth > 0) {
                        val myAspect = 1.0f * SDLSurface.fixedWidth / SDLSurface.fixedHeight
                        var resultWidth = widthSize.toFloat()
                        var resultHeight = resultWidth / myAspect
                        if (resultHeight > heightSize) {
                            resultHeight = heightSize.toFloat()
                            resultWidth = resultHeight * myAspect
                        }
                        mWidth = resultWidth
                        mHeight = resultHeight
                    } else {
                        mWidth = widthSize.toFloat()
                        mHeight = heightSize.toFloat()
                    }

                    val placeable = measurable.measure(
                        Constraints.fixed(mWidth.roundToInt(), mHeight.roundToInt())
                    )

                    layout(mWidth.roundToInt(), mHeight.roundToInt()) {
                        placeable.place(0, 0)
                    }
                }
                .alpha(0f)
                .pointerInteropFilter { motionEvent ->
                    onTouchEvent(motionEvent)
                    return@pointerInteropFilter true
                }
        )
    }

    companion object {
        const val SDL3_SCREEN_CONTROLLER_NAME = "SDL3_SCREEN_CONTROLLER"
    }
}