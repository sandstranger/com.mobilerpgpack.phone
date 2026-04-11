package com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries

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
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3OnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3RadialWheel

val doomBFAScreenStickControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL3MouseImageButton(
        "alternate_left_mouse_button",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.52f,
        offsetYPercent = 0.03f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.mouse,
        sdlKeyEvent = LEFT_MOUSE_BUTTON_ID,
        controlsType = ControlsType.OnScreenStick,
        useToggle = true,
        isDeleted = true
    ),
    SDL3RadialWheel(
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        isDeleted = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    EnableEditModeButton(
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3Dpad(
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3OnScreenStick(
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        stickType = StickType.RightStick,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick,
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "zero_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zero_number,
        sdlKeyEvent = KeyEvent.KEYCODE_0,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "first_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.first_number,
        sdlKeyEvent = KeyEvent.KEYCODE_1,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "second_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.second_number,
        sdlKeyEvent = KeyEvent.KEYCODE_2,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "third_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.third_number,
        sdlKeyEvent = KeyEvent.KEYCODE_3,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "fourth_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fourth_number,
        sdlKeyEvent = KeyEvent.KEYCODE_4,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "five_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.46f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.five_number,
        sdlKeyEvent = KeyEvent.KEYCODE_5,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "six_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.52f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.six_number,
        sdlKeyEvent = KeyEvent.KEYCODE_6,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "seven_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.seven_number,
        sdlKeyEvent = KeyEvent.KEYCODE_7,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "eight_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "nine_number",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nine_number,
        sdlKeyEvent = KeyEvent.KEYCODE_9,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "soul_cube",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.pentagram_rose,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "terminal",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.terminal,
        sdlKeyEvent = KeyEvent.KEYCODE_GRAVE,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "delete",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.delete_icon,
        sdlKeyEvent = KeyEvent.KEYCODE_FORWARD_DEL,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "attack",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER,
        ignoreOutOfBoundsTouchEvents = true,
        consumeTouchEventsByDefault = false,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quick_save",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F6,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quick_load",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "next_weapon",
        EngineTypes.Classic_RBDOOM_3_BFG,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "prev_weapon",
        EngineTypes.Classic_RBDOOM_3_BFG,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "use",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "PDA",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.37f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.pda,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "flashlight",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.3f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.flashlight,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "reload",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.5f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.reload_gun,
        sdlKeyEvent = KeyEvent.KEYCODE_R,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "jump",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.jump,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "autorun",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.run,
        sdlKeyEvent = KeyEvent.KEYCODE_SHIFT_LEFT,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "crouch",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.66f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_C,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "zoom",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.72f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.zoom,
        sdlKeyEvent = KeyEvent.KEYCODE_CTRL_LEFT,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.Classic_RBDOOM_3_BFG,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        controlsType = ControlsType.OnScreenStick
    )
)
