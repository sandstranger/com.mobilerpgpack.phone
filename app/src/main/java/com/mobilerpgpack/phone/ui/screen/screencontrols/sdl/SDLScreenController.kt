package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.view.InputDevice
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
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.ScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.CustomSDL2Button
import com.mobilerpgpack.phone.utils.getBlockingValue
import com.mobilerpgpack.phone.utils.keyCodeMap
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.get
import org.koin.core.qualifier.named
import kotlin.math.roundToInt

abstract class SDLScreenController : ScreenController() {

    private val customViews : MutableMap<EngineTypes, MutableMap<ControlsType,Collection<IScreenControlsView>>> = mutableMapOf()

    protected abstract val viewWidth : Int

    protected abstract val viewHeight : Int

    protected val controlsProvider get() =
        get<ControlsProvider>(named(preferencesStorage.activeEngineAsFlowString.getBlockingValue()))

    protected val engineInfo by lazy {
        get <IEngineInfo> (named(preferencesStorage.activeEngineAsFlowString.getBlockingValue()))
    }

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
                                        event.motionEvent?.deviceId ?: defaultTouchDeviceId)
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

                            event.motionEvent?.let {
                                onMotionEventFinished(it)
                            }
                        }
                    }
                }
        )
    }

    protected abstract fun handlePointer(pointerId: Int, pressure: Float, x: Float, y: Float,
                                         viewWidth : Float, viewHeight : Float,eventAction : Int, touchDeviceId : Int)

    protected open fun onMotionEventFinished (event: MotionEvent){}

    protected abstract fun buildCustomView (id : String, engineTypes: EngineTypes, keyCode : Int) : IScreenControlsView

    final override fun buildCustomViews(engineTypes: EngineTypes): Collection<IScreenControlsView> {
        return customViews.getOrPut(engineTypes) { mutableMapOf() }.run {
            getOrPut(controlsProvider.activeControlsType) { buildCustomViewsCollection(engineTypes)}
        }
    }

    private fun buildCustomViewsCollection (engineTypes: EngineTypes) : Collection<IScreenControlsView>{
        return mutableListOf<IScreenControlsView>().apply {
            keyCodeMap.forEach {
                this.add(buildCustomView(it.value, engineTypes, it.key))
            }
        }
    }

    private companion object{
        private const val UNKNOWN_POINTER_ID = Int.MIN_VALUE

        private val defaultTouchDeviceId : Int by lazy {
            InputDevice.getDeviceIds()
                .map { InputDevice.getDevice(it) }
                .firstOrNull { it != null && it.sources and InputDevice.SOURCE_TOUCHSCREEN ==
                        InputDevice.SOURCE_TOUCHSCREEN }?.id ?: -1
        }
    }
}