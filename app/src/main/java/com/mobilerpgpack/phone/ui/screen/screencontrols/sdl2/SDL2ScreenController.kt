package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLScreenController
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface
import org.libsdl.app.onKeyDown

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
        touchDeviceId: Int,
        invokeMousePressingEvents: Boolean
    ) {
        val normalizedX = x / viewWidth
        val normalizedY = y / viewHeight
        SDLActivity.onNativeTouch(touchDeviceId, pointerId,
            eventAction, normalizedX, normalizedY, pressure,
            invokeMousePressingEvents)
    }

    override fun getMouseX() = SDLActivity.getMouseX().toFloat()

    override fun getMouseY() = SDLActivity.getMouseY().toFloat()

    override fun buildCustomView(id: String, engineTypes: EngineTypes, keyCode: Int,
                                 controlsProvider: ControlsProvider) =
        CustomSDL2Button(id, engineTypes,offsetXPercent = 0.55f, offsetYPercent = 0.03f,
            sizePercent = 0.05f, sdlKeyEvent = keyCode, controlsType = controlsProvider.activeControlsType)

    override fun onRadialWheelItemSelected(keycode: Int) = onKeyDown(keycode, delayBeforeKeyRelease = 50L)

    companion object{
        const val SDL2_SCREEN_CONTROLLER_NAME = "SDL2_SCREEN_CONTROLLER"
    }
}
