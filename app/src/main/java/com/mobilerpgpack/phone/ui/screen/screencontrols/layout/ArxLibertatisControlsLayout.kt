package com.mobilerpgpack.phone.ui.screen.screencontrols.layout

import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateQuickPanelVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateScreenControlsVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.RescanSDLGamepadsForcedButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2OnScreenStick

val arxLibertatisOnScreenStickControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2OnScreenStick(
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        stickType = StickType.RightStick,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2OnScreenStick(
        engineType = EngineTypes.ArxLibertatis,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL2ImageButton(
        "attack",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.44f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        consumeTouchEventsByDefault = false,
        ignoreOutOfBoundsTouchEvents = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2MouseWheelButton(
        "next_weapon",
        EngineTypes.ArxLibertatis,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2MouseWheelButton(
        "prev_weapon",
        EngineTypes.ArxLibertatis,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "use",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.45f,
        sizePercent = 0.047f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "aim_mode",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.26f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.zoom,
        sdlKeyEvent = KeyEvent.KEYCODE_Z,
        useToggle = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "reload",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.73f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.machine_gun_magazine,
        sdlKeyEvent = KeyEvent.KEYCODE_R,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "fire_mode",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.2f,
        sizePercent = 0.047f,
        buttonResId = R.drawable.reload_gun,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "radial_menu",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.33f,
        sizePercent = 0.047f,
        buttonResId = R.drawable.circles_ext,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        useToggle = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "full_crouch",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.63f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.smash_arrows,
        sdlKeyEvent = KeyEvent.KEYCODE_CTRL_LEFT,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "half_crouch",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.53f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_SHIFT_LEFT,
        controlsType = ControlsType.OnScreenStick
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.005f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        controlsType = ControlsType.OnScreenStick
    ),
    RescanSDLGamepadsForcedButton(
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.gamepad,
        isDeleted = true
    ))