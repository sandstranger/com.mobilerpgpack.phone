package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
    val buttonResId: Int = NOT_EXISTING_RES,
    private val defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    val isCustomButton : Boolean = false) : KoinComponent {

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
    private val viewRenderRulePrefsKey = stringPreferencesKey("${engineTypeString}_${id}_view_render_rule")
    private val isDeletedPrefsKey = booleanPreferencesKey("${engineTypeString}_${id}_is_deleted")

    private val preferencesStorage : PreferencesStorage = get()

    val allowToEditKeyEvent get() = buttonResId != NOT_EXISTING_RES && sdlKeyCode!= Int.MIN_VALUE

    var offsetXPercent by mutableFloatStateOf(offsetXPercent)
    var offsetYPercent by mutableFloatStateOf(offsetYPercent)
    var sizePercent by mutableFloatStateOf(sizePercent)
    var alpha by mutableFloatStateOf(alpha)
    var sdlKeyCode by mutableIntStateOf(sdlKeyEvent)
    var viewRenderRule by mutableStateOf(defaultViewRenderRule)
    var isDeleted by mutableStateOf(isCustomButton)

    suspend fun load() {
        offsetXPercent = preferencesStorage.getFloatValue( keyX, defaultOffsetXPercent).first()
        offsetYPercent = preferencesStorage.getFloatValue( keyY, defaultOffsetYPercent).first()
        sizePercent = preferencesStorage.getFloatValue( keySize, defaultSizePercent).first()
        alpha = preferencesStorage.getFloatValue(keyAlpha, defaultAlpha).first()
        sdlKeyCode = preferencesStorage.getIntValue( sdlKeyEventPrefsKey, defaultSdlKeyEvent).first()
        viewRenderRule = enumValueOf<ViewRenderRule>(preferencesStorage.getStringValue(viewRenderRulePrefsKey,
            defaultViewRenderRule.toString()).first())
        isDeleted = preferencesStorage.getBooleanValue(isDeletedPrefsKey, isCustomButton).first()
    }

    suspend fun save() {
        preferencesStorage.setFloatValueAsync( keyX, offsetXPercent)
        preferencesStorage.setFloatValueAsync( keyY, offsetYPercent)
        preferencesStorage.setFloatValueAsync( keySize, sizePercent)
        preferencesStorage.setFloatValueAsync( keyAlpha, alpha)
        preferencesStorage.setIntValueAsync( sdlKeyEventPrefsKey, sdlKeyCode)
        preferencesStorage.setStringValueAsync(viewRenderRulePrefsKey, viewRenderRule.toString())
        preferencesStorage.setBooleanValueAsync(isDeletedPrefsKey,isDeleted)
    }

    fun resetToDefaults() {
        offsetXPercent = defaultOffsetXPercent
        offsetYPercent = defaultOffsetYPercent
        sizePercent = defaultSizePercent
        alpha = defaultAlpha
        sdlKeyCode = defaultSdlKeyEvent
        viewRenderRule = defaultViewRenderRule
        isDeleted = isCustomButton
    }

    suspend fun resetKeyEvent() {
        sdlKeyCode = defaultSdlKeyEvent
        save()
    }

    internal companion object{
        internal const val NOT_EXISTING_RES = android.R.drawable.ic_menu_add
    }
}

