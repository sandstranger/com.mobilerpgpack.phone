package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.view.MotionEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.MouseViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class SDLMouseWheelButton(
    id: String,
    engineType: EngineTypes,
    private val wheelUp : Boolean,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = ViewState.NOT_EXISTING_RES,
    override val isQuickPanel: Boolean = false,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    invokeWheelEventsWhilePressingDefaultState : Boolean = false) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha, Int.MIN_VALUE, buttonResId, useToggle = false,
        defaultViewRenderRule,controlsType, isDeleted, consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents) {

    private val scope = CoroutineScope(Dispatchers.Default)

    val mouseViewState = MouseViewState(
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
        invokeWheelEventsWhilePressingDefaultState = invokeWheelEventsWhilePressingDefaultState)

    final override val viewState get() = mouseViewState

    init {
        show = !isQuickPanel
    }

    final override fun onTouchDown(keyCode: Int) {
        if (mouseViewState.invokeWheelEventsWhilePressing){
            scope.launch { mouseWheelAsync(keyCode) }
        }
        else{
            mouseWheel(keyCode)
        }
    }

    final override fun onTouchUp(keyCode: Int) {
        scope.coroutineContext.cancelChildren()
    }

    protected abstract fun onMouseWheel (keyCode: Int, x : Float, y : Float, event: Int )

    private suspend fun mouseWheelAsync(keyCode: Int){
        val wheelPosition = if (wheelUp) 100.0f else -1f * 100f
        while (true){
            onMouseWheel(keyCode,0f, wheelPosition, MotionEvent.ACTION_SCROLL)
            delay(32)
        }
    }

    private fun mouseWheel(keyCode: Int){
        onMouseWheel(keyCode,0f, if (wheelUp) DEFAULT_POSITION else -DEFAULT_POSITION,
            MotionEvent.ACTION_SCROLL)
    }

    private companion object{
        private const val DEFAULT_POSITION = 500.0f
    }
}