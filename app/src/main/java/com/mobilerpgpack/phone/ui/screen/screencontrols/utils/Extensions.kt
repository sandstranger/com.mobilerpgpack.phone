package com.mobilerpgpack.phone.ui.screen.screencontrols.utils

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.isOutOfBounds
import androidx.compose.ui.input.pointer.pointerInput
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun Modifier.touchListenerModifier (isEditMode: Boolean, viewState: ViewState,
                                    onTouchDown : () -> Unit = {}, onTouchUp : () -> Unit = {}) : Modifier {

    var isPressed by rememberSaveable { mutableStateOf(false) }
    val isEditMode by remember (isEditMode) { mutableStateOf(isEditMode) }
    val viewState = remember { viewState }
    val preferencesStorage : PreferencesStorage = koinInject()
    val activeEngineString = remember (preferencesStorage.activeEngineString) {
        preferencesStorage.activeEngineString }
    val engineInfo : IEngineInfo = koinInject(named(activeEngineString))
    val mouseButtonsEventsCanBeInvoked by engineInfo.mouseButtonsEventsCanBeInvokedAsFlow.collectAsState(initial = false)
    val ignoreOutOfBoundsTouchEvents by remember (viewState.ignoreOutOfBoundsTouchEvents)
    { mutableStateOf(viewState.ignoreOutOfBoundsTouchEvents) }
    val consumeTouchEvents by remember (viewState.consumeTouchEvents)
    { mutableStateOf(viewState.consumeTouchEvents) }
    val useViewAsToggle by remember (viewState.useViewAsToggle) { mutableStateOf(viewState.useViewAsToggle) }
    var pointerId by remember { mutableStateOf<PointerId?>(null) }
    val sdlKeyCode by remember (viewState.sdlKeyCode) { mutableIntStateOf(viewState.sdlKeyCode) }
    val colorFilterToUse by remember (isPressed, isEditMode, useViewAsToggle) { mutableStateOf(
        ColorFilter.tint(if (isPressed && !isEditMode && useViewAsToggle) Color.Yellow else Color.White)
    ) }
    val showInQuickPanel by remember (viewState.showInQuickPanel) {
        mutableStateOf(viewState.showInQuickPanel)
    }
    val viewRenderRule by remember (viewState.viewRenderRule) {
        mutableStateOf(viewState.viewRenderRule)
    }

    fun clearResources(){
        pointerId = null
        isPressed = false
        onTouchUp()
    }

    LaunchedEffect(isEditMode, mouseButtonsEventsCanBeInvoked, consumeTouchEvents,
        ignoreOutOfBoundsTouchEvents, useViewAsToggle,sdlKeyCode,showInQuickPanel,viewRenderRule) {
        clearResources()
    }

    DisposableEffect(Unit) {
        onDispose {
            clearResources()
        }
    }

    return this.pointerInput(isEditMode,mouseButtonsEventsCanBeInvoked,
        consumeTouchEvents, ignoreOutOfBoundsTouchEvents, useViewAsToggle,
        sdlKeyCode,showInQuickPanel,viewRenderRule) {
        if (isEditMode) {
            return@pointerInput
        }

        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                val consumeEvents = consumeTouchEvents || mouseButtonsEventsCanBeInvoked
                for (change in event.changes) {
                    val pid = change.id

                    when {
                        change.changedToDown() -> {
                            if (pointerId == null) {
                                pointerId = pid
                                if (!useViewAsToggle) {
                                    onTouchUp()
                                    onTouchDown()
                                } else {
                                    if (!isPressed){
                                        onTouchUp()
                                        onTouchDown()
                                    }
                                    else{
                                        onTouchUp()
                                    }
                                    isPressed = !isPressed
                                }
                            }
                        }

                        change.changedToUp() || change.isOutOfBounds(size, extendedTouchPadding) &&
                                !ignoreOutOfBoundsTouchEvents -> {
                            if (pointerId == pid){
                                pointerId = null
                                if (!useViewAsToggle) {
                                    onTouchUp()
                                }
                            }
                        }
                    }
                    if (consumeEvents) {
                        change.consume()
                    }
                }
            }
        }
    }.graphicsLayer { colorFilter = colorFilterToUse }
}