package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.content.Context
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.core.component.get
import org.koin.core.qualifier.named

class RescanSDLGamepadsForcedButton (
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
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    showInQuickPanel : Boolean = false) :
    ImageButton("rescan_gamepads_forced", engineType, offsetXPercent, offsetYPercent,
        sizePercent, alpha, buttonResId, defaultViewRenderRule, controlsType, isDeleted,
        consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents, showInQuickPanel) {

    private val engineInfo : IEngineInfo by lazy {
        with(get<PreferencesStorage> ()){
            get<IEngineInfo> (named(this.activeEngineString.value!!))
        }
    }

    override fun onClick(context: Context) = engineInfo.rescanGameControllers()
}