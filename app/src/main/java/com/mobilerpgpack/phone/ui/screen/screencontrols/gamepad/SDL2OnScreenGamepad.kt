package com.mobilerpgpack.phone.ui.screen.screencontrols.gamepad

import com.mobilerpgpack.phone.engine.EngineTypes
import org.libsdl.app.SDLControllerManager

class SDL2OnScreenGamepad (engineType: EngineTypes,
                           offsetXPercent: Float = 0f,
                           offsetYPercent: Float = 0f,
                           sizePercent: Float = 0.13f,
                           alpha: Float = 0.65f) :
    SDLOnScreenGamepad(engineType, offsetXPercent, offsetYPercent, sizePercent, alpha) {

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