package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface IScreenControlsView {

    val buttonState : ButtonState

    var show : Boolean

    val isQuickPanel : Boolean

    @Composable
    fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp)

    fun setScreenController (screenController: IScreenController){}
}

val IScreenControlsView.isHideControlsButton get() = this is UpdateScreenControlsVisibilityImageButton

val IScreenControlsView.allowToEditKeyEvent get() = this.buttonState.allowToEditKeyEvent

val IScreenControlsView.renderView : Boolean
    get() {
        return when (this.buttonState.viewRenderRule) {
            ViewRenderRule.Default -> this.show
            ViewRenderRule.AlwaysShow -> true
            ViewRenderRule.Disable -> false
        }
    }

