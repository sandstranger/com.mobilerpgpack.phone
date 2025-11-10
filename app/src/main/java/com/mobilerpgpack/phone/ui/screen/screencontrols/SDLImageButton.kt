package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES

abstract class SDLImageButton(private val id: String,
    engineType: EngineTypes,
    private val offsetXPercent: Float = 0f,
    private val offsetYPercent: Float = 0f,
    private val sizePercent: Float = 0.13f,
    private val alpha: Float = 0.65f,
    private val sdlKeyEvent: Int = 0,
    private val buttonResId: Int = NOT_EXISTING_RES) : IScreenControlsView {

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

    override var onClick: (() -> Unit)?
        get() = null
        set(_) {}

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        Image(
            painter = painterResource(id = buttonState.buttonResId),
            contentDescription = id,
            modifier = Modifier
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
        )
    }

    protected abstract fun onTouchDown(keyCode: Int)

    protected abstract fun onTouchUp(keyCode: Int)
}