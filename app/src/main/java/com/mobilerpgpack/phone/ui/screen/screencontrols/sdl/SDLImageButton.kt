package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES

abstract class SDLImageButton(
    private val id: String,
    engineType: EngineTypes,
    private val offsetXPercent: Float = 0f,
    private val offsetYPercent: Float = 0f,
    private val sizePercent: Float = 0.13f,
    private val alpha: Float = 0.65f,
    private val sdlKeyEvent: Int = 0,
    private val buttonResId: Int = NOT_EXISTING_RES,
    useToggle: Boolean = false,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,
    isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true) : IScreenControlsView {

    private var isPressed by mutableStateOf(false)

    final override var show: Boolean by mutableStateOf(true)

    override val viewState: ViewState = ViewState(
        id,
        engineType,
        offsetXPercent = offsetXPercent,
        offsetYPercent = offsetYPercent,
        sizePercent = sizePercent,
        buttonResId = buttonResId,
        sdlKeyEvent = sdlKeyEvent,
        alpha = alpha,
        defaultViewRenderRule = defaultViewRenderRule,
        controlsType = controlsType,
        allowToUseViewAsToggle = true,
        useViewAsToggleInitialState = useToggle,
        isDeletedInitialState = isDeleted,
        alwaysConsumeTouchEvents = false,
        consumeTouchEventsInitialState = consumeTouchEventsByDefault)

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        Image(
            painter = painterResource(id = viewState.buttonResId),
            contentDescription = id,
            modifier = Modifier.interactiveControlModifier(isEditMode, inGame))
    }

    protected abstract fun onTouchDown(keyCode: Int)

    protected abstract fun onTouchUp(keyCode: Int)

    @Composable
    protected fun Modifier.interactiveControlModifier (isEditMode: Boolean, inGame: Boolean) : Modifier{
        return this
            .fillMaxSize()
            .minimumInteractiveComponentSize()
            .pointerInput(Unit) {
                if (isEditMode || !inGame) {
                    return@pointerInput
                }
                awaitEachGesture {
                    viewState.apply {
                        val down = awaitFirstDown(pass = if (consumeTouchEvents) PointerEventPass.Initial
                        else PointerEventPass.Main)
                        if (consumeTouchEvents){
                            down.consume()
                        }
                        if (!this.useViewAsToggle || !isPressed) {
                            onTouchDown(this.sdlKeyCode)
                        }
                        else if (this.useViewAsToggle && isPressed){
                            onTouchUp(sdlKeyCode)
                        }
                        val up = waitForUpOrCancellation()
                        if (consumeTouchEvents){
                            up?.consume()
                        }
                        if (!useViewAsToggle) {
                            onTouchUp(sdlKeyCode)
                        }
                        isPressed=!isPressed
                    }
                }
            }
    }
}