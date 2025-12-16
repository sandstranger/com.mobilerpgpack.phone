package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getBlockingValue
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ControlsProvider (engineType: EngineTypes,
                        private val controls : Map<ControlsType,Collection<IScreenControlsView>>) : KoinComponent {

    private val preferencesStorage : PreferencesStorage = get()
    private val activeControlTypePrefsKey = "${engineType.name.lowercase()}_active_controls_type"
    private val blockTouchCameraEventsPrefsKey = "${engineType.name.lowercase()}_block_touch_camera_events"

    private var  _blockTouchCameraEventsWhenOnScreenStickActive by mutableStateOf<Boolean?>(null)
    private var _activeControlsType by mutableStateOf<ControlsType?>(null)

    val activeControlsTypeAsFlow = preferencesStorage.getEnumValue(activeControlTypePrefsKey,
        ControlsType::class.java, ControlsType.Default)

    var activeControlsType : ControlsType
        get() {
            _activeControlsType ?: run { _activeControlsType = activeControlsTypeAsFlow.getBlockingValue() }
            return _activeControlsType!!
        }
        set(value) {
            if (controls.containsKey(value)){
                _activeControlsType = value
                preferencesStorage.setEnumValue(activeControlTypePrefsKey, value)
            }
        }

    val controlsToDraw get() = controls[activeControlsType]!!

    val blockTouchCameraEventsWhenOnScreenStickActiveAsFlow =
        preferencesStorage.getBooleanValue(blockTouchCameraEventsPrefsKey,true)

    var blockTouchCameraEventsWhenOnScreenStickActive : Boolean
        get() {
            _blockTouchCameraEventsWhenOnScreenStickActive ?: run {
                _blockTouchCameraEventsWhenOnScreenStickActive = blockTouchCameraEventsWhenOnScreenStickActiveAsFlow.getBlockingValue() }
            return _blockTouchCameraEventsWhenOnScreenStickActive!! && activeControlsType == ControlsType.OnScreenStick
        }
        set(value) {
            _blockTouchCameraEventsWhenOnScreenStickActive = value
            preferencesStorage.setBooleanValue(blockTouchCameraEventsPrefsKey, value)
        }

    val drawControlsTypesInMenu get() = controls.size > 1
}