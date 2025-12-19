package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.floatPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.intPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

open class ViewState(
    val id: String,
    val engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent : Int = Int.MIN_VALUE,
    val buttonResId: Int = NOT_EXISTING_RES,
    private val defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    private val isDeletedInitialState : Boolean = false,
    controlsType: ControlsType = ControlsType.Default,
    val allowToUseViewAsToggle : Boolean = false,
    private val useViewAsToggleInitialState : Boolean = false,
    val alwaysConsumeTouchEvents : Boolean = true,
    private val consumeTouchEventsInitialState : Boolean = true,
    val touchEventsCanIgnoreOutOfBounds : Boolean = false,
    private val ignoreOutOfBoundsTouchEventsInitialState : Boolean = false) : KoinComponent {

    protected val preferencesStorage : PreferencesStorage = get()

    private val defaultSdlKeyEvent = sdlKeyEvent
    private val defaultOffsetXPercent = offsetXPercent
    private val defaultOffsetYPercent = offsetYPercent
    private val defaultSizePercent = sizePercent
    private val defaultAlpha = alpha
    private val engineTypeString = engineType.toString().lowercase()
    private val controlsTypeString = controlsType.name.lowercase()
    private val keyX = floatPreferencesKey("${engineTypeString}_${controlsTypeString}_${id}_x")
    private val keyY = floatPreferencesKey("${engineTypeString}_${controlsTypeString}__${id}_y")
    private val keySize = floatPreferencesKey("${engineTypeString}_${controlsTypeString}_${id}_size_percent")
    private val keyAlpha = floatPreferencesKey("${engineTypeString}_${controlsTypeString}_${id}_alpha")
    private val sdlKeyEventPrefsKey = intPreferencesKey("${engineTypeString}_${controlsTypeString}_${id}_sdl_key")
    private val viewRenderRulePrefsKey = stringPreferencesKey("${engineTypeString}_${controlsTypeString}_${id}_view_render_rule")
    private val isDeletedPrefsKey = booleanPreferencesKey("${engineTypeString}_${controlsTypeString}_${id}_is_deleted")
    private val useViewAsTogglePrefsKey = booleanPreferencesKey("${engineTypeString}_${controlsTypeString}_${id}_use_view_as_toggle")
    private val consumeTouchEventsPrefsKey = booleanPreferencesKey("${engineTypeString}_${controlsTypeString}_${id}_consume_touch_events")
    private val ignoreOutOfBoundsTouchEventsPrefsKey = booleanPreferencesKey("${engineTypeString}_${controlsTypeString}_${id}_ignore_out_of_bounds_touch_events")

    private var wasLoaded by mutableStateOf(false)

    val allowToEditKeyEvent get() = buttonResId != NOT_EXISTING_RES && sdlKeyCode!= Int.MIN_VALUE

    var offsetXPercent by mutableFloatStateOf(defaultOffsetXPercent)
    var offsetYPercent by mutableFloatStateOf(defaultOffsetYPercent)
    var sizePercent by mutableFloatStateOf(defaultSizePercent)
    var alpha by mutableFloatStateOf(defaultAlpha)
    var sdlKeyCode by mutableIntStateOf(defaultSdlKeyEvent)
    var viewRenderRule by mutableStateOf(defaultViewRenderRule)
    var isDeleted by mutableStateOf(isDeletedInitialState)
    var useViewAsToggle by mutableStateOf(useViewAsToggleInitialState)
    var consumeTouchEvents by mutableStateOf(consumeTouchEventsInitialState)
    var ignoreOutOfBoundsTouchEvents by mutableStateOf(ignoreOutOfBoundsTouchEventsInitialState)

    open suspend fun load() {
        if (wasLoaded){
            return
        }
        wasLoaded = true
        offsetXPercent = preferencesStorage.getFloatValue( keyX, defaultOffsetXPercent).first()
        offsetYPercent = preferencesStorage.getFloatValue( keyY, defaultOffsetYPercent).first()
        sizePercent = preferencesStorage.getFloatValue( keySize, defaultSizePercent).first()
        alpha = preferencesStorage.getFloatValue(keyAlpha, defaultAlpha).first()
        sdlKeyCode = preferencesStorage.getIntValue( sdlKeyEventPrefsKey, defaultSdlKeyEvent).first()
        viewRenderRule = enumValueOf<ViewRenderRule>(preferencesStorage.getStringValue(viewRenderRulePrefsKey,
            defaultViewRenderRule.toString()).first())
        isDeleted = preferencesStorage.getBooleanValue(isDeletedPrefsKey, isDeletedInitialState).first()
        useViewAsToggle = preferencesStorage.getBooleanValue(useViewAsTogglePrefsKey, useViewAsToggleInitialState).first()
        consumeTouchEvents = preferencesStorage.getBooleanValue(consumeTouchEventsPrefsKey, consumeTouchEventsInitialState).first()
        ignoreOutOfBoundsTouchEvents = preferencesStorage.getBooleanValue(ignoreOutOfBoundsTouchEventsPrefsKey, ignoreOutOfBoundsTouchEventsInitialState).first()
    }

    open fun save() {
        preferencesStorage.setFloatValue( keyX, offsetXPercent)
        preferencesStorage.setFloatValue( keyY, offsetYPercent)
        preferencesStorage.setFloatValue( keySize, sizePercent)
        preferencesStorage.setFloatValue( keyAlpha, alpha)
        preferencesStorage.setIntValue( sdlKeyEventPrefsKey, sdlKeyCode)
        preferencesStorage.setStringValue(viewRenderRulePrefsKey, viewRenderRule.toString())
        preferencesStorage.setBooleanValue(isDeletedPrefsKey,isDeleted)
        preferencesStorage.setBooleanValue(useViewAsTogglePrefsKey, useViewAsToggle)
        preferencesStorage.setBooleanValue(consumeTouchEventsPrefsKey, consumeTouchEvents)
        preferencesStorage.setBooleanValue(ignoreOutOfBoundsTouchEventsPrefsKey, ignoreOutOfBoundsTouchEvents)
    }

    open fun resetToDefaults() {
        offsetXPercent = defaultOffsetXPercent
        offsetYPercent = defaultOffsetYPercent
        sizePercent = defaultSizePercent
        alpha = defaultAlpha
        sdlKeyCode = defaultSdlKeyEvent
        viewRenderRule = defaultViewRenderRule
        isDeleted = isDeletedInitialState
        useViewAsToggle = useViewAsToggleInitialState
        consumeTouchEvents = consumeTouchEventsInitialState
        ignoreOutOfBoundsTouchEvents = ignoreOutOfBoundsTouchEventsInitialState
    }

    open fun resetToDefaultsFromViewEditor(){
        sdlKeyCode = defaultSdlKeyEvent
        viewRenderRule = defaultViewRenderRule
        useViewAsToggle = useViewAsToggleInitialState
        consumeTouchEvents = consumeTouchEventsInitialState
        ignoreOutOfBoundsTouchEvents = ignoreOutOfBoundsTouchEventsInitialState
    }

    internal companion object{
        internal const val NOT_EXISTING_RES = android.R.drawable.ic_menu_add
    }
}

