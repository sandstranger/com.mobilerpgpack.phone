package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.doom64.Doom64AutorunButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2OnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3OnScreenStick
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView

val wolfensteinButtons : Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.5f,
        sizePercent = 0.25f,
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        isDeleted = true
    ),
    SDL2ImageButton(
        "move_left",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.previous,
        sdlKeyEvent = KeyEvent.KEYCODE_A
    ),
    SDL2ImageButton(
        "move_right",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.23f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.next,
        sdlKeyEvent = KeyEvent.KEYCODE_D
    ),
    SDL2ImageButton(
        "attack",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER
    ),
    SDL2ImageButton(
        "next_weapon",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.1f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.next_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_Z
    ),
    SDL2ImageButton(
        "prev_weapon",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.prev_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_X
    ),
    SDL2ImageButton(
        "pass_turn",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.pass_turn,
        sdlKeyEvent = KeyEvent.KEYCODE_C
    ),
    SDL2ImageButton(
        "automap",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    SDL2ImageButton(
        "journal",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.26f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.journal,
        sdlKeyEvent = KeyEvent.KEYCODE_P
    ),
    SDL2ImageButton(
        "items",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.items,
        sdlKeyEvent = KeyEvent.KEYCODE_I
    ),
    SDL2ImageButton(
        "syringes",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.45f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.syringe,
        sdlKeyEvent = KeyEvent.KEYCODE_O
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.05f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        keyboardInputType = CustomKeyboardView.KeyboardType.NUMBER_DECIMAL
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.5f,
        offsetYPercent = 0.05f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.toggles
    )
)

val psyDoomAbsoluteTouchControls: Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.PsyDoom,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
    ),
    SDL2OnScreenStick(
        engineType = EngineTypes.PsyDoom,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.PsyDoom,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
    ),
    SDL2ImageButton(
        "first_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.first_number,
        sdlKeyEvent = KeyEvent.KEYCODE_1,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "second_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.second_number,
        sdlKeyEvent = KeyEvent.KEYCODE_2,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "third_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.third_number,
        sdlKeyEvent = KeyEvent.KEYCODE_3,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "fourth_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fourth_number,
        sdlKeyEvent = KeyEvent.KEYCODE_4,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "five_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.five_number,
        sdlKeyEvent = KeyEvent.KEYCODE_5,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "six_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.46f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.six_number,
        sdlKeyEvent = KeyEvent.KEYCODE_6,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "seven_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.seven_number,
        sdlKeyEvent = KeyEvent.KEYCODE_7,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "eight_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "delete",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.delete_icon,
        sdlKeyEvent = KeyEvent.KEYCODE_DEL,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "pan",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.follow,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "zoom_in",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_in,
        sdlKeyEvent = KeyEvent.KEYCODE_EQUALS,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "zoom_out",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_out,
        sdlKeyEvent = KeyEvent.KEYCODE_MINUS,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "demo_player",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.screen_record,
        sdlKeyEvent = KeyEvent.KEYCODE_V,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "attack",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER,
        consumeTouchEventsByDefault = false,
        ignoreOutOfBoundsTouchEvents = true
    ),
    SDL2MouseWheelButton(
        "next_weapon",
        EngineTypes.PsyDoom,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
    ),
    SDL2MouseWheelButton(
        "prev_weapon",
        EngineTypes.PsyDoom,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
    ),
    SDL2ImageButton(
        "autorun",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        sdlKeyEvent = KeyEvent.KEYCODE_CAPS_LOCK,
        buttonResId = R.drawable.run,
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    SDL2ImageButton(
        "quick_save",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F5
    ),
    SDL2ImageButton(
        "quick_load",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9
    ),
    SDL2ImageButton(
        "use",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE
    ),
    SDL2ImageButton(
        "automap",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.9f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.PsyDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.PsyDoom,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles
    ))

val psyDoomOnScreenStickControls: Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.PsyDoom,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2OnScreenStick(
        engineType = EngineTypes.PsyDoom,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2OnScreenStick(
        engineType = EngineTypes.PsyDoom,
        stickType = StickType.RightStick,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.PsyDoom,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "first_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.first_number,
        sdlKeyEvent = KeyEvent.KEYCODE_1,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "second_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.second_number,
        sdlKeyEvent = KeyEvent.KEYCODE_2,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "third_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.third_number,
        sdlKeyEvent = KeyEvent.KEYCODE_3,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "fourth_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fourth_number,
        sdlKeyEvent = KeyEvent.KEYCODE_4,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "five_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.five_number,
        sdlKeyEvent = KeyEvent.KEYCODE_5,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "six_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.46f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.six_number,
        sdlKeyEvent = KeyEvent.KEYCODE_6,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "seven_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.seven_number,
        sdlKeyEvent = KeyEvent.KEYCODE_7,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "eight_number",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "delete",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.delete_icon,
        sdlKeyEvent = KeyEvent.KEYCODE_DEL,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "pan",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.follow,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "zoom_in",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_in,
        sdlKeyEvent = KeyEvent.KEYCODE_EQUALS,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "zoom_out",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_out,
        sdlKeyEvent = KeyEvent.KEYCODE_MINUS,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "demo_player",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.screen_record,
        sdlKeyEvent = KeyEvent.KEYCODE_V,
        showInQuickPanel = true,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "attack",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.47f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER,
        controlsType = ControlsType.OnScreenStick,
        ignoreOutOfBoundsTouchEvents = true,
        consumeTouchEventsByDefault = false
    ),
    SDL2MouseWheelButton(
        "next_weapon",
        EngineTypes.PsyDoom,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2MouseWheelButton(
        "prev_weapon",
        EngineTypes.PsyDoom,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "autorun",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        sdlKeyEvent = KeyEvent.KEYCODE_CAPS_LOCK,
        buttonResId = R.drawable.run,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "quick_save",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F5,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "quick_load",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "use",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.4f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "automap",
        EngineTypes.PsyDoom,
        offsetXPercent = 0.73f,
        offsetYPercent = 0.9f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB,
        controlsType = ControlsType.OnScreenStick
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.PsyDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        controlsType = ControlsType.OnScreenStick
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.PsyDoom,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        controlsType = ControlsType.OnScreenStick
    ))

val uzDoomAbsoluteTouchControls: Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.UZDoom,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
    ),
    SDL2OnScreenStick(
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
    SDL2ImageButton(
        "zero_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.16f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zero_number,
        sdlKeyEvent = KeyEvent.KEYCODE_0,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "first_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.first_number,
        sdlKeyEvent = KeyEvent.KEYCODE_1,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "second_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.second_number,
        sdlKeyEvent = KeyEvent.KEYCODE_2,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "third_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.third_number,
        sdlKeyEvent = KeyEvent.KEYCODE_3,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "fourth_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.fourth_number,
        sdlKeyEvent = KeyEvent.KEYCODE_4,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "five_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.46f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.five_number,
        sdlKeyEvent = KeyEvent.KEYCODE_5,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "six_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.52f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.six_number,
        sdlKeyEvent = KeyEvent.KEYCODE_6,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "seven_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.seven_number,
        sdlKeyEvent = KeyEvent.KEYCODE_7,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "eight_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.eight_number,
        sdlKeyEvent = KeyEvent.KEYCODE_8,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "nine_number",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nine_number,
        sdlKeyEvent = KeyEvent.KEYCODE_9,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "pan",
        EngineTypes.UZDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.15f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.follow,
        sdlKeyEvent = KeyEvent.KEYCODE_F,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "zoom_in",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_in,
        sdlKeyEvent = KeyEvent.KEYCODE_EQUALS,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "zoom_out",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.zoom_out,
        sdlKeyEvent = KeyEvent.KEYCODE_MINUS,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "say",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.27f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.nothing_to_say,
        sdlKeyEvent = KeyEvent.KEYCODE_T,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "previous_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.22f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.previous,
        sdlKeyEvent = KeyEvent.KEYCODE_LEFT_BRACKET,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "next_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.28f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.next,
        sdlKeyEvent = KeyEvent.KEYCODE_RIGHT_BRACKET,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "drop_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.34f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.delete_icon,
        sdlKeyEvent = KeyEvent.KEYCODE_DEL,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "query_item",
        EngineTypes.UZDoom,
        offsetXPercent = 0.4f,
        offsetYPercent = 0.39f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.items,
        sdlKeyEvent = KeyEvent.KEYCODE_Q,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "toggle_grid",
        EngineTypes.UZDoom,
        offsetXPercent = 0.23f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.grid,
        sdlKeyEvent = KeyEvent.KEYCODE_G,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "toggle_texture",
        EngineTypes.UZDoom,
        offsetXPercent = 0.29f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.texture,
        sdlKeyEvent = KeyEvent.KEYCODE_P,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
        "open_console",
        EngineTypes.UZDoom,
        offsetXPercent = 0.35f,
        offsetYPercent = 0.51f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.terminal,
        sdlKeyEvent = KeyEvent.KEYCODE_GRAVE,
        showInQuickPanel = true
    ),
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2MouseWheelButton(
        "next_weapon",
        EngineTypes.UZDoom,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
    ),
    SDL2MouseWheelButton(
        "prev_weapon",
        EngineTypes.UZDoom,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
    ),
    SDL2ImageButton(
        "fly_up",
        EngineTypes.UZDoom,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.2f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.fly_up,
        sdlKeyEvent = KeyEvent.KEYCODE_PAGE_UP,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL2ImageButton(
        "fly_down",
        EngineTypes.UZDoom,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.37f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.fly_down,
        sdlKeyEvent = KeyEvent.KEYCODE_INSERT,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL2ImageButton(
        "stop_flying",
        EngineTypes.UZDoom,
        offsetXPercent = 0.94f,
        offsetYPercent = 0.55f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.cancel,
        sdlKeyEvent = KeyEvent.KEYCODE_HOME,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL2ImageButton(
        "autorun",
        EngineTypes.UZDoom,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        sdlKeyEvent = KeyEvent.KEYCODE_CAPS_LOCK,
        buttonResId = R.drawable.run,
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.UZDoom,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    SDL2ImageButton(
        "quick_save",
        EngineTypes.UZDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F6
    ),
    SDL2ImageButton(
        "quick_load",
        EngineTypes.UZDoom,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9
    ),
    SDL2ImageButton(
        "use",
        EngineTypes.UZDoom,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E
    ),
    SDL2ImageButton(
        "automap",
        EngineTypes.UZDoom,
        offsetXPercent = 0.68f,
        offsetYPercent = 0.9f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    SDL2ImageButton(
        "crouch",
        EngineTypes.UZDoom,
        offsetXPercent = 0.79f,
        offsetYPercent = 0.9f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.crouch,
        sdlKeyEvent = KeyEvent.KEYCODE_X,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL2ImageButton(
        "jump",
        EngineTypes.UZDoom,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.7f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.jump,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        defaultViewRenderRule = ViewRenderRule.Disable
    ),
    SDL2ImageButton(
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
    SDL2ImageButton(
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
        KeyboardType.SDL2Keyboard,
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

val uzDoomOnScreenStickControls: Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.UZDoom,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2OnScreenStick(
        engineType = EngineTypes.UZDoom,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.54f,
        sizePercent = 0.21f,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2OnScreenStick(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2MouseWheelButton(
        "next_weapon",
        EngineTypes.UZDoom,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2MouseWheelButton(
        "prev_weapon",
        EngineTypes.UZDoom,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
        "autorun",
        EngineTypes.UZDoom,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        sdlKeyEvent = KeyEvent.KEYCODE_CAPS_LOCK,
        buttonResId = R.drawable.run,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.UZDoom,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "quick_save",
        EngineTypes.UZDoom,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F6,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "quick_load",
        EngineTypes.UZDoom,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "use",
        EngineTypes.UZDoom,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.45f,
        sizePercent = 0.055f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_E,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
        "automap",
        EngineTypes.UZDoom,
        offsetXPercent = 0.64f,
        offsetYPercent = 0.9f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB,
        controlsType = ControlsType.OnScreenStick
    ),
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
    SDL2ImageButton(
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
        KeyboardType.SDL2Keyboard,
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

val doom64AbsoluteTouchControls: Collection<IScreenControlsView> = listOf(
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

val doom64OnScreenStickControls: Collection<IScreenControlsView> = listOf(
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

val doomRPGButtons : Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.DoomRpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.5f,
        sizePercent = 0.25f,
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.DoomRpg,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        isDeleted = true
    ),
    SDL2ImageButton(
        "move_left",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.previous,
        sdlKeyEvent = KeyEvent.KEYCODE_A
    ),
    SDL2ImageButton(
        "move_right",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.23f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.next,
        sdlKeyEvent = KeyEvent.KEYCODE_D
    ),
    SDL2ImageButton(
        "attack",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.45f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER
    ),
    SDL2ImageButton(
        "next_weapon",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.1f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.next_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_Z
    ),
    SDL2ImageButton(
        "prev_weapon",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.prev_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_X
    ),
    SDL2ImageButton(
        "pass_turn",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.65f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.pass_turn,
        sdlKeyEvent = KeyEvent.KEYCODE_C
    ),
    SDL2ImageButton(
        "automap",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.78f,
        offsetYPercent = 0.75f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.DoomRpg,
        offsetXPercent = 0.3f,
        offsetYPercent = 0.05f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.toggles
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.DoomRpg,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.05f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        keyboardInputType = CustomKeyboardView.KeyboardType.NUMBER_DECIMAL
    )
)

val doom2RPGButtons : Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.5f,
        sizePercent = 0.25f
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
        isDeleted = true
    ),
    SDL2ImageButton(
        "move_left",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.previous,
        sdlKeyEvent = KeyEvent.KEYCODE_A
    ),
    SDL2ImageButton(
        "move_right",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.23f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.next,
        sdlKeyEvent = KeyEvent.KEYCODE_D
    ),
    SDL2ImageButton(
        "attack",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER
    ),
    SDL2ImageButton(
        "next_weapon",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.1f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.next_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_Z
    ),
    SDL2ImageButton(
        "prev_weapon",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.prev_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_X
    ),
    SDL2ImageButton(
        "pass_turn",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.pass_turn,
        sdlKeyEvent = KeyEvent.KEYCODE_C
    ),
    SDL2ImageButton(
        "automap",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    SDL2ImageButton(
        "pda_menu",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.pda,
        sdlKeyEvent = KeyEvent.KEYCODE_P
    ),
    SDL2ImageButton(
        "items",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.items,
        sdlKeyEvent = KeyEvent.KEYCODE_I
    ),
    SDL2ImageButton(
        "drinks",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.45f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.bottle,
        sdlKeyEvent = KeyEvent.KEYCODE_O
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    SDL2ImageButton(
        "bot",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.bot,
        sdlKeyEvent = KeyEvent.KEYCODE_B
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.05f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        keyboardInputType = CustomKeyboardView.KeyboardType.NUMBER_DECIMAL
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.5f,
        offsetYPercent = 0.05f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.toggles
    )
)
