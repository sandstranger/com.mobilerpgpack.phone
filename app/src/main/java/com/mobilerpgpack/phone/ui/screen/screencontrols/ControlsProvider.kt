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

    var activeControlsType : ControlsType
        get() {
            return preferencesStorage.getEnumValue(activeControlTypePrefsKey,
                ControlsType::class.java, ControlsType.Default)
        }
        set(value) {
            if (controls.containsKey(value)){
                preferencesStorage.setEnumValue(activeControlTypePrefsKey, value)
            }
        }

    val controlsToDraw get() = controls[activeControlsType]!!

    var blockTouchCameraEventsWhenOnScreenStickActive : Boolean
        get() {
            return preferencesStorage.getBooleanValue(blockTouchCameraEventsPrefsKey,true)
        }
        set(value) {
            preferencesStorage.setBooleanValue(blockTouchCameraEventsPrefsKey, value)
        }

    val drawControlsTypesInMenu get() = controls.size > 1
}