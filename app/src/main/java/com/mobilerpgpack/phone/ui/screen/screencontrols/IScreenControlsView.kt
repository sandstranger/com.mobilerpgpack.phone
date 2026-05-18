package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLMouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseImageButton

interface IScreenControlsView {
    val viewState : ViewState
    var screenController : IScreenController?
    @Composable
    fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp)
}

val IScreenControlsView.isHideControlsButton get() = this is UpdateScreenControlsVisibilityImageButton

val IScreenControlsView.isUpdateQuickPanelStateButton get() = this is UpdateQuickPanelVisibilityImageButton

val IScreenControlsView.isMouseButton get() = this is SDL3MouseImageButton || this is SDLMouseWheelButton

val IScreenControlsView.renderView : Boolean
    get() {
        this.viewState.apply {
            if (this.isDeleted.value!! || screenController == null){
                return false
            }

            val showAsCommonScreenControlsView = !showInQuickPanel.value!! && (screenController!!.showScreenControls ||
                    this@renderView.isHideControlsButton)
            val showAsQuickPanelItem = showInQuickPanel.value!! && screenController!!.showQuickPanelItems &&
                    screenController!!.showScreenControls

            return when (viewRenderRule.value!!) {
                ViewRenderRule.Default -> showAsCommonScreenControlsView || showAsQuickPanelItem
                ViewRenderRule.AlwaysShow -> screenController!!.showScreenControls || this@renderView.isHideControlsButton
                ViewRenderRule.Disable -> false
            }
        }
    }

