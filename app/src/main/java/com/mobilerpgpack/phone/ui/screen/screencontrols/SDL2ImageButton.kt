package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import org.libsdl.app.SDLActivity.onNativeKeyDown
import org.libsdl.app.SDLActivity.onNativeKeyUp

class SDL2ImageButton(private val id: String,
                      private val engineType: EngineTypes,
                      private val offsetXPercent: Float = 0f,
                      private val offsetYPercent: Float = 0f,
                      private val sizePercent: Float = 0.13f,
                      private val alpha: Float = 0.65f,
                      private val sdlKeyEvent: Int = 0,
                      private val buttonResId: Int = NOT_EXISTING_RES) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha,sdlKeyEvent, buttonResId) {

    override fun onTouchDown(keyCode: Int) = onNativeKeyDown(keyCode)

    override fun onTouchUp(keyCode: Int) = onNativeKeyUp(keyCode)
}