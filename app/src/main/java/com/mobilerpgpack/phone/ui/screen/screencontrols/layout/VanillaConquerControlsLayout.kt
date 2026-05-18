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
import com.mobilerpgpack.phone.ui.screen.screencontrols.vanillaconquer.EnableDragModeButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3OnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3RadialWheel

val vanillaConquerOnScreenStickControlsLayout: Collection<IScreenControlsView> = listOf(
    EnableEditModeButton(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3OnScreenStick(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        stickType = StickType.RightStick,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.VanillaConquer,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        stickType = StickType.LeftStick,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3RadialWheel(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        isDeleted = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3Dpad(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    SDL3MouseImageButton(
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
    EnableDragModeButton(
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.45f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.photo_camera,
        consumeTouchEventsByDefault = true,
        ignoreOutOfBoundsTouchEvents = false,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "scroll_up",
        EngineTypes.VanillaConquer,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.06f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.next_weapon,
        invokeWheelEventsWhilePressingDefaultState = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "scroll_down",
        EngineTypes.VanillaConquer,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.prev_weapon,
        invokeWheelEventsWhilePressingDefaultState = true,
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
    SDL3ImageButton(
        "team_10",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zero_number,
        sdlKeyEvent = KeyEvent.KEYCODE_0,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "team_1",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.first_number,
        sdlKeyEvent = KeyEvent.KEYCODE_1,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "team_2",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.second_number,
        sdlKeyEvent = KeyEvent.KEYCODE_2,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "team_3",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.third_number,
        sdlKeyEvent = KeyEvent.KEYCODE_3,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "team_4",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fourth_number,
        sdlKeyEvent = KeyEvent.KEYCODE_4,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "team_5",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.46f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.five_number,
        sdlKeyEvent = KeyEvent.KEYCODE_5,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "team_6",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.52f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.six_number,
        sdlKeyEvent = KeyEvent.KEYCODE_6,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "team_7",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.seven_number,
        sdlKeyEvent = KeyEvent.KEYCODE_7,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "team_8",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "team_9",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nine_number,
        sdlKeyEvent = KeyEvent.KEYCODE_9,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "formation",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.stone_tower,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "home",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.warehouse,
        sdlKeyEvent = KeyEvent.KEYCODE_MOVE_HOME,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "base",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.barracks,
        sdlKeyEvent = KeyEvent.KEYCODE_H,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "resign",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.flying_flag,
        sdlKeyEvent = KeyEvent.KEYCODE_R,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "alliance",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.tower_flag,
        sdlKeyEvent = KeyEvent.KEYCODE_A,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "bookmark_1",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.position_marker,
        sdlKeyEvent = KeyEvent.KEYCODE_F9,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "bookmark_2",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.position_marker,
        sdlKeyEvent = KeyEvent.KEYCODE_F10,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "bookmark_3",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.position_marker,
        sdlKeyEvent = KeyEvent.KEYCODE_F11,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "bookmark_4",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.position_marker,
        sdlKeyEvent = KeyEvent.KEYCODE_F12,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "select_view",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.select_all,
        sdlKeyEvent = KeyEvent.KEYCODE_E,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "repair_toggle",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.auto_repair,
        sdlKeyEvent = KeyEvent.KEYCODE_T,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "sell_toggle",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.sell_card,
        sdlKeyEvent = KeyEvent.KEYCODE_Y,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "queue_move",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.directions_walk,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "options",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.63f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.settings_knobs,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "sidebar",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.63f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.menu_open,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "map_toggle",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.79f,
        offsetYPercent = 0.85f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_U,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = false
    ),
    SDL3ImageButton(
        "force_move",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.85f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.run,
        sdlKeyEvent = KeyEvent.KEYCODE_ALT_LEFT,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "force_attack",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.87f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_CTRL_LEFT,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "select",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.85f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.select,
        sdlKeyEvent = KeyEvent.KEYCODE_SHIFT_LEFT,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "scatter",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.25f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.sprint,
        sdlKeyEvent = KeyEvent.KEYCODE_X,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "guard",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.4f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.checked_shield,
        sdlKeyEvent = KeyEvent.KEYCODE_G,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "stop",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.55f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.halt,
        sdlKeyEvent = KeyEvent.KEYCODE_S,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "previous",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.22f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.previous_item,
        sdlKeyEvent = KeyEvent.KEYCODE_B,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
        "next",
        EngineTypes.VanillaConquer,
        offsetXPercent = 0.78f,
        offsetYPercent = 0.22f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_item,
        sdlKeyEvent = KeyEvent.KEYCODE_N,
        controlsType = ControlsType.OnScreenStick,
    ),
    SDL3ImageButton(
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
        KeyboardType.SDL3Keyboard,
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