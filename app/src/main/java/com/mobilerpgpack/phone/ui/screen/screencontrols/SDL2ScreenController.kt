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
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface
import kotlin.math.roundToInt

class SDL2ScreenController : ScreenController() {

    @Composable
    override fun DrawTouchCamera() {
        var mWidth by remember { mutableFloatStateOf(0.0f) }
        var mHeight by remember { mutableFloatStateOf(0.0f) }
        var isActionDownActive by remember { mutableStateOf(false) }
        var widthSize = 0
        var heightSize = 0

        fun onTouchEvent(event: MotionEvent): Boolean {
            var touchDevId = event.deviceId
            val pointerCount = event.pointerCount
            var action = event.actionMasked
            var pointerFingerId: Int
            var i = -1
            var x: Float
            var y: Float
            var p: Float

            if (touchDevId < 0) {
                touchDevId -= 1
            }

            when (action) {
                MotionEvent.ACTION_MOVE -> {
                    i = 0
                    while (i < pointerCount) {
                        pointerFingerId =
                            if (isActionDownActive) event.getPointerId(i) else (event.getPointerId(i) - 1)
                        if (pointerFingerId < 0) pointerFingerId = 0
                        x = event.getX(i) / mWidth
                        y = event.getY(i) / mHeight
                        p = event.getPressure(i)
                        if (p > 1.0f) p = 1.0f
                        SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action, x, y, p)
                        i++
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_DOWN -> {
                    isActionDownActive = event.actionMasked == MotionEvent.ACTION_DOWN
                    i = 0
                    if (i == -1) i = event.actionIndex
                    pointerFingerId = event.getPointerId(i)
                    x = event.getX(i) / mWidth
                    y = event.getY(i) / mHeight
                    p = event.getPressure(i)
                    if (p > 1.0f) p = 1.0f
                    SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action, x, y, p)
                }

                MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_POINTER_DOWN -> {
                    if (i == -1) i = event.actionIndex
                    pointerFingerId =
                        if (isActionDownActive) event.getPointerId(i) else (event.getPointerId(i) - 1)
                    if (pointerFingerId < 0) pointerFingerId = 0
                    if (!isActionDownActive && pointerFingerId == 0) {
                        action =
                            if (action == MotionEvent.ACTION_POINTER_DOWN) MotionEvent.ACTION_DOWN else MotionEvent.ACTION_UP
                    }
                    x = event.getX(i) / mWidth
                    y = event.getY(i) / mHeight
                    p = event.getPressure(i)
                    if (p > 1.0f) p = 1.0f
                    SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action, x, y, p)
                }

                MotionEvent.ACTION_CANCEL -> {
                    isActionDownActive = false
                    i = 0
                    while (i < pointerCount) {
                        pointerFingerId = event.getPointerId(i)
                        x = event.getX(i) / mWidth
                        y = event.getY(i) / mHeight
                        p = event.getPressure(i)
                        if (p > 1.0f) p = 1.0f
                        SDLActivity.onNativeTouch(
                            touchDevId,
                            pointerFingerId,
                            MotionEvent.ACTION_UP,
                            x,
                            y,
                            p
                        )
                        i++
                    }
                }

                else -> {}
            }

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

    companion object{
        const val SDL2_SCREEN_CONTROLLER_NAME = "SDL2_SCREEN_CONTROLLER"
    }
}
