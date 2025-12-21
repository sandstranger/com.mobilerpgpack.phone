package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.engine.EngineTypes
import kotlinx.coroutines.flow.first

class MouseViewState(id: String,
                     engineType: EngineTypes,
                     offsetXPercent: Float = 0f,
                     offsetYPercent: Float = 0f,
                     sizePercent: Float = 0.13f,
                     alpha: Float = 0.65f,
                     sdlKeyEvent : Int = Int.MIN_VALUE,
                     buttonResId: Int = NOT_EXISTING_RES,
                     defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
                     isDeletedInitialState : Boolean = false,
                     controlsType: ControlsType = ControlsType.Default,
                     private val invokeWheelEventsWhilePressingDefaultState : Boolean = false,
                     private val showInQuickPanelInitialState : Boolean = false) :
    ViewState (id, engineType, offsetXPercent, offsetYPercent, sizePercent,
    alpha, sdlKeyEvent, buttonResId,defaultViewRenderRule,isDeletedInitialState,controlsType,
        false,false,true,true,
        false,false, showInQuickPanelInitialState) {

    private val invokeWheelEventsWhilePressingPrefsKey = "invoke_wheel_events_while_pressing"

    var invokeWheelEventsWhilePressing by mutableStateOf(invokeWheelEventsWhilePressingDefaultState)

    override suspend fun load() {
        super.load()
        invokeWheelEventsWhilePressing = preferencesStorage.getBooleanValue(invokeWheelEventsWhilePressingPrefsKey,
            invokeWheelEventsWhilePressingDefaultState).first()
    }

    override fun save() {
        super.save()
        preferencesStorage.setBooleanValue(invokeWheelEventsWhilePressingPrefsKey, invokeWheelEventsWhilePressing)
    }

    override fun resetToDefaults() {
        super.resetToDefaults()
        invokeWheelEventsWhilePressing = invokeWheelEventsWhilePressingDefaultState
    }

    override fun resetToDefaultsFromViewEditor(){
        super.resetToDefaultsFromViewEditor()
        invokeWheelEventsWhilePressing = invokeWheelEventsWhilePressingDefaultState
    }
}