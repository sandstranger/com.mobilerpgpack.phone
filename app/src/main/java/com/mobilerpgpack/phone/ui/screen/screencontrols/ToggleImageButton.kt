package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES

abstract class ToggleImageButton(id: String,
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
                                 ignoreOutOfBoundsTouchEvents : Boolean = false):
    ImageButton (id,engineType, offsetXPercent, offsetYPercent, sizePercent, alpha,
        buttonResId,defaultViewRenderRule, controlsType, isDeleted, consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents) {

    private var currentToggleState by mutableStateOf(false)

    final override fun onClick(context: Context) {
        currentToggleState=!currentToggleState
        onToggleStateChanged(currentToggleState)
    }

    protected abstract fun  onToggleStateChanged (isActive : Boolean)
}