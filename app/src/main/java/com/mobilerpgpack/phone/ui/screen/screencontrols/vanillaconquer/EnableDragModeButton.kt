package com.mobilerpgpack.phone.ui.screen.screencontrols.vanillaconquer

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer.VanillaConquerGames
import com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer.VanillaConquerPreferencesStorage
import com.mobilerpgpack.phone.main.RED_ALERT_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.TIBERIAN_DAWN_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ToggleImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.sun.jna.Native
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class EnableDragModeButton (engineType: EngineTypes,
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
                            showInQuickPanel : Boolean = false):
    ToggleImageButton("enable_drag_mode",engineType, offsetXPercent, offsetYPercent,
        sizePercent, alpha, buttonResId, defaultViewRenderRule, controlsType, isDeleted,
        consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents, showInQuickPanel), KoinComponent {

    private val preferencesStorage : VanillaConquerPreferencesStorage by inject (
        named(EngineTypes.VanillaConquer.name))

    private var jnaWasInit = false

    private external fun updateRawInputState (enableRawInput : Boolean)

    override fun onToggleStateChanged(isActive: Boolean) {
        if (!jnaWasInit){
            jnaWasInit = true
            val gameMode = preferencesStorage.activeVanillaConquerGame.value!!
            Native.register(EnableDragModeButton::class.java, if (gameMode == VanillaConquerGames.TiberianDawn)
                TIBERIAN_DAWN_NATIVE_LIB_NAME else RED_ALERT_NATIVE_LIB_NAME)
        }
        updateRawInputState(isActive)
    }

}