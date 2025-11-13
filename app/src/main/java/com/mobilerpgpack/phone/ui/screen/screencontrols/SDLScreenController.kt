package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt

abstract class SDLScreenController : ScreenController() {

    protected abstract val viewWidth : Int

    protected abstract val viewHeight : Int

    @Composable
    final override fun DrawTouchCamera() {
        var mWidth by remember { mutableFloatStateOf(0.0f) }
        var mHeight by remember { mutableFloatStateOf(0.0f) }
        var widthSize by remember { mutableIntStateOf(0) }
        var heightSize by remember { mutableIntStateOf(0) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .layout { measurable, constraints ->
                    widthSize = constraints.maxWidth
                    heightSize = constraints.maxHeight

                    if (viewWidth > 0) {
                        val myAspect = 1.0f * viewWidth / viewHeight
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
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            handlePointer(DEFAULT_POINTER_ID, DEFAULT_PRESSURE ,offset.x,
                                offset.y, MotionEvent.ACTION_DOWN, mWidth, mHeight)
                        },
                        onDrag = { change, _ ->
                            val pressure = (change.pressure).coerceAtMost(1.0f)
                            handlePointer(DEFAULT_POINTER_ID, pressure ,change.position.x
                                ,change.position.y, MotionEvent.ACTION_MOVE,
                                mWidth, mHeight)
                        },
                        onDragEnd = {
                            handlePointer(DEFAULT_POINTER_ID, DEFAULT_PRESSURE ,DEFAULT_SCREEN_POSITION,
                                DEFAULT_SCREEN_POSITION, MotionEvent.ACTION_UP, mWidth, mHeight)
                        },
                        onDragCancel = {
                            handlePointer(DEFAULT_POINTER_ID, DEFAULT_PRESSURE ,DEFAULT_SCREEN_POSITION,
                                DEFAULT_SCREEN_POSITION, MotionEvent.ACTION_CANCEL, mWidth, mHeight)
                        }
                    )
                }
        )
    }

    protected abstract fun handlePointer(pointerId: Int, pressure: Float, x: Float, y: Float, motionEvent : Int,
                                         viewWidth : Float, viewHeight : Float)

    protected companion object{

        const val DEFAULT_TOUCH_DEVICE_ID = -1

        private const val DEFAULT_POINTER_ID = 0

        private const val DEFAULT_PRESSURE = 1.0f

        private const val DEFAULT_SCREEN_POSITION = -1.0f
    }
}