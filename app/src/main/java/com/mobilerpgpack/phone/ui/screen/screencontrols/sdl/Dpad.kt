package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.utils.touchListenerModifier
import org.koin.core.component.KoinComponent

abstract class Dpad(
    engineType: EngineTypes,
    private val offsetXPercent: Float = 0f,
    private val offsetYPercent: Float = 0f,
    private val sizePercent: Float = 0.25f,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    showInQuickPanel : Boolean = false) : KoinComponent,
    IScreenControlsView {

    private val dpadButtonState: ViewState

    private val dpadButtons: Collection<ViewState>

    final override var screenController: IScreenController? = null

    final override val viewState: ViewState get() = dpadButtonState

    init {
        val buttons = mutableListOf<ViewState>()
        buttons.add(
            ViewState(
                DPAD_DOWN,
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_DOWN,
                buttonResId = R.drawable.dpad_down
            )
        )
        buttons.add(
            ViewState(
                DPAD_UP,
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_UP,
                buttonResId = R.drawable.dpad_up
            )
        )
        buttons.add(
            ViewState(
                DPAD_LEFT,
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_LEFT,
                buttonResId = R.drawable.dpad_left
            )
        )
        buttons.add(
            ViewState(
                DPAD_RIGHT,
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_RIGHT,
                buttonResId = R.drawable.dpad_right
            )
        )
        dpadButtons = buttons
        dpadButtonState = ViewState(
            DPAD_ID,
            engineType,
            offsetXPercent = offsetXPercent,
            offsetYPercent = offsetYPercent,
            sizePercent = sizePercent,
            defaultViewRenderRule = defaultViewRenderRule,
            controlsType = controlsType,
            isDeletedInitialState = isDeleted,
            alwaysConsumeTouchEvents = false,
            consumeTouchEventsInitialState = consumeTouchEventsByDefault,
            touchEventsCanIgnoreOutOfBounds = true,
            ignoreOutOfBoundsTouchEventsInitialState = ignoreOutOfBoundsTouchEvents,
            showInQuickPanelInitialState = showInQuickPanel
        )
    }

    @Composable
    final override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        val inGame = remember { inGame }
        val isEditMode by remember (isEditMode) { mutableStateOf(isEditMode) }
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (inGame && !isEditMode) {
                Box(modifier = Modifier.fillMaxSize().minimumInteractiveComponentSize()
                    .touchListenerModifier(false,viewState, changeItemColor = false))
            }

            val buttonSize by remember (size) { mutableStateOf(size * 0.4f)}
            val offsetAmount by remember (size) { mutableStateOf(size * 0.33f) }
            val offsetYStorage = remember (offsetAmount) { hashMapOf(
                DPAD_UP to -offsetAmount,
                DPAD_DOWN to offsetAmount
            ) }
            val offsetXStorage = remember (offsetAmount) { hashMapOf(
                DPAD_LEFT to -offsetAmount,
                DPAD_RIGHT to offsetAmount
            ) }
            val dpadDownCollection = rememberSaveable { dpadDownCollection }
            val dpadLeftCollection = rememberSaveable {dpadLeftCollection }
            val dpadButtons = remember { dpadButtons }

            @Composable
            fun dpadButton(
                painterId: Int,
                desc: String,
                sdlKeyEvent: Int = 0,
                offsetX: Dp = 0.dp,
                offsetY: Dp = 0.dp
            ) {
                Image(painter = painterResource(painterId),
                    contentDescription = desc,
                    modifier = Modifier.interactiveControlModifier(isEditMode, inGame,buttonSize,
                        offsetX, offsetY, sdlKeyEvent)
                )
            }
            for (button in dpadButtons) {
                if (button.id in dpadDownCollection) {
                    dpadButton(
                        button.buttonResId,
                        button.id,
                        button.sdlKeyCode,
                        offsetY = offsetYStorage[button.id]!!
                    )
                } else if (button.id in dpadLeftCollection) {
                    dpadButton(
                        button.buttonResId,
                        button.id,
                        button.sdlKeyCode,
                        offsetX = offsetXStorage[button.id]!!
                    )
                }
            }
        }
    }

    protected abstract fun onTouchDown(keyCode: Int)

    protected abstract fun onTouchUp(keyCode: Int)

    @Composable
    private fun Modifier.interactiveControlModifier(isEditMode: Boolean, inGame: Boolean,
                                                    buttonSize : Dp,  offsetX: Dp,
                                                    offsetY: Dp,
                                                    sdlKeyEvent : Int): Modifier {
        val modifier = this
            .size(buttonSize)
            .minimumInteractiveComponentSize()
            .offset(x = offsetX, y = offsetY)

        if (!inGame) {
            return modifier
        }

        val sdlKeyCode by rememberSaveable  (sdlKeyEvent) { mutableIntStateOf(sdlKeyEvent) }
        return modifier.touchListenerModifier(isEditMode, viewState, changeItemColor = false, onTouchDown = {
            onTouchDown(sdlKeyCode)
        }, onTouchUp = {
            onTouchUp(sdlKeyCode)
        })
    }

    private companion object {
        private const val DPAD_ID = "dpad"
        private const val DPAD_LEFT = "DpadLeft"
        private const val DPAD_RIGHT = "DpadRight"
        private const val DPAD_DOWN = "DpadDown"
        private const val DPAD_UP = "DpadUp"

        private val dpadDownCollection: Collection<String> = setOf(DPAD_UP, DPAD_DOWN)
        private val dpadLeftCollection: Collection<String> = setOf(DPAD_LEFT, DPAD_RIGHT)
    }
}