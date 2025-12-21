package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.content.Context
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES

class UpdateQuickPanelVisibilityImageButton(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,
    isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false) :
    ImageButton(UPDATE_QUICK_PANEL_VISIBILITY_BUTTON_ID, engineType, offsetXPercent, offsetYPercent,
        sizePercent, alpha, buttonResId, defaultViewRenderRule, controlsType, isDeleted,
        consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents, false) {

    override fun onClick(context: Context) {
        screenController?.apply {
            showQuickPanelItems = !showQuickPanelItems
        }
    }

    private companion object {
        private const val UPDATE_QUICK_PANEL_VISIBILITY_BUTTON_ID = "update_quick_panel"
    }
}