package com.mobilerpgpack.phone.ui.screen

import CustomTopBar
import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.defaultPathToLogcatFile
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.logcatFileName
import com.mobilerpgpack.phone.engine.startEngine
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.activity.ScreenControlsEditorActivity
import com.mobilerpgpack.phone.ui.items.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.PreferenceItem
import com.mobilerpgpack.phone.ui.items.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.items.TranslatedText
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.isTelevision
import com.mobilerpgpack.phone.utils.requestDirectory
import com.mobilerpgpack.phone.utils.requestResourceFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun SettingsScreen() {
    val context = LocalContext.current;
    val scope = rememberCoroutineScope()
    val useDarkTheme by PreferencesStorage.getUseDarkThemeValue(context).collectAsState(initial = false)
    val backgroundColor = if (useDarkTheme) Color.Black else Color.White
    val topBarColor = if (useDarkTheme) Color.Gray else Color.Blue

    Theme (darkTheme = useDarkTheme ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(topBarColor)
                .systemBarsPadding()
        ) {
            CustomTopBar(title = context.getString(R.string.app_name), useDarkTheme)

            if (context.isTelevision) {
                DrawTelevisionSettings(context, scope,backgroundColor)
            } else {
                DrawPhoneSettings(context, scope,backgroundColor)
            }
        }
    }
}

@Composable
private fun DrawTelevisionSettings(context: Context, scope: CoroutineScope, backgroundColor : Color ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        Button(
            onClick = { scope.launch { startEngine(context) } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            TranslatedText(context.getString(R.string.start_game), textAlign = TextAlign.Center, fontSize = 22.sp)
        }

        DrawAllSettings(context, scope)
    }
}

@Composable
private fun DrawPhoneSettings(context: Context, scope: CoroutineScope, backgroundColor: Color) {
    Scaffold(modifier = Modifier.background(backgroundColor),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { scope.launch { startEngine(context) } }
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = context.getString(R.string.start_game)
                )
            }
        }
    ) { innerPadding ->
        DrawAllSettings(context, innerPadding, scope)
    }
}

@Composable
private fun DrawAllSettings(context: Context, scope: CoroutineScope) {
    DrawAllSettings(context, PaddingValues(), scope)
}

@Composable
private fun DrawAllSettings(context: Context, innerPadding: PaddingValues, scope: CoroutineScope) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(5.dp)
            .verticalScroll(scrollState),
    ) {
        DrawCommonSettings(context, scope)
        DrawGraphicsSettings(context,scope)
        DrawUserInterfaceSettings(context,scope)
    }
}

@Composable
private fun DrawCommonSettings(context: Context, scope: CoroutineScope) {
    val activeEngineString by PreferencesStorage.getActiveEngineValueAsFlowString(context)
        .collectAsState(initial = EngineTypes.DefaultActiveEngine.toString())
    val activeEngine = rememberSaveable (activeEngineString) { enumValueOf<EngineTypes>(activeEngineString!!) }

    TranslatedText(context.getString(R.string.common_settings), style = MaterialTheme.typography.titleLarge)

    ListPreferenceItem(
        context.getString(R.string.active_engine),
        activeEngine,
        EngineTypes.entries
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setActiveEngineValue(context, enumValueOf<EngineTypes>(newValue))
        }
    }

    HorizontalDivider()

    when (activeEngine) {
        EngineTypes.WolfensteinRpg -> DrawWolfensteinRpgSettings(context,scope)
        EngineTypes.DoomRpg -> DrawDoomRpgSettings(context,scope)
        EngineTypes.Doom2Rpg -> DrawDoom2RpgSettings(context,scope)
    }

    val pathToLogFile by PreferencesStorage.getPathToLogFileValue(context)
        .collectAsState(initial = defaultPathToLogcatFile)
    val requestPathHelper = RequestPathHelper(context, scope, onPathSelected = { selectedPath ->
        scope.launch { PreferencesStorage.setPathToLogFile(context, selectedPath) }
    }, requestDirectory = true)

    requestPathHelper.DrawRequestPathItem(
        context.getString(R.string.path_to_log), pathToLogFile!!,
        logcatFileName
    )

    HorizontalDivider()

    val activeTranslationTypeString by PreferencesStorage.getTranslationModelTypeValue(context)
        .collectAsState(initial = TranslationType.DefaultTranslationType.toString())
    val activeTranslation = rememberSaveable (enumValueOf<TranslationType>(activeTranslationTypeString)) {
        enumValueOf<TranslationType>(activeTranslationTypeString) }

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

    HorizontalDivider()

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
        checkedFlow = PreferencesStorage.getUseDarkThemeValue(context),
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
    val pathToIpaFileFlow by PreferencesStorage.getPathToWolfensteinRpgIpaFileValue(context)
        .collectAsState(initial = "")
    val requestPathHelper = RequestPathHelper(context, scope, onPathSelected = { selectedPath ->
        scope.launch { PreferencesStorage.setPathToWolfensteinRpgIpaFile(context, selectedPath) }
    })

    requestPathHelper.DrawRequestPathItem(
        context.getString(R.string.wolfenstein_rpg_ipa_file),
        pathToIpaFileFlow!!
    )

    HorizontalDivider()
}

@Composable
private fun DrawDoomRpgSettings(context: Context, scope: CoroutineScope) {
    val pathToZipFileState by PreferencesStorage.getPathToDoomRpgZipFileValue(context)
        .collectAsState(initial = "")
    val requestPathHelper = RequestPathHelper(context, scope, onPathSelected = { selectedPath ->
        scope.launch { PreferencesStorage.setPathToDoomRpgZipFile(context, selectedPath) }
    })

    requestPathHelper.DrawRequestPathItem(
        context.getString(R.string.doom_rpg_zip_file),
        pathToZipFileState!!
    )

    HorizontalDivider()
}

@Composable
private fun DrawDoom2RpgSettings(context: Context, scope: CoroutineScope) {
    val pathToIpaFileState by PreferencesStorage.getPathToDoom2RpgIpaFile(context)
        .collectAsState(initial = "")
    val requestPathHelper = RequestPathHelper(context, scope, onPathSelected = { selectedPath ->
        scope.launch { PreferencesStorage.setPathToDoom2RpgIpaFile(context, selectedPath) }
    })

    requestPathHelper.DrawRequestPathItem(
        context.getString(R.string.doom2_rpg_ipa_file),
        pathToIpaFileState!!
    )

    HorizontalDivider()
}

private fun buildTranslationsDescription (context: Context) : Collection<String>{
    val result : MutableList<String> = mutableListOf()

    for (type in TranslationType.entries) {
        if (BuildConfig.FDROID_BUILD && type == TranslationType.MLKit){
            continue
        }

        when (type) {
            TranslationType.MLKit ->
                result.add("${TranslationType.MLKit} ${context.getString(R.string.mlkit_description)}")
            TranslationType.OpusMt ->
                result.add("${TranslationType.OpusMt} ${context.getString(R.string.opus_mt_description)}")
            TranslationType.M2M100 ->
                result.add("${TranslationType.M2M100} ${context.getString(R.string.m2m_mt_description)}")
            TranslationType.Small100 ->
                result.add("${TranslationType.Small100} ${context.getString(R.string.small100_mt_description)}")
            TranslationType.GoogleTranslate ->
                result.add("${TranslationType.GoogleTranslate} ${context.getString(R.string.google_translate_description)}")
            TranslationType.BingTranslate ->
                result.add("${TranslationType.BingTranslate} ${context.getString(R.string.bing_translate_description)}")
            TranslationType.NLLB200 ->
                result.add("${TranslationType.NLLB200} ${context.getString(R.string.nllb200_translate_description)}")
        }
    }

    return result
}

private class RequestPathHelper(
    private val context: Context, val scope: CoroutineScope,
    private val onPathSelected: (String) -> Unit,
    private val requestDirectory: Boolean = false
) {

    @Composable
    fun DrawRequestPathItem(itemName: String, savedPath: String, selectedPathPostFix: String = "") {
        var currentPath by rememberSaveable(savedPath) { mutableStateOf(savedPath) }

        fun onPathSelected(selectedPath: String) {
            if (selectedPath.isNotEmpty()) {
                currentPath =
                    selectedPath + if (selectedPathPostFix.isNotEmpty()) File.separator + selectedPathPostFix else ""
                saveSelectedPath(currentPath)
            }
        }

        PreferenceItem(
            itemName, currentPath,
            onClick = {
                scope.launch {
                    if (requestDirectory) {
                        context.requestDirectory(
                            onDirectorySelected =
                                { selectedPath -> onPathSelected(selectedPath) })
                    } else {
                        context.requestResourceFile(
                            onFileSelected =
                                { selectedPath -> onPathSelected(selectedPath) })
                    }
                }
            })
    }

    private fun saveSelectedPath(pathToFile: String) {
        scope.launch {
            onPathSelected(pathToFile)
        }
    }
}
