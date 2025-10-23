package com.mobilerpgpack.phone.utils

import android.content.Context
import androidx.compose.runtime.collectAsState
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
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.get
import org.koin.java.KoinJavaComponent.inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences_storage")

class PreferencesStorage : KoinComponent {
    private val context by inject<Context>()
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
    private val enableDoom64ModsPrefsKey = booleanPreferencesKey("enable_doom64_mods")

    val savedDoomRpgScreenWidthPrefsKey = intPreferencesKey("doomrpg_screen_width")
    val savedDoomRpgScreenHeightPrefsKey = intPreferencesKey("doomrpg_screen_height")

    val translationModelTypeValue
        get() = getStringValue( translationModelTypePrefsKey,
            TranslationType.DefaultTranslationType.toString())

    val allowDownloadingModelsOverMobileValue
        get() = getBooleanValue( allowDownloadingModelsOverMobilePrefsKey)

    val enableDoom64Mods get() = getBooleanValue( enableDoom64ModsPrefsKey)

    val enableDisplayInSafeArea get() = getBooleanValue( displayInSafeAreaPrefsKey)

    fun getUseSDLTTFForFontsRenderingValue() =
        getBooleanValue( useSDLTTFForFontsRenderingPrefsKey, defaultValue = false)



    suspend fun setTranslationModelTypeValue( valueToSave : String) =
        setStringValue( translationModelTypePrefsKey, valueToSave)

    suspend fun setTranslationModelTypeValue( valueToSave : TranslationType) =
        setStringValue( translationModelTypePrefsKey, valueToSave.toString())

    suspend fun setAllowDownloadingModelsOverMobileValue( valueToSave : Boolean) =
        setBooleanValue( allowDownloadingModelsOverMobilePrefsKey, valueToSave)

    suspend fun setEnableDoom64ModsValue( valueToSave : Boolean) =
        setBooleanValue( enableDoom64ModsPrefsKey, valueToSave)

    suspend fun setDisplayInSafeAreaValue( valueToSave : Boolean) =
        setBooleanValue( displayInSafeAreaPrefsKey, valueToSave)

    fun getEnableLauncherTextTranslationValue() =
        getBooleanValue( enableLauncherTextTranslationPrefsKey, defaultValue = false)

    suspend fun setEnableLauncherTextTranslationValue( valueToSave : Boolean) =
        setBooleanValue( enableLauncherTextTranslationPrefsKey, valueToSave)

    fun getEnableGameMachineTextTranslationValue() =
        getBooleanValue( gamesMachineTranslationsPrefsKey, defaultValue = false)

    suspend fun setEnableGameMachineTextTranslationValue( valueToSave : Boolean) =
        setBooleanValue( gamesMachineTranslationsPrefsKey, valueToSave)

    suspend fun setUseSDLTTFForFontsRenderingValue( valueToSave : Boolean) =
        setBooleanValue( useSDLTTFForFontsRenderingPrefsKey, valueToSave)

    fun getEditCustomScreenControlsInGameValue() =
        getBooleanValue( editCustomScreenControlsInGamePrefsKey, defaultValue = true)

    suspend fun setEditCustomScreenControlsInGameValue( valueToSave : Boolean) =
        setBooleanValue( editCustomScreenControlsInGamePrefsKey, valueToSave)

    fun getHideScreenControlsValue() =
        getBooleanValue( hideScreenControlsPrefsKey, defaultValue = false)

    suspend fun setHideControlsValue( valueToSave : Boolean) =
        setBooleanValue( hideScreenControlsPrefsKey, valueToSave)

    fun getCustomScreenResolutionValue() = getStringValue( customScreenResolutionPrefsKey )

    suspend fun setCustomScreenResolution( valueToSave : String) =
        setStringValue( customScreenResolutionPrefsKey, valueToSave)

    fun getCustomAspectRatioValue() = getStringValue( customAspectRatioPrefsKey )

    suspend fun setCustomAspectRatio( valueToSave : String) =
        setStringValue( customAspectRatioPrefsKey, valueToSave)

    fun getPathToLogFileValue() = getStringValue( pathToLogFilePrefsKey,
        defaultPathToLogcatFile)

    suspend fun setPathToLogFile( valueToSave : String) =
        setStringValue( pathToLogFilePrefsKey, valueToSave)

    fun getPathToWolfensteinRpgIpaFileValue() = getStringValue( pathToWolfensteinRpgIpaPrefsKey)

    fun getPathToDoom64ModsFolder() = getStringValue( pathToDoom64FolderWithMods)

    suspend fun setPathToDoom64ModsFolder( valueToSave : String) =
        setStringValue( pathToDoom64FolderWithMods, valueToSave)

    fun getPathToDoom64MainWadsFolder() =
        getStringValue( pathToDoom64FolderWithMainWads)

    suspend fun setPathToDoom64MainWadsFolder( valueToSave : String) =
        setStringValue( pathToDoom64FolderWithMainWads, valueToSave)

    suspend fun setPathToWolfensteinRpgIpaFile( valueToSave : String) =
        setStringValue( pathToWolfensteinRpgIpaPrefsKey, valueToSave)

    fun getPathToDoom2RpgIpaFile() = getStringValue( pathToDoom2RpgIpaPrefsKey)

    suspend fun setPathToDoom2RpgIpaFile( valueToSave : String) =
        setStringValue( pathToDoom2RpgIpaPrefsKey, valueToSave)

    fun getPathToDoomRpgZipFileValue() = getStringValue( pathToDoomRpgZipFilePrefsKey)

    suspend fun setPathToDoomRpgZipFile( valueToSave : String) =
        setStringValue( pathToDoomRpgZipFilePrefsKey, valueToSave)

    fun getControlsAutoHidingValue() =
        getBooleanValue( enableControlsAutoHiding, defaultValue = false)

    suspend fun setControlsAutoHidingValue( valueToSave : Boolean) =
        setBooleanValue( enableControlsAutoHiding, valueToSave)

    fun getShowCustomMouseCursorValue () = getBooleanValue( showCustomMouseCursorPrefsKey)

    suspend fun setShowCustomMouseCursorValue ( valueToSave : Boolean) =
        setBooleanValue( showCustomMouseCursorPrefsKey, valueToSave)

    fun getUseDarkThemeValue ( initialValue : Boolean = false ) =
        getBooleanValue( useDarkThemePrefsKey, initialValue)

    suspend fun setUseDarkThemeValue ( valueToSave : Boolean) =
        setBooleanValue( useDarkThemePrefsKey, valueToSave)

    suspend fun getActiveEngineValue () : EngineTypes{
        val activeEngine = getStringValue( activeEnginePrefsKey, EngineTypes.DefaultActiveEngine.toString()).first()
        return if (activeEngine.isNullOrEmpty()) EngineTypes.DefaultActiveEngine else enumValueOf<EngineTypes>(activeEngine)
    }

    fun getActiveEngineValueAsFlowString () = getStringValue( activeEnginePrefsKey,
        EngineTypes.DefaultActiveEngine.toString())

    suspend fun setActiveEngineValue ( valueToSave : EngineTypes) =
        setStringValue( activeEnginePrefsKey, valueToSave.toString())

    fun getFloatValue( prefsKey : Preferences.Key<Float>, defaultValue : Float = 0.0f ): Flow<Float?> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    suspend fun setFloatValue( prefsKey : Preferences.Key<Float>, valueToSave : Float) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    suspend fun setIntValue( prefsKey : Preferences.Key<Int>, valueToSave : Int ) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    fun getOffsetXMouse(): Flow<Float> {
        return context.dataStore.data.map { preferences ->
            preferences[OFFSET_X_MOUSE] ?: 0.0f
        }
    }

    val offsetYMouse : Flow<Float>
        get() {
            return context.dataStore.data.map { preferences ->
                preferences[OFFSET_Y_MOUSE] ?: 0.0f
            }
        }

    suspend fun setOffsetXMouse( offsetX: Float) {
        context.dataStore.edit { preferences ->
            preferences[OFFSET_X_MOUSE] = offsetX
        }
    }

    suspend fun setOffsetYMouse( offsetY: Float) {
        context.dataStore.edit { preferences ->
            preferences[OFFSET_Y_MOUSE] = offsetY
        }
    }

     fun getBooleanValue( prefsKey : Preferences.Key<Boolean>, defaultValue : Boolean = false): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    suspend fun setBooleanValue( prefsKey : Preferences.Key<Boolean>, valueToSave : Boolean) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

     fun getIntValue( prefsKey : Preferences.Key<Int>, defaultValue : Int = 0): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    private fun getStringValue( prefsKey : Preferences.Key<String>, defaultValue : String = ""): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    private suspend fun setStringValue( prefsKey : Preferences.Key<String>, valueToSave : String) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }
}