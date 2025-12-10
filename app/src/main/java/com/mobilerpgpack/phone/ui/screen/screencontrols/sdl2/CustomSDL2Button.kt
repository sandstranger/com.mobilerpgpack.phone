package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.CustomSDLButton
import org.libsdl.app.SDLActivity.onNativeKeyDown
import org.libsdl.app.SDLActivity.onNativeKeyUp

class CustomSDL2Button(
    private val id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = 0,
    useToggle: Boolean = false,
    override val isQuickPanel: Boolean = false,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default) :
    CustomSDLButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha, sdlKeyEvent,
        useToggle, defaultViewRenderRule) {

    init {
        show = !isQuickPanel
    }

    override fun onTouchDown(keyCode: Int) = onNativeKeyDown(keyCode)

    override fun onTouchUp(keyCode: Int) = onNativeKeyUp(keyCode)
}