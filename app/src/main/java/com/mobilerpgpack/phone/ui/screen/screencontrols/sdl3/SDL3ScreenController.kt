package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import android.view.MotionEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLScreenController
import org.libsdl3.app.SDLActivity
import org.libsdl3.app.SDLSurface
import org.libsdl3.app.SDLSurface.getNormalizedX
import org.libsdl3.app.SDLSurface.getNormalizedY

class SDL3ScreenController : SDLScreenController() {

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
        SDLActivity.onNativeTouch(touchDeviceId, pointerId,
            eventAction, getNormalizedX(x), getNormalizedY(y), pressure,
            invokeMousePressingEvents)
    }

    override fun getMouseX() = SDL3MouseIconHelper.getMouseX()

    override fun getMouseY() = SDL3MouseIconHelper.getMouseY()

    override fun onMotionEventFinished(event: MotionEvent) {
        super.onMotionEventFinished(event)
        SDLSurface.getScaleGestureDetector().onTouchEvent(event)
    }

    override fun onPinchZoom(zoom: Float, event: Int) =
        SDLActivity.onNativeMouse(0, event, 0f, zoom, false)

    override fun buildCustomView(id: String, engineTypes: EngineTypes, keyCode: Int,controlsProvider: ControlsProvider) =
        CustomSDL3Button(id, engineTypes,offsetXPercent = 0.55f, offsetYPercent = 0.03f,
            sizePercent = 0.05f, sdlKeyEvent = keyCode, controlsType = controlsProvider.activeControlsType.value!!)

    companion object {
        const val SDL3_SCREEN_CONTROLLER_NAME = "SDL3_SCREEN_CONTROLLER"
    }
}