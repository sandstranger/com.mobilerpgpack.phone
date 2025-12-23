package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.waitForUpOrCancellation
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named

abstract class SDLImageButton(
    id: String,
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
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    showInQuickPanel : Boolean = false) : IScreenControlsView, KoinComponent {

    final override var screenController: IScreenController? = null

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
        ignoreOutOfBoundsTouchEventsInitialState = ignoreOutOfBoundsTouchEvents,
        showInQuickPanelInitialState = showInQuickPanel)

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        val viewState = remember { viewState }
        Image(painter = painterResource(id = viewState.buttonResId),
            contentDescription = viewState.id,
            modifier = Modifier.interactiveControlModifier(isEditMode, inGame))
    }

    protected abstract fun onTouchDown(keyCode: Int)

    protected abstract fun onTouchUp(keyCode: Int)

    @Composable
    protected fun Modifier.interactiveControlModifier (isEditMode: Boolean, inGame: Boolean) : Modifier{
        val modifierTouse = this.fillMaxSize().minimumInteractiveComponentSize()
        if (!inGame){
            return modifierTouse
        }

        val viewState = remember { viewState }
        var wasPressed by rememberSaveable {mutableStateOf(false) }
        var isPressed by rememberSaveable { mutableStateOf(false) }

        val preferencesStorage : PreferencesStorage = koinInject()
        val activeEngineString = preferencesStorage.activeEngineString
        if ((isEditMode && (wasPressed || isPressed)) ||
            (!viewState.useViewAsToggle && isPressed)){
            wasPressed = false
            isPressed = false
            onTouchUp(sdlKeyEvent)
        }

        val engineInfo : IEngineInfo = koinInject(named(activeEngineString))
        val mouseButtonsEventsCanBeInvokedFlow by engineInfo.mouseButtonsEventsCanBeInvokedAsFlow.collectAsState(initial = false)
        val mouseButtonsEventsCanBeInvoked by remember(mouseButtonsEventsCanBeInvokedFlow) { mutableStateOf(mouseButtonsEventsCanBeInvokedFlow)}
        val ignoreOutOfBoundsTouchEvents by remember (viewState.ignoreOutOfBoundsTouchEvents)
        { mutableStateOf(viewState.ignoreOutOfBoundsTouchEvents) }
        val consumeTouchEvents by remember (viewState.consumeTouchEvents)
        { mutableStateOf(viewState.consumeTouchEvents) }
        val useViewAsToggle by remember (viewState.useViewAsToggle) { mutableStateOf(viewState.useViewAsToggle) }
        val sdlKeyCode by remember (viewState.sdlKeyCode) { mutableStateOf(viewState.sdlKeyCode) }

        return with(modifierTouse.pointerInput(isEditMode, mouseButtonsEventsCanBeInvoked) {
                if (isEditMode) {
                    return@pointerInput
                }

                awaitEachGesture {
                        val consumeEvents = consumeTouchEvents || mouseButtonsEventsCanBeInvoked
                        val pointerPassToUse = if (consumeEvents) PointerEventPass.Initial
                        else PointerEventPass.Main
                        val down = awaitFirstDown(pass = pointerPassToUse)
                        if (consumeEvents) {
                            down.consume()
                        }
                        if (!useViewAsToggle) {
                            wasPressed = true
                            onTouchUp(sdlKeyCode)
                            onTouchDown(sdlKeyCode)
                        } else {
                            if (!isPressed){
                                onTouchUp(sdlKeyCode)
                                onTouchDown(sdlKeyCode)
                            }
                            else{
                                onTouchUp(sdlKeyCode)
                            }
                            isPressed = !isPressed
                        }
                        val up = waitForUpOrCancellation(pass = pointerPassToUse,
                            ignoreOutOfBoundsTouchEvents)
                        if (consumeEvents) {
                            up?.consume()
                        }
                        if (!useViewAsToggle) {
                            wasPressed = false
                            onTouchUp(sdlKeyCode)
                    }
                }
            }){
            if (isPressed && !isEditMode && useViewAsToggle)
                this.graphicsLayer { colorFilter = ColorFilter.tint(color = Color.Yellow) } else this
        }
    }
}