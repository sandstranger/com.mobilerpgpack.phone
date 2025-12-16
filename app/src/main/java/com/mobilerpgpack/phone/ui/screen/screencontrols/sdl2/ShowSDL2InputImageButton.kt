package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLInputImageButton
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import org.libsdl.app.SDLInputConnection

class ShowSDL2InputImageButton(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = ViewState.NOT_EXISTING_RES,
    keyboardInputType : CustomKeyboardView.KeyboardType = DEFAULT_KEYBOARD_INPUT_TYPE,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,
    isDeleted : Boolean = false)  :
    ShowSDLInputImageButton(engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha, buttonResId, keyboardInputType,
        defaultViewRenderRule = defaultViewRenderRule, controlsType, isDeleted)
{
    override fun onKeyDown(keyCode: Int) = org.libsdl.app.onKeyDown(keyCode, delayBeforeKeyRelease = 50L)

    override fun onCharClicked(char: Char) = SDLInputConnection.nativeCommitText(char.toString(),0)
}