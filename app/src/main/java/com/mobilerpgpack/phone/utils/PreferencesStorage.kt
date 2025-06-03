package com.mobilerpgpack.phone.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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
    private val hideWolfensteinRpgScreenControlsPrefsKey = booleanPreferencesKey("hide_wolfenstein_screen_controls")
    private val pathToLogFilePrefsKey = stringPreferencesKey("path_to_log_file")
    private val customScreenResolutionPrefsKey = stringPreferencesKey("custom_screen_resolution")

    fun getDisplayInSafeAreaValue(context: Context) = getBooleanValue(context, displayInSafeAreaPrefsKey)

    suspend fun setDisplayInSafeAreaValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, displayInSafeAreaPrefsKey, valueToSave)

    fun getHideWolfensteinRpgScreenControlsValue(context: Context) =
        getBooleanValue(context, hideWolfensteinRpgScreenControlsPrefsKey, defaultValue = true)

    suspend fun setHideWolfensteinRpgScreenControlsValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, hideWolfensteinRpgScreenControlsPrefsKey, valueToSave)

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

    suspend fun setActiveEngineValue (context: Context, valueToSave : EngineTypes) =
        setStringValue(context, activeEnginePrefsKey, valueToSave.toString())

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