package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES

class ImageButton(
    val id: String,
    val engineType: EngineTypes,
    private val offsetXPercent: Float = 0f,
    private val offsetYPercent: Float = 0f,
    private val sizePercent: Float = 0.13f,
    private val alpha: Float = 0.65f,
    private val sdlKeyEvent: Int = 0,
    private val buttonResId: Int = NOT_EXISTING_RES
) : IScreenControlsView {

    private var _onClick : (() -> Unit)? = null

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
        get() = _onClick
        set(value) {
            _onClick = value
        }

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        Image(
            painter = painterResource(id = buttonState.buttonResId),
            contentDescription = id,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (!isEditMode && inGame) {
                        Modifier
                            .minimumInteractiveComponentSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                _onClick?.invoke()
                            }
                    } else {
                        Modifier
                    }
                )
        )
    }

    companion object{
        const val SHOW_KEYBOARD_BUTTON_ID = "keyboard"

        const val HIDE_CONTROLS_BUTTON_ID = "hide_controls"
    }
}