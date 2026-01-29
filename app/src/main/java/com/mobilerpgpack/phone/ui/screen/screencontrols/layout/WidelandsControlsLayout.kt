package com.mobilerpgpack.phone.ui.screen.screencontrols.layout

import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateScreenControlsVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2MouseImageButton

private const val SDL_BUTTON_RIGHT = 3

val widelandsAbsoluteControlsLayout: Collection<IScreenControlsView> = listOf(
    SDL2MouseImageButton(
        "attack",
        EngineTypes.Widelands,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.49f,
        sizePercent = 0.11f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = SDL_BUTTON_RIGHT,
        consumeTouchEventsByDefault = true,
        ignoreOutOfBoundsTouchEvents = false,
        useToggle = true,
        onTouchDownEvent = { screenControls, _ -> screenControls?.apply { isZoomMode = true } },
        onTouchUpEvent = { screenControls, _ -> screenControls?.apply { isZoomMode = false } }
    ),
    ShowSDLKeyboardButton(
        KeyboardType.SDL2Keyboard,
        EngineTypes.Widelands,
        offsetXPercent = 0.75f,
        offsetYPercent = 0.15f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.keyboard,
    ),
    UpdateScreenControlsVisibilityImageButton(
        EngineTypes.Widelands,
        offsetXPercent = 0.62f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
    ))