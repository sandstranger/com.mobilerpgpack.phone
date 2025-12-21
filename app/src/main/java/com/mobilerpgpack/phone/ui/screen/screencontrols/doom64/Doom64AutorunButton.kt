package com.mobilerpgpack.phone.ui.screen.screencontrols.doom64

import android.content.Context
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.ui.screen.screencontrols.ToggleImageButton
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.sun.jna.Function
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class Doom64AutorunButton (engineType: EngineTypes,
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
    ToggleImageButton(AUTORUN_BUTTON_ID,engineType, offsetXPercent, offsetYPercent,
        sizePercent, alpha, buttonResId, defaultViewRenderRule, controlsType, isDeleted,
        consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents, showInQuickPanel), KoinComponent {

    private val preferencesStorage : PreferencesStorage by inject ()

    private val objectsArray : Array <Any?> = arrayOfNulls <Any>(1)

    private val onAutoRunStateChangedNativeDelegate by lazy {
        val mainEngineLibName = preferencesStorage.let {
            var activeEngine : String
            runBlocking {
                activeEngine = it.activeEngineAsFlowString.first()
            }
            get <IEngineInfo> (named(activeEngine)).mainLibraryName
        }
        Function.getFunction(mainEngineLibName,
            "OnAutoRunStateChanged")
    }

    override fun onToggleStateChanged(isActive: Boolean) {
        onAutoRunStateChangedNativeDelegate.invokeVoid(isActive.let {
            objectsArray[0] = it
            objectsArray
        })
    }

    private companion object{
        private const val AUTORUN_BUTTON_ID = "autorun"
    }
}