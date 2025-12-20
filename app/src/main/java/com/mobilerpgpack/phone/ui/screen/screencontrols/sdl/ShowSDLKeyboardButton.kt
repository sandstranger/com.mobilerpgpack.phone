package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.annotation.SuppressLint
import android.content.Context
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class ShowSDLKeyboardButton(
    keyboardType : KeyboardType,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    private val keyboardInputType : CustomKeyboardView.KeyboardType = SDLKeyboard.DEFAULT_KEYBOARD_INPUT_TYPE,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,
    isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false) : KoinComponent,
    ImageButton(SHOW_KEYBOARD_BUTTON_ID, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha,
        buttonResId, defaultViewRenderRule, controlsType, isDeleted, consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents)
{
    private val keyboard : SDLKeyboard by inject (named(keyboardType.name))

    @SuppressLint("CheckResult")
    override fun onClick(context: Context) =
        keyboard.showKeyboard(useReturnButton = false, keyboardInputType)

    private companion object {
        private const val SHOW_KEYBOARD_BUTTON_ID = "keyboard"
    }
}