package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES

class UpdateQuickPanelVisibilityImageButton(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES) :
    ImageButton(UPDATE_QUICK_PANEL_VISIBILITY_BUTTON_ID, engineType, offsetXPercent, offsetYPercent,
        sizePercent, alpha, buttonResId) {

    override fun onClick() {
        screenController?.activeViewsToDraw?.forEach {
            if (it.isQuickPanel) {
                it.canBeDrawn = !it.canBeDrawn
            }
        }
    }

    private companion object {
        private const val UPDATE_QUICK_PANEL_VISIBILITY_BUTTON_ID = "update_quick_panel"
    }
}