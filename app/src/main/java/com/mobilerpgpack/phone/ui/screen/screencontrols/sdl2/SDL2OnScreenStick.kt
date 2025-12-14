package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLOnScreenStick
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.StickType
import org.libsdl.app.SDLControllerManager

class SDL2OnScreenStick (engineType: EngineTypes,
                         stickType: StickType = StickType.LeftStick,
                         offsetXPercent: Float = 0f,
                         offsetYPercent: Float = 0f,
                         sizePercent: Float = 0.25f,
                         alpha: Float = 0.65f,
                         defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
                         controlsType: ControlsType = ControlsType.Default) :
    SDLOnScreenStick(engineType, stickType,offsetXPercent, offsetYPercent,
        sizePercent, alpha, defaultViewRenderRule = defaultViewRenderRule, controlsType) {

    override fun nativeAddJoystick(
        device_id: Int,
        name: String?,
        desc: String?,
        vendor_id: Int,
        product_id: Int,
        is_accelerometer: Boolean,
        button_mask: Int,
        naxes: Int,
        axis_mask: Int,
        nhats: Int,
        nballs: Int
    ): Int {
        return SDLControllerManager.nativeAddJoystick(device_id, name,desc, vendor_id, product_id,
            is_accelerometer, button_mask, naxes, axis_mask, nhats, nballs)
    }

    override fun onNativeJoy(device_id: Int, axis: Int, value: Float) =
        SDLControllerManager.onNativeJoy(device_id, axis, value)
}