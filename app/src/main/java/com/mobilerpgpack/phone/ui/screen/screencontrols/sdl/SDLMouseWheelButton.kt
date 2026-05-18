package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.view.KeyEvent
import android.view.MotionEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.main.ONE_FRAME_DELAY
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.MouseViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.qualifier.named

abstract class SDLMouseWheelButton(
    id: String,
    engineType: EngineTypes,
    private val wheelUp : Boolean,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = ViewState.NOT_EXISTING_RES,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    invokeWheelEventsWhilePressingDefaultState : Boolean = false,
    showInQuickPanel : Boolean = false) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha, Int.MIN_VALUE, buttonResId, useToggle = false,
        defaultViewRenderRule,controlsType, isDeleted, consumeTouchEventsByDefault,
        ignoreOutOfBoundsTouchEvents, showInQuickPanel) {

    private val preferencesStorage : PreferencesStorage by inject ()

    private val wheelValue get() = if (wheelUp) preferencesStorage.zoomSensitivity.value!! else -1f *
            preferencesStorage.zoomSensitivity.value!!

    private val scope : CoroutineScope by inject (
        named(KoinModulesProvider.MAIN_THREAD_COROUTINE_KEY))

    private val mouseViewState = MouseViewState(
        id,
        engineType,
        offsetXPercent = offsetXPercent,
        offsetYPercent = offsetYPercent,
        sizePercent = sizePercent,
        buttonResId = buttonResId,
        alpha = alpha,
        defaultViewRenderRule = defaultViewRenderRule,
        controlsType = controlsType,
        isDeletedInitialState = isDeleted,
        invokeWheelEventsWhilePressingDefaultState = invokeWheelEventsWhilePressingDefaultState,
        showInQuickPanelInitialState = showInQuickPanel)

    final override val viewState get() = mouseViewState

    final override fun onTouchDown(keyCode: Int) {
        scope.coroutineContext.cancelChildren()
        if (mouseViewState.invokeWheelEventsWhilePressing.value!!){
            scope.launch { mouseWheelAsync() }
        }
        else{
            mouseWheel()
        }
    }

    final override fun onTouchUp(keyCode: Int) {
        scope.coroutineContext.cancelChildren()
    }

    protected abstract fun onMouseWheel (keyCode: Int, x : Float, y : Float, event: Int )

    private suspend fun mouseWheelAsync(){
        val wheelValue = this.wheelValue
        while (currentCoroutineContext().isActive){
            onMouseWheel(KeyEvent.KEYCODE_UNKNOWN,0f, wheelValue, MotionEvent.ACTION_SCROLL)
            delay(ONE_FRAME_DELAY)
        }
    }

    private fun mouseWheel() =
        onMouseWheel(KeyEvent.KEYCODE_UNKNOWN,0f, wheelValue, MotionEvent.ACTION_SCROLL)
}