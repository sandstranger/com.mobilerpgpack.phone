package com.mobilerpgpack.phone.ui.screen.screencontrols

import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface

class SDL2MouseIcon : MouseIcon() {

    override val fixedWidth: Int get() = SDLSurface.fixedWidth

    override val fixedHeight: Int get() = SDLSurface.fixedHeight

    override fun getMouseX(): Float = SDLActivity.getMouseX().toFloat()

    override fun getMouseY(): Float = SDLActivity.getMouseY().toFloat()
}