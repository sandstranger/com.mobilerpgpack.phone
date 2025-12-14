package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ControlsProvider (engineType: EngineTypes,
                        val controlsToDraw : Map<ControlsType,Collection<IScreenControlsView>>) : KoinComponent {

    private val preferencesStorage : PreferencesStorage = get()

    private val activeControlTypePrefsKey = "${engineType.name}_active_controls_type"

    val activeControlsTypeAsFlow = preferencesStorage.getEnumValue(activeControlTypePrefsKey,
        ControlsType::class.java,controlsToDraw.keys.first())

    var activeControlsType : ControlsType
        get() = runBlocking { activeControlsTypeAsFlow.first() }
        set(value) {
            if (controlsToDraw.containsKey(value)){
                preferencesStorage.setEnumValue(activeControlTypePrefsKey, value)
            }
        }

    val drawControlsTypesInMenu get() = controlsToDraw.size > 1
}