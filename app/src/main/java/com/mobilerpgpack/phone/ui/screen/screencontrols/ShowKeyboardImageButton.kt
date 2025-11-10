package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES

class ShowKeyboardImageButton (
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES) :
    ImageButton(SHOW_KEYBOARD_BUTTON_ID, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha, buttonResId) {

    override fun onClick() {
        screenController?.updateVirtualKeyboardVisibility()
    }

    private companion object{
        private const val SHOW_KEYBOARD_BUTTON_ID = "keyboard"
    }
}