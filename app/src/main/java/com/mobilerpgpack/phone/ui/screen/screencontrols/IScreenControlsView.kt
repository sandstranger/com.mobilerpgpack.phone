package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

interface IScreenControlsView {
    val viewState : ViewState
    var screenController : IScreenController?
    @Composable
    fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp)
}

val IScreenControlsView.isHideControlsButton get() = this is UpdateScreenControlsVisibilityImageButton

val IScreenControlsView.isUpdateQuickPanelStateButton get() = this is UpdateQuickPanelVisibilityImageButton

val IScreenControlsView.renderView : Boolean
    get() {
        this.viewState.apply {
            if (this.isDeleted || screenController == null){
                return false
            }

            val showAsCommonScreenControlsView = !showInQuickPanel && (screenController!!.showScreenControls ||
                    this@renderView.isHideControlsButton)
            val showAsQuickPanelItem = showInQuickPanel && screenController!!.showQuickPanelItems &&
                    screenController!!.showScreenControls

            return when (viewRenderRule) {
                ViewRenderRule.Default -> showAsCommonScreenControlsView || showAsQuickPanelItem
                ViewRenderRule.AlwaysShow -> screenController!!.showScreenControls || this@renderView.isHideControlsButton
                ViewRenderRule.Disable -> false
            }
        }
    }

