package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface IScreenControlsView {

    val buttonState : ButtonState

    val enabled : Boolean

    var show : Boolean

    val isQuickPanel : Boolean

    @Composable
    fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp = 10.dp)

    fun setScreenController (screenController: IScreenController){}
}

val IScreenControlsView.isHideControlsButton get() = this is UpdateScreenControlsVisibilityImageButton

val IScreenControlsView.allowToEditKeyEvent get() = this.buttonState.allowToEditKeyEvent


