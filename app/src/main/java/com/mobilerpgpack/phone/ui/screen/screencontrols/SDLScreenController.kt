package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.MotionEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt

abstract class SDLScreenController : ScreenController() {

    private val trackedPointerIds = mutableSetOf<Int>()

    protected abstract val viewWidth : Int

    protected abstract val viewHeight : Int

    @Composable
    final override fun DrawTouchCamera() {
        var mWidth by remember { mutableFloatStateOf(0.0f) }
        var mHeight by remember { mutableFloatStateOf(0.0f) }
        var widthSize: Int
        var heightSize: Int

        fun onTouchEvent(event: MotionEvent) {
            val action = event.actionMasked
            val actionIndex = event.actionIndex

            fun onPointerMoved (){
                for (pid in trackedPointerIds) {
                    val idx = event.findPointerIndex(pid)
                    if (idx >= 0) {
                        handlePointerAtIndex(idx, pid,
                            mWidth,mHeight,event)
                    }
                }
            }

            when (action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    val pid = event.getPointerId(actionIndex)
                    val xRaw = event.getX(actionIndex)
                    val yRaw = event.getY(actionIndex)
                    if (xRaw in 0f..mWidth && yRaw >= 0f && yRaw <= mHeight) {
                        trackedPointerIds.add(pid)
                        handlePointerAtIndex(actionIndex, pid,
                            mWidth,mHeight, event)
                    }
                }

                MotionEvent.ACTION_MOVE -> onPointerMoved()

                MotionEvent.ACTION_POINTER_UP -> {
                    val upPid = event.getPointerId(actionIndex)
                    if (trackedPointerIds.remove(upPid)) {
                        handlePointerAtIndex(actionIndex, upPid,
                            mWidth,mHeight,event)
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    onPointerMoved()
                    trackedPointerIds.clear()
                }
            }

            onMotionEventFinished(event)
        }
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
                .pointerInteropFilter { motionEvent ->
                    onTouchEvent(motionEvent)
                    return@pointerInteropFilter true
                }
        )
    }

    protected abstract fun handlePointerAtIndex(i: Int, pointerId: Int,
                                                viewWidth : Float, viewHeight : Float, event: MotionEvent)

    protected open fun onMotionEventFinished (event: MotionEvent){}
}