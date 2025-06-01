package com.mobilerpgpack.phone.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mobilerpgpack.phone.engine.EngineTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences_storage")

object PreferencesStorage {
    private val displayInSafeAreaPrefsKey = booleanPreferencesKey("display_in_safe_area")
    private val preserveScreenAspectRatioPrefsKey = booleanPreferencesKey("preserve_screen_aspect_ratio")
    private val showCustomMouseCursorPrefsKey = booleanPreferencesKey("show_custom_mouse_cursor")
    private val activeEnginePrefsKey = stringPreferencesKey("current_engine")

    fun getDisplayInSafeAreaValue(context: Context) = getBooleanValue(context, displayInSafeAreaPrefsKey)

    suspend fun setDisplayInSafeAreaValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, displayInSafeAreaPrefsKey, valueToSave)

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