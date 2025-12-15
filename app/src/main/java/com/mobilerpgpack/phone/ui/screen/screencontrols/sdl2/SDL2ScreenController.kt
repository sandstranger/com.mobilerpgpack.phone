package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLScreenController
import com.mobilerpgpack.phone.utils.getBlockingValue
import com.mobilerpgpack.phone.utils.keyCodeMap
import kotlinx.coroutines.runBlocking
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface

class SDL2ScreenController : SDLScreenController() {

    override val viewWidth: Int get() = SDLSurface.fixedWidth

    override val viewHeight: Int get() = SDLSurface.fixedHeight

    override fun handlePointer(
        pointerId: Int,
        pressure: Float,
        x: Float,
        y: Float,
        viewWidth: Float,
        viewHeight: Float,
        eventAction: Int,
        touchDeviceId: Int
    ) {
        val normalizedX = x / viewWidth
        val normalizedY = y / viewHeight
        SDLActivity.onNativeTouch(touchDeviceId, pointerId,
            eventAction, normalizedX, normalizedY, pressure,
            engineInfo.mouseButtonsEventsCanBeInvoked)
    }

    override fun buildCustomView(id: String, engineTypes: EngineTypes, keyCode: Int,controlsProvider: ControlsProvider) =
        CustomSDL2Button(id, engineTypes,offsetXPercent = 0.55f, offsetYPercent = 0.03f,
            sizePercent = 0.05f, sdlKeyEvent = keyCode, controlsType = controlsProvider.activeControlsType)

    companion object{
        const val SDL2_SCREEN_CONTROLLER_NAME = "SDL2_SCREEN_CONTROLLER"
    }
}
