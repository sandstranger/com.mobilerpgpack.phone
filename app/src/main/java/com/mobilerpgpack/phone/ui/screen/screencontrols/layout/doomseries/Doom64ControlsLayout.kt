package com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries

import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateQuickPanelVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateScreenControlsVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.doom64.Doom64AutorunButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.RescanSDLGamepadsForcedButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3OnScreenStick

val doom64AbsoluteTouchControlsLayout: Collection<IScreenControlsView> = listOf(
    RescanSDLGamepadsForcedButton(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.gamepad,
        isDeleted = true
    ),
    SDL3Dpad(
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
    ),
    SDL3ImageButton(
        "zero_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zero_number,
        sdlKeyEvent = KeyEvent.KEYCODE_0,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "first_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.first_number,
        sdlKeyEvent = KeyEvent.KEYCODE_1,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "second_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.second_number,
        sdlKeyEvent = KeyEvent.KEYCODE_2,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "third_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.third_number,
        sdlKeyEvent = KeyEvent.KEYCODE_3,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "fourth_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fourth_number,
        sdlKeyEvent = KeyEvent.KEYCODE_4,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "five_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.46f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.five_number,
        sdlKeyEvent = KeyEvent.KEYCODE_5,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "six_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.52f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.six_number,
        sdlKeyEvent = KeyEvent.KEYCODE_6,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "seven_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.seven_number,
        sdlKeyEvent = KeyEvent.KEYCODE_7,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "eight_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "nine_number",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nine_number,
        sdlKeyEvent = KeyEvent.KEYCODE_9,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "zoom_in",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_in,
        sdlKeyEvent = KeyEvent.KEYCODE_F7,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "zoom_out",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_out,
        sdlKeyEvent = KeyEvent.KEYCODE_F6,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "terminal",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.terminal,
        sdlKeyEvent = KeyEvent.KEYCODE_GRAVE,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "pan_zoom",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.magnifying_glass,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "follow_mode",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.follow,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "delete",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.delete_icon,
        sdlKeyEvent = KeyEvent.KEYCODE_FORWARD_DEL,
        showInQuickPanel = true
    ),
    SDL3ImageButton(
        "attack",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER,
        ignoreOutOfBoundsTouchEvents = true,
        consumeTouchEventsByDefault = false
    ),
    SDL3ImageButton(
        "quick_save",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F5
    ),
    SDL3ImageButton(
        "quick_load",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9
    ),
    SDL3MouseWheelButton(
        "next_weapon",
        EngineTypes.Doom64ExPlus,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
    ),
    SDL3MouseWheelButton(
        "prev_weapon",
        EngineTypes.Doom64ExPlus,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
    ),
    SDL3ImageButton(
        "use",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE
    ),
    SDL3ImageButton(
        "automap",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.9f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    Doom64AutorunButton(
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.run,
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles
    )
)

val doom64OnScreenStickControlsLayout: Collection<IScreenControlsView> = listOf(
    RescanSDLGamepadsForcedButton(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.gamepad,
        isDeleted = true
    ),
    SDL3Dpad(
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.Doom64ExPlus,
        stickType = StickType.RightStick,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3OnScreenStick(
        engineType = EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "zero_number",
        EngineTypes.Doom64ExPlus,
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
        EngineTypes.Doom64ExPlus,
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
        EngineTypes.Doom64ExPlus,
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
        EngineTypes.Doom64ExPlus,
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
        EngineTypes.Doom64ExPlus,
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
        EngineTypes.Doom64ExPlus,
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
        EngineTypes.Doom64ExPlus,
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
        EngineTypes.Doom64ExPlus,
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
        EngineTypes.Doom64ExPlus,
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
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nine_number,
        sdlKeyEvent = KeyEvent.KEYCODE_9,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "zoom_in",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_in,
        sdlKeyEvent = KeyEvent.KEYCODE_F7,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "zoom_out",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_out,
        sdlKeyEvent = KeyEvent.KEYCODE_F6,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "terminal",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.terminal,
        sdlKeyEvent = KeyEvent.KEYCODE_GRAVE,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "pan_zoom",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.magnifying_glass,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "follow_mode",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.follow,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "delete",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.delete_icon,
        sdlKeyEvent = KeyEvent.KEYCODE_FORWARD_DEL,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "attack",
        EngineTypes.Doom64ExPlus,
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
        "quick_save",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F5,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "quick_load",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "next_weapon",
        EngineTypes.Doom64ExPlus,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3MouseWheelButton(
        "prev_weapon",
        EngineTypes.Doom64ExPlus,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "use",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.4f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "automap",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.73f,
        offsetYPercent = 0.9f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB,
        controlsType = ControlsType.OnScreenStick
    ),
    Doom64AutorunButton(
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.run,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.Doom64ExPlus,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        controlsType = ControlsType.OnScreenStick
    )
)