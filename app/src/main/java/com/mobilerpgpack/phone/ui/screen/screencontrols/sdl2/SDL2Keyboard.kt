package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2

import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLKeyboard
import org.libsdl.app.SDLInputConnection

class SDL2Keyboard : SDLKeyboard() {
    override fun onKeyDown(keyCode: Int) = org.libsdl.app.onKeyDown(keyCode, delayBeforeKeyRelease = 50L)

    override fun onCharClicked(char: Char) = SDLInputConnection.nativeCommitText(char.toString(),0)
}