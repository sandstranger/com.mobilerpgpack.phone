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
import com.mobilerpgpack.phone.translator.models.TranslationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences_storage")

open class PreferencesStorage(private val context: Context, private val scope : CoroutineScope) {

    val displayInSafeAreaPrefsKey = booleanPreferencesKey("display_in_safe_area")
    val showCustomMouseCursorPrefsKey = booleanPreferencesKey("show_custom_mouse_cursor")
    val activeEnginePrefsKey = stringPreferencesKey("current_engine")
    val pathToWolfensteinRpgIpaPrefsKey = stringPreferencesKey("wolfenstein_rpg_ipa_file")
    val pathToDoom2RpgIpaPrefsKey = stringPreferencesKey("doom2_rpg_ipa_file")
    val pathToDoomRpgZipFilePrefsKey = stringPreferencesKey("doom_rpg_zip_file")
    val hideScreenControlsPrefsKey = booleanPreferencesKey("hide_screen_controls")
    val customScreenResolutionPrefsKey = stringPreferencesKey("custom_screen_resolution")
    val customAspectRatioPrefsKey = stringPreferencesKey("custom_aspect_ratio")
    val editCustomScreenControlsInGamePrefsKey = booleanPreferencesKey("edit_screen_controls_in_game")
    val useDarkThemePrefsKey = booleanPreferencesKey("use_dark_theme")
    val OFFSET_X_MOUSE = floatPreferencesKey("offset_x_mouse")
    val OFFSET_Y_MOUSE = floatPreferencesKey("offset_y_mouse")
    val enableControlsAutoHiding = booleanPreferencesKey("constols_autohiding")
    val useSDLTTFForFontsRenderingPrefsKey = booleanPreferencesKey("sdl_ttf_render")
    val gamesMachineTranslationsPrefsKey = booleanPreferencesKey("enable_games_translation")
    val enableLauncherTextTranslationPrefsKey = booleanPreferencesKey("enable_launcher_translation")
    val allowDownloadingModelsOverMobilePrefsKey = booleanPreferencesKey("allow_downloading_over_mobile")
    val translationModelTypePrefsKey = stringPreferencesKey("translation_model_type")
    val pathToDoom64FolderWithMainWads = stringPreferencesKey("path_to_doom64_folder_wads")
    val pathToDoom64FolderWithMods = stringPreferencesKey("path_to_doom64_folder_mods")
    val enableDoom64ModsPrefsKey = booleanPreferencesKey("enable_doom64_mods")

    val savedDoomRpgScreenWidthPrefsKey = intPreferencesKey("doomrpg_screen_width")

    val savedDoomRpgScreenHeightPrefsKey = intPreferencesKey("doomrpg_screen_height")

    val doom64CommandLineArgsStringPrefsKey = stringPreferencesKey("doom64_command_line_args")

    val useStandardSDLTextInputPrefsKey = booleanPreferencesKey("use_standard_sdl_text_input")

    val dataStore : DataStore<Preferences> = context.dataStore

    val useStandardSDLTextInput = getBooleanValue(useStandardSDLTextInputPrefsKey)

    val translationModelType = getStringValue(
        translationModelTypePrefsKey,
        TranslationType.DefaultTranslationType.toString()
    )

    val doom64CommandLineArgsString = getStringValue(doom64CommandLineArgsStringPrefsKey)

    val allowDownloadingModelsOverMobile = getBooleanValue(allowDownloadingModelsOverMobilePrefsKey)

    val enableDoom64Mods = getBooleanValue(enableDoom64ModsPrefsKey)

    val enableDisplayInSafeArea = getBooleanValue(displayInSafeAreaPrefsKey)

    val useSDLTTFForFontsRendering =
        getBooleanValue(useSDLTTFForFontsRenderingPrefsKey, defaultValue = false)

    val hideScreenControls =
        getBooleanValue(hideScreenControlsPrefsKey, defaultValue = false)

    val enableGameMachineTextTranslation =
        getBooleanValue(gamesMachineTranslationsPrefsKey, defaultValue = false)

    val editCustomScreenControlsInGame =
        getBooleanValue(editCustomScreenControlsInGamePrefsKey, defaultValue = true)

    val customScreenResolution = getStringValue(customScreenResolutionPrefsKey)

    val customAspectRatio = getStringValue(customAspectRatioPrefsKey)

    val pathToWolfensteinRpgIpaFile = getStringValue(pathToWolfensteinRpgIpaPrefsKey)

    val pathToDoom64ModsFolder =
        getStringValue(pathToDoom64FolderWithMods)

    val pathToDoom64MainWadsFolder = getStringValue(pathToDoom64FolderWithMainWads)

    val pathToDoom2RpgIpaFile = getStringValue(pathToDoom2RpgIpaPrefsKey)

    val pathToDoomRpgZipFile = getStringValue(pathToDoomRpgZipFilePrefsKey)

    val autoHideScreenControls = getBooleanValue(enableControlsAutoHiding, defaultValue = false)

    val showCustomMouseCursor = getBooleanValue(showCustomMouseCursorPrefsKey)

    val activeEngineAsFlowString = getStringValue(
        activeEnginePrefsKey,
        EngineTypes.DefaultActiveEngine.toString()
    )

    val offsetXMouse =
        context.dataStore.data.map { preferences -> preferences[OFFSET_X_MOUSE] ?: 0.0f }

    val offsetYMouse =
        context.dataStore.data.map { preferences -> preferences[OFFSET_Y_MOUSE] ?: 0.0f }

    suspend fun setEnableDoom64WideScreenValue(valueToSave: Boolean) =
        setBooleanValueAsync(enableDoom64ModsPrefsKey, valueToSave)

    suspend fun setTranslationModelTypeValue(valueToSave: String) =
        setStringValueAsync(translationModelTypePrefsKey, valueToSave)

    suspend fun setTranslationModelTypeValue(valueToSave: TranslationType) =
        setStringValueAsync(translationModelTypePrefsKey, valueToSave.toString())

    suspend fun setAllowDownloadingModelsOverMobileValue(valueToSave: Boolean) =
        setBooleanValueAsync(allowDownloadingModelsOverMobilePrefsKey, valueToSave)

    suspend fun setEnableDoom64ModsValue(valueToSave: Boolean) =
        setBooleanValueAsync(enableDoom64ModsPrefsKey, valueToSave)

    suspend fun setDisplayInSafeAreaValue(valueToSave: Boolean) =
        setBooleanValueAsync(displayInSafeAreaPrefsKey, valueToSave)

    suspend fun setEnableLauncherTextTranslationValue(valueToSave: Boolean) =
        setBooleanValueAsync(enableLauncherTextTranslationPrefsKey, valueToSave)

    suspend fun setEnableGameMachineTextTranslationValue(valueToSave: Boolean) =
        setBooleanValueAsync(gamesMachineTranslationsPrefsKey, valueToSave)

    suspend fun setUseSDLTTFForFontsRenderingValue(valueToSave: Boolean) =
        setBooleanValueAsync(useSDLTTFForFontsRenderingPrefsKey, valueToSave)

    suspend fun setEditCustomScreenControlsInGameValue(valueToSave: Boolean) =
        setBooleanValueAsync(editCustomScreenControlsInGamePrefsKey, valueToSave)

    suspend fun setHideControlsValue(valueToSave: Boolean) =
        setBooleanValueAsync(hideScreenControlsPrefsKey, valueToSave)

    suspend fun setCustomScreenResolution(valueToSave: String) =
        setStringValueAsync(customScreenResolutionPrefsKey, valueToSave)

    suspend fun setCustomAspectRatio(valueToSave: String) =
        setStringValueAsync(customAspectRatioPrefsKey, valueToSave)

    suspend fun setPathToDoom64ModsFolder(valueToSave: String) =
        setStringValueAsync(pathToDoom64FolderWithMods, valueToSave)

    suspend fun setPathToDoom64MainWadsFolder(valueToSave: String) =
        setStringValueAsync(pathToDoom64FolderWithMainWads, valueToSave)

    suspend fun setPathToWolfensteinRpgIpaFile(valueToSave: String) =
        setStringValueAsync(pathToWolfensteinRpgIpaPrefsKey, valueToSave)

    suspend fun setPathToDoom2RpgIpaFile(valueToSave: String) =
        setStringValueAsync(pathToDoom2RpgIpaPrefsKey, valueToSave)

    suspend fun setPathToDoomRpgZipFile(valueToSave: String) =
        setStringValueAsync(pathToDoomRpgZipFilePrefsKey, valueToSave)

    suspend fun setControlsAutoHidingValue(valueToSave: Boolean) =
        setBooleanValueAsync(enableControlsAutoHiding, valueToSave)

    suspend fun setShowCustomMouseCursorValue(valueToSave: Boolean) =
        setBooleanValueAsync(showCustomMouseCursorPrefsKey, valueToSave)

    fun getUseDarkThemeValue(initialValue: Boolean = false) =
        getBooleanValue(useDarkThemePrefsKey, initialValue)

    suspend fun setUseDarkThemeValue(valueToSave: Boolean) =
        setBooleanValueAsync(useDarkThemePrefsKey, valueToSave)

    suspend fun getActiveEngineValue(): EngineTypes {
        val activeEngine = getStringValue(activeEnginePrefsKey, EngineTypes.DefaultActiveEngine.toString()).first()
        return if (activeEngine.isNullOrEmpty()) EngineTypes.DefaultActiveEngine else enumValueOf<EngineTypes>(activeEngine)
    }

    suspend fun setActiveEngineValue(valueToSave: EngineTypes) =
        setStringValueAsync(activeEnginePrefsKey, valueToSave.toString())

    fun getFloatValue(prefsKey: Preferences.Key<Float>, defaultValue: Float = 0.0f): Flow<Float?> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    fun setFloatValue(prefsKey: Preferences.Key<Float>, valueToSave: Float) {
        scope.launch { setFloatValueAsync(prefsKey, valueToSave) }
    }

    suspend fun setFloatValueAsync(prefsKey: Preferences.Key<Float>, valueToSave: Float) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    fun setIntValue(prefsKey: Preferences.Key<Int>, valueToSave: Int) {
        scope.launch { setIntValueAsync(prefsKey, valueToSave) }
    }

    suspend fun setIntValueAsync(prefsKey: Preferences.Key<Int>, valueToSave: Int) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    suspend fun setOffsetXMouse(offsetX: Float) {
        context.dataStore.edit { preferences ->
            preferences[OFFSET_X_MOUSE] = offsetX
        }
    }

    suspend fun setOffsetYMouse(offsetY: Float) {
        context.dataStore.edit { preferences ->
            preferences[OFFSET_Y_MOUSE] = offsetY
        }
    }

    fun getBooleanValue(prefsKey: Preferences.Key<Boolean>, defaultValue: Boolean = false): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    fun setBooleanValue(prefsKey: Preferences.Key<Boolean>, valueToSave: Boolean) {
        scope.launch { setBooleanValueAsync(prefsKey, valueToSave) }
    }

    suspend fun setBooleanValueAsync(prefsKey: Preferences.Key<Boolean>, valueToSave: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    fun getIntValue(prefsKey: Preferences.Key<Int>, defaultValue: Int = 0): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    fun setStringValue(prefsKey: Preferences.Key<String>, valueToSave: String) =
        scope.launch { setStringValueAsync(prefsKey, valueToSave) }

    protected fun getStringValue(prefsKey: Preferences.Key<String>, defaultValue: String = ""): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[prefsKey] ?: defaultValue
        }
    }

    protected suspend fun setStringValueAsync(prefsKey: Preferences.Key<String>, valueToSave: String) {
        context.dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }
}