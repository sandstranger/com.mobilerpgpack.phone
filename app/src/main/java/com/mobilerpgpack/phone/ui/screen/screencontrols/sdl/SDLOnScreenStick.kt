package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.sun.jna.Native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.roundToInt

abstract class SDLOnScreenStick(engineType: EngineTypes,
                                stickType : StickType = StickType.LeftStick,
                                private val offsetXPercent: Float = 0f,
                                private val offsetYPercent: Float = 0f,
                                private val sizePercent: Float = 0.13f,
                                private val alpha: Float = 0.65f,
                                defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
                                controlsType: ControlsType = ControlsType.Default,
                                isDeleted : Boolean = false,
                                showInQuickPanel : Boolean = false) : IScreenControlsView, KoinComponent {

    private val scope : CoroutineScope by inject ()
    private val axisX = stickType.value * 2
    private val axisY = stickType.value * 2 + 1

    protected val engineInfo by lazy {
        val preferencesStorage : PreferencesStorage = get()
        get <IEngineInfo> (named(preferencesStorage.activeEngineString))
    }

    final override var screenController: IScreenController? = null

    private external fun createVirtualController()

    private external fun destroyVirtualController()

    private external fun setVirtualAxis(axis : Int, axisValue : Float)

    protected abstract val virtualControllerLibraryName : String

    final override val viewState: ViewState = ViewState(
        if (stickType == StickType.LeftStick) LEFT_STICK_ID else RIGHT_STICK_ID,
        engineType,
        offsetXPercent = offsetXPercent,
        offsetYPercent = offsetYPercent,
        sizePercent = sizePercent,
        alpha = alpha,
        defaultViewRenderRule = defaultViewRenderRule,
        controlsType = controlsType,
        isDeletedInitialState = isDeleted,
        showInQuickPanelInitialState = showInQuickPanel)

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) = DrawStick(isEditMode, inGame)

    @Composable
    private fun DrawStick(isEditMode: Boolean, inGame: Boolean) {
        fun updateStick(x: Float, y: Float, invokeUpdateStickEventForced : Boolean = false) {
            if ((isEditMode || !inGame) && !invokeUpdateStickEventForced){
                return
            }

            if (!joystickRegistered) {
                joystickRegistered = true
                scope.launch {
                    Native.register(SDLOnScreenStick::class.java, virtualControllerLibraryName)
                    createVirtualController()
                    engineInfo.rescanGameControllers()
                    joystickRegisteredInSDL = true
                }
            }

            if (joystickRegisteredInSDL) {
                val processedX = when {
                    abs(x) < STICK_DEAD_ZONE -> 0f
                    x > 0 -> (x * STICK_SCALE).coerceAtMost(1f)
                    else -> (x * STICK_SCALE).coerceAtLeast(-1f)
                }
                val processedY = when {
                    abs(y) < STICK_DEAD_ZONE -> 0f
                    y > 0 -> (y * STICK_SCALE).coerceAtMost(1f)
                    else -> (y * STICK_SCALE).coerceAtLeast(-1f)
                }
                setVirtualAxis(axisX, processedX)
                setVirtualAxis(axisY, processedY)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DrawStick(
                isEditMode, inGame,
                onUpdateStick = ::updateStick
            )
        }
    }

    @Composable
    private fun DrawStick(
        isEditMode: Boolean,
        inGame: Boolean,
        onUpdateStick: (Float, Float, Boolean) -> Unit
    ) {
        val inGame = remember { inGame }
        val isEditMode by remember (isEditMode) { mutableStateOf(isEditMode) }
        var currentX by remember { mutableFloatStateOf(-1f) }
        var currentY by remember { mutableFloatStateOf(-1f) }
        var down by remember { mutableStateOf(false) }
        var dragId by remember { mutableStateOf<PointerId?>(null) }
        var canvasW by remember { mutableIntStateOf(0) }
        var canvasH by remember { mutableIntStateOf(0) }
        val viewState = remember { viewState }
        val showInQuickPanel by remember (viewState.showInQuickPanel) {
            mutableStateOf(viewState.showInQuickPanel)
        }
        val viewRenderRule by remember (viewState.viewRenderRule) {
            mutableStateOf(viewState.viewRenderRule)
        }

        fun clearResources(){
            if (inGame && (dragId!=null || down)){
                onUpdateStick(0f, 0f, true)
                currentX = -1f
                currentY = -1f
                dragId = null
                down = false
            }
        }

        LaunchedEffect (isEditMode, inGame, showInQuickPanel, viewRenderRule) {
            clearResources()
        }

        DisposableEffect(Unit) {
            onDispose {
                clearResources()
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    clip = false
                    compositingStrategy = CompositingStrategy.ModulateAlpha
                }
                .minimumInteractiveComponentSize()
                .onSizeChanged { size ->
                    canvasW = size.width
                    canvasH = size.height
                }
                .pointerInput(isEditMode,inGame, showInQuickPanel, viewRenderRule) {
                    if (isEditMode || !inGame) {
                        return@pointerInput
                    }

                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            for (change in event.changes) {
                                change.apply {
                                    val pos = position
                                    val x = pos.x
                                    val y = pos.y

                                    when{
                                        changedToDown() && dragId == null -> {
                                            dragId = id
                                            down = true
                                            currentX = x
                                            currentY = y
                                        }

                                        (changedToUp() || !pressed) && dragId == id -> {
                                            down = false
                                            currentX = -1f
                                            currentY = -1f
                                            onUpdateStick(0f, 0f, false)
                                            dragId = null
                                        }

                                        positionChanged() && dragId == id -> run {
                                            currentX = x
                                            currentY = y
                                            val strokeWidthPx = 2.dp.toPx()
                                            val w = canvasW.toFloat().takeIf { it > 0f } ?: return@run
                                            val h = canvasH.toFloat().takeIf { it > 0f } ?: return@run
                                            val minDim = min(w, h)

                                            val outerRadius = minDim / 2f - strokeWidthPx
                                            val knobRadius = minDim / 5f
                                            val allowedRadius = outerRadius - knobRadius
                                            val overshoot = knobRadius * 0.3f
                                            val maxAllowed = allowedRadius + overshoot

                                            val centerX = w / 2f
                                            val centerY = h / 2f

                                            var vx = currentX - centerX
                                            var vy = currentY - centerY
                                            val dist = hypot(vx, vy)

                                            if (dist > maxAllowed && dist > 0f) {
                                                val s = maxAllowed / dist
                                                vx *= s
                                                vy *= s
                                            }

                                            val drawX = (centerX + vx).coerceIn(knobRadius, w - knobRadius)
                                            val drawY = (centerY + vy).coerceIn(knobRadius, h - knobRadius)

                                            val normX = ((drawX - centerX) /
                                                    (allowedRadius.coerceAtLeast(1f))).coerceIn(-1f, 1f)
                                            val normY = ((drawY - centerY) /
                                                    (allowedRadius.coerceAtLeast(1f))).coerceIn(-1f, 1f)

                                            onUpdateStick(normX, normY,false)
                                        }
                                    }

                                    consume()
                                }
                            }
                        }
                    }
                }
        ) {
            val w = canvasW.toFloat().takeIf { it > 0f } ?: size.width
            val h = canvasH.toFloat().takeIf { it > 0f } ?: size.height
            val minDim = min(w, h)
            val strokeWidthPx = 2.dp.toPx()
            val outerRadius = minDim / 2f - strokeWidthPx
            val knobRadius = minDim / 5f
            val allowedRadius = outerRadius - knobRadius
            val overshoot = knobRadius * 0.3f
            val maxAllowed = allowedRadius + overshoot

            val centerX = w / 2f
            val centerY = h / 2f

            drawCircle(
                color = Color.Gray,
                radius = outerRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = strokeWidthPx)
            )

            if (down) {
                var vx = currentX - centerX
                var vy = currentY - centerY
                val dist = hypot(vx, vy)

                if (dist > maxAllowed && dist > 0f) {
                    val s = maxAllowed / dist
                    vx *= s
                    vy *= s
                }

                var drawX = centerX + vx
                var drawY = centerY + vy

                drawX = drawX.coerceIn(knobRadius, w - knobRadius)
                drawY = drawY.coerceIn(knobRadius, h - knobRadius)

                drawCircle(
                    color = Color.Gray,
                    radius = knobRadius,
                    center = Offset(drawX, drawY),
                    style = Stroke(width = strokeWidthPx)
                )
            }
        }
    }

    private companion object{
        private const val STICK_DEAD_ZONE = 0.05f
        private const val STICK_SCALE = 1.0f
        private const val LEFT_STICK_ID = "left_onscreen_stick"
        private const val RIGHT_STICK_ID = "right_onscreen_stick"
        private var joystickRegistered by mutableStateOf(false)
        @Volatile
        private var joystickRegisteredInSDL = false
    }
}

