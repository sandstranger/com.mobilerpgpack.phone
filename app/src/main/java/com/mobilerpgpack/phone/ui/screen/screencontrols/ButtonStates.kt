package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes

private const val dpadId = "dpad"

val wolfensteinButtons : Collection<ButtonState> = listOf(
    ButtonState(
        dpadId,
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.5f,
        sizePercent = 0.25f,
        buttonType = ButtonType.Dpad
    ),
    ButtonState(
        "attack",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.13f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER
    ),
    ButtonState(
        "next_weapon",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.1f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.next_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_Z
    ),
    ButtonState(
        "prev_weapon",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.prev_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_X
    ),
    ButtonState(
        "pass_turn",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.pass_turn,
        sdlKeyEvent = KeyEvent.KEYCODE_C
    ),
    ButtonState(
        "automap",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    ButtonState(
        "journal",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.26f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.journal,
        sdlKeyEvent = KeyEvent.KEYCODE_P
    ),
    ButtonState(
        "items",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.items,
        sdlKeyEvent = KeyEvent.KEYCODE_I
    ),
    ButtonState(
        "syringes",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.45f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.syringe,
        sdlKeyEvent = KeyEvent.KEYCODE_O
    ),
    ButtonState(
        "escape",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    ButtonState(
        "keyboard",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.05f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        buttonType = ButtonType.Keyboard
    ),
    ButtonState(
        "hide_controls",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.5f,
        offsetYPercent = 0.05f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.toggles,
        buttonType = ButtonType.ControlsHider
    ),
    ButtonState(
        ButtonType.DpadDown.toString().lowercase(),
        EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_DOWN,
        buttonType = ButtonType.DpadDown,
        buttonResId = R.drawable.dpad_down,
    ),
    ButtonState(
        ButtonType.DpadUp.toString().lowercase(),
        EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_UP,
        buttonType = ButtonType.DpadUp,
        buttonResId = R.drawable.dpad_up,
    ),
    ButtonState(
        ButtonType.DpadLeft.toString().lowercase(),
        EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_LEFT,
        buttonType = ButtonType.DpadLeft,
        buttonResId = R.drawable.dpad_left,
    ),
    ButtonState(
        ButtonType.DpadRight.toString().lowercase(),
        EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_RIGHT,
        buttonType = ButtonType.DpadRight,
        buttonResId = R.drawable.dpad_right,
    )
)

val doomRPGButtons : Collection<ButtonState> = listOf(
    ButtonState(
        dpadId,
        EngineTypes.DoomRpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.5f,
        sizePercent = 0.25f,
        buttonType = ButtonType.Dpad
    ),
    ButtonState(
        "attack",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.45f,
        sizePercent = 0.13f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER
    ),
    ButtonState(
        "next_weapon",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.1f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.next_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_Z
    ),
    ButtonState(
        "prev_weapon",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.prev_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_X
    ),
    ButtonState(
        "pass_turn",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.65f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.pass_turn,
        sdlKeyEvent = KeyEvent.KEYCODE_C
    ),
    ButtonState(
        "automap",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.78f,
        offsetYPercent = 0.75f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    ButtonState(
        "escape",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    ButtonState(
        "hide_controls",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.3f,
        offsetYPercent = 0.05f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.toggles,
        buttonType = ButtonType.ControlsHider
    ),
    ButtonState(
        "keyboard",
        EngineTypes.DoomRpg,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.05f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        buttonType = ButtonType.Keyboard
    ),
    ButtonState(
        ButtonType.DpadDown.toString().lowercase(),
        EngineTypes.DoomRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_DOWN,
        buttonType = ButtonType.DpadDown,
        buttonResId = R.drawable.dpad_down,
    ),
    ButtonState(
        ButtonType.DpadUp.toString().lowercase(),
        EngineTypes.DoomRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_UP,
        buttonType = ButtonType.DpadUp,
        buttonResId = R.drawable.dpad_up,
    ),
    ButtonState(
        ButtonType.DpadLeft.toString().lowercase(),
        EngineTypes.DoomRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_LEFT,
        buttonType = ButtonType.DpadLeft,
        buttonResId = R.drawable.dpad_left,
    ),
    ButtonState(
        ButtonType.DpadRight.toString().lowercase(),
        EngineTypes.DoomRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_RIGHT,
        buttonType = ButtonType.DpadRight,
        buttonResId = R.drawable.dpad_right,
    )
)

val doom2RPGButtons : Collection<ButtonState> = listOf(
    ButtonState(
        dpadId,
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.5f,
        sizePercent = 0.25f,
        buttonType = ButtonType.Dpad
    ),
    ButtonState(
        "attack",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.13f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER
    ),
    ButtonState(
        "next_weapon",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.1f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.next_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_Z
    ),
    ButtonState(
        "prev_weapon",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.prev_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_X
    ),
    ButtonState(
        "pass_turn",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.pass_turn,
        sdlKeyEvent = KeyEvent.KEYCODE_C
    ),
    ButtonState(
        "automap",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    ButtonState(
        "pda_menu",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.pda,
        sdlKeyEvent = KeyEvent.KEYCODE_P
    ),
    ButtonState(
        "items",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.items,
        sdlKeyEvent = KeyEvent.KEYCODE_I
    ),
    ButtonState(
        "drinks",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.45f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.bottle,
        sdlKeyEvent = KeyEvent.KEYCODE_O
    ),
    ButtonState(
        "escape",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    ButtonState(
        "bot",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.35f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.bot,
        sdlKeyEvent = KeyEvent.KEYCODE_B
    ),
    ButtonState(
        "keyboard",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.05f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
        buttonType = ButtonType.Keyboard
    ),
    ButtonState(
        "hide_controls",
        EngineTypes.Doom2Rpg,
        offsetXPercent = 0.5f,
        offsetYPercent = 0.05f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.toggles,
        buttonType = ButtonType.ControlsHider
    ),
    ButtonState(
        ButtonType.DpadDown.toString().lowercase(),
        EngineTypes.Doom2Rpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_DOWN,
        buttonType = ButtonType.DpadDown,
        buttonResId = R.drawable.dpad_down,
    ),
    ButtonState(
        ButtonType.DpadUp.toString().lowercase(),
        EngineTypes.Doom2Rpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_UP,
        buttonType = ButtonType.DpadUp,
        buttonResId = R.drawable.dpad_up,
    ),
    ButtonState(
        ButtonType.DpadLeft.toString().lowercase(),
        EngineTypes.Doom2Rpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_LEFT,
        buttonType = ButtonType.DpadLeft,
        buttonResId = R.drawable.dpad_left,
    ),
    ButtonState(
        ButtonType.DpadRight.toString().lowercase(),
        EngineTypes.Doom2Rpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_RIGHT,
        buttonType = ButtonType.DpadRight,
        buttonResId = R.drawable.dpad_right,
    )
)
