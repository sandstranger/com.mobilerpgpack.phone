package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.lifecycle.MutableLiveData
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getNotNullValue
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.floatPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.intPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey
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
    private val ignoreOutOfBoundsTouchEventsInitialState : Boolean = false,
    private val showInQuickPanelInitialState : Boolean = false) : KoinComponent {

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
    private val showInQuickPanelPrefsKey = "${engineTypeString}_${controlsTypeString}_${id}_show_in_quick_panel"

    private var wasLoaded = false

    val allowToEditKeyEvent get() = buttonResId != NOT_EXISTING_RES && sdlKeyCode.value!= Int.MIN_VALUE

    val offsetXPercent = MutableLiveData(defaultOffsetXPercent)
    val offsetYPercent = MutableLiveData(defaultOffsetYPercent)
    val sizePercent = MutableLiveData(defaultSizePercent)
    val alpha = MutableLiveData(defaultAlpha)
    val sdlKeyCode = MutableLiveData(defaultSdlKeyEvent)
    val viewRenderRule = MutableLiveData(defaultViewRenderRule)
    val isDeleted = MutableLiveData(isDeletedInitialState)
    val useViewAsToggle = MutableLiveData(useViewAsToggleInitialState)
    val consumeTouchEvents = MutableLiveData(consumeTouchEventsInitialState)
    val ignoreOutOfBoundsTouchEvents = MutableLiveData(ignoreOutOfBoundsTouchEventsInitialState)
    val showInQuickPanel = MutableLiveData(showInQuickPanelInitialState)

    open fun load() {
        if (wasLoaded){
            return
        }
        wasLoaded = true
        offsetXPercent.value = preferencesStorage.getFloatValue( keyX, defaultOffsetXPercent).getNotNullValue()
        offsetYPercent.value = preferencesStorage.getFloatValue( keyY, defaultOffsetYPercent).getNotNullValue()
        sizePercent.value = preferencesStorage.getFloatValue( keySize, defaultSizePercent).getNotNullValue()
        alpha.value = preferencesStorage.getFloatValue(keyAlpha, defaultAlpha).getNotNullValue()
        sdlKeyCode.value = preferencesStorage.getIntValue( sdlKeyEventPrefsKey, defaultSdlKeyEvent).getNotNullValue()
        viewRenderRule.value = enumValueOf<ViewRenderRule>(preferencesStorage.getStringValue(viewRenderRulePrefsKey,
            defaultViewRenderRule.toString()).getNotNullValue())
        isDeleted.value = preferencesStorage.getBooleanValue(isDeletedPrefsKey, isDeletedInitialState).getNotNullValue()
        useViewAsToggle.value = preferencesStorage.getBooleanValue(useViewAsTogglePrefsKey, useViewAsToggleInitialState).getNotNullValue()
        consumeTouchEvents.value = preferencesStorage.getBooleanValue(consumeTouchEventsPrefsKey, consumeTouchEventsInitialState).getNotNullValue()
        ignoreOutOfBoundsTouchEvents.value = preferencesStorage.getBooleanValue(ignoreOutOfBoundsTouchEventsPrefsKey, ignoreOutOfBoundsTouchEventsInitialState).getNotNullValue()
        showInQuickPanel.value = preferencesStorage.getBooleanValue(showInQuickPanelPrefsKey, showInQuickPanel.getNotNullValue()).getNotNullValue()
    }

    open fun save() {
        preferencesStorage.setFloatValue( keyX, offsetXPercent.getNotNullValue())
        preferencesStorage.setFloatValue( keyY, offsetYPercent.getNotNullValue())
        preferencesStorage.setFloatValue( keySize, sizePercent.getNotNullValue())
        preferencesStorage.setFloatValue( keyAlpha, alpha.getNotNullValue())
        preferencesStorage.setIntValue( sdlKeyEventPrefsKey, sdlKeyCode.getNotNullValue())
        preferencesStorage.setStringValue(viewRenderRulePrefsKey, viewRenderRule.getNotNullValue().toString())
        preferencesStorage.setBooleanValue(isDeletedPrefsKey,isDeleted.getNotNullValue())
        preferencesStorage.setBooleanValue(useViewAsTogglePrefsKey, useViewAsToggle.getNotNullValue())
        preferencesStorage.setBooleanValue(consumeTouchEventsPrefsKey, consumeTouchEvents.getNotNullValue())
        preferencesStorage.setBooleanValue(ignoreOutOfBoundsTouchEventsPrefsKey, ignoreOutOfBoundsTouchEvents.getNotNullValue())
        preferencesStorage.setBooleanValue(showInQuickPanelPrefsKey, showInQuickPanel.getNotNullValue())
    }

    open fun resetToDefaults() {
        offsetXPercent.value = defaultOffsetXPercent
        offsetYPercent.value = defaultOffsetYPercent
        sizePercent.value = defaultSizePercent
        alpha.value = defaultAlpha
        sdlKeyCode.value = defaultSdlKeyEvent
        viewRenderRule.value = defaultViewRenderRule
        isDeleted.value= isDeletedInitialState
        useViewAsToggle.value = useViewAsToggleInitialState
        consumeTouchEvents.value = consumeTouchEventsInitialState
        ignoreOutOfBoundsTouchEvents.value = ignoreOutOfBoundsTouchEventsInitialState
        showInQuickPanel.value = showInQuickPanelInitialState
    }

    open fun resetToDefaultsFromViewEditor(){
        sdlKeyCode.value = defaultSdlKeyEvent
        viewRenderRule.value = defaultViewRenderRule
        useViewAsToggle.value = useViewAsToggleInitialState
        consumeTouchEvents.value = consumeTouchEventsInitialState
        ignoreOutOfBoundsTouchEvents.value = ignoreOutOfBoundsTouchEventsInitialState
        showInQuickPanel.value = showInQuickPanelInitialState
    }

    internal companion object{
        internal const val NOT_EXISTING_RES = android.R.drawable.ic_menu_add
    }
}

