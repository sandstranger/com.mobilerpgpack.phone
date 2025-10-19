package com.mobilerpgpack.phone.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.defaultPathToLogcatFile
import com.mobilerpgpack.phone.translator.models.TranslationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences_storage")

object PreferencesStorage {
    private val displayInSafeAreaPrefsKey = booleanPreferencesKey("display_in_safe_area")
    private val showCustomMouseCursorPrefsKey = booleanPreferencesKey("show_custom_mouse_cursor")
    private val activeEnginePrefsKey = stringPreferencesKey("current_engine")
    private val pathToWolfensteinRpgIpaPrefsKey = stringPreferencesKey("wolfenstein_rpg_ipa_file")
    private val pathToDoom2RpgIpaPrefsKey = stringPreferencesKey("doom2_rpg_ipa_file")
    private val pathToDoomRpgZipFilePrefsKey = stringPreferencesKey("doom_rpg_zip_file")
    private val hideScreenControlsPrefsKey = booleanPreferencesKey("hide_screen_controls")
    private val pathToLogFilePrefsKey = stringPreferencesKey("path_to_log_file")
    private val customScreenResolutionPrefsKey = stringPreferencesKey("custom_screen_resolution")
    private val customAspectRatioPrefsKey = stringPreferencesKey("custom_aspect_ratio")
    private val editCustomScreenControlsInGamePrefsKey = booleanPreferencesKey("edit_screen_controls_in_game")
    private val useDarkThemePrefsKey = booleanPreferencesKey("use_dark_theme")
    private val OFFSET_X_MOUSE = floatPreferencesKey("offset_x_mouse")
    private val OFFSET_Y_MOUSE = floatPreferencesKey("offset_y_mouse")
    private val enableControlsAutoHiding = booleanPreferencesKey("constols_autohiding")
    private val useSDLTTFForFontsRenderingPrefsKey = booleanPreferencesKey("sdl_ttf_render")
    private val gamesMachineTranslationsPrefsKey = booleanPreferencesKey("enable_games_translation")
    private val enableLauncherTextTranslationPrefsKey = booleanPreferencesKey("enable_launcher_translation")
    private val allowDownloadingModelsOverMobilePrefsKey = booleanPreferencesKey("allow_downloading_over_mobile")
    private val translationModelTypePrefsKey = stringPreferencesKey("translation_model_type")
    private val pathToDoom64FolderWithMainWads = stringPreferencesKey("path_to_doom64_folder_wads")
    private val pathToDoom64FolderWithMods = stringPreferencesKey("path_to_doom64_folder_mods")

    private val enableDoom64Mods = booleanPreferencesKey("enable_doom64_mods")

    val savedDoomRpgScreenWidthPrefsKey = intPreferencesKey("doomrpg_screen_width")
    val savedDoomRpgScreenHeightPrefsKey = intPreferencesKey("doomrpg_screen_height")

    fun getTranslationModelTypeValue(context: Context) =
        getStringValue(context, translationModelTypePrefsKey,TranslationType.DefaultTranslationType.toString())

    suspend fun setTranslationModelTypeValue(context: Context, valueToSave : String) =
        setStringValue(context, translationModelTypePrefsKey, valueToSave)

    suspend fun setTranslationModelTypeValue(context: Context, valueToSave : TranslationType) =
        setStringValue(context, translationModelTypePrefsKey, valueToSave.toString())

    fun getAllowDownloadingModelsOverMobileValue(context: Context) =
        getBooleanValue(context, allowDownloadingModelsOverMobilePrefsKey)

    suspend fun setAllowDownloadingModelsOverMobileValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, allowDownloadingModelsOverMobilePrefsKey, valueToSave)

    fun getEnableDoom64ModsValue(context: Context) = getBooleanValue(context, enableDoom64Mods)

    suspend fun setEnableDoom64ModsValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, enableDoom64Mods, valueToSave)

    fun getDisplayInSafeAreaValue(context: Context) = getBooleanValue(context, displayInSafeAreaPrefsKey)

    suspend fun setDisplayInSafeAreaValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, displayInSafeAreaPrefsKey, valueToSave)

    fun getEnableLauncherTextTranslationValue(context: Context) =
        getBooleanValue(context, enableLauncherTextTranslationPrefsKey, defaultValue = false)

    suspend fun setEnableLauncherTextTranslationValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, enableLauncherTextTranslationPrefsKey, valueToSave)

    fun getEnableGameMachineTextTranslationValue(context: Context) =
        getBooleanValue(context, gamesMachineTranslationsPrefsKey, defaultValue = false)

    suspend fun setEnableGameMachineTextTranslationValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, gamesMachineTranslationsPrefsKey, valueToSave)

    fun getUseSDLTTFForFontsRenderingValue(context: Context) =
        getBooleanValue(context, useSDLTTFForFontsRenderingPrefsKey, defaultValue = false)

    suspend fun setUseSDLTTFForFontsRenderingValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, useSDLTTFForFontsRenderingPrefsKey, valueToSave)

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

    fun getCustomAspectRatioValue(context: Context) = getStringValue(context, customAspectRatioPrefsKey )

    suspend fun setCustomAspectRatio(context: Context, valueToSave : String) =
        setStringValue(context, customAspectRatioPrefsKey, valueToSave)

    fun getPathToLogFileValue(context: Context) = getStringValue(context, pathToLogFilePrefsKey,
        defaultPathToLogcatFile)

    suspend fun setPathToLogFile(context: Context, valueToSave : String) =
        setStringValue(context, pathToLogFilePrefsKey, valueToSave)

    fun getPathToWolfensteinRpgIpaFileValue(context: Context) = getStringValue(context, pathToWolfensteinRpgIpaPrefsKey)

    fun getPathToDoom64ModsFolder(context: Context) = getStringValue(context, pathToDoom64FolderWithMods)

    suspend fun setPathToDoom64ModsFolder(context: Context, valueToSave : String) =
        setStringValue(context, pathToDoom64FolderWithMods, valueToSave)

    fun getPathToDoom64MainWadsFolder(context: Context) =
        getStringValue(context, pathToDoom64FolderWithMainWads)

    suspend fun setPathToDoom64MainWadsFolder(context: Context, valueToSave : String) =
        setStringValue(context, pathToDoom64FolderWithMainWads, valueToSave)

    suspend fun setPathToWolfensteinRpgIpaFile(context: Context, valueToSave : String) =
        setStringValue(context, pathToWolfensteinRpgIpaPrefsKey, valueToSave)

    fun getPathToDoom2RpgIpaFile(context: Context) = getStringValue(context, pathToDoom2RpgIpaPrefsKey)

    suspend fun setPathToDoom2RpgIpaFile(context: Context, valueToSave : String) =
        setStringValue(context, pathToDoom2RpgIpaPrefsKey, valueToSave)

    fun getPathToDoomRpgZipFileValue(context: Context) = getStringValue(context, pathToDoomRpgZipFilePrefsKey)

    suspend fun setPathToDoomRpgZipFile(context: Context, valueToSave : String) =
        setStringValue(context, pathToDoomRpgZipFilePrefsKey, valueToSave)

    fun getControlsAutoHidingValue(context: Context) =
        getBooleanValue(context, enableControlsAutoHiding, defaultValue = false)

    suspend fun setControlsAutoHidingValue(context: Context, valueToSave : Boolean) =
        setBooleanValue(context, enableControlsAutoHiding, valueToSave)

    fun getShowCustomMouseCursorValue (context: Context) = getBooleanValue(context, showCustomMouseCursorPrefsKey)

    suspend fun setShowCustomMouseCursorValue (context: Context, valueToSave : Boolean) =
        setBooleanValue(context, showCustomMouseCursorPrefsKey, valueToSave)

    fun getUseDarkThemeValue (context: Context, initialValue : Boolean = false ) =
        getBooleanValue(context, useDarkThemePrefsKey, initialValue)

    suspend fun setUseDarkThemeValue (context: Context, valueToSave : Boolean) =
        setBooleanValue(context, useDarkThemePrefsKey, valueToSave)

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

    suspend fun setIntValue(context: Context, prefsKey : Preferences.Key<Int>, valueToSave : Int ) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    fun getOffsetXMouse(context: Context): Flow<Float> {
        return context.dataStore.data.map { preferences ->
            preferences[OFFSET_X_MOUSE] ?: 0.0f
        }
    }

    fun getOffsetYMouse(context: Context): Flow<Float> {
        return context.dataStore.data.map { preferences ->
            preferences[OFFSET_Y_MOUSE] ?: 0.0f
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

     fun getBooleanValue(context: Context, prefsKey : Preferences.Key<Boolean>, defaultValue : Boolean = false): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    suspend fun setBooleanValue(context: Context, prefsKey : Preferences.Key<Boolean>, valueToSave : Boolean) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

     fun getIntValue(context: Context, prefsKey : Preferences.Key<Int>, defaultValue : Int = 0): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    private fun getStringValue(context: Context, prefsKey : Preferences.Key<String>, defaultValue : String = ""): Flow<String> {
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