package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.lifecycle.MutableLiveData
import com.mobilerpgpack.phone.engine.EngineTypes

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
                     showInQuickPanelInitialState : Boolean = false) :
    ViewState (id, engineType, offsetXPercent, offsetYPercent, sizePercent,
    alpha, sdlKeyEvent, buttonResId,defaultViewRenderRule,isDeletedInitialState,controlsType,
        false,false,true,true,
        false,false, showInQuickPanelInitialState) {

    private val invokeWheelEventsWhilePressingPrefsKey = "invoke_wheel_events_while_pressing"

    val invokeWheelEventsWhilePressing = MutableLiveData(invokeWheelEventsWhilePressingDefaultState)

    override fun load() {
        super.load()
        invokeWheelEventsWhilePressing.value = preferencesStorage.getBooleanValue(invokeWheelEventsWhilePressingPrefsKey,
            invokeWheelEventsWhilePressingDefaultState).value!!
    }

    override fun save() {
        super.save()
        preferencesStorage.setBooleanValue(invokeWheelEventsWhilePressingPrefsKey, invokeWheelEventsWhilePressing.value!!)
    }

    override fun resetToDefaults() {
        super.resetToDefaults()
        invokeWheelEventsWhilePressing.value = invokeWheelEventsWhilePressingDefaultState
    }

    override fun resetToDefaultsFromViewEditor(){
        super.resetToDefaultsFromViewEditor()
        invokeWheelEventsWhilePressing.value = invokeWheelEventsWhilePressingDefaultState
    }
}