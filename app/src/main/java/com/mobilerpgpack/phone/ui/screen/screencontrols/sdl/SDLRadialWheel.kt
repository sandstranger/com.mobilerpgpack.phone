@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.isOutOfBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.utils.IKeyCodesProvider
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

abstract class SDLRadialWheel(
    engineType: EngineTypes,
    private val offsetXPercent: Float = 0f,
    private val offsetYPercent: Float = 0f,
    private val sizePercent: Float = 0.25f,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,
    isDeleted: Boolean = false,
    consumeTouchEventsByDefault: Boolean = true,
    ignoreOutOfBoundsTouchEvents: Boolean = true,
    showInQuickPanel: Boolean = false) : IScreenControlsView {

    override var screenController: IScreenController? = null

    override val viewState: ViewState = ViewState(
        "radial_wheel",
        engineType,
        offsetXPercent = offsetXPercent,
        offsetYPercent = offsetYPercent,
        sizePercent = sizePercent,
        defaultViewRenderRule = defaultViewRenderRule,
        controlsType = controlsType,
        isDeletedInitialState = isDeleted,
        alwaysConsumeTouchEvents = false,
        sdlKeyEvent = Int.MIN_VALUE,
        consumeTouchEventsInitialState = consumeTouchEventsByDefault,
        touchEventsCanIgnoreOutOfBounds = true,
        ignoreOutOfBoundsTouchEventsInitialState = ignoreOutOfBoundsTouchEvents,
        showInQuickPanelInitialState = showInQuickPanel
    )

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) = DrawRadialWheel(isEditMode, inGame)

    protected abstract fun onItemSelected(keyCode : Int)

    @Composable
    private fun DrawRadialWheel(isEditMode: Boolean, inGame: Boolean) {
        val count = rememberSaveable { ITEMS_COUNT }
        val items = rememberSaveable { (0..count).map { it.toString() }.toList() }
        val anglePerItem = rememberSaveable { 360f / count }
        val backgroundColor = remember { Color.LightGray }
        val selectedColor = remember { Color.White }
        var selectedIndex by remember { mutableIntStateOf(-1) }
        var showRadialMenu by remember { mutableStateOf(false) }
        val keyCodesProvider = koinInject<IKeyCodesProvider>()
        var pointerId by remember { mutableStateOf<PointerId?>(null) }
        var hitRect by remember { mutableStateOf(Rect.Zero) }
        val viewState = remember { viewState }
        val preferencesStorage: PreferencesStorage = koinInject()
        val activeEngineString = remember(preferencesStorage.activeEngineString) {
            preferencesStorage.activeEngineString
        }
        val engineInfo: IEngineInfo = koinInject(named(activeEngineString))
        val ignoreOutOfBoundsTouchEvents by remember(viewState.ignoreOutOfBoundsTouchEvents)
        { mutableStateOf(viewState.ignoreOutOfBoundsTouchEvents) }
        val consumeTouchEvents by remember(viewState.consumeTouchEvents)
        { mutableStateOf(viewState.consumeTouchEvents) }
        val showInQuickPanel by remember(viewState.showInQuickPanel) {
            mutableStateOf(viewState.showInQuickPanel)
        }
        val viewRenderRule by remember(viewState.viewRenderRule) {
            mutableStateOf(viewState.viewRenderRule)
        }

        @Composable
        fun getMouseEventsCanBeInvokedFlow () : Boolean {
            if (inGame){
                val mouseButtonsEventsCanBeInvoked by engineInfo.mouseButtonsEventsCanBeInvokedAsFlow.collectAsState(
                    initial = false
                )
                return mouseButtonsEventsCanBeInvoked
            }
            return false
        }

        val mouseButtonsEventsCanBeInvoked = getMouseEventsCanBeInvokedFlow()

        fun clearResources(){
            pointerId = null
            selectedIndex = -1
            showRadialMenu = false
        }

        LaunchedEffect(isEditMode, mouseButtonsEventsCanBeInvoked, consumeTouchEvents,
            ignoreOutOfBoundsTouchEvents, showInQuickPanel, viewRenderRule) {
            clearResources()
            showRadialMenu = isEditMode
        }

        DisposableEffect(Unit) {
            onDispose {
                clearResources()
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .minimumInteractiveComponentSize()
                .pointerInput(isEditMode, inGame, mouseButtonsEventsCanBeInvoked,
                    consumeTouchEvents, ignoreOutOfBoundsTouchEvents, showInQuickPanel, viewRenderRule) {
                    if (isEditMode || !inGame){
                        return@pointerInput
                    }
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val center = Offset(size.width / 2f, size.height / 2f)

                            fun angle(pos: Offset): Float {
                                val dx = pos.x - center.x
                                val dy = pos.y - center.y
                                var a =
                                    Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                                a += 90f
                                if (a < 0f) a += 360f
                                return a
                            }

                            fun indexFromAngle(a: Float): Int =
                                (a / anglePerItem).toInt().coerceIn(0, count - 1)

                            for (change in event.changes) {
                                change.apply {
                                    when{
                                        changedToDown() && pointerId == null && hitRect.contains(position) -> {
                                            showRadialMenu = true
                                            pointerId = id
                                            selectedIndex = indexFromAngle(angle(position))
                                        }

                                        (changedToUp() || !pressed || ( isOutOfBounds(size, extendedTouchPadding)
                                                    && !ignoreOutOfBoundsTouchEvents) ) && pointerId == id -> {
                                            pointerId = null
                                            showRadialMenu = false
                                            if (selectedIndex >= 0) {
                                                onItemSelected(keyCodesProvider.getKeyCode(
                                                    items[selectedIndex].first()))
                                            }
                                            selectedIndex = -1
                                        }

                                        positionChanged() && pointerId == id -> {
                                            val idx = indexFromAngle(angle(change.position))
                                            if (idx != selectedIndex) {
                                                selectedIndex = idx
                                            }
                                        }
                                    }

                                    val consumeEvents = (consumeTouchEvents || mouseButtonsEventsCanBeInvoked) && pointerId!=null
                                    if (consumeEvents) {
                                        consume()
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
            if (showRadialMenu) {
                Canvas(Modifier.fillMaxSize()) {
                    val radius = size.minDimension / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val minDim = min(size.width, size.height)

                    drawCircle(backgroundColor,
                        radius,
                        center = center,
                        style = Stroke(width = 2.dp.toPx()))

                    for (i in 0 until count) {
                        val start = -90f + i * anglePerItem
                        val isSelected = i == selectedIndex
                        val strokeWidthPx = if (isSelected) 4.dp.toPx() else 2.dp.toPx()
                        val ignoreDrawingArx = (i + 1 == selectedIndex || i - 1 == selectedIndex || (i == 0 && count -1 == selectedIndex)
                                || (i + 1 >= count && selectedIndex == 0)) && selectedIndex >=0
                        val textSizeInPx = if(isSelected) minDim / 5f else minDim / 6f

                        if (!ignoreDrawingArx) {
                            drawArc(
                                color = if (isSelected) selectedColor else backgroundColor,
                                startAngle = start,
                                sweepAngle = anglePerItem,
                                useCenter = true,
                                topLeft = Offset(center.x - radius, center.y - radius),
                                size = Size(radius * 2, radius * 2),
                                style = Stroke(width = strokeWidthPx, join = StrokeJoin.Miter)
                            )
                        }

                        val mid = Math.toRadians((start + anglePerItem / 2).toDouble())
                        val rText = radius * 0.65f
                        val tx = center.x + cos(mid).toFloat() * rText
                        val ty = center.y + sin(mid).toFloat() * rText

                        drawIntoCanvas {
                            val paint = android.graphics.Paint().apply {
                                color =
                                    if (isSelected) android.graphics.Color.WHITE else android.graphics.Color.LTGRAY
                                textSize = textSizeInPx
                                isAntiAlias = true
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                            it.nativeCanvas.drawText(
                                items[i],
                                tx,
                                ty + textSizeInPx / 3f,
                                paint
                            )
                        }
                    }
                }
            } else {
                val sizeDivider = rememberSaveable { 0.3f }
                val w by remember (maxWidth) { mutableStateOf(maxWidth * sizeDivider) }
                val h by remember(maxHeight) { mutableStateOf(maxHeight * sizeDivider) }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(w, h)
                        .onGloballyPositioned { coords ->
                            val pos = coords.positionInParent()
                            val size = coords.size
                            hitRect = Rect(
                                pos.x,
                                pos.y,
                                pos.x + size.width,
                                pos.y + size.height
                            )
                        }) {
                    Canvas(Modifier.fillMaxSize()) {
                        val strokeWidthPx = 2.dp.toPx()
                        val radius = size.minDimension / 2f - strokeWidthPx
                        val center = Offset(size.width / 2f, size.height / 2f)
                        drawCircle(backgroundColor,
                            radius,
                            center = center,
                            style = Stroke(width = strokeWidthPx))
                    }
                }
            }
        }
    }

    private companion object{
        private const val ITEMS_COUNT = 10
    }
}