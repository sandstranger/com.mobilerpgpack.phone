package com.mobilerpgpack.phone.ui.screen

import android.content.Context
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule

class EnableEditModeButton(engineType: EngineTypes,
                           offsetXPercent: Float = 0f,
                           offsetYPercent: Float = 0f,
                           sizePercent: Float = 0.13f,
                           alpha: Float = 0.5f,
                           defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
                           controlsType: ControlsType = ControlsType.Default,
                           isDeleted : Boolean = false,
                           consumeTouchEventsByDefault : Boolean = true,
                           ignoreOutOfBoundsTouchEvents : Boolean = false,
                           showInQuickPanel : Boolean = false) :
    ImageButton("enable_edit_mode",engineType, offsetXPercent, offsetYPercent,
        sizePercent, alpha, R.drawable.cog,defaultViewRenderRule, controlsType, isDeleted,
        consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents, showInQuickPanel ) {

    override fun onClick(context: Context) {
        screenController?.run { isEditMode = true }
    }
}