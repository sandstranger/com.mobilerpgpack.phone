package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.sun.jna.Native

internal object SDL3MouseIconHelper {
    external fun getMouseX(): Float
    external fun getMouseY(): Float
    external fun isMouseShown(): Boolean

    init {
        Native.register(SDL3MouseIconHelper::class.java, "SDL3MouseHelper")
    }
}