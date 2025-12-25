package com.mobilerpgpack.phone.ui.screen.screencontrols.layout

import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateQuickPanelVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateScreenControlsVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2OnScreenStick


val perfectDarkAbsoluteTouchControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.21f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        showInQuickPanel = true
    ),
    SDL2OnScreenStick(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        stickType = StickType.RightStick,
    ),
    SDL2OnScreenStick(
        engineType = EngineTypes.PerfectDark,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
    ),
    SDL2ImageButton(
        "attack",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.5f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        consumeTouchEventsByDefault = false,
        ignoreOutOfBoundsTouchEvents = true
    ),
    SDL2MouseWheelButton(
        "prev_weapon",
        EngineTypes.PerfectDark,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
    ),
    SDL2MouseWheelButton(
        "next_weapon",
        EngineTypes.PerfectDark,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    SDL2ImageButton(
        "use",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E
    ),
    SDL2ImageButton(
        "aim_mode",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.89f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.zoom,
        sdlKeyEvent = KeyEvent.KEYCODE_Z,
        useToggle = true
    ),
    SDL2ImageButton(
        "reload",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.79f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.machine_gun_magazine,
        sdlKeyEvent = KeyEvent.KEYCODE_R
    ),
    SDL2ImageButton(
        "fire_mode",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.25f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.reload_gun,
        sdlKeyEvent = KeyEvent.KEYCODE_F
    ),
    SDL2ImageButton(
        "radial_menu",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.43f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.circles_ext,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        useToggle = true
    ),
    SDL2ImageButton(
        "full_crouch",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.69f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.smash_arrows,
        sdlKeyEvent = KeyEvent.KEYCODE_CTRL_LEFT
    ),
    SDL2ImageButton(
        "half_crouch",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.6f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_SHIFT_LEFT
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.PerfectDark,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.005f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles
    ))