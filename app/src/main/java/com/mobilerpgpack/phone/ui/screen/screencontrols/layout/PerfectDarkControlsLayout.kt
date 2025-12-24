package com.mobilerpgpack.phone.ui.screen.screencontrols.layout

import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateQuickPanelVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateScreenControlsVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2MouseWheelButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2OnScreenStick


val perfectDarkAbsoluteTouchControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL2Dpad(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.21f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        showInQuickPanel = true
    ),
    SDL2OnScreenStick(
        EngineTypes.PerfectDark,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
        stickType = StickType.RightStick,
    ),
    SDL2OnScreenStick(
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
    SDL2ImageButton(
        "attack",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE,
        consumeTouchEventsByDefault = false,
        ignoreOutOfBoundsTouchEvents = true
    ),
    SDL2MouseWheelButton(
        "next_weapon",
        EngineTypes.PerfectDark,
        wheelUp = true,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.18f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.next_weapon,
    ),
    SDL2MouseWheelButton(
        "prev_weapon",
        EngineTypes.PerfectDark,
        wheelUp = false,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.32f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.prev_weapon,
    ),
    SDL2ImageButton(
        "autorun",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.85f,
        sizePercent = 0.06f,
        sdlKeyEvent = KeyEvent.KEYCODE_CAPS_LOCK,
        buttonResId = R.drawable.run,
    ),
    SDL2ImageButton(
        "escape",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    SDL2ImageButton(
        "quick_save",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.save,
        sdlKeyEvent = KeyEvent.KEYCODE_F5
    ),
    SDL2ImageButton(
        "quick_load",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.load,
        sdlKeyEvent = KeyEvent.KEYCODE_F9
    ),
    SDL2ImageButton(
        "use",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.use,
        sdlKeyEvent = KeyEvent.KEYCODE_SPACE
    ),
    SDL2ImageButton(
        "automap",
        EngineTypes.PerfectDark,
        offsetXPercent = 0.83f,
        offsetYPercent = 0.9f,
        sizePercent = 0.065f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.PerfectDark,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
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