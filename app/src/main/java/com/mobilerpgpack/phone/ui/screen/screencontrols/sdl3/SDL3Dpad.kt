package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import org.libsdl3.app.SDLActivity.*

class SDL3Dpad(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.25f,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    showInQuickPanel : Boolean = false) :
    Dpad(engineType, offsetXPercent, offsetYPercent, sizePercent,
        defaultViewRenderRule = defaultViewRenderRule, controlsType, isDeleted,
        consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents, showInQuickPanel) {

    override fun onTouchDown(keyCode: Int) = onNativeKeyDown(keyCode)

    override fun onTouchUp(keyCode: Int) = onNativeKeyUp(keyCode)
}