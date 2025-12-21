package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLMouseWheelButton
import org.libsdl.app.SDLActivity

class SDL2MouseWheelButton(
    id: String,
    engineType: EngineTypes,
    wheelUp : Boolean,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,
    isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    invokeWheelEventsWhilePressingDefaultState : Boolean = false,
    showInQuickPanel : Boolean = false) :
    SDLMouseWheelButton(id, engineType, wheelUp,offsetXPercent, offsetYPercent, sizePercent,
        alpha,buttonResId,defaultViewRenderRule,controlsType, isDeleted,
        consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents, invokeWheelEventsWhilePressingDefaultState, showInQuickPanel,
        ){

    override fun onMouseWheel (keyCode: Int, x : Float, y : Float, event: Int ) =
        SDLActivity.onNativeMouse(keyCode, event, x, y, false)
}
