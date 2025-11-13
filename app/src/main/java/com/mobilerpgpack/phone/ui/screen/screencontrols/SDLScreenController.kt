package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
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
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
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
        var trackedPointerId by remember { mutableIntStateOf(UNKNOWN_POINTER_ID) }

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
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            for (change in event.changes) {
                                val pid = change.id.value.toInt()
                                val pos = change.position
                                val x = pos.x
                                val y = pos.y
                                val pressure = (change.pressure).coerceAtMost(1.0f)

                                fun handlePointer(touchAction: Int) {
                                    handlePointer(trackedPointerId, pressure, x, y,
                                        mWidth, mHeight,touchAction,
                                        event.motionEvent!!)
                                }

                                when {
                                    change.changedToDown() -> {
                                        if (trackedPointerId==UNKNOWN_POINTER_ID) {
                                            trackedPointerId = pid
                                            handlePointer(MotionEvent.ACTION_DOWN)
                                        }
                                    }

                                    change.changedToUp() -> {
                                        if (trackedPointerId == pid){
                                            handlePointer(MotionEvent.ACTION_UP)
                                            trackedPointerId = UNKNOWN_POINTER_ID
                                        }
                                    }

                                    change.positionChanged() -> {
                                        if (trackedPointerId == pid) {
                                            handlePointer(MotionEvent.ACTION_MOVE)
                                        }
                                    }

                                    !change.pressed && trackedPointerId == pid -> {
                                        handlePointer(MotionEvent.ACTION_CANCEL)
                                        trackedPointerId = UNKNOWN_POINTER_ID
                                    }
                                }
                            }
                            onMotionEventFinished(event.motionEvent!!)
                        }
                    }
                }
        )
    }

    protected abstract fun handlePointer(pointerId: Int, pressure: Float, x: Float, y: Float,
                                         viewWidth : Float, viewHeight : Float,eventAction : Int, event : MotionEvent)

    protected open fun onMotionEventFinished (event: MotionEvent){}

    private companion object{
        private const val UNKNOWN_POINTER_ID = Int.MIN_VALUE
    }
}