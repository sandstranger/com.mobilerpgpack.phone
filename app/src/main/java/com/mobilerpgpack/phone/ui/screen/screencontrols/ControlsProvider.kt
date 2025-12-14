package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getBlockingValue
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ControlsProvider (engineType: EngineTypes,
                        private val controls : Map<ControlsType,Collection<IScreenControlsView>>) : KoinComponent {

    private val preferencesStorage : PreferencesStorage = get()
    private val activeControlTypePrefsKey = "${engineType.name}_active_controls_type"
    private val blockTouchCameraEventsPrefsKey = "${engineType.name}_block_touch_camera_events"

    val activeControlsTypeAsFlow = preferencesStorage.getEnumValue(activeControlTypePrefsKey,
        ControlsType::class.java, ControlsType.Default)

    var activeControlsType : ControlsType
        get() = activeControlsTypeAsFlow.getBlockingValue()
        set(value) {
            if (controls.containsKey(value)){
                preferencesStorage.setEnumValue(activeControlTypePrefsKey, value)
            }
        }

    val controlsToDraw get() = controls[activeControlsType]!!

    val blockTouchCameraEventsWhenOnScreenStickActiveAsFlow =
        preferencesStorage.getBooleanValue(blockTouchCameraEventsPrefsKey,true)

    var blockTouchCameraEventsWhenOnScreenStickActive
        get() = blockTouchCameraEventsWhenOnScreenStickActiveAsFlow.getBlockingValue() &&
                activeControlsType == ControlsType.OnScreenStick
        set(value) {
            preferencesStorage.setBooleanValue(blockTouchCameraEventsPrefsKey, value)
        }

    val drawControlsTypesInMenu get() = controls.size > 1
}