package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.annotation.SuppressLint
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import org.koin.core.component.KoinComponent

abstract class Dpad(engineType: EngineTypes,
    private val offsetXPercent: Float = 0f,
    private val offsetYPercent: Float = 0f,
    private val sizePercent: Float = 0.25f) : KoinComponent, IScreenControlsView {

    private val dpadButtonState: ButtonState

    private val dpadButtons: Collection<ButtonState>

    override var show: Boolean by mutableStateOf(true)

    override val isQuickPanel: Boolean = false

    override val buttonState: ButtonState get() = dpadButtonState

    init {
        val buttons = mutableListOf<ButtonState>()
        buttons.add(
            ButtonState(
                ButtonType.DpadDown.toString().lowercase(),
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_DOWN,
                buttonType = ButtonType.DpadDown,
                buttonResId = R.drawable.dpad_down,
            )
        )
        buttons.add(
            ButtonState(
                ButtonType.DpadUp.toString().lowercase(),
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_UP,
                buttonType = ButtonType.DpadUp,
                buttonResId = R.drawable.dpad_up,
            )
        )
        buttons.add(
            ButtonState(
                ButtonType.DpadLeft.toString().lowercase(),
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_LEFT,
                buttonType = ButtonType.DpadLeft,
                buttonResId = R.drawable.dpad_left,
            )
        )
        buttons.add(
            ButtonState(
                ButtonType.DpadRight.toString().lowercase(),
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_RIGHT,
                buttonType = ButtonType.DpadRight,
                buttonResId = R.drawable.dpad_right,
            )
        )
        dpadButtons = buttons
        dpadButtonState = ButtonState(
            dpadId,
            engineType,
            offsetXPercent = offsetXPercent,
            offsetYPercent = offsetYPercent,
            sizePercent = sizePercent
        )
    }

    @SuppressLint("UnusedBoxWithConstraintsScope")
    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val buttonSize = size * 0.4f
            val offsetAmount = size * 0.33f

            val offsetYStorage = hashMapOf<ButtonType, Dp>(
                ButtonType.DpadUp to -offsetAmount,
                ButtonType.DpadDown to offsetAmount
            )
            val offsetXStorage = hashMapOf<ButtonType, Dp>(
                ButtonType.DpadLeft to -offsetAmount,
                ButtonType.DpadRight to offsetAmount
            )

            @Composable
            fun dpadButton(
                painterId: Int,
                desc: String,
                sdlKeyEvent: Int = 0,
                offsetX: Dp = 0.dp,
                offsetY: Dp = 0.dp
            ) {
                Image(
                    painter = painterResource(painterId),
                    contentDescription = desc,
                    modifier = Modifier
                        .size(buttonSize)
                        .minimumInteractiveComponentSize()
                        .offset(x = offsetX, y = offsetY)
                        .pointerInput(!isEditMode && inGame) {
                            if (isEditMode || !inGame) return@pointerInput

                            detectTapGestures(
                                onPress = {
                                    onTouchDown(sdlKeyEvent)
                                    try {
                                        awaitRelease()
                                    } finally {
                                        onTouchUp(sdlKeyEvent)
                                    }
                                }
                            )
                        }
                )
            }

            for (button in dpadButtons) {
                if (button.buttonType in listOf(ButtonType.DpadUp, ButtonType.DpadDown)) {
                    dpadButton(
                        button.buttonResId,
                        button.id,
                        button.sdlKeyCode,
                        offsetY = offsetYStorage[button.buttonType]!!
                    )
                } else if (button.buttonType in listOf(ButtonType.DpadLeft, ButtonType.DpadRight)) {
                    dpadButton(
                        button.buttonResId,
                        button.id,
                        button.sdlKeyCode,
                        offsetX = offsetXStorage[button.buttonType]!!
                    )
                }
            }
        }
    }

    protected abstract fun onTouchDown(keyCode: Int)

    protected abstract fun onTouchUp(keyCode: Int)

    private companion object {
        private const val dpadId = "dpad"
    }
}