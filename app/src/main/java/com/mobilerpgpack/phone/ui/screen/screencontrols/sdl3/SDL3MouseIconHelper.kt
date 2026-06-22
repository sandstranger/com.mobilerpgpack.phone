package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import com.mobilerpgpack.phone.main.ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME
import com.sun.jna.Native

internal object SDL3MouseIconHelper {
    external fun getMouseX(): Float
    external fun getMouseY(): Float
    external fun isMouseShown(): Boolean

    init {
        Native.register(SDL3MouseIconHelper::class.java, ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME)
    }
}