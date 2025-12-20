package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLKeyboard
import org.libsdl3.app.SDLInputConnection

class SDL3Keyboard : SDLKeyboard() {
    override fun onKeyDown(keyCode: Int) = org.libsdl3.app.onKeyDown(keyCode, delayBeforeKeyRelease = 50L)

    override fun onCharClicked(char: Char) = SDLInputConnection.nativeCommitText(char.toString(),0)
}