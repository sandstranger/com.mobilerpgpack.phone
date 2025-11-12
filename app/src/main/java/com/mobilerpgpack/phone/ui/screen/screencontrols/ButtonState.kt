package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ButtonState(
    val id: String,
    val engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = Int.MIN_VALUE,
    val buttonResId: Int = NOT_EXISTING_RES) : KoinComponent {

    private val defaultSdlKeyEvent = sdlKeyEvent
    private val defaultOffsetXPercent = offsetXPercent
    private val defaultOffsetYPercent = offsetYPercent
    private val defaultSizePercent = sizePercent
    private val defaultAlpha = alpha
    private val engineTypeString = engineType.toString().lowercase()

    private val keyX: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_x")
    private val keyY: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_y")
    private val keySize: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_size_percent")
    private val keyAlpha: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_alpha")
    private val sdlKeyEventPrefsKey: Preferences.Key<Int> = intPreferencesKey("${engineTypeString}_${id}_sdl_key")

    private val preferencesStorage : PreferencesStorage = get()

    val allowToEditKeyEvent get() = buttonResId != NOT_EXISTING_RES && sdlKeyCode!= Int.MIN_VALUE

    var offsetXPercent by mutableFloatStateOf(offsetXPercent)
    var offsetYPercent by mutableFloatStateOf(offsetYPercent)
    var sizePercent by mutableFloatStateOf(sizePercent)
    var alpha by mutableFloatStateOf(alpha)
    var sdlKeyCode by mutableIntStateOf(sdlKeyEvent)

    suspend fun loadButtonState() {
        offsetXPercent = preferencesStorage.getFloatValue( keyX, defaultOffsetXPercent).first()!!
        offsetYPercent = preferencesStorage.getFloatValue( keyY, defaultOffsetYPercent).first()!!
        sizePercent = preferencesStorage.getFloatValue( keySize, defaultSizePercent).first()!!
        alpha = preferencesStorage.getFloatValue(keyAlpha, defaultAlpha).first()!!
        sdlKeyCode = preferencesStorage.getIntValue( sdlKeyEventPrefsKey, defaultSdlKeyEvent).first()!!
    }

    suspend fun saveButtonState() {
        preferencesStorage.setFloatValue( keyX, offsetXPercent)
        preferencesStorage.setFloatValue( keyY, offsetYPercent)
        preferencesStorage.setFloatValue( keySize, sizePercent)
        preferencesStorage.setFloatValue( keyAlpha, alpha)
        preferencesStorage.setIntValue( sdlKeyEventPrefsKey, sdlKeyCode)
    }

    suspend fun resetToDefaults() {
        offsetXPercent = defaultOffsetXPercent
        offsetYPercent = defaultOffsetYPercent
        sizePercent = defaultSizePercent
        alpha = defaultAlpha
        sdlKeyCode = defaultSdlKeyEvent
        saveButtonState()
    }

    suspend fun resetKeyEvent() {
        sdlKeyCode = defaultSdlKeyEvent
        saveButtonState()
    }

    internal companion object{
        internal const val NOT_EXISTING_RES = android.R.drawable.ic_menu_add
    }
}