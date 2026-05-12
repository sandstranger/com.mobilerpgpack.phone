package com.mobilerpgpack.phone.ui.screen

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.activity.ScreenControlsEditorActivity
import com.mobilerpgpack.phone.ui.getButtonsColors
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.ui.getOnPrimaryColor
import com.mobilerpgpack.phone.ui.items.CircularProgressDialog
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.ui.items.ShowYesNoDialog
import com.mobilerpgpack.phone.ui.items.SwitchItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.ui.screen.viewmodels.DownloadViewModel
import com.mobilerpgpack.phone.ui.screen.viewmodels.SettingsScreenViewModel
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.component.get
import org.koin.core.qualifier.named

class SettingsScreen : ComposeScreen(SCREEN_NAME) {

    @Composable
    override fun DrawScreenContent(innerPadding: PaddingValues, navController: NavHostController) {
        val activity = LocalActivity.current!!
        val preferencesStorage : PreferencesStorage = koinInject()
        val activeEngineString = preferencesStorage.activeEngineString.getComposableValue(EngineTypes.DefaultActiveEngine.name)
        val activeEngine = rememberSaveable(activeEngineString) {
            enumValueOf<EngineTypes>(activeEngineString)
        }
        val settingsScreenViewModel : SettingsScreenViewModel = koinViewModel ()
        settingsScreenViewModel.apply {
            val useFloatingStartGameButton = preferencesStorage.useFloatingStartGameButton.getComposableValue()

            LaunchedEffect(Unit) {
                initialize()
                drawFloatingActionButton.value = preferencesStorage.useFloatingStartGameButton.value
            }

            super.onFloatingActionButtonClickedDelegate = {
                onStartGameClicked(activeEngine, activity)
            }

            if (!useFloatingStartGameButton) {
                DrawTelevisionSettings(innerPadding,
                    activeEngine,
                    navController, this
                )
            } else {
                DrawAllSettings( innerPadding, activeEngine,navController, this)
            }

            DrawUnpackingFilesProgressDialog(this)
        }
    }

    override fun onMainActivityFinish() {
        super.onMainActivityFinish()
        val downloadViewModel : DownloadViewModel = get ()
        downloadViewModel.cancelDownload()
    }

    @Composable
    private fun DrawTelevisionSettings(
        innerPadding: PaddingValues,
        activeEngine: EngineTypes,
        navController: NavHostController,
        viewModel: SettingsScreenViewModel
    ) {
        val activity = LocalActivity.current!!
        val transparentColor = remember { Color.Transparent }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(transparentColor)
                .padding(innerPadding)
        ) {
            Button(
                onClick = { viewModel.onStartGameClicked(activeEngine, activity) },
                modifier = Modifier
                    .fillMaxWidth().padding(start = 4.dp, end = 4.dp), colors = getButtonsColors()
            ) {
                Text(stringResource(R.string.start_game),
                    textAlign = TextAlign.Center, fontSize = 22.sp, color = getOnPrimaryColor()
                )
            }

            DrawAllSettings( innerPadding, activeEngine,navController, viewModel)
        }
    }

    @Composable
    private fun DrawAllSettings(
        innerPadding: PaddingValues,
        activeEngine: EngineTypes,
        navController: NavHostController,
        viewModel: SettingsScreenViewModel) {
        val scrollState = rememberScrollState()
        val transparentColor = remember { Color.Transparent }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(transparentColor)
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            DrawCommonSettings(activeEngine, viewModel, navController)
            DrawGraphicsSettings()
            DrawUserInterfaceSettings()
            DrawCustomUserPathSettings()
        }
    }

    @Composable
    private fun DrawCommonSettings(activeEngine: EngineTypes,
        viewModel: SettingsScreenViewModel, navController: NavHostController) {
        val preferencesStorage : PreferencesStorage = koinInject()
        DrawTitleText(stringResource(R.string.common_settings))

        ListPreferenceItem(
            stringResource(R.string.active_engine),
            activeEngine) { newValue ->
            preferencesStorage.setActiveEngineValue(newValue)
        }

        DrawHorizontalDivider()

        SwitchPreferenceItem(stringResource(R.string.use_floating_start_game_button),
            preferencesStorage.useFloatingStartGameButton,
            preferencesStorage.useFloatingStartGameButtonPrefsKey.name){
            drawFloatingActionButton.value = it
        }

        DrawHorizontalDivider()
        DrawResetResourcesDialog(viewModel)
        DrawHorizontalDivider()

        val engineInfoUIController : IEngineUIController = koinInject(named(activeEngine.name))
        engineInfoUIController.DrawSettings(navController)

        val engineInfo : IEngineInfo = koinInject(named(activeEngine.name))

        if (engineInfo.supportRenderChanges){
            DrawHorizontalDivider()
            ListPreferenceItem(
                stringResource(R.string.uzdoom_rendering_api),
                preferencesStorage.glesRenderVersion) {
                preferencesStorage.setEnumValue(preferencesStorage.glesRenderVersionPrefsKey, it)
            }
        }

        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawResetResourcesDialog(viewModel: SettingsScreenViewModel){
        var resetResources by rememberSaveable(false) { mutableStateOf(false) }

        fun dismissResetResources () { resetResources = false}

        val resetAllResources = stringResource(R.string.reset_all_resources)

        PreferenceItem(resetAllResources ){ resetResources = true }

        if (resetResources){
            ShowYesNoDialog(resetAllResources,
                stringResource(R.string.reset_all_resources_confirm_message),
                positiveAction = {
                    dismissResetResources()
                    viewModel.onResetResourcesClicked()
                }, negativeAction = {
                    dismissResetResources()
                } )
        }
    }

    @Composable
    private fun DrawGraphicsSettings() {
        val preferencesStorage : PreferencesStorage = koinInject()

        DrawTitleText(stringResource(R.string.graphics_settings))
        val customScreenResolution = preferencesStorage.customScreenResolution
        val customAspectRatio = preferencesStorage.customAspectRatio

        SwitchPreferenceItem(
            stringResource(R.string.dark_theme),
            preferencesStorage.getUseDarkThemeValue(),
            preferencesStorage.useDarkThemePrefsKey.name)

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.display_in_safe_area),
            preferencesStorage.enableDisplayInSafeArea,
            preferencesStorage.displayInSafeAreaPrefsKey.name)

        DrawHorizontalDivider()

        EditTextPreferenceItem(stringResource(R.string.custom_aspect_ratio), customAspectRatio,
            preferencesStorage.customAspectRatioPrefsKey.name, stringResource(R.string.custom_aspect_ratio_hint))

        DrawHorizontalDivider()

        EditTextPreferenceItem(stringResource(R.string.custom_resolution),
            customScreenResolution,preferencesStorage.customScreenResolutionPrefsKey.name, stringResource(R.string.custom_resolution_hint))

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.use_mediump_precision),
            preferencesStorage.useMediumpShaderPrecision,
            preferencesStorage.useMediumpShaderPrecisionKey.name)

        DrawHorizontalDivider()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SwitchPreferenceItem(
                stringResource(R.string.enable_angle_support),
                preferencesStorage.enableAngleSupport,
                preferencesStorage.enableAngleSupportPrefsKey.name
            )

            DrawHorizontalDivider()
        }
    }

    @Composable
    private fun DrawUserInterfaceSettings() {
        DrawTitleText(stringResource(R.string.user_interface_settings))
        DrawEditScreenControlsSettings()
        DrawHorizontalDivider()

        DrawMouseCustomCursorSettings()
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawCustomUserPathSettings(){
        val context = LocalContext.current
        val preferencesStorage : PreferencesStorage = koinInject()
        val settingsViewModel : SettingsScreenViewModel = koinViewModel ()
        val sourceFolder = remember { context.getExternalFilesDir("")!!.absolutePath }
        val pathToUserFolder =preferencesStorage.pathToRootUserFolder.getComposableValue()
        var showCopyContentDialog by rememberSaveable { mutableStateOf(false) }

        DrawTitleText(stringResource(R.string.custom_path_settings))

        Row (modifier = Modifier.padding(start = 4.dp, end = 4.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = stringResource(R.string.copy_engines_content_info),
                modifier = Modifier.weight(1f, fill = false),
                color = getOnBackgroundColor())

            Button(
                onClick = { showCopyContentDialog = true },
                colors = getButtonsColors()
            ) {
                Text(stringResource(R.string.copy_engines_content),
                    textAlign = TextAlign.Center, color = getOnPrimaryColor()
                )
            }
        }

        DrawHorizontalDivider()

        Button( modifier = Modifier.padding(start = 4.dp),
            onClick = {
                preferencesStorage.setStringValue(preferencesStorage.pathToRootUserFolderPrefsKey, sourceFolder)
                settingsViewModel.restartApplication() },
            colors = getButtonsColors()
        ) {
            Text(stringResource(R.string.reset_user_path),
                textAlign = TextAlign.Center, color = getOnPrimaryColor()
            )
        }

        RequestPath(
            stringResource(R.string.path_to_user_folder),
            preferencesStorage.pathToRootUserFolder,
            preferencesStorage.pathToRootUserFolderPrefsKey,
            RequestPathMode.Directory,
        ){
            settingsViewModel.restartApplication()
        }

        DrawHorizontalDivider()

        if (showCopyContentDialog){
            ShowYesNoDialog(title = stringResource(R.string.copy_engines_content),
                stringResource(R.string.copy_engines_content_info_dialog)
                    .format(pathToUserFolder),
                negativeAction = {
                    showCopyContentDialog = false
                }, positiveAction = {
                    showCopyContentDialog = false
                    settingsViewModel.copyContentFromInternalStorage()
                } )
        }
    }

    @Composable
    private fun DrawEditScreenControlsSettings (){
        val preferencesStorage : PreferencesStorage = koinInject()
        val activity = LocalActivity.current!!
        val engineState = preferencesStorage.activeEngineString.getComposableValue(EngineTypes.DefaultActiveEngine.name)
        val activeEngine = rememberSaveable(engineState) { enumValueOf<EngineTypes>(engineState) }
        val engineInfo : IEngineInfo = koinInject(named(engineState))
        val controlsProvider : ControlsProvider = koinInject (named(activeEngine.name))

        if (controlsProvider.drawControlsTypesInMenu){
            ListPreferenceItem(stringResource(R.string.controls_type),
                controlsProvider.activeControlsType){
                controlsProvider.setControlsTypeValue(it)
            }
            DrawHorizontalDivider()
        }

        SwitchItem(stringResource(R.string.block_touch_camera_events),
            controlsProvider.blockTouchCameraEventsWhenOnScreenStickActive){
            controlsProvider.setBlockTouchCameraEventsValue(it)
        }

        DrawHorizontalDivider()

        PreferenceItem(stringResource(R.string.configure_screen_controls)) {
            ScreenControlsEditorActivity.editControls( activity,activeEngine,
                preferencesStorage.enableDisplayInSafeArea.value!!)
        }

        DrawHorizontalDivider()

        SwitchPreferenceItem(stringResource(R.string.always_show_keyboard_button),
            preferencesStorage.alwaysShowKeyboardButton,
            preferencesStorage.alwaysShowKeyboardButtonPrefsKey.name)

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.enable_absolute_touch_mouse_mode),
            preferencesStorage.enableAbsoluteTouchMouseMode,
            preferencesStorage.enableAbsoluteTouchMouseModePrefsKey.name
        )

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.enable_touchscreen_pressing_events),
            preferencesStorage.enableTouchScreenPressingEvents,
            preferencesStorage.enableTouchScreenPressingEventsPrefsKey.name
        )
        DrawHorizontalDivider()

        if (engineInfo.touchFullScreenModeCanBeUsed) {
            SwitchPreferenceItem(
                stringResource(R.string.fullscreen_touch_mode),
                preferencesStorage.alwaysUseFullScreenTouchMode,
                preferencesStorage.alwaysUseFullScreenTouchModePrefsKey.name
            )
            DrawHorizontalDivider()
        }

        SwitchPreferenceItem(
            stringResource(R.string.hide_custom_screen_controls),
            preferencesStorage.hideScreenControls,
            preferencesStorage.hideScreenControlsPrefsKey.name)

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.controls_autohing),
            preferencesStorage.autoHideScreenControls,
            preferencesStorage.enableControlsAutoHiding.name)

        DrawHorizontalDivider()

        EditTextPreferenceItem(
            stringResource(R.string.custom_keyboard_transparency),
            preferencesStorage.customOnScreenKeyboardTransparency){
            preferencesStorage.setFloatValue(preferencesStorage.customOnScreenKeyboardTransparencyPrefsKey,
                it.coerceIn(0f, 1.0f))
        }

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.enable_gyroscope),
            preferencesStorage.enableGyroscope,
            preferencesStorage.enableGyroscopePrefsKey.name)

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.gyroscope_invert_x_axis),
            preferencesStorage.invertGyroscopeXAxis,
            preferencesStorage.invertGyroscopeXAxisPrefsKey.name)

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.gyroscope_invert_y_axis),
            preferencesStorage.invertGyroscopeYAxis,
            preferencesStorage.invertGyroscopeYAxisPrefsKey.name)

        DrawHorizontalDivider()

        EditTextPreferenceItem(
            stringResource(R.string.gyroscope_x_sensitivity),
            preferencesStorage.gyroscopeXSensitivity){
            preferencesStorage.setFloatValue(preferencesStorage.gyroscopeXSensitivityPrefsKey,
                it.coerceAtLeast(0f))
        }

        DrawHorizontalDivider()

        EditTextPreferenceItem(
            stringResource(R.string.gyroscope_y_sensitivity),
            preferencesStorage.gyroscopeYSensitivity){
            preferencesStorage.setFloatValue(preferencesStorage.gyroscopeYSensitivityPrefsKey,
                it.coerceAtLeast(0f))
        }

        DrawHorizontalDivider()

        EditTextPreferenceItem(
            stringResource(R.string.gyroscope_dead_zone),
            preferencesStorage.gyroscopeDeadZone){
            preferencesStorage.setFloatValue(preferencesStorage.gyroscopeDeadZonePrefsKey,
                it.coerceAtLeast(0f))
        }

        DrawHorizontalDivider()

        EditTextItem(stringResource(R.string.zoom_sensitivity),
            preferencesStorage.zoomSensitivity){
            preferencesStorage.setFloatValue(preferencesStorage.zoomSensitivityPrefsKey,
                it.coerceAtLeast(0.3f))
        }
    }

    @Composable
    private fun DrawMouseCustomCursorSettings (){
        val preferencesStorage : PreferencesStorage = koinInject()

        SwitchPreferenceItem(
            stringResource(R.string.show_custom_mouse_cursor),
            preferencesStorage.showCustomMouseCursor,
            preferencesStorage.showCustomMouseCursorPrefsKey.name)

        DrawHorizontalDivider()

        EditTextPreferenceItem(stringResource(R.string.custom_mouse_cursor_horizontal_offset),
            preferencesStorage.offsetXMouse, preferencesStorage.OFFSET_X_MOUSE.name)

        DrawHorizontalDivider()

        EditTextPreferenceItem(stringResource(R.string.custom_mouse_cursor_vertical_offset),
            preferencesStorage.offsetYMouse, preferencesStorage.OFFSET_Y_MOUSE.name)
    }

    @Composable
    private fun DrawUnpackingFilesProgressDialog(settingsScreenViewModel : SettingsScreenViewModel){
        val allAssetsCopied = settingsScreenViewModel.allAssetsCopied
            .getComposableValue(true)

        if (!allAssetsCopied){
            CircularProgressDialog(stringResource(R.string.files_unpacking_title),
                stringResource(R.string.files_unpacking_text))
        }
    }

    companion object{
        const val SCREEN_NAME = "settings"
    }
}

