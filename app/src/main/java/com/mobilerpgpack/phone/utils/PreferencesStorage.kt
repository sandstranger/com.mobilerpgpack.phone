package com.mobilerpgpack.phone.utils

import android.content.Context
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.utils.sharesprefs.SharedPrefsRepository
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.floatPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.intPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

open class PreferencesStorage : SharedPrefsRepository(), KoinComponent  {

    private val context : Context = get()

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
    val allowDownloadingModelsOverMobilePrefsKey = booleanPreferencesKey("allow_downloading_over_mobile")
    val translationModelTypePrefsKey = stringPreferencesKey("translation_model_type")
    val pathToDoom64FolderWithMainWads = stringPreferencesKey("path_to_doom64_folder_wads")
    val pathToDoom64FolderWithMods = stringPreferencesKey("path_to_doom64_folder_mods")
    val enableDoom64ModsPrefsKey = booleanPreferencesKey("enable_doom64_mods")
    val savedDoomRpgScreenWidthPrefsKey = intPreferencesKey("doomrpg_screen_width")
    val savedDoomRpgScreenHeightPrefsKey = intPreferencesKey("doomrpg_screen_height")
    val doom64CommandLineArgsStringPrefsKey = stringPreferencesKey("doom64_command_line_args")
    val customOnScreenKeyboardTransparencyPrefsKey = floatPreferencesKey("custom_keyboard_transparency")
    val alwaysUseFullScreenTouchModePrefsKey = booleanPreferencesKey("always_use_fullscreen_touch_mode")
    val useFloatingStartGameButtonPrefsKey = booleanPreferencesKey("use_floating_start_game_button")
    val alwaysShowKeyboardButtonPrefsKey = booleanPreferencesKey("always_show_keyboard_button")
    val useTouchScreenInGamesMenuPrefsKey = booleanPreferencesKey("use_touchscreen_in_games_menu")

    val useTouchScreenInGamesMenu get() = getBooleanValue(useTouchScreenInGamesMenuPrefsKey, true)

    val useFloatingStartGameButton get() = getBooleanValue(useFloatingStartGameButtonPrefsKey, !context.isTelevision)

    val alwaysShowKeyboardButton get() = getBooleanValue(alwaysShowKeyboardButtonPrefsKey, true)

    val alwaysUseFullScreenTouchMode get() = getBooleanValue(alwaysUseFullScreenTouchModePrefsKey, true)

    val customOnScreenKeyboardTransparency get() = getFloatValue(customOnScreenKeyboardTransparencyPrefsKey, 1.0f)

    val translationModelType get() = getStringValue(
        translationModelTypePrefsKey,
        TranslationType.DefaultTranslationType.toString()
    )

    val doom64CommandLineArgsString get() = getStringValue(doom64CommandLineArgsStringPrefsKey)

    val allowDownloadingModelsOverMobile get() = getBooleanValue(allowDownloadingModelsOverMobilePrefsKey)

    val enableDoom64Mods get() = getBooleanValue(enableDoom64ModsPrefsKey)

    val enableDisplayInSafeArea get() = getBooleanValue(displayInSafeAreaPrefsKey)

    val useSDLTTFForFontsRendering get() = getBooleanValue(useSDLTTFForFontsRenderingPrefsKey, defaultValue = false)

    val hideScreenControls get() = getBooleanValue(hideScreenControlsPrefsKey, defaultValue = false)

    val enableGameMachineTextTranslation get() = getBooleanValue(gamesMachineTranslationsPrefsKey, defaultValue = false)

    val editCustomScreenControlsInGame get() = getBooleanValue(editCustomScreenControlsInGamePrefsKey, defaultValue = true)

    val customScreenResolution get() = getStringValue(customScreenResolutionPrefsKey)

    val customAspectRatio get() = getStringValue(customAspectRatioPrefsKey)

    val pathToWolfensteinRpgIpaFile get() = getStringValue(pathToWolfensteinRpgIpaPrefsKey)

    val pathToDoom64ModsFolder get() = getStringValue(pathToDoom64FolderWithMods)

    val pathToDoom64MainWadsFolder get() = getStringValue(pathToDoom64FolderWithMainWads)

    val pathToDoom2RpgIpaFile get() = getStringValue(pathToDoom2RpgIpaPrefsKey)

    val pathToDoomRpgZipFile get() = getStringValue(pathToDoomRpgZipFilePrefsKey)

    val autoHideScreenControls get() = getBooleanValue(enableControlsAutoHiding, defaultValue = false)

    val showCustomMouseCursor get() = getBooleanValue(showCustomMouseCursorPrefsKey)

    val activeEngineString get() = getStringValue(
        activeEnginePrefsKey,
        EngineTypes.DefaultActiveEngine.toString()
    )

    val offsetXMouse get() = getFloatValue(OFFSET_X_MOUSE, 0f)

    val offsetYMouse get() = getFloatValue(OFFSET_Y_MOUSE, 0f)

    fun setTranslationModelTypeValue(valueToSave: TranslationType) =
        setStringValue(translationModelTypePrefsKey, valueToSave.toString())

    fun setEnableGameMachineTextTranslationValue(valueToSave: Boolean) =
        setBooleanValue(gamesMachineTranslationsPrefsKey, valueToSave)

    fun setPathToDoom64ModsFolder(valueToSave: String) =
        setStringValue(pathToDoom64FolderWithMods, valueToSave)

    fun setPathToDoom64MainWadsFolder(valueToSave: String) =
        setStringValue(pathToDoom64FolderWithMainWads, valueToSave)

    fun setPathToWolfensteinRpgIpaFile(valueToSave: String) =
        setStringValue(pathToWolfensteinRpgIpaPrefsKey, valueToSave)

    fun setPathToDoom2RpgIpaFile(valueToSave: String) =
        setStringValue(pathToDoom2RpgIpaPrefsKey, valueToSave)

    fun setPathToDoomRpgZipFile(valueToSave: String) =
        setStringValue(pathToDoomRpgZipFilePrefsKey, valueToSave)

    fun getUseDarkThemeValue() = getBooleanValue(useDarkThemePrefsKey)

    fun setActiveEngineValue(valueToSave: EngineTypes) =
        setStringValue(activeEnginePrefsKey, valueToSave.toString())
}