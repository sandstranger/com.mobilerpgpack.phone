package com.mobilerpgpack.phone.ui.screen

import CustomTopBar
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.startEngine
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.activity.ScreenControlsEditorActivity
import com.mobilerpgpack.phone.ui.items.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.PreferenceItem
import com.mobilerpgpack.phone.ui.items.RequestPath
import com.mobilerpgpack.phone.ui.items.SetupNavigationBar
import com.mobilerpgpack.phone.ui.items.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.items.TranslatedText
import com.mobilerpgpack.phone.ui.screen.utils.buildTranslationsDescription
import com.mobilerpgpack.phone.ui.screen.viewmodels.DownloadViewModel
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.isTelevision
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val context = LocalContext.current;
    val scope = rememberCoroutineScope()
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val useDarkTheme by PreferencesStorage.getUseDarkThemeValue(context,isSystemInDarkTheme)
        .collectAsState(initial = isSystemInDarkTheme)
    val backgroundColor = if (useDarkTheme) Color.Black else Color.White
    val topBarColor = if (useDarkTheme) Color.Gray else Color.Blue
    val activeEngineString by PreferencesStorage.getActiveEngineValueAsFlowString(context)
        .collectAsState(initial = EngineTypes.DefaultActiveEngine.toString())

    val activeEngine = rememberSaveable (activeEngineString) {
        enumValueOf<EngineTypes>(activeEngineString)
    }

    Theme (darkTheme = useDarkTheme ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(topBarColor)
                .systemBarsPadding()
        ) {
            CustomTopBar(title = context.getString(R.string.app_name), useDarkTheme)

            if (context.isTelevision) {
                DrawTelevisionSettings(context, scope,backgroundColor, activeEngine)
            } else {
                DrawPhoneSettings(context, scope,backgroundColor, activeEngine)
            }
        }
    }

    SetupNavigationBar(useDarkTheme)
}

@Composable
private fun DrawTelevisionSettings(context: Context, scope: CoroutineScope,
                                   backgroundColor : Color, activeEngine : EngineTypes ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        Button(
            onClick = { scope.launch { startEngine(context, activeEngine) } },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TranslatedText(context.getString(R.string.start_game), textAlign = TextAlign.Center, fontSize = 22.sp)
        }

        DrawAllSettings(context, scope,activeEngine)
    }
}

@Composable
private fun DrawPhoneSettings(context: Context, scope: CoroutineScope,
                              backgroundColor: Color, activeEngine: EngineTypes) {
    Scaffold(modifier = Modifier.background(backgroundColor),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { scope.launch { startEngine(context,activeEngine) } }
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = context.getString(R.string.start_game)
                )
            }
        }
    ) { innerPadding ->
        DrawAllSettings(context, innerPadding, scope, activeEngine)
    }
}

@Composable
private fun DrawAllSettings(context: Context, scope: CoroutineScope,activeEngine : EngineTypes) {
    DrawAllSettings(context, PaddingValues(), scope,activeEngine)
}

@Composable
private fun DrawAllSettings(context: Context, innerPadding: PaddingValues,
                            scope: CoroutineScope,activeEngine : EngineTypes) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState),
    ) {
        DrawCommonSettings(context, scope, activeEngine)
        DrawTranslationModelSettings(context,scope)
        DrawGraphicsSettings(context,scope)
        DrawUserInterfaceSettings(context,scope)
    }
}

@Composable
private fun DrawCommonSettings(context: Context, scope: CoroutineScope,activeEngine : EngineTypes) {
    TranslatedText(context.getString(R.string.common_settings), style = MaterialTheme.typography.titleLarge)

    ListPreferenceItem(
        context.getString(R.string.active_engine),
        activeEngine,
        EngineTypes.entries
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setActiveEngineValue(context,
                enumValueOf<EngineTypes>(newValue))
        }
    }

    HorizontalDivider()

    when (activeEngine) {
        EngineTypes.WolfensteinRpg -> DrawWolfensteinRpgSettings(context,scope)
        EngineTypes.DoomRpg -> DrawDoomRpgSettings(context,scope)
        EngineTypes.Doom2Rpg -> DrawDoom2RpgSettings(context,scope)
        EngineTypes.Doom64ExPlus -> DrawDoom64Settings(context,scope)
    }

    HorizontalDivider()
}

@Composable
private fun DrawTranslationModelSettings(context: Context, scope: CoroutineScope){
    TranslatedText(context.getString(R.string.translation_settings), style = MaterialTheme.typography.titleLarge)

    val activeTranslationTypeString by PreferencesStorage.getTranslationModelTypeValue(context)
        .collectAsState(initial = TranslationType.DefaultTranslationType.toString())

    val translationModelEntries = buildTranslationsDescription(context)
    val initialModelValue = translationModelEntries.first { it.startsWith(activeTranslationTypeString) }

    ListPreferenceItem(
        context.getString(R.string.translation_model_title),
        initialModelValue,
        translationModelEntries
    ) { newValue ->
        scope.launch {
            val translationModelType = TranslationType.getTranslationType(newValue)
            TranslationManager.activeTranslationType = translationModelType
            PreferencesStorage.setTranslationModelTypeValue(context, translationModelType)
        }
    }

    HorizontalDivider()

    SwitchPreferenceItem(
        context.getString(R.string.allow_downloading_over_mobile_network),
        checkedFlow = PreferencesStorage.getAllowDownloadingModelsOverMobileValue(context),
    ) { newValue ->
        TranslationManager.allowDownloadingOveMobile = newValue
        scope.launch {
            PreferencesStorage.setAllowDownloadingModelsOverMobileValue(context, newValue)
        }
    }

    HorizontalDivider()

    DrawPreloadModelsSetting(context)

    HorizontalDivider()
}

@Composable
private fun DrawPreloadModelsSetting(context: Context,vm: DownloadViewModel = viewModel()){

    val activeTranslationTypeString by PreferencesStorage.getTranslationModelTypeValue(context)
        .collectAsState(initial = "")

    LaunchedEffect(activeTranslationTypeString) {
        if (activeTranslationTypeString!="") {
            vm.onTranslationTypeChanged(activeTranslationTypeString)
        }
    }

    PreferenceItem(context.getString(R.string.load_translation_model)) {
        vm.startDownload()
    }

    LoadingModelDialogWithCancel(
        show = vm.isLoading,
        progress = vm.downloadProgress,
        onClose = {
            vm.isLoading = false
        },
        onCancel = {
            vm.cancelDownload()
        }
    )
}

@Composable
private fun DrawGraphicsSettings(context: Context, scope: CoroutineScope) {

    TranslatedText(context.getString(R.string.graphics_settings), style = MaterialTheme.typography.titleLarge)

    val customScreenResolution by PreferencesStorage.getCustomScreenResolutionValue(context)
        .collectAsState(initial = "")

    val customAspectRatio by PreferencesStorage.getCustomAspectRatioValue(context)
        .collectAsState(initial = "")

    SwitchPreferenceItem(
        context.getString(R.string.dark_theme),
        checkedFlow = PreferencesStorage.getUseDarkThemeValue(context, isSystemInDarkTheme()),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setUseDarkThemeValue(context, newValue)
        }
    }

    HorizontalDivider()

    SwitchPreferenceItem(
        context.getString(R.string.display_in_safe_area),
        checkedFlow = PreferencesStorage.getDisplayInSafeAreaValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setDisplayInSafeAreaValue(context, newValue)
        }
    }

    HorizontalDivider()

    EditTextPreferenceItem(
        context.getString(R.string.custom_aspect_ratio),
        customAspectRatio, context.getString(R.string.custom_aspect_ratio_hint)
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setCustomAspectRatio(context, newValue)
        }
    }

    HorizontalDivider()

    EditTextPreferenceItem(
        context.getString(R.string.custom_resolution),
        customScreenResolution!!, context.getString(R.string.custom_resolution_hint)
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setCustomScreenResolution(context, newValue)
        }
    }

    HorizontalDivider()
}

@Composable
private fun DrawUserInterfaceSettings(context: Context, scope: CoroutineScope){
    val engineState by PreferencesStorage.getActiveEngineValueAsFlowString(context).collectAsState(initial =
        EngineTypes.DefaultActiveEngine.toString())
    val activeEngine = rememberSaveable (engineState) { enumValueOf<EngineTypes>(engineState!!) }
    var drawKeysEditor by rememberSaveable { mutableStateOf(false) }
    val isModelDownloaded by TranslationManager.isTranslationSupportedAsFlow().collectAsState(initial = true)
    val showLauncherTranslationOption = TranslationManager.targetLocale != TranslationManager.sourceLocale
            && TranslationManager.targetLocale != TranslationManager.RUSSIAN_LOCALE

    LaunchedEffect(isModelDownloaded, showLauncherTranslationOption) {
        if (!showLauncherTranslationOption){
            PreferencesStorage.setEnableLauncherTextTranslationValue(context, false)
        }

        if (!isModelDownloaded) {
            PreferencesStorage.setEnableLauncherTextTranslationValue(context, false)
            PreferencesStorage.setEnableGameMachineTextTranslationValue(context, false)
        }
    }

    TranslatedText(context.getString(R.string.user_interface_settings), style = MaterialTheme.typography.titleLarge)

    SwitchPreferenceItem(
        context.getString(R.string.use_sdl_ttf_for_rendering),
        checkedFlow = PreferencesStorage.getUseSDLTTFForFontsRenderingValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setUseSDLTTFForFontsRenderingValue(context, newValue)
        }
    }

    if (showLauncherTranslationOption) {
        HorizontalDivider()
        SwitchPreferenceItem(
            context.getString(R.string.enable_launcher_text_translation),
            checkedFlow = PreferencesStorage.getEnableLauncherTextTranslationValue(context),
            enabled = isModelDownloaded
        ) { newValue ->
            scope.launch {
                PreferencesStorage.setEnableLauncherTextTranslationValue(context, newValue)
            }
        }
    }

    HorizontalDivider()

    SwitchPreferenceItem(
        context.getString(R.string.use_ai_for_text_translations),
        checkedFlow = PreferencesStorage.getEnableGameMachineTextTranslationValue(context),
        enabled = isModelDownloaded
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setEnableGameMachineTextTranslationValue(context, newValue)
        }
    }

    HorizontalDivider()

    PreferenceItem(context.getString(R.string.keys_editor)) {
        drawKeysEditor = true
    }

    HorizontalDivider()

    PreferenceItem(context.getString(R.string.configure_screen_controls)) {
        ScreenControlsEditorActivity.editControls(context, activeEngine)
    }

    HorizontalDivider()

    SwitchPreferenceItem(
        context.getString(R.string.show_custom_mouse_cursor),
        checkedFlow = PreferencesStorage.getShowCustomMouseCursorValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setShowCustomMouseCursorValue(context, newValue)
        }
    }

    HorizontalDivider()

    SwitchPreferenceItem(
        context.getString(R.string.allow_to_edit_controls_in_game),
        checkedFlow = PreferencesStorage.getEditCustomScreenControlsInGameValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setEditCustomScreenControlsInGameValue(context, newValue)
        }
    }

    HorizontalDivider()

    SwitchPreferenceItem(
        context.getString(R.string.hide_custom_screen_controls),
        checkedFlow = PreferencesStorage.getHideScreenControlsValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setHideControlsValue(context, newValue)
        }
    }

    HorizontalDivider()

    SwitchPreferenceItem(
        context.getString(R.string.controls_autohing),
        checkedFlow = PreferencesStorage.getControlsAutoHidingValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setControlsAutoHidingValue(context, newValue)
        }
    }

    HorizontalDivider()

    if (drawKeysEditor){
        val buttonsToDraw = enginesInfo[activeEngine]!!.buttonsToDraw
        KeysEditor(buttonsToDraw) {
            drawKeysEditor = false
        }
    }
}

@Composable
private fun DrawWolfensteinRpgSettings(context: Context, scope: CoroutineScope) {
    val previousPathToWolfensteinRpgIPa by PreferencesStorage.getPathToWolfensteinRpgIpaFileValue(context)
        .collectAsState(initial = "")

    RequestPath(context.getString(R.string.wolfenstein_rpg_ipa_file),onPathSelected = { selectedPath ->
        scope.launch { PreferencesStorage.setPathToWolfensteinRpgIpaFile(context, selectedPath) }},
        previousPathToWolfensteinRpgIPa )
}

@Composable
private fun DrawDoomRpgSettings(context: Context, scope: CoroutineScope) {
    val savedPathToDoomRpgZip by PreferencesStorage.getPathToDoomRpgZipFileValue(context)
        .collectAsState(initial = "")

    RequestPath(context.getString(R.string.doom_rpg_zip_file),
        onPathSelected = { selectedPath ->
            scope.launch { PreferencesStorage.setPathToDoomRpgZipFile(context, selectedPath) } },
        savedPathToDoomRpgZip,
    )
}

@Composable
private fun DrawDoom2RpgSettings(context: Context, scope: CoroutineScope) {
    val previousPathToDoom2RpgIpa by PreferencesStorage.getPathToDoom2RpgIpaFile(context)
        .collectAsState(initial = "")

    RequestPath( context.getString(R.string.doom2_rpg_ipa_file),
        onPathSelected = { selectedPath ->
            scope.launch { PreferencesStorage.setPathToDoom2RpgIpaFile(context, selectedPath) } },
        previousPathToDoom2RpgIpa )
}

@Composable
private fun DrawDoom64Settings(context: Context, scope: CoroutineScope) {
    val previousPathToDoom64WadsFolder by PreferencesStorage.getPathToDoom64MainWadsFolder(context)
        .collectAsState(initial = "")

    RequestPath( context.getString(R.string.path_to_doom64_folder),
        onPathSelected = { selectedPath ->
            scope.launch { PreferencesStorage.setPathToDoom64MainWadsFolder(context, selectedPath) } },
        previousPathToDoom64WadsFolder, requestOnlyDirectory = true )

    HorizontalDivider()

    val previousPathToDoom64ModsFolder by PreferencesStorage.getPathToDoom64ModsFolder(context)
        .collectAsState(initial = "")

    RequestPath( context.getString(R.string.path_to_doom64_mods_folder),
        onPathSelected = { selectedPath ->
            scope.launch { PreferencesStorage.setPathToDoom64ModsFolder(context, selectedPath) } },
        previousPathToDoom64ModsFolder, requestOnlyDirectory = true )
}

