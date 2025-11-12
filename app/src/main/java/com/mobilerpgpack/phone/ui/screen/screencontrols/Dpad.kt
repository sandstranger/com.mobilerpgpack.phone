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

    override val enabled: Boolean = true

    override val isQuickPanel: Boolean = false

    override val buttonState: ButtonState get() = dpadButtonState

    init {
        val buttons = mutableListOf<ButtonState>()
        buttons.add(
            ButtonState(
                DPAD_DOWN,
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_DOWN,
                buttonResId = R.drawable.dpad_down,
            )
        )
        buttons.add(
            ButtonState(
                DPAD_UP,
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_UP,
                buttonResId = R.drawable.dpad_up,
            )
        )
        buttons.add(
            ButtonState(DPAD_LEFT,
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_LEFT,
                buttonResId = R.drawable.dpad_left,
            )
        )
        buttons.add(
            ButtonState(DPAD_RIGHT,
                engineType,
                sdlKeyEvent = KeyEvent.KEYCODE_DPAD_RIGHT,
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

            val offsetYStorage = hashMapOf(
                DPAD_UP to -offsetAmount,
                DPAD_DOWN to offsetAmount
            )
            val offsetXStorage = hashMapOf(
                DPAD_LEFT to -offsetAmount,
                DPAD_RIGHT to offsetAmount
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


    private companion object {
        private const val dpadId = "dpad"
        private const val DPAD_LEFT = "DpadLeft"
        private const val DPAD_RIGHT = "DpadRight"
        private const val DPAD_DOWN = "DpadDown"
        private const val DPAD_UP = "DpadUp"

        private val dpadDownCollection : Collection <String> = setOf(DPAD_UP, DPAD_DOWN)
        private val dpadLeftCollection : Collection <String> = setOf(DPAD_LEFT, DPAD_RIGHT)
    }
}