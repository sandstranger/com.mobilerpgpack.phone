package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.content.Context
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES

class UpdateScreenControlsVisibilityImageButton(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,
    isDeleted : Boolean = false) :
    ImageButton(HIDE_CONTROLS_BUTTON_ID, engineType, offsetXPercent, offsetYPercent,
    sizePercent, alpha, buttonResId, defaultViewRenderRule, controlsType, isDeleted) {

    override fun onClick(context: Context) {
        screenController?.activeViewsToDraw?.forEach {
            if (it !== this) {
                it.show = if (it.isQuickPanel) false else !it.show
            }
        }
    }

    private companion object {
        private const val HIDE_CONTROLS_BUTTON_ID = "hide_controls"
    }
}