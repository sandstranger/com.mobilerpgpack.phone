package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES

abstract class SDLImageButton(
    private val id: String,
    engineType: EngineTypes,
    private val offsetXPercent: Float = 0f,
    private val offsetYPercent: Float = 0f,
    private val sizePercent: Float = 0.13f,
    private val alpha: Float = 0.65f,
    private val sdlKeyEvent: Int = 0,
    private val buttonResId: Int = NOT_EXISTING_RES,
    private val useToggle: Boolean = false) : IScreenControlsView {

    private var isPressed by mutableStateOf(false)

    override var canBeDrawn: Boolean by mutableStateOf(true)

    override val buttonState: ButtonState = ButtonState(
        id,
        engineType,
        offsetXPercent = offsetXPercent,
        offsetYPercent = offsetYPercent,
        sizePercent = sizePercent,
        buttonResId = buttonResId,
        sdlKeyEvent = sdlKeyEvent,
        alpha = alpha
    )

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        Image(
            painter = painterResource(id = buttonState.buttonResId),
            contentDescription = id,
            modifier = if (!useToggle) Modifier
                .fillMaxSize()
                .minimumInteractiveComponentSize()
                .pointerInput(!isEditMode && inGame) {
                    if (isEditMode || !inGame) return@pointerInput

                    detectTapGestures(
                        onPress = {
                            onTouchDown(buttonState.sdlKeyCode)
                            try {
                                awaitRelease()
                            } finally {
                                onTouchUp(buttonState.sdlKeyCode)
                            }
                        }
                    )
                }
            else Modifier
                .fillMaxSize()
                .minimumInteractiveComponentSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    if (!isPressed){
                        onTouchDown(buttonState.sdlKeyCode)
                    }
                    else{
                        onTouchUp(buttonState.sdlKeyCode)
                    }
                    isPressed=!isPressed
                }
        )
    }

    protected abstract fun onTouchDown(keyCode: Int)

    protected abstract fun onTouchUp(keyCode: Int)
}