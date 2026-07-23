package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendModeColorFilter
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.ui.screen.screencontrols.utils.touchListenerModifier
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.core.component.KoinComponent

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
    showInQuickPanel : Boolean = false,
    private val onTouchDownEvent : ((IScreenController?, Int) -> Unit)? = null,
    private val onTouchUpEvent : ((IScreenController?, Int) -> Unit)? = null) : IScreenControlsView, KoinComponent {

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
        var colorFilterToUse by remember { mutableStateOf<BlendModeColorFilter?>(null) }
        Image(painter = painterResource(id = viewState.buttonResId),
            contentDescription = viewState.id,
            modifier = Modifier.interactiveControlModifier(isEditMode, inGame) {
                colorFilterToUse = it
            },
            colorFilter = colorFilterToUse)
    }

    protected abstract fun onTouchDown(keyCode: Int)

    protected abstract fun onTouchUp(keyCode: Int)

    @Composable
    protected fun Modifier.interactiveControlModifier (isEditMode: Boolean, inGame: Boolean,
                                                       onColorFilterChanged : (BlendModeColorFilter?) -> Unit = {}) : Modifier{
        val modifierTouse = this.fillMaxSize().minimumInteractiveComponentSize()
        val inGame = remember { inGame }

        if (!inGame){
            return modifierTouse
        }

        val viewState = remember { viewState }
        val sdlKeyCode = viewState.sdlKeyCode.getComposableValue()

        return modifierTouse.touchListenerModifier(isEditMode,viewState,
            onTouchDown = {
                onTouchDown(sdlKeyCode)
                onTouchDownEvent?.invoke (screenController, sdlKeyCode)
        }, onTouchUp = {
            onTouchUp(sdlKeyCode)
            onTouchUpEvent?.invoke(screenController, sdlKeyCode)
        }, onColorFilterChanged = { onColorFilterChanged(it) })
    }
}