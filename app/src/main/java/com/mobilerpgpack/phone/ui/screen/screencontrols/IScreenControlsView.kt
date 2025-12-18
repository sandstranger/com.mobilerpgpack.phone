package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLMouseWheelButton

interface IScreenControlsView {
    val viewState : ViewState
    var show : Boolean
    val isQuickPanel : Boolean
    @Composable
    fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp)
    fun setScreenController (screenController: IScreenController){}
}

val IScreenControlsView.isHideControlsButton get() = this is UpdateScreenControlsVisibilityImageButton

val IScreenControlsView.isMouseWheelButton get() = this is SDLMouseWheelButton

val IScreenControlsView.allowToEditKeyEvent get() = this.viewState.allowToEditKeyEvent

val IScreenControlsView.renderView : Boolean
    get() {
        this.viewState.apply {
            if (this.isDeleted){
                return false
            }

            return when (viewRenderRule) {
                ViewRenderRule.Default -> this@renderView.show
                ViewRenderRule.AlwaysShow -> true
                ViewRenderRule.Disable -> false
            }
        }
    }

