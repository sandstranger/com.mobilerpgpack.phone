package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLOnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType

class SDL3OnScreenStick(engineType: EngineTypes,
                        stickType: StickType = StickType.LeftStick,
                        offsetXPercent: Float = 0f,
                        offsetYPercent: Float = 0f,
                        sizePercent: Float = 0.25f,
                        alpha: Float = 0.65f,
                        defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
                        controlsType: ControlsType = ControlsType.Default,
                        isDeleted : Boolean = false,
                        showInQuickPanel : Boolean = false) :
    SDLOnScreenStick(engineType, stickType,offsetXPercent,
        offsetYPercent, sizePercent, alpha, defaultViewRenderRule = defaultViewRenderRule, controlsType, isDeleted, showInQuickPanel) {

    override val virtualControllerLibraryName: String get() = VIRTUAL_CONTROLLER_LIBRARY_NAME

    private companion object{
        private const val VIRTUAL_CONTROLLER_LIBRARY_NAME = "SDL3VirtualController"
    }
}