package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface IScreenControlsView {

    val buttonState : ButtonState

    @Composable
    fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp = 10.dp, onClick : () -> Unit = { })
}

val IScreenControlsView.isKeyboardButton get() = this is ToggleImageButton &&
        this.buttonState.id == ToggleImageButton.SHOW_KEYBOARD_BUTTON_ID

val IScreenControlsView.isHideControlsButton get() = this is ToggleImageButton &&
        this.buttonState.id == ToggleImageButton.HIDE_CONTROLS_BUTTON_ID

val IScreenControlsView.isDpad get() = this is Dpad && this.buttonState.id == Dpad.dpadId

val IScreenControlsView.allowToEditKeyEvent get() = this.buttonState.allowToEditKeyEvent &&
        !isDpad && !isHideControlsButton


