package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.sun.jna.Native

internal object SDL3MouseIconHelper {
    public external fun getMouseX(): Float
    public external fun getMouseY(): Float
    public external fun isMouseShown(): Boolean

    init {
        Native.register(SDL3MouseIconHelper::class.java, "SDL3MouseHelper")
    }
}