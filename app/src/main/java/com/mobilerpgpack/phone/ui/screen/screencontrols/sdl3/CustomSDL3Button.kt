package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.CustomSDLButton
import org.libsdl3.app.SDLActivity.onNativeKeyDown
import org.libsdl3.app.SDLActivity.onNativeKeyUp

class CustomSDL3Button (
    private val id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = 0,
    useToggle: Boolean = false,
    override val isQuickPanel: Boolean = false,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default) :
    CustomSDLButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha, sdlKeyEvent,
        useToggle, defaultViewRenderRule, controlsType) {

    init {
        show = !isQuickPanel
    }

    override fun onTouchDown(keyCode: Int) = onNativeKeyDown(keyCode)

    override fun onTouchUp(keyCode: Int) = onNativeKeyUp(keyCode)
}