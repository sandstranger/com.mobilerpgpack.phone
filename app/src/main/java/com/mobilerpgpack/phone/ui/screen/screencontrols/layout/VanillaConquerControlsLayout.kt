package com.mobilerpgpack.phone.ui.screen.screencontrols.layout

import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.EnableEditModeButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateQuickPanelVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateScreenControlsVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLScreenController.Companion.LEFT_MOUSE_BUTTON_ID
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2MouseImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2OnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2RadialWheel

val vanillaConquerOnScreenStickControlsLayout: Collection<IScreenControlsView> = listOf(
    EnableEditModeButton(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2OnScreenStick(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        stickType = StickType.LeftStick,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL2OnScreenStick(
        engineType = EngineTypes.VanillaConquer,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        stickType = StickType.RightStick,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2RadialWheel(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        isDeleted = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2Dpad(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2MouseImageButton(
        "left_mouse_button",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.52f,
        offsetYPercent = 0.03f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.mouse,
        sdlKeyEvent = LEFT_MOUSE_BUTTON_ID,
        useToggle = false,
        isDeleted = true,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        useReturnButton = false,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        controlsType = ControlsType.OnScreenStick
    ))