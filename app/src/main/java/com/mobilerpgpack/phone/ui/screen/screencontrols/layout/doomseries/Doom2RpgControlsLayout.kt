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
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3OnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3RadialWheel

val doom2RPGControlsLayout : Collection<IScreenControlsView> = listOf(
    SDL3RadialWheel(
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.55f,
        offsetYPercent = 0.28f,
        sizePercent = 0.21f,
        isDeleted = true
    ),
    EnableEditModeButton(
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
    ),
    SDL3Dpad(
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
    SDL3ImageButton(
        "move_left",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.previous,
        sdlKeyEvent = KeyEvent.KEYCODE_A
    ),
    SDL3ImageButton(
        "move_right",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.23f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.next,
        sdlKeyEvent = KeyEvent.KEYCODE_D
    ),
    SDL3ImageButton(
        "attack",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER
    ),
    SDL3ImageButton(
        "next_weapon",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.1f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.next_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_Z
    ),
    SDL3ImageButton(
        "prev_weapon",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.prev_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_X
    ),
    SDL3ImageButton(
        "pass_turn",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.pass_turn,
        sdlKeyEvent = KeyEvent.KEYCODE_C
    ),
    SDL3ImageButton(
        "automap",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    SDL3ImageButton(
        "pda_menu",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.pda,
        sdlKeyEvent = KeyEvent.KEYCODE_P
    ),
    SDL3ImageButton(
        "items",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.items,
        sdlKeyEvent = KeyEvent.KEYCODE_I
    ),
    SDL3ImageButton(
        "drinks",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.45f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.bottle,
        sdlKeyEvent = KeyEvent.KEYCODE_O
    ),
    SDL3ImageButton(
        "escape",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    SDL3ImageButton(
        "bot",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.bot,
        sdlKeyEvent = KeyEvent.KEYCODE_B
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL3Keyboard,
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
