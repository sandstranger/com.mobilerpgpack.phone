package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import android.view.MotionEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.fteqw.FTEQWEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLScreenController.Companion.LEFT_MOUSE_BUTTON_ID
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.libsdl3.app.SDLActivity
import org.libsdl3.app.SDLActivity.onNativeKeyDown
import org.libsdl3.app.SDLActivity.onNativeKeyUp

// Sends its key like SDL3ImageButton, or the left mouse button in games that attack with it (FTEQWGames.attackWithLeftMouseButton)
class FTEQWAttackButton(
    id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = 0,
    buttonResId: Int = NOT_EXISTING_RES,
    useToggle: Boolean = false,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default, isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    showInQuickPanel : Boolean = false) :
    SDLImageButton(id, engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha,sdlKeyEvent, buttonResId, useToggle = useToggle,
        defaultViewRenderRule = defaultViewRenderRule, controlsType, isDeleted, consumeTouchEventsByDefault,
        ignoreOutOfBoundsTouchEvents, showInQuickPanel) {

    private val engineInfo : FTEQWEngineInfo by inject(named(EngineTypes.FTEQW.name))

    override fun onTouchDown(keyCode: Int) {
        if (engineInfo.attackWithLeftMouseButton) {
            SDLActivity.onVirtualMouse(LEFT_MOUSE_BUTTON_ID, MotionEvent.ACTION_DOWN)
        } else {
            onNativeKeyDown(keyCode)
        }
    }

    override fun onTouchUp(keyCode: Int) {
        if (engineInfo.attackWithLeftMouseButton) {
            SDLActivity.onVirtualMouse(LEFT_MOUSE_BUTTON_ID, MotionEvent.ACTION_UP)
        } else {
            onNativeKeyUp(keyCode)
        }
    }

    // in a menu a press passed on to the touch screen would move the mouse cursor onto this button
    override fun consumesPress() = engineInfo.attackWithLeftMouseButton && engineInfo.menuUsesMouse
}
