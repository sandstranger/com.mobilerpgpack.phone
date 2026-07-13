package com.mobilerpgpack.phone.utils

import android.content.Context
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.GlesRenderVersions
import com.mobilerpgpack.phone.engine.engineinfo.widelands.WidelandsEngineInfo
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLScreenController
import com.mobilerpgpack.phone.utils.sharesprefs.Key
import com.mobilerpgpack.phone.utils.sharesprefs.SharedPrefsRepository
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.enumPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.floatPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.intPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.longPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

open class PreferencesStorage : SharedPrefsRepository(), KoinComponent {

    private val context: Context by inject ()

    val displayInSafeAreaPrefsKey = booleanPreferencesKey("display_in_safe_area")
    val showCustomMouseCursorPrefsKey = booleanPreferencesKey("show_custom_mouse_cursor")
    val activeEnginePrefsKey = stringPreferencesKey("current_engine")
    val pathToWolfensteinRpgIpaPrefsKey = stringPreferencesKey("wolfenstein_rpg_ipa_file")
    val pathToDoom2RpgIpaPrefsKey = stringPreferencesKey("doom2_rpg_ipa_file")
    val pathToDoomRpgZipFilePrefsKey = stringPreferencesKey("doom_rpg_zip_file")
    val hideScreenControlsPrefsKey = booleanPreferencesKey("hide_screen_controls")
    val customScreenResolutionPrefsKey = stringPreferencesKey("custom_screen_resolution")
    val customAspectRatioPrefsKey = stringPreferencesKey("custom_aspect_ratio")
    val editCustomScreenControlsInGamePrefsKey =
        booleanPreferencesKey("edit_screen_controls_in_game")
    val useDarkThemePrefsKey = booleanPreferencesKey("use_dark_theme")
    val OFFSET_X_MOUSE = floatPreferencesKey("offset_x_mouse")
    val OFFSET_Y_MOUSE = floatPreferencesKey("offset_y_mouse")
    val enableControlsAutoHiding = booleanPreferencesKey("constols_autohiding")
    val useSDLTTFForFontsRenderingPrefsKey = booleanPreferencesKey("sdl_ttf_render")
    val gamesMachineTranslationsPrefsKey = booleanPreferencesKey("enable_games_translation")
    val allowDownloadingModelsOverMobilePrefsKey =
        booleanPreferencesKey("allow_downloading_over_mobile")
    val translationModelTypePrefsKey = stringPreferencesKey("doom_rpg_series_translation_model_type")
    val pathToDoom64FolderWithMainWads = stringPreferencesKey("path_to_doom64_folder_wads")
    val pathToDoom64FolderWithMods = stringPreferencesKey("path_to_doom64_folder_mods")
    val enableDoom64ModsPrefsKey = booleanPreferencesKey("enable_doom64_mods")
    val savedDoomRpgScreenWidthPrefsKey = intPreferencesKey("doomrpg_screen_width")
    val savedDoomRpgScreenHeightPrefsKey = intPreferencesKey("doomrpg_screen_height")
    val doom64CommandLineArgsStringPrefsKey = stringPreferencesKey("doom64_command_line_args")
    val customOnScreenKeyboardTransparencyPrefsKey =
        floatPreferencesKey("custom_keyboard_transparency")
    val alwaysUseFullScreenTouchModePrefsKey =
        booleanPreferencesKey("always_use_fullscreen_touch_mode")
    val useFloatingStartGameButtonPrefsKey = booleanPreferencesKey("use_floating_start_game_button")
    val alwaysShowKeyboardButtonPrefsKey = booleanPreferencesKey("always_show_keyboard_button")
    val enableTouchScreenPressingEventsPrefsKey =
        booleanPreferencesKey("use_touchscreen_in_games_menu")
    val enableAbsoluteTouchMouseModePrefsKey =
        booleanPreferencesKey("use_absolute_touch_mouse_mode")
    val pathToRootUserFolderPrefsKey = stringPreferencesKey("path_to_user_root_folder")
    val enableGyroscopePrefsKey = booleanPreferencesKey("enable_gyroscope")
    val gyroscopeXSensitivityPrefsKey = floatPreferencesKey("gyroscope_x_sensitivity")
    val gyroscopeYSensitivityPrefsKey = floatPreferencesKey("gyroscope_y_sensitivity")
    val gyroscopeDeadZonePrefsKey = floatPreferencesKey("gyroscope_dead_zone")
    val invertGyroscopeXAxisPrefsKey = booleanPreferencesKey("invert_gyroscope_x_axis")
    val invertGyroscopeYAxisPrefsKey = booleanPreferencesKey("invert_gyroscope_y_axis")
    val widelandsCommandLineArgsPrefsKey = stringPreferencesKey("widelands_command_line_args")
    val widelandsScreenScalePrefeKey = floatPreferencesKey("widelands_screen_scale")
    val zoomSensitivityPrefsKey = floatPreferencesKey("zoom_sensitivity")
    val enableAngleSupportPrefsKey = booleanPreferencesKey("enable_angle_support")
    val glesRenderVersionPrefsKey = enumPreferencesKey<GlesRenderVersions>("target_g4les_render_version")
    val allowWidelandsDownloadsOverMobileNetworkPrefsKey = booleanPreferencesKey("allow_widelands_downloads_over_mobile")
    val widelandsFilesContentVersionPrefsKey = intPreferencesKey("widelands_files_content_version")
    val widelandsFilesContentDownloadedPrefsKey = booleanPreferencesKey("widelands_files_content_downloaded")
    val enableFramePacingAutoPipelineModePrefsKey = booleanPreferencesKey("enable_framepacing_auto_pipeline_mode")
    val enableFramePacingAutoSwapPrefsKey = booleanPreferencesKey("enable_framepacing_auto_swap")
    val framePacingTargetFPSPrefsKey = intPreferencesKey("framepacing_target_fps")
    val enableFramePacingPrefsKey = booleanPreferencesKey("enable_framepacings")
    val bufferStaffingFixWaitPrefsKey = intPreferencesKey("buffer_staffing_fix_wait")
    val enableBlockingWaitPrefsKey = booleanPreferencesKey("enable_blocking_wait")
    val framePacingFenceTimeoutPrefsKey = longPreferencesKey("framepacing_fence_timeout")
    val assetsCurrentVersionPrefsKey = intPreferencesKey("assets_current_version")
    val allAssetsCopiedPrefsKey = booleanPreferencesKey("all_assets_copied")
    val doom64AnisotropyTexturesValuePrefsKey = intPreferencesKey("doom64_anisotropy_textures_value")

    val doom64AnisotropyTexturesValue = getIntValue(doom64AnisotropyTexturesValuePrefsKey,2)
    val assetsCurrentVersion = getIntValue(assetsCurrentVersionPrefsKey, 0)
    val allAssetsCopied = getBooleanValue(allAssetsCopiedPrefsKey, false)
    val framePacingFenceTimeout = getLongValue(framePacingFenceTimeoutPrefsKey, 50L)
    val enableFramePacing = getBooleanValue(enableFramePacingPrefsKey, true)
    val bufferStaffingFixWait = getIntValue(bufferStaffingFixWaitPrefsKey, 0)
    val enableBlockingWait = getBooleanValue(enableBlockingWaitPrefsKey, true)
    val enableFramePacingAutoPipelineMode = getBooleanValue(enableFramePacingAutoPipelineModePrefsKey, true)
    val enableFramePacingAutoSwap = getBooleanValue(enableFramePacingAutoSwapPrefsKey, false)
    val framePacingTargetFPS = getIntValue(framePacingTargetFPSPrefsKey, 60)

    val widelandsFilesContentDownloaded = getBooleanValue(widelandsFilesContentDownloadedPrefsKey, false)
    val widelandsFilesContentVersion = getIntValue(widelandsFilesContentVersionPrefsKey,
        WidelandsEngineInfo.WIDELANDS_FILES_CONTENT_CURRENT_VERSION)
    val allowWidelandsDownloadsOverMobile = getBooleanValue(allowWidelandsDownloadsOverMobileNetworkPrefsKey, false)
    val glesRenderVersion = getEnumValue(glesRenderVersionPrefsKey,
        GlesRenderVersions::class.java, GlesRenderVersions.DefaultValue)

    val widelandsScreenScale = getFloatValue(widelandsScreenScalePrefeKey, 2.0f)

    val widelandsCommandLineArgs = getStringValue(widelandsCommandLineArgsPrefsKey)

    val zoomSensitivity = getFloatValue(zoomSensitivityPrefsKey,
        SDLScreenController.DEFAULT_ZOOM_SENSITIVITY)

    val pathToRootUserFolder = getStringValue(
        pathToRootUserFolderPrefsKey,
        context.filesDir.absolutePath)

    val enableGyroscope = getBooleanValue(enableGyroscopePrefsKey, false)

    val invertGyroscopeXAxis = getBooleanValue(invertGyroscopeXAxisPrefsKey, false)

    val invertGyroscopeYAxis = getBooleanValue(invertGyroscopeYAxisPrefsKey, false)

    val gyroscopeDeadZone = getFloatValue(gyroscopeDeadZonePrefsKey, GyroInput.DEFAULT_DEAD_ZONE)

    val gyroscopeXSensitivity =
        getFloatValue(gyroscopeXSensitivityPrefsKey, GyroInput.DEFAULT_SENS_X)

    val gyroscopeYSensitivity =
        getFloatValue(gyroscopeYSensitivityPrefsKey, GyroInput.DEFAULT_SENS_Y)

    val enableAbsoluteTouchMouseMode = getBooleanValue(enableAbsoluteTouchMouseModePrefsKey, true)

    val enableTouchScreenPressingEvents =
        getBooleanValue(enableTouchScreenPressingEventsPrefsKey, true)

    val enableAngleSupport = getBooleanValue(enableAngleSupportPrefsKey, false)

    val useFloatingStartGameButton =
        getBooleanValue(useFloatingStartGameButtonPrefsKey, !context.isTelevision)

    val alwaysShowKeyboardButton = getBooleanValue(alwaysShowKeyboardButtonPrefsKey, true)

    val alwaysUseFullScreenTouchMode = getBooleanValue(alwaysUseFullScreenTouchModePrefsKey, true)

    val customOnScreenKeyboardTransparency =
        getFloatValue(customOnScreenKeyboardTransparencyPrefsKey, 1.0f)

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

    val hideScreenControls = getBooleanValue(hideScreenControlsPrefsKey, defaultValue = false)

    val enableGameMachineTextTranslation =
        getBooleanValue(gamesMachineTranslationsPrefsKey, defaultValue = false)

    val customScreenResolution = getStringValue(customScreenResolutionPrefsKey)

    val customAspectRatio = getStringValue(customAspectRatioPrefsKey)

    val pathToWolfensteinRpgIpaFile = getStringValue(pathToWolfensteinRpgIpaPrefsKey)

    val pathToDoom64ModsFolder = getStringValue(pathToDoom64FolderWithMods)

    val pathToDoom64MainWadsFolder = getStringValue(pathToDoom64FolderWithMainWads)

    val pathToDoom2RpgIpaFile = getStringValue(pathToDoom2RpgIpaPrefsKey)

    val pathToDoomRpgZipFile = getStringValue(pathToDoomRpgZipFilePrefsKey)

    val autoHideScreenControls = getBooleanValue(enableControlsAutoHiding, defaultValue = false)

    val showCustomMouseCursor = getBooleanValue(showCustomMouseCursorPrefsKey)

    val activeEngineString = getStringValue(
        activeEnginePrefsKey,
        EngineTypes.DefaultActiveEngine.toString()
    )

    val offsetXMouse = getFloatValue(OFFSET_X_MOUSE, 0f)

    val offsetYMouse = getFloatValue(OFFSET_Y_MOUSE, 0f)

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

    fun getClampButtonsValue(key: Key<Boolean>, defaultValue: Boolean = true) =
        getBooleanValue(key, defaultValue)

    fun getUseDarkThemeValue() = getBooleanValue(useDarkThemePrefsKey)

    fun setActiveEngineValue(valueToSave: EngineTypes) =
        setStringValue(activeEnginePrefsKey, valueToSave.toString())
}