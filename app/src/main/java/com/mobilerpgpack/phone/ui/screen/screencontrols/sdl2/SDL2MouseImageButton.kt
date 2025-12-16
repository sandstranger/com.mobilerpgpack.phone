package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2

import android.view.MotionEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLImageButton
import org.libsdl.app.SDLActivity

class SDL2MouseImageButton(
    id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = 0,
    buttonResId: Int = NOT_EXISTING_RES,
    override val isQuickPanel: Boolean = false,
    useToggle: Boolean = false,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,isDeleted : Boolean = false) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha,sdlKeyEvent, buttonResId, useToggle = useToggle, defaultViewRenderRule = defaultViewRenderRule,
        controlsType, isDeleted) {

    init {
        show = !isQuickPanel
    }

    override fun onTouchDown(keyCode: Int) =
        SDLActivity.onVirtualMouse(keyCode, MotionEvent.ACTION_DOWN)

    override fun onTouchUp(keyCode: Int) =
        SDLActivity.onVirtualMouse(keyCode, MotionEvent.ACTION_UP)
}