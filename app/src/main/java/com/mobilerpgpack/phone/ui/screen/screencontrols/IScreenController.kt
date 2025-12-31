package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLKeyboard
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView

interface IScreenController{

    var showScreenControls : Boolean
    var showQuickPanelItems : Boolean
    val activeViewsToDraw: Collection<IScreenControlsView>

    @Composable
    fun DrawScreenControls(
        activeEngine : EngineTypes,
        inGame: Boolean,
        blockTouchCameraEvents : Boolean = false,
        allowToEditControls: Boolean = true,
        drawInSafeArea : Boolean = false,
        hideOnScreenControls : Boolean = false,
        keyboardInputType : CustomKeyboardView.KeyboardType = SDLKeyboard.DEFAULT_KEYBOARD_INPUT_TYPE,
        onBack: () -> Unit = { })
}