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
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3OnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3RadialWheel

val arxLibertatisOnScreenStickControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL3MouseImageButton(
        "alternate_left_mouse_button",
        EngineTypes.ArxLibertatis,
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
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    EnableEditModeButton(
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3Dpad(
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3OnScreenStick(
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        stickType = StickType.RightStick,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3OnScreenStick(
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
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "open_book",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.17f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.open_book,
        sdlKeyEvent = KeyEvent.KEYCODE_DEL,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "character_sheet",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.24f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.info,
        sdlKeyEvent = KeyEvent.KEYCODE_F1,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "map",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.30f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_F3,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "spell_book",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.36f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.spell_book,
        sdlKeyEvent = KeyEvent.KEYCODE_F2,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quest_book",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.42f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.secret_book,
        sdlKeyEvent = KeyEvent.KEYCODE_F4,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "life_potion",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.round_potion,
        sdlKeyEvent = KeyEvent.KEYCODE_H,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "mana_potion",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.17f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.magic_potion,
        sdlKeyEvent = KeyEvent.KEYCODE_G,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "antidote_potion",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.24f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.bottle,
        sdlKeyEvent = KeyEvent.KEYCODE_N,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "torch",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.31f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.torch,
        sdlKeyEvent = KeyEvent.KEYCODE_T,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "open_console",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.38f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.terminal,
        sdlKeyEvent = KeyEvent.KEYCODE_GRAVE,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "precast_spell_1",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.first_number,
        sdlKeyEvent = KeyEvent.KEYCODE_1,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "precast_spell_2",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.17f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.second_number,
        sdlKeyEvent = KeyEvent.KEYCODE_2,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "precast_spell_3",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.24f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.third_number,
        sdlKeyEvent = KeyEvent.KEYCODE_3,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "interrupt_current_spell",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.31f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fourth_number,
        sdlKeyEvent = KeyEvent.KEYCODE_4,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "previous",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.previous_item,
        sdlKeyEvent = KeyEvent.KEYCODE_MINUS,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3ImageButton(
        "next",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.17f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.next_item,
        sdlKeyEvent = KeyEvent.KEYCODE_EQUALS,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3ImageButton(
        "keyboard_enter_button",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.24f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.keyboard_return,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3ImageButton(
        "show_minimap",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.31f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.position_marker,
        sdlKeyEvent = KeyEvent.KEYCODE_R,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3MouseImageButton(
        "action",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.53f,
        sizePercent = 0.095f,
        buttonResId = R.drawable.broadsword,
        sdlKeyEvent = LEFT_MOUSE_BUTTON_ID,
        consumeTouchEventsByDefault = false,
        ignoreOutOfBoundsTouchEvents = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "lean_left",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.78f,
        offsetYPercent = 0.33f,
        sizePercent = 0.055f,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        buttonResId = R.drawable.previous,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "lean_right",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.87f,
        offsetYPercent = 0.33f,
        sizePercent = 0.055f,
        sdlKeyEvent = KeyEvent.KEYCODE_E,
        buttonResId = R.drawable.next,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "use",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.66f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quick_save",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F5,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quick_load",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9,
        controlsType = ControlsType.OnScreenStick
    )
    ,
    SDL3ImageButton(
        "jump",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.84f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.jump,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "combat_mode",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.92f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.diamond_hilt,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "stealth_mode",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.visibility_off,
        sdlKeyEvent = KeyEvent.KEYCODE_SHIFT_LEFT,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "inventory",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.25f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.locked_chest,
        sdlKeyEvent = KeyEvent.KEYCODE_I,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "magic_mode",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.43f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.lunar_wand,
        sdlKeyEvent = KeyEvent.KEYCODE_CTRL_LEFT,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "crouch",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.68f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_C,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "drop_weapon",
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.59f,
        offsetYPercent = 0.88f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.drop_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_B,
        controlsType = ControlsType.OnScreenStick
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.13f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        controlsType = ControlsType.OnScreenStick,
        useReturnButton = true
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.ArxLibertatis,
        offsetXPercent = 0.6f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        controlsType = ControlsType.OnScreenStick
    ))