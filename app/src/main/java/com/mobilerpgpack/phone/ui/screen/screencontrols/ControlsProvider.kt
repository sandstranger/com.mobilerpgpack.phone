package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ControlsProvider (engineType: EngineTypes,
                        private val controls : Map<ControlsType,Collection<IScreenControlsView>>) : KoinComponent {

    private val preferencesStorage : PreferencesStorage = get()
    private val activeControlTypePrefsKey = "${engineType.name.lowercase()}_active_controls_type"
    private val blockTouchCameraEventsPrefsKey = "${engineType.name.lowercase()}_block_touch_camera_events"

    val activeControlsType = preferencesStorage.getEnumValue(activeControlTypePrefsKey,
        ControlsType::class.java, if (controls.containsKey(ControlsType.Default))
            ControlsType.Default else ControlsType.OnScreenStick)

    val controlsToDraw get() = controls[activeControlsType.value!!]!!

    val blockTouchCameraEventsWhenOnScreenStickActive =
        preferencesStorage.getBooleanValue(blockTouchCameraEventsPrefsKey,false)

    val drawControlsTypesInMenu get() = controls.size > 1

    fun setControlsTypeValue (controlsType: ControlsType){
        preferencesStorage.setEnumValue(activeControlTypePrefsKey, controlsType)
    }

    fun setBlockTouchCameraEventsValue (blockTouchEvents : Boolean){
        preferencesStorage.setBooleanValue(blockTouchCameraEventsPrefsKey, blockTouchEvents)
    }
}