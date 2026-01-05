package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLRadialWheel
import org.libsdl3.app.onKeyDown

class SDL3RadialWheel(engineType: EngineTypes,
                      offsetXPercent: Float = 0f,
                      offsetYPercent: Float = 0f,
                      sizePercent: Float = 0.25f,
                      defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
                      controlsType: ControlsType = ControlsType.Default,
                      isDeleted: Boolean = false,
                      consumeTouchEventsByDefault: Boolean = true,
                      ignoreOutOfBoundsTouchEvents: Boolean = true,
                      showInQuickPanel: Boolean = false) :
    SDLRadialWheel(engineType, offsetXPercent, offsetYPercent, sizePercent, defaultViewRenderRule, controlsType,
        isDeleted, consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents, showInQuickPanel) {

    override fun onItemSelected(keyCode: Int) = onKeyDown(keyCode, delayBeforeKeyRelease = 50L)
}