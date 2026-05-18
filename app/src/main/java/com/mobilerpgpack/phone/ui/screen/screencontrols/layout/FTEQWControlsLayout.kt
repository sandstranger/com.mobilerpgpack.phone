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

val fteQWOnScreenStickControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL3RadialWheel(
        EngineTypes.FTEQW,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        isDeleted = true,
        controlsType = ControlsType.OnScreenStick
    ),
    EnableEditModeButton(
        EngineTypes.FTEQW,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3Dpad(
        EngineTypes.FTEQW,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.FTEQW,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.FTEQW,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        stickType = StickType.RightStick,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.FTEQW,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "zero_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zero_number,
        sdlKeyEvent = KeyEvent.KEYCODE_0,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "first_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.first_number,
        sdlKeyEvent = KeyEvent.KEYCODE_1,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "second_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.second_number,
        sdlKeyEvent = KeyEvent.KEYCODE_2,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "third_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.third_number,
        sdlKeyEvent = KeyEvent.KEYCODE_3,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "fourth_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fourth_number,
        sdlKeyEvent = KeyEvent.KEYCODE_4,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "five_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.46f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.five_number,
        sdlKeyEvent = KeyEvent.KEYCODE_5,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "six_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.six_number,
        sdlKeyEvent = KeyEvent.KEYCODE_6,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "seven_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.seven_number,
        sdlKeyEvent = KeyEvent.KEYCODE_7,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "eight_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "nine_number",
        EngineTypes.FTEQW,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "open_console",
        EngineTypes.FTEQW,
        offsetXPercent = 0.46f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.terminal,
        sdlKeyEvent = KeyEvent.KEYCODE_GRAVE,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "previous_item",
        EngineTypes.FTEQW,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.previous,
        sdlKeyEvent = KeyEvent.KEYCODE_LEFT_BRACKET,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "next_item",
        EngineTypes.FTEQW,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.next,
        sdlKeyEvent = KeyEvent.KEYCODE_RIGHT_BRACKET,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "swim_up",
        EngineTypes.FTEQW,
        offsetXPercent = 0.95f,
        offsetYPercent = 0.2f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.fly_up,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "swim_down",
        EngineTypes.FTEQW,
        offsetXPercent = 0.95f,
        offsetYPercent = 0.34f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.fly_down,
        sdlKeyEvent = KeyEvent.KEYCODE_Z,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "drop_item",
        EngineTypes.FTEQW,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.drop_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_B,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "help_objectives",
        EngineTypes.FTEQW,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.info,
        sdlKeyEvent = KeyEvent.KEYCODE_F1,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "info_frag",
        EngineTypes.FTEQW,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.position_marker,
        sdlKeyEvent = KeyEvent.KEYCODE_F2,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "torch",
        EngineTypes.FTEQW,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.torch,
        sdlKeyEvent = KeyEvent.KEYCODE_F3,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "qrtz_flask",
        EngineTypes.FTEQW,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.round_potion,
        sdlKeyEvent = KeyEvent.KEYCODE_O,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "mystic_urn",
        EngineTypes.FTEQW,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.dragon_orb,
        sdlKeyEvent = KeyEvent.KEYCODE_P,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "krater",
        EngineTypes.FTEQW,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.volcano,
        sdlKeyEvent = KeyEvent.KEYCODE_G,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "chaos_devc",
        EngineTypes.FTEQW,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.misdirection,
        sdlKeyEvent = KeyEvent.KEYCODE_H,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "tome_power",
        EngineTypes.FTEQW,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.secret_book,
        sdlKeyEvent = KeyEvent.KEYCODE_J,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "attack",
        EngineTypes.FTEQW,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.49f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER,
        consumeTouchEventsByDefault = false,
        ignoreOutOfBoundsTouchEvents = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseImageButton(
        "quake3_zoom",
        EngineTypes.FTEQW,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.42f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.zoom,
        sdlKeyEvent = 2,
        controlsType = ControlsType.OnScreenStick,
        useToggle = true,
        isDeleted = true
    ),
    SDL3ImageButton(
        "lift_item",
        EngineTypes.FTEQW,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.57f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.reload_gun,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3MouseWheelButton(
        "next_weapon",
        EngineTypes.FTEQW,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.next_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "prev_weapon",
        EngineTypes.FTEQW,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.prev_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "autorun",
        EngineTypes.FTEQW,
        offsetXPercent = 0.6f,
        offsetYPercent = 0.89f,
        sizePercent = 0.055f,
        sdlKeyEvent = KeyEvent.KEYCODE_SHIFT_LEFT,
        buttonResId = R.drawable.run,
        useToggle = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.FTEQW,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quick_save",
        EngineTypes.FTEQW,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F6,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quick_load",
        EngineTypes.FTEQW,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "inventory",
        EngineTypes.FTEQW,
        offsetXPercent = 0.93f,
        offsetYPercent = 0.5f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.locked_chest,
        sdlKeyEvent = KeyEvent.KEYCODE_I,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "use",
        EngineTypes.FTEQW,
        offsetXPercent = 0.93f,
        offsetYPercent = 0.68f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "jump",
        EngineTypes.FTEQW,
        offsetXPercent = 0.87f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.jump,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "crouch",
        EngineTypes.FTEQW,
        offsetXPercent = 0.78f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_C,
        useToggle = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "toggle_zoom",
        EngineTypes.FTEQW,
        offsetXPercent = 0.69f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.zoom,
        sdlKeyEvent = KeyEvent.KEYCODE_F11,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "autorun",
        EngineTypes.FTEQW,
        offsetXPercent = 0.60f,
        offsetYPercent = 0.89f,
        sizePercent = 0.055f,
        sdlKeyEvent = KeyEvent.KEYCODE_SHIFT_LEFT,
        buttonResId = R.drawable.run,
        useToggle = true,
        controlsType = ControlsType.OnScreenStick
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
        EngineTypes.FTEQW,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.FTEQW,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        controlsType = ControlsType.OnScreenStick
    ))