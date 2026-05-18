package com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries

import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.EnableEditModeButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateQuickPanelVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateScreenControlsVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3OnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3RadialWheel

val uzDoomAbsoluteTouchControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL3RadialWheel(
        EngineTypes.UZDoom,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        isDeleted = true
    ),
    EnableEditModeButton(
        EngineTypes.UZDoom,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
    ),
    SDL3Dpad(
        EngineTypes.UZDoom,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.UZDoom,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.UZDoom,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
    ),
    SDL3ImageButton(
        "zero_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zero_number,
        sdlKeyEvent = KeyEvent.KEYCODE_0,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "first_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.first_number,
        sdlKeyEvent = KeyEvent.KEYCODE_1,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "second_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.second_number,
        sdlKeyEvent = KeyEvent.KEYCODE_2,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "third_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.third_number,
        sdlKeyEvent = KeyEvent.KEYCODE_3,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "fourth_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fourth_number,
        sdlKeyEvent = KeyEvent.KEYCODE_4,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "five_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.46f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.five_number,
        sdlKeyEvent = KeyEvent.KEYCODE_5,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "six_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.52f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.six_number,
        sdlKeyEvent = KeyEvent.KEYCODE_6,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "seven_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.seven_number,
        sdlKeyEvent = KeyEvent.KEYCODE_7,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "eight_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "nine_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nine_number,
        sdlKeyEvent = KeyEvent.KEYCODE_9,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "pan",
        EngineTypes.UZDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.follow,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "zoom_in",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_in,
        sdlKeyEvent = KeyEvent.KEYCODE_EQUALS,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "zoom_out",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_out,
        sdlKeyEvent = KeyEvent.KEYCODE_MINUS,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "say",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nothing_to_say,
        sdlKeyEvent = KeyEvent.KEYCODE_T,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "previous_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.previous,
        sdlKeyEvent = KeyEvent.KEYCODE_LEFT_BRACKET,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "next_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.next,
        sdlKeyEvent = KeyEvent.KEYCODE_RIGHT_BRACKET,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "drop_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.delete_icon,
        sdlKeyEvent = KeyEvent.KEYCODE_DEL,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "query_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.items,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "toggle_grid",
        EngineTypes.UZDoom,
        offsetXPercent = 0.23f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.grid,
        sdlKeyEvent = KeyEvent.KEYCODE_G,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "toggle_texture",
        EngineTypes.UZDoom,
        offsetXPercent = 0.29f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.texture,
        sdlKeyEvent = KeyEvent.KEYCODE_P,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "open_console",
        EngineTypes.UZDoom,
        offsetXPercent = 0.35f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.terminal,
        sdlKeyEvent = KeyEvent.KEYCODE_GRAVE,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "attack",
        EngineTypes.UZDoom,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER,
        ignoreOutOfBoundsTouchEvents = true,
        consumeTouchEventsByDefault = false
    ),
    SDL3ImageButton(
        "secondary_fire",
        EngineTypes.UZDoom,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.crossed_pistols,
        sdlKeyEvent = KeyEvent.KEYCODE_CTRL_LEFT,
        isDeleted = true,
        ignoreOutOfBoundsTouchEvents = true,
        consumeTouchEventsByDefault = false
    ),
    SDL3MouseWheelButton(
        "next_weapon",
        EngineTypes.UZDoom,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
    ),
    SDL3MouseWheelButton(
        "prev_weapon",
        EngineTypes.UZDoom,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
    ),
    SDL3ImageButton(
        "fly_up",
        EngineTypes.UZDoom,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.2f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.fly_up,
        sdlKeyEvent = KeyEvent.KEYCODE_PAGE_UP,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL3ImageButton(
        "fly_down",
        EngineTypes.UZDoom,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.37f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.fly_down,
        sdlKeyEvent = KeyEvent.KEYCODE_INSERT,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL3ImageButton(
        "stop_flying",
        EngineTypes.UZDoom,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.55f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.cancel,
        sdlKeyEvent = KeyEvent.KEYCODE_HOME,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL3ImageButton(
        "autorun",
        EngineTypes.UZDoom,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        sdlKeyEvent = KeyEvent.KEYCODE_CAPS_LOCK,
        buttonResId = R.drawable.run,
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.UZDoom,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    SDL3ImageButton(
        "quick_save",
        EngineTypes.UZDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F6
    ),
    SDL3ImageButton(
        "quick_load",
        EngineTypes.UZDoom,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9
    ),
    SDL3ImageButton(
        "use",
        EngineTypes.UZDoom,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E
    ),
    SDL3ImageButton(
        "automap",
        EngineTypes.UZDoom,
        offsetXPercent = 0.68f,
        offsetYPercent = 0.9f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    SDL3ImageButton(
        "crouch",
        EngineTypes.UZDoom,
        offsetXPercent = 0.79f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_X,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL3ImageButton(
        "jump",
        EngineTypes.UZDoom,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.7f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.jump,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL3ImageButton(
        "weapon_zoom",
        EngineTypes.UZDoom,
        offsetXPercent = 0.81f,
        offsetYPercent = 0.7f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom,
        sdlKeyEvent = KeyEvent.KEYCODE_Z,
        defaultViewRenderRule = ViewRenderRule.Disable,
        useToggle = true
    ),
    SDL3ImageButton(
        "weapon_reload",
        EngineTypes.UZDoom,
        offsetXPercent = 0.9f,
        offsetYPercent = 0.88f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.reload_gun,
        sdlKeyEvent = KeyEvent.KEYCODE_R,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
        EngineTypes.UZDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.UZDoom,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles
    ))

val uzDoomOnScreenStickControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL3RadialWheel(
        EngineTypes.UZDoom,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true
    ),
    EnableEditModeButton(
        EngineTypes.UZDoom,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3Dpad(
        EngineTypes.UZDoom,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.UZDoom,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.UZDoom,
        stickType = StickType.RightStick,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.UZDoom,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "zero_number",
        EngineTypes.UZDoom,
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
        EngineTypes.UZDoom,
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
        EngineTypes.UZDoom,
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
        EngineTypes.UZDoom,
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
        EngineTypes.UZDoom,
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
        EngineTypes.UZDoom,
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
        EngineTypes.UZDoom,
        offsetXPercent = 0.52f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.six_number,
        sdlKeyEvent = KeyEvent.KEYCODE_6,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "seven_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.seven_number,
        sdlKeyEvent = KeyEvent.KEYCODE_7,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "eight_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "nine_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nine_number,
        sdlKeyEvent = KeyEvent.KEYCODE_9,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "pan",
        EngineTypes.UZDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.follow,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "zoom_in",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_in,
        sdlKeyEvent = KeyEvent.KEYCODE_EQUALS,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "zoom_out",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_out,
        sdlKeyEvent = KeyEvent.KEYCODE_MINUS,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "say",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nothing_to_say,
        sdlKeyEvent = KeyEvent.KEYCODE_T,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "previous_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.previous,
        sdlKeyEvent = KeyEvent.KEYCODE_LEFT_BRACKET,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "next_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.next,
        sdlKeyEvent = KeyEvent.KEYCODE_RIGHT_BRACKET,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "drop_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.delete_icon,
        sdlKeyEvent = KeyEvent.KEYCODE_DEL,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "query_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.items,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "toggle_grid",
        EngineTypes.UZDoom,
        offsetXPercent = 0.23f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.grid,
        sdlKeyEvent = KeyEvent.KEYCODE_G,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "toggle_texture",
        EngineTypes.UZDoom,
        offsetXPercent = 0.29f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.texture,
        sdlKeyEvent = KeyEvent.KEYCODE_P,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "open_console",
        EngineTypes.UZDoom,
        offsetXPercent = 0.35f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.terminal,
        sdlKeyEvent = KeyEvent.KEYCODE_GRAVE,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "attack",
        EngineTypes.UZDoom,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.47f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER,
        controlsType = ControlsType.OnScreenStick,
        ignoreOutOfBoundsTouchEvents = true,
        consumeTouchEventsByDefault = false
    ),
    SDL3ImageButton(
        "secondary_fire",
        EngineTypes.UZDoom,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.47f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.crossed_pistols,
        sdlKeyEvent = KeyEvent.KEYCODE_CTRL_LEFT,
        controlsType = ControlsType.OnScreenStick,
        isDeleted = true,
        ignoreOutOfBoundsTouchEvents = true,
        consumeTouchEventsByDefault = false
    ),
    SDL3MouseWheelButton(
        "next_weapon",
        EngineTypes.UZDoom,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "prev_weapon",
        EngineTypes.UZDoom,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "fly_up",
        EngineTypes.UZDoom,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.2f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fly_up,
        sdlKeyEvent = KeyEvent.KEYCODE_PAGE_UP,
        defaultViewRenderRule = ViewRenderRule.Disable,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "fly_down",
        EngineTypes.UZDoom,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.34f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fly_down,
        sdlKeyEvent = KeyEvent.KEYCODE_INSERT,
        defaultViewRenderRule = ViewRenderRule.Disable,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "stop_flying",
        EngineTypes.UZDoom,
        offsetXPercent = 0.68f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.cancel,
        sdlKeyEvent = KeyEvent.KEYCODE_HOME,
        defaultViewRenderRule = ViewRenderRule.Disable,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "autorun",
        EngineTypes.UZDoom,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        sdlKeyEvent = KeyEvent.KEYCODE_CAPS_LOCK,
        buttonResId = R.drawable.run,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.UZDoom,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quick_save",
        EngineTypes.UZDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F6,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quick_load",
        EngineTypes.UZDoom,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "use",
        EngineTypes.UZDoom,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.45f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "automap",
        EngineTypes.UZDoom,
        offsetXPercent = 0.64f,
        offsetYPercent = 0.9f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "crouch",
        EngineTypes.UZDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_X,
        defaultViewRenderRule = ViewRenderRule.Disable,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "jump",
        EngineTypes.UZDoom,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.7f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.jump,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        defaultViewRenderRule = ViewRenderRule.Disable,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "weapon_zoom",
        EngineTypes.UZDoom,
        offsetXPercent = 0.77f,
        offsetYPercent = 0.35f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom,
        sdlKeyEvent = KeyEvent.KEYCODE_Z,
        defaultViewRenderRule = ViewRenderRule.Disable,
        controlsType = ControlsType.OnScreenStick,
        useToggle = true
    ),
    SDL3ImageButton(
        "weapon_reload",
        EngineTypes.UZDoom,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.35f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.reload_gun,
        sdlKeyEvent = KeyEvent.KEYCODE_R,
        defaultViewRenderRule = ViewRenderRule.Disable,
        controlsType = ControlsType.OnScreenStick
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
        EngineTypes.UZDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.UZDoom,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        controlsType = ControlsType.OnScreenStick
    ))