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
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3OnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3RadialWheel

val perfectDarkAbsoluteTouchControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL3RadialWheel(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        isDeleted = true
    ),
    EnableEditModeButton(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
    ),
    SDL3Dpad(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.21f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        showInQuickPanel = true
    ),
    SDL3OnScreenStick(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        stickType = StickType.RightStick,
    ),
    SDL3OnScreenStick(
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
    SDL3ImageButton(
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
    SDL3MouseWheelButton(
        "next_weapon",
        EngineTypes.PerfectDark,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
    ),
    SDL3MouseWheelButton(
        "prev_weapon",
        EngineTypes.PerfectDark,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    SDL3ImageButton(
        "use",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E
    ),
    SDL3ImageButton(
        "aim_mode",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.89f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.zoom,
        sdlKeyEvent = KeyEvent.KEYCODE_Z,
        useToggle = true
    ),
    SDL3ImageButton(
        "reload",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.79f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.machine_gun_magazine,
        sdlKeyEvent = KeyEvent.KEYCODE_R
    ),
    SDL3ImageButton(
        "fire_mode",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.25f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.reload_gun,
        sdlKeyEvent = KeyEvent.KEYCODE_F
    ),
    SDL3ImageButton(
        "radial_menu",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.43f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.circles_ext,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        useToggle = true
    ),
    SDL3ImageButton(
        "full_crouch",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.69f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.smash_arrows,
        sdlKeyEvent = KeyEvent.KEYCODE_CTRL_LEFT
    ),
    SDL3ImageButton(
        "half_crouch",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.6f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_SHIFT_LEFT
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
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

val perfectDarkOnScreenStickControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL3RadialWheel(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3Dpad(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3OnScreenStick(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        stickType = StickType.RightStick,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.PerfectDark,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3ImageButton(
        "attack",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.44f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        consumeTouchEventsByDefault = false,
        ignoreOutOfBoundsTouchEvents = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "next_weapon",
        EngineTypes.PerfectDark,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "prev_weapon",
        EngineTypes.PerfectDark,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "use",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.45f,
        sizePercent = 0.047f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "aim_mode",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.26f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.zoom,
        sdlKeyEvent = KeyEvent.KEYCODE_Z,
        useToggle = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "reload",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.73f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.machine_gun_magazine,
        sdlKeyEvent = KeyEvent.KEYCODE_R,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "fire_mode",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.2f,
        sizePercent = 0.047f,
        buttonResId = R.drawable.reload_gun,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "radial_menu",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.33f,
        sizePercent = 0.047f,
        buttonResId = R.drawable.circles_ext,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        useToggle = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "full_crouch",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.63f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.smash_arrows,
        sdlKeyEvent = KeyEvent.KEYCODE_CTRL_LEFT,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "half_crouch",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.53f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_SHIFT_LEFT,
        controlsType = ControlsType.OnScreenStick
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
        EngineTypes.PerfectDark,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.005f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        controlsType = ControlsType.OnScreenStick
    ),
    EnableEditModeButton(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
        controlsType = ControlsType.OnScreenStick
    ))