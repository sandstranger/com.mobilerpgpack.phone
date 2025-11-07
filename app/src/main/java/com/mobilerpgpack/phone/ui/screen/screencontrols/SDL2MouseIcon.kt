package com.mobilerpgpack.phone.ui.screen.screencontrols

import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface

class SDL2MouseIcon : MouseIcon() {

    override val fixedWidth: Int = SDLSurface.fixedWidth

    override val fixedHeight: Int = SDLSurface.fixedHeight

    override fun getMouseX(): Float = SDLActivity.getMouseX().toFloat()

    override fun getMouseY(): Float = SDLActivity.getMouseY().toFloat()
}