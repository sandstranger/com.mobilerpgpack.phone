package com.mobilerpgpack.phone.utils

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.utils.sharesprefs.SharedPrefsRepository
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.floatPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.intPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

open class PreferencesStorage : SharedPrefsRepository()  {

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
    val customOnScreenKeyboardTransparencyPrefsKey = floatPreferencesKey("custom_keyboard_transparency")
    val alwaysUseFullScreenTouchModePrefsKey = booleanPreferencesKey("always_use_fullscreen_touch_mode")

    val alwaysUseFullScreenTouchMode = getBooleanValue(alwaysUseFullScreenTouchModePrefsKey, true)

    val customOnScreenKeyboardTransparency = getFloatValue(customOnScreenKeyboardTransparencyPrefsKey, 1.0f)

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

    val offsetXMouse = getFloatValue(OFFSET_X_MOUSE, 0f)

    val offsetYMouse = getFloatValue(OFFSET_Y_MOUSE, 0f)

    suspend fun setTranslationModelTypeValue(valueToSave: TranslationType) =
        setStringValueAsync(translationModelTypePrefsKey, valueToSave.toString())

    suspend fun setEnableGameMachineTextTranslationValue(valueToSave: Boolean) =
        setBooleanValueAsync(gamesMachineTranslationsPrefsKey, valueToSave)

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

    fun getUseDarkThemeValue(initialValue: Boolean = false) =
        getBooleanValue(useDarkThemePrefsKey, initialValue)

    fun setActiveEngineValue(valueToSave: EngineTypes) =
        setStringValue(activeEnginePrefsKey, valueToSave.toString())
}