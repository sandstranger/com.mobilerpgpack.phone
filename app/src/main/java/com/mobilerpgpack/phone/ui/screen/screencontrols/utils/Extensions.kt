package com.mobilerpgpack.phone.ui.screen.screencontrols.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlendModeColorFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.isOutOfBounds
import androidx.compose.ui.input.pointer.pointerInput
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun Modifier.onTouchDown(
    isEditMode: Boolean, ignoreConsuming: Boolean = false,
    onTouchDown: () -> Unit
): Modifier {
    val preferencesStorage: PreferencesStorage = koinInject()
    val activeEngineString = remember { preferencesStorage.activeEngineString.value!! }
    val engineInfo: IEngineInfo = koinInject(named(activeEngineString))
    val mouseButtonsEventsCanBeInvoked by engineInfo.mouseButtonsEventsCanBeInvokedAsFlow.collectAsState(
        initial = false
    )
    var pointerId by remember { mutableStateOf<PointerId?>(null) }
    val ignoreConsuming = remember(ignoreConsuming) { ignoreConsuming }

    LaunchedEffect(isEditMode, mouseButtonsEventsCanBeInvoked) {
        pointerId = null
    }

    DisposableEffect(Unit) {
        onDispose {
            pointerId = null
        }
    }

    return this.pointerInput(isEditMode, mouseButtonsEventsCanBeInvoked) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                for (change in event.changes) {
                    change.apply {
                        when{
                            changedToDown() && pointerId == null -> {
                                pointerId = id
                                onTouchDown()
                            }

                            (changedToUp() || isOutOfBounds(size, extendedTouchPadding) ||
                                    !pressed) && pointerId == id -> {
                                pointerId = null
                            }
                        }

                        if (!ignoreConsuming) {
                            consume()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Modifier.touchListenerModifier(
    isEditMode: Boolean, viewState: ViewState, changeItemColor: Boolean = true,
    onTouchDown: () -> Unit = {}, onTouchUp: () -> Unit = {},
    onColorFilterChanged : (colorFilter : BlendModeColorFilter?) -> Unit = {}): Modifier {
    var inToggleMode by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    val isEditMode by remember(isEditMode) { mutableStateOf(isEditMode) }
    val viewState = remember { viewState }
    val preferencesStorage: PreferencesStorage = koinInject()
    val activeEngineString = remember { preferencesStorage.activeEngineString.value!! }
    val engineInfo: IEngineInfo = koinInject(named(activeEngineString))
    val mouseButtonsEventsCanBeInvoked by engineInfo.mouseButtonsEventsCanBeInvokedAsFlow.collectAsState(
        initial = false
    )
    val ignoreOutOfBoundsTouchEvents =viewState.ignoreOutOfBoundsTouchEvents.getComposableValue()
    val consumeTouchEvents =viewState.consumeTouchEvents.getComposableValue()
    val useViewAsToggle = viewState.useViewAsToggle.getComposableValue()
    var pointerId by remember { mutableStateOf<PointerId?>(null) }
    val sdlKeyCode = viewState.sdlKeyCode.getComposableValue()
    val showInQuickPanel by remember(viewState.showInQuickPanel) {
        mutableStateOf(viewState.showInQuickPanel)
    }
    val viewRenderRule by remember(viewState.viewRenderRule) {
        mutableStateOf(viewState.viewRenderRule)
    }
    val yellowColorFilter = retain { BlendModeColorFilter(Color.Yellow, BlendMode.SrcAtop) }

    fun clearResources() {
        pointerId = null
        if (inToggleMode || isPressed) {
            onTouchUp()
        }
        isPressed = false
        inToggleMode = false
    }

    LaunchedEffect(isEditMode, mouseButtonsEventsCanBeInvoked, consumeTouchEvents,
        ignoreOutOfBoundsTouchEvents, useViewAsToggle, sdlKeyCode, showInQuickPanel, viewRenderRule) {
        clearResources()
    }

    LaunchedEffect(inToggleMode, isEditMode, useViewAsToggle) {
        onColorFilterChanged(if (inToggleMode && !isEditMode && useViewAsToggle) yellowColorFilter else null)
    }

    DisposableEffect(Unit) {
        onDispose {
            clearResources()
        }
    }

    return this.pointerInput(
        isEditMode, mouseButtonsEventsCanBeInvoked,
        consumeTouchEvents, ignoreOutOfBoundsTouchEvents, useViewAsToggle,
        sdlKeyCode, showInQuickPanel, viewRenderRule
    ) {
        if (isEditMode) {
            return@pointerInput
        }

        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                val consumeEvents = consumeTouchEvents || mouseButtonsEventsCanBeInvoked
                for (change in event.changes) {
                    change.apply {
                        when{
                            changedToDown() && pointerId == null -> {
                                pointerId = id
                                if (!useViewAsToggle) {
                                    isPressed = true
                                    onTouchDown()
                                } else {
                                    if (!inToggleMode) {
                                        onTouchDown()
                                    } else {
                                        onTouchUp()
                                    }
                                    inToggleMode = !inToggleMode
                                }
                            }

                            (changedToUp() || (isOutOfBounds(size, extendedTouchPadding) && !ignoreOutOfBoundsTouchEvents) ||
                                        !pressed) && pointerId == id -> {
                                pointerId = null
                                if (!useViewAsToggle) {
                                    isPressed = false
                                    onTouchUp()
                                }
                            }
                        }

                        if (consumeEvents) {
                            consume()
                        }
                    }
                }
            }
        }
    }
}