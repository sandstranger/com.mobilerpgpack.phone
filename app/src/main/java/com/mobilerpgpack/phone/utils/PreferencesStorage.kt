package com.mobilerpgpack.phone.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.defaultPathToLogcatFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences_storage")

object PreferencesStorage {
    private val displayInSafeAreaPrefsKey = booleanPreferencesKey("display_in_safe_area")
    private val preserveScreenAspectRatioPrefsKey = booleanPreferencesKey("preserve_screen_aspect_ratio")
    private val showCustomMouseCursorPrefsKey = booleanPreferencesKey("show_custom_mouse_cursor")
    private val activeEnginePrefsKey = stringPreferencesKey("current_engine")
    private val useCustomFilePickerPrefsKey = booleanPreferencesKey("use_custom_file_picker")
    private val pathToWolfensteinRpgIpaPrefsKey = stringPreferencesKey("wolfenstein_rpg_ipa_file")
    private val hideScreenControlsPrefsKey = booleanPreferencesKey("hide_screen_controls")
    private val pathToLogFilePrefsKey = stringPreferencesKey("path_to_log_file")
    private val customScreenResolutionPrefsKey = stringPreferencesKey("custom_screen_resolution")
    private val editCustomScreenControlsInGamePrefsKey = booleanPreferencesKey("edit_screen_controls_in_game")
    private val OFFSET_X_MOUSE = floatPreferencesKey("offset_x_mouse")
    private val OFFSET_Y_MOUSE = floatPreferencesKey("offset_y_mouse")

    fun getDisplayInSafeAreaValue(context: Context) = getBooleanValue(context, displayInSafeAreaPrefsKey)

    suspend fun setDisplayInSafeAreaValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, displayInSafeAreaPrefsKey, valueToSave)

    fun getEditCustomScreenControlsInGameValue(context: Context) =
        getBooleanValue(context, editCustomScreenControlsInGamePrefsKey, defaultValue = true)

    suspend fun setEditCustomScreenControlsInGameValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, editCustomScreenControlsInGamePrefsKey, valueToSave)

    fun getHideScreenControlsValue(context: Context) =
        getBooleanValue(context, hideScreenControlsPrefsKey, defaultValue = false)

    suspend fun setHideControlsValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, hideScreenControlsPrefsKey, valueToSave)

    fun getCustomScreenResolutionValue(context: Context) = getStringValue(context, customScreenResolutionPrefsKey )

    suspend fun setCustomScreenResolution(context: Context, valueToSave : String) =
        setStringValue(context, customScreenResolutionPrefsKey, valueToSave)

    fun getPathToLogFileValue(context: Context) = getStringValue(context, pathToLogFilePrefsKey,
        defaultPathToLogcatFile)

    suspend fun setPathToLogFile(context: Context, valueToSave : String) =
        setStringValue(context, pathToLogFilePrefsKey, valueToSave)

    fun getPathToWolfensteinRpgIpaFileValue(context: Context) = getStringValue(context, pathToWolfensteinRpgIpaPrefsKey)

    suspend fun setPathToWolfensteinRpgIpaFile(context: Context, valueToSave : String) =
        setStringValue(context, pathToWolfensteinRpgIpaPrefsKey, valueToSave)

    fun getUseCustomFilePickerValue(context: Context) =
        getBooleanValue(context, useCustomFilePickerPrefsKey, defaultValue = context.isTelevision)

    suspend fun setUseCustomFilePickerValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, useCustomFilePickerPrefsKey, valueToSave)

    fun getPreserveAspectRatioValue (context: Context) = getBooleanValue(context, preserveScreenAspectRatioPrefsKey)

    suspend fun setPreserveAspectRationValue (context: Context, valueToSave : Boolean) =
        setBooleanValue(context, preserveScreenAspectRatioPrefsKey, valueToSave)

    fun getShowCustomMouseCursorValue (context: Context) = getBooleanValue(context, showCustomMouseCursorPrefsKey)

    suspend fun setShowCustomMouseCursorValue (context: Context, valueToSave : Boolean) =
        setBooleanValue(context, showCustomMouseCursorPrefsKey, valueToSave)

    suspend fun getActiveEngineValue (context: Context) : EngineTypes{
        val activeEngine = getStringValue(context, activeEnginePrefsKey, EngineTypes.DefaultActiveEngine.toString()).first()
        return if (activeEngine.isNullOrEmpty()) EngineTypes.DefaultActiveEngine else enumValueOf<EngineTypes>(activeEngine)
    }

    fun getActiveEngineValueAsFlowString (context: Context) = getStringValue(context, activeEnginePrefsKey,
        EngineTypes.DefaultActiveEngine.toString())

    suspend fun setActiveEngineValue (context: Context, valueToSave : EngineTypes) =
        setStringValue(context, activeEnginePrefsKey, valueToSave.toString())

    fun getFloatValue(context: Context, prefsKey : Preferences.Key<Float>, defaultValue : Float = 0.0f ): Flow<Float?> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    suspend fun setFloatValue(context: Context, prefsKey : Preferences.Key<Float>, valueToSave : Float) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    fun getIntValue(context: Context, prefsKey : Preferences.Key<Int>, defaultValue : Int = 0 ): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    suspend fun setIntValue(context: Context, prefsKey : Preferences.Key<Int>, valueToSave : Int ) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    fun getOffsetXMouse(context: Context): Flow<Float?> {
        return context.dataStore.data.map { preferences ->
            preferences[OFFSET_X_MOUSE]
        }
    }

    fun getOffsetYMouse(context: Context): Flow<Float?> {
        return context.dataStore.data.map { preferences ->
            preferences[OFFSET_Y_MOUSE]
        }
    }

    suspend fun setOffsetXMouse(context: Context, offsetX: Float) {
        context.dataStore.edit { preferences ->
            preferences[OFFSET_X_MOUSE] = offsetX
        }
    }

    suspend fun setOffsetYMouse(context: Context, offsetY: Float) {
        context.dataStore.edit { preferences ->
            preferences[OFFSET_Y_MOUSE] = offsetY
        }
    }

    private fun getBooleanValue(context: Context, prefsKey : Preferences.Key<Boolean>, defaultValue : Boolean = false): Flow<Boolean?> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    private suspend fun setBooleanValue(context: Context, prefsKey : Preferences.Key<Boolean>, valueToSave : Boolean) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    private fun getStringValue(context: Context, prefsKey : Preferences.Key<String>, defaultValue : String = ""): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    private suspend fun setStringValue(context: Context, prefsKey : Preferences.Key<String>, valueToSave : String) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }
}