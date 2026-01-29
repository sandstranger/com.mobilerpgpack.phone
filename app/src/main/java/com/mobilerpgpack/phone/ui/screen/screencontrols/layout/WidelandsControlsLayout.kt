package com.mobilerpgpack.phone.ui.screen.screencontrols.layout

import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.EnableEditModeButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateQuickPanelVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.UpdateScreenControlsVisibilityImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLScreenController.Companion.LEFT_MOUSE_BUTTON_ID
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLScreenController.Companion.SDL_BUTTON_RIGHT
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLKeyboardButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2Dpad
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2MouseImageButton

val widelandsAbsoluteControlsLayout: Collection<IScreenControlsView> = listOf(
    EnableEditModeButton(
        EngineTypes.Widelands,
        offsetXPercent = 0.015f,
        offsetYPercent = 0.02f,
        sizePercent = 0.055f,
    ),
    SDL2Dpad(
        EngineTypes.Widelands,
        offsetXPercent = 0.03f,
        offsetYPercent = 0.16f,
        sizePercent = 0.16f,
    ),
    SDL2MouseImageButton(
        "left_mouse_button",
        EngineTypes.Widelands,
        offsetXPercent = 0.52f,
        offsetYPercent = 0.03f,
        sizePercent = 0.06f,
        buttonResId = R.drawable.mouse,
        sdlKeyEvent = LEFT_MOUSE_BUTTON_ID,
        useToggle = false,
        isDeleted = true
    ),
    UpdateQuickPanelVisibilityImageButton(
        EngineTypes.Widelands,
        offsetXPercent = 0.1f,
        offsetYPercent = 0.03f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.quick_panel,
    ),
    SDL2MouseImageButton(
        "zoom_mode",
        EngineTypes.Widelands,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.49f,
        sizePercent = 0.08f,
        buttonResId = R.drawable.magnifying_glass,
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