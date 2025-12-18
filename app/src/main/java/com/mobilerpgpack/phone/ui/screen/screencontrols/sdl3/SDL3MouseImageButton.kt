package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import android.view.MotionEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLImageButton
import org.libsdl3.app.SDLActivity

class SDL3MouseImageButton(
    id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    private val sdlKeyEvent: Int = 0,
    buttonResId: Int = NOT_EXISTING_RES,
    override val isQuickPanel: Boolean = false,
    useToggle: Boolean = false,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha,Int.MIN_VALUE, buttonResId,
        useToggle = useToggle, defaultViewRenderRule = defaultViewRenderRule,controlsType, isDeleted, consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents) {

    init {
        show = !isQuickPanel
    }

    override fun onTouchDown(keyCode: Int) =
        SDLActivity.onVirtualMouse(sdlKeyEvent, MotionEvent.ACTION_DOWN)

    override fun onTouchUp(keyCode: Int) =
        SDLActivity.onVirtualMouse(sdlKeyEvent, MotionEvent.ACTION_UP)
}