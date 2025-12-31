package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.view.InputDevice
import android.view.MotionEvent
import android.view.ViewTreeObserver
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.ScreenController
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.keyCodeMap
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import kotlin.math.roundToInt

abstract class SDLScreenController : ScreenController(), KoinComponent {

    private val customViews : MutableMap<EngineTypes, MutableMap<ControlsType,Collection<IScreenControlsView>>> = mutableMapOf()

    protected abstract val viewWidth : Int

    protected abstract val viewHeight : Int

    protected abstract fun getMouseX(): Float

    protected abstract fun getMouseY(): Float

    @Composable
    final override fun DrawTouchScreen(activeEngine : EngineTypes, blockTouchCameraEvents : Boolean, inSafeArea : Boolean,
                                       isEditMode: Boolean, inGame: Boolean, content: @Composable () -> Unit) {

        var rootSize by remember { mutableStateOf(IntSize.Zero) }
        val activity = LocalActivity.current!!
        val rootView = activity.window.decorView.rootView
        val inGame = remember { inGame }

        rootView.apply {
            DisposableEffect(this) {
                val listener = ViewTreeObserver.OnGlobalLayoutListener {
                    rootSize = IntSize(width, height)
                }
                viewTreeObserver.addOnGlobalLayoutListener(listener)
                onDispose {
                    viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        }

        val contentModifier = (if (inSafeArea) Modifier.safeDrawingPadding() else Modifier).run {
             return@run this.layout { measurable, constraints ->
                val width = rootSize.width.takeIf { it > 0 } ?: constraints.maxWidth
                val height = rootSize.height.takeIf { it > 0 } ?: constraints.maxHeight
                val placeable = measurable.measure(Constraints.fixed(width, height))
                layout(width, height) { placeable.place(0, 0) }
            }
        }

        if (!inGame){
            Box(modifier = contentModifier){ content() }
            return
        }

        val preferencesStorage : PreferencesStorage = koinInject()
        val activeEngine = rememberSaveable (activeEngine) { activeEngine.name }
        val engineInfo : IEngineInfo = koinInject(named(activeEngine))
        var mWidth by remember { mutableFloatStateOf(0.0f) }
        var mHeight by remember { mutableFloatStateOf(0.0f) }
        var widthSize by remember { mutableIntStateOf(0) }
        var heightSize by remember { mutableIntStateOf(0) }
        var trackedPointerId by remember { mutableIntStateOf(UNKNOWN_POINTER_ID) }
        val mouseButtonsEventsCanBeInvoked by engineInfo.mouseButtonsEventsCanBeInvokedAsFlow.collectAsState(initial = false)
        val useTouchFullScreenMode by rememberSaveable(preferencesStorage.alwaysUseFullScreenTouchMode,
            engineInfo.touchFullScreenModeCanBeUsed, mouseButtonsEventsCanBeInvoked) {
            mutableStateOf(preferencesStorage.alwaysUseFullScreenTouchMode && engineInfo.touchFullScreenModeCanBeUsed &&
                    !mouseButtonsEventsCanBeInvoked) }
        val blockTouchEventsSaved by rememberSaveable(blockTouchCameraEvents) { mutableStateOf(blockTouchCameraEvents) }
        var touchId by rememberSaveable { mutableStateOf<Int?>(null) }
        val enableTouchScreenPressingEvents by rememberSaveable(preferencesStorage.enableTouchScreenPressingEvents) {
            mutableStateOf(preferencesStorage.enableTouchScreenPressingEvents)
        }
        val blockTouchScreen by rememberSaveable(isEditMode, mouseButtonsEventsCanBeInvoked, blockTouchEventsSaved) {
            mutableStateOf(isEditMode || blockTouchEventsSaved)
        }
        val enableAbsoluteTouchMouseMode by rememberSaveable(preferencesStorage.enableAbsoluteTouchMouseMode) {
            mutableStateOf(preferencesStorage.enableAbsoluteTouchMouseMode)
        }
        val defaultTouchDeviceId = rememberSaveable { defaultTouchDeviceId }
        val UNKNOWN_POINTER_ID = rememberSaveable { UNKNOWN_POINTER_ID }

        var lastTouchX by rememberSaveable { mutableFloatStateOf(0f) }
        var lastTouchY by rememberSaveable { mutableFloatStateOf(0f) }
        var lastMouseX by rememberSaveable { mutableFloatStateOf(0f) }
        var lastMouseY by rememberSaveable { mutableFloatStateOf(0f) }
        var tracking by rememberSaveable { mutableStateOf(false) }

        fun clearResources(){
            if (trackedPointerId != UNKNOWN_POINTER_ID) {
                handlePointer(trackedPointerId, 0f, 0f, 0f,
                    mWidth, mHeight, MotionEvent.ACTION_UP,
                    touchId ?: defaultTouchDeviceId, false)
                lastTouchX = 0f
                lastTouchY = 0f
                lastMouseX = 0f
                lastMouseY = 0f
                tracking = false
                trackedPointerId = UNKNOWN_POINTER_ID
            }
        }

        LaunchedEffect(isEditMode, blockTouchScreen, mouseButtonsEventsCanBeInvoked) {
            clearResources()
        }

        DisposableEffect(Unit) {
            onDispose {
                clearResources()
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .layout { measurable, constraints ->
                widthSize = constraints.maxWidth
                heightSize = constraints.maxHeight

                if (viewWidth > 0 && !useTouchFullScreenMode) {
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
            .background(Color.Transparent)
            .pointerInput(
                isEditMode, mouseButtonsEventsCanBeInvoked, blockTouchScreen,
                enableTouchScreenPressingEvents, enableAbsoluteTouchMouseMode
            ) {
                if (isEditMode || blockTouchScreen) {
                    return@pointerInput
                }
                awaitPointerEventScope {
                    while (true) {
                        val useAbsoluteTouchMode = enableAbsoluteTouchMouseMode || !mouseButtonsEventsCanBeInvoked
                        val event = awaitPointerEvent()
                        for (change in event.changes) {
                            val pid = change.id.value.toInt()
                            val pos = change.position
                            val x = pos.x
                            val y = pos.y
                            val pressure = (change.pressure).coerceAtMost(1.0f)

                            fun handlePointerLocal(
                                touchAction: Int,
                                xPosition: Float,
                                yPosition: Float
                            ) {
                                touchId = event.motionEvent?.deviceId ?: defaultTouchDeviceId
                                handlePointer(
                                    trackedPointerId, pressure, xPosition, yPosition,
                                    mWidth, mHeight, touchAction,
                                    touchId!!,
                                    mouseButtonsEventsCanBeInvoked && enableTouchScreenPressingEvents
                                )
                            }

                            when {
                                change.changedToDown() -> {
                                    if (trackedPointerId == UNKNOWN_POINTER_ID) {
                                        trackedPointerId = pid

                                        if (useAbsoluteTouchMode) {
                                            lastTouchX = x
                                            lastTouchY = y
                                            lastMouseX = x
                                            lastMouseY = y
                                            tracking = true
                                            handlePointerLocal(MotionEvent.ACTION_DOWN, x, y)
                                        } else {
                                            lastTouchX = x
                                            lastTouchY = y
                                            lastMouseX = getMouseX()
                                            lastMouseY = getMouseY()
                                            tracking = true
                                            handlePointerLocal(
                                                MotionEvent.ACTION_DOWN, lastMouseX,
                                                lastMouseY
                                            )
                                        }
                                    }
                                }

                                change.changedToUp() -> {
                                    if (trackedPointerId == pid && tracking) {
                                        if (useAbsoluteTouchMode) {
                                            handlePointerLocal(MotionEvent.ACTION_UP, x, y)
                                        } else {
                                            val dx = x - lastTouchX
                                            val dy = y - lastTouchY
                                            var newMouseX = lastMouseX + dx
                                            var newMouseY = lastMouseY + dy
                                            newMouseX = newMouseX.coerceIn(0f, mWidth)
                                            newMouseY = newMouseY.coerceIn(0f, mHeight)
                                            handlePointerLocal(
                                                MotionEvent.ACTION_UP, newMouseX,
                                                newMouseY
                                            )
                                        }

                                        trackedPointerId = UNKNOWN_POINTER_ID
                                        tracking = false
                                    }
                                }

                                change.positionChanged() -> {
                                    if (trackedPointerId == pid && tracking) {
                                        if (useAbsoluteTouchMode) {
                                            handlePointerLocal(MotionEvent.ACTION_MOVE, x, y)
                                        } else {
                                            val dx = x - lastTouchX
                                            val dy = y - lastTouchY
                                            var newMouseX = lastMouseX + dx
                                            var newMouseY = lastMouseY + dy

                                            newMouseX = newMouseX.coerceIn(0f, mWidth)
                                            newMouseY = newMouseY.coerceIn(0f, mHeight)

                                            handlePointerLocal(
                                                MotionEvent.ACTION_MOVE, newMouseX,
                                                newMouseY
                                            )

                                            lastTouchX = x
                                            lastTouchY = y
                                            lastMouseX = newMouseX
                                            lastMouseY = newMouseY
                                        }
                                    }
                                }

                                !change.pressed && trackedPointerId == pid -> {
                                    if (useAbsoluteTouchMode) {
                                        handlePointerLocal(MotionEvent.ACTION_CANCEL, x, y)
                                    } else {
                                        handlePointerLocal(
                                            MotionEvent.ACTION_CANCEL,
                                            lastMouseX,
                                            lastMouseY
                                        )
                                    }
                                    trackedPointerId = UNKNOWN_POINTER_ID
                                    tracking = false
                                }
                            }
                        }

                        event.motionEvent?.let {
                            onMotionEventFinished(it)
                        }
                    }
                }
            }
        ){
            Box(modifier = contentModifier){ content() }
        }
    }

    protected abstract fun handlePointer(pointerId: Int, pressure: Float, x: Float, y: Float,
                                         viewWidth : Float, viewHeight : Float,eventAction : Int,
                                         touchDeviceId : Int,
                                         invokeMousePressingEvents : Boolean)

    protected open fun onMotionEventFinished (event: MotionEvent){}

    protected abstract fun buildCustomView (id : String, engineTypes: EngineTypes,
                                            keyCode : Int, controlsProvider: ControlsProvider) : IScreenControlsView

    final override fun buildCustomViews(engineTypes: EngineTypes): Collection<IScreenControlsView> {
        val preferencesStorage : PreferencesStorage = get ()
        val controlsProvider= get<ControlsProvider>(named(preferencesStorage.activeEngineString))
        return customViews.getOrPut(engineTypes) { mutableMapOf() }.run {
            getOrPut(controlsProvider.activeControlsType) { buildCustomViewsCollection(engineTypes, controlsProvider)}
        }
    }

    private fun buildCustomViewsCollection (engineTypes: EngineTypes, controlsProvider: ControlsProvider) : Collection<IScreenControlsView>{
        return mutableListOf<IScreenControlsView>().apply {
            keyCodeMap.values.forEach {
                this.add(buildCustomView(it.keyCodeName, engineTypes, it.keyCode, controlsProvider))
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