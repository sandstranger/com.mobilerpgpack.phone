package com.mobilerpgpack.phone.ui.screen.screencontrols

import org.libsdl3.app.SDLSurface

class SDL3MouseIcon : MouseIcon() {

    override val fixedWidth: Int get() = SDLSurface.fixedWidth

    override val fixedHeight: Int get() = SDLSurface.fixedHeight

    override fun getMouseX(): Float = SDL3MouseIconHelper.getMouseX()

    override fun getMouseY(): Float = SDL3MouseIconHelper.getMouseY()
}