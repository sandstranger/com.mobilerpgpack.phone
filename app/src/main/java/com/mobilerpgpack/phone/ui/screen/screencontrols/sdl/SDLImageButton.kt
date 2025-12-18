package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getBlockingValue
import com.mobilerpgpack.phone.utils.waitForUpOrCancellation
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

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
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false) : IScreenControlsView, KoinComponent {

    private val engineInfo by lazy {
        val preferencesStorage : PreferencesStorage = get()
        get <IEngineInfo> (named(preferencesStorage.activeEngineAsFlowString.getBlockingValue()))
    }

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
        consumeTouchEventsInitialState = consumeTouchEventsByDefault,
        touchEventsCanIgnoreOutOfBounds = true,
        ignoreOutOfBoundsTouchEventsInitialState = ignoreOutOfBoundsTouchEvents)

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        Image(painter = painterResource(id = viewState.buttonResId),
            contentDescription = id,
            modifier = Modifier.interactiveControlModifier(isEditMode, inGame).let{
                if (isPressed && !isEditMode && inGame && viewState.useViewAsToggle)
                    it.graphicsLayer { colorFilter = ColorFilter.tint(color = Color.Yellow) } else it
            } )
    }

    protected abstract fun onTouchDown(keyCode: Int)

    protected abstract fun onTouchUp(keyCode: Int)

    @Composable
    protected fun Modifier.interactiveControlModifier (isEditMode: Boolean, inGame: Boolean) : Modifier{
        return this
            .fillMaxSize()
            .minimumInteractiveComponentSize()
            .pointerInput(!isEditMode && inGame) {
                if (isEditMode || !inGame) {
                    return@pointerInput
                }
                awaitEachGesture {
                    viewState.apply {
                        val consumeEvents =
                            consumeTouchEvents || engineInfo.mouseButtonsEventsCanBeInvoked
                        val pointerPassToUse = if (consumeEvents) PointerEventPass.Initial
                        else PointerEventPass.Main
                        val down = awaitFirstDown(pass = pointerPassToUse)
                        if (consumeTouchEvents) {
                            down.consume()
                        }
                        if (!this.useViewAsToggle || !isPressed) {
                            onTouchDown(this.sdlKeyCode)
                        } else if (this.useViewAsToggle && isPressed) {
                            onTouchUp(sdlKeyCode)
                        }
                        val up = waitForUpOrCancellation(pass = pointerPassToUse,
                            ignoreOutOfBoundsTouchEvents)
                        if (consumeTouchEvents) {
                            up?.consume()
                        }
                        if (!useViewAsToggle) {
                            onTouchUp(sdlKeyCode)
                        }
                        else{
                            isPressed = !isPressed
                        }
                    }
                }
            }
    }
}