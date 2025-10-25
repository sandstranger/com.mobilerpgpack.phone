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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.Engine
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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsScreen : KoinComponent {

    private val context : Context by inject()

    private val preferencesStorage : PreferencesStorage by inject()

    private val translationManager : TranslationManager by inject ()

    private val engine : Engine by inject ()

    @Composable
    fun DrawSettingsScreen() {
        val scope = rememberCoroutineScope()
        val isSystemInDarkTheme = isSystemInDarkTheme()
        val useDarkTheme by preferencesStorage.getUseDarkThemeValue(isSystemInDarkTheme)
            .collectAsState(initial = isSystemInDarkTheme)
        val backgroundColor = if (useDarkTheme) Color.Black else Color.White
        val topBarColor = if (useDarkTheme) Color.Gray else Color.Blue
        val activeEngineString by preferencesStorage.activeEngineAsFlowString
            .collectAsState(initial = EngineTypes.DefaultActiveEngine.toString())

        val activeEngine = rememberSaveable(activeEngineString) {
            enumValueOf<EngineTypes>(activeEngineString)
        }

        Theme(darkTheme = useDarkTheme) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(topBarColor)
                    .systemBarsPadding()
            ) {
                CustomTopBar(title = context.getString(R.string.app_name), useDarkTheme)

                if (context.isTelevision) {
                    DrawTelevisionSettings( scope, backgroundColor, activeEngine)
                } else {
                    DrawPhoneSettings( scope, backgroundColor, activeEngine)
                }
            }
        }

        SetupNavigationBar(useDarkTheme)
    }

    @Composable
    private fun DrawTelevisionSettings(
        scope: CoroutineScope,
        backgroundColor: Color, activeEngine: EngineTypes
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
        ) {
            Button(
                onClick = { scope.launch { engine.startEngine( activeEngine) } },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TranslatedText(context.getString(R.string.start_game), textAlign = TextAlign.Center, fontSize = 22.sp)
            }

            DrawAllSettings( scope, activeEngine)
        }
    }

    @Composable
    private fun DrawPhoneSettings(
        scope: CoroutineScope,
        backgroundColor: Color, activeEngine: EngineTypes
    ) {
        Scaffold(
            modifier = Modifier.background(backgroundColor),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { scope.launch { engine.startEngine( activeEngine) } }
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = context.getString(R.string.start_game)
                    )
                }
            }
        ) { innerPadding ->
            DrawAllSettings( innerPadding, scope, activeEngine)
        }
    }

    @Composable
    private fun DrawAllSettings(scope: CoroutineScope, activeEngine: EngineTypes) {
        DrawAllSettings( PaddingValues(), scope, activeEngine)
    }

    @Composable
    private fun DrawAllSettings(
        innerPadding: PaddingValues,
        scope: CoroutineScope, activeEngine: EngineTypes
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            DrawCommonSettings( scope, activeEngine)
            DrawTranslationModelSettings( scope)
            DrawGraphicsSettings( scope)
            DrawUserInterfaceSettings( scope)
        }
    }

    @Composable
    private fun DrawCommonSettings(scope: CoroutineScope, activeEngine: EngineTypes) {
        TranslatedText(context.getString(R.string.common_settings), style = MaterialTheme.typography.titleLarge)

        ListPreferenceItem(
            context.getString(R.string.active_engine),
            activeEngine,
            EngineTypes.entries
        ) { newValue ->
            scope.launch {
                preferencesStorage.setActiveEngineValue(
                    enumValueOf<EngineTypes>(newValue)
                )
            }
        }

        HorizontalDivider()

        when (activeEngine) {
            EngineTypes.WolfensteinRpg -> DrawWolfensteinRpgSettings( scope)
            EngineTypes.DoomRpg -> DrawDoomRpgSettings( scope)
            EngineTypes.Doom2Rpg -> DrawDoom2RpgSettings( scope)
            EngineTypes.Doom64ExPlus -> DrawDoom64Settings( scope)
        }

        HorizontalDivider()
    }

    @Composable
    private fun DrawTranslationModelSettings(scope: CoroutineScope) {
        TranslatedText(context.getString(R.string.translation_settings), style = MaterialTheme.typography.titleLarge)

        val activeTranslationTypeString by preferencesStorage.translationModelType
            .collectAsState(initial = TranslationType.DefaultTranslationType.toString())

        val translationModelEntries = buildTranslationsDescription()
        val initialModelValue = translationModelEntries.first { it.startsWith(activeTranslationTypeString) }

        ListPreferenceItem(
            context.getString(R.string.translation_model_title),
            initialModelValue,
            translationModelEntries
        ) { newValue ->
            scope.launch {
                val translationModelType = TranslationType.getTranslationType(newValue)
                translationManager.activeTranslationType = translationModelType
                preferencesStorage.setTranslationModelTypeValue( translationModelType)
            }
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.allow_downloading_over_mobile_network),
            checkedFlow = preferencesStorage.allowDownloadingModelsOverMobile,
        ) { newValue ->
            translationManager.allowDownloadingOveMobile = newValue
            scope.launch {
                preferencesStorage.setAllowDownloadingModelsOverMobileValue( newValue)
            }
        }

        HorizontalDivider()

        DrawPreloadModelsSetting(context)

        HorizontalDivider()
    }

    @Composable
    private fun DrawPreloadModelsSetting(vm: DownloadViewModel = koinViewModel()) {
        val activeTranslationTypeString by preferencesStorage.translationModelType
            .collectAsState(initial = "")

        LaunchedEffect(activeTranslationTypeString) {
            if (activeTranslationTypeString != "") {
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
    private fun DrawGraphicsSettings(scope: CoroutineScope) {

        TranslatedText(context.getString(R.string.graphics_settings), style = MaterialTheme.typography.titleLarge)

        val customScreenResolution by preferencesStorage.customScreenResolution
            .collectAsState(initial = "")

        val customAspectRatio by preferencesStorage.customAspectRatio
            .collectAsState(initial = "")

        SwitchPreferenceItem(
            context.getString(R.string.dark_theme),
            checkedFlow = preferencesStorage.getUseDarkThemeValue( isSystemInDarkTheme()),
        ) { newValue ->
            scope.launch {
                preferencesStorage.setUseDarkThemeValue( newValue)
            }
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.display_in_safe_area),
            checkedFlow = preferencesStorage.enableDisplayInSafeArea,
        ) { newValue ->
            scope.launch {
                preferencesStorage.setDisplayInSafeAreaValue( newValue)
            }
        }

        HorizontalDivider()

        EditTextPreferenceItem(
            context.getString(R.string.custom_aspect_ratio),
            customAspectRatio, context.getString(R.string.custom_aspect_ratio_hint)
        ) { newValue ->
            scope.launch {
                preferencesStorage.setCustomAspectRatio( newValue)
            }
        }

        HorizontalDivider()

        EditTextPreferenceItem(
            context.getString(R.string.custom_resolution),
            customScreenResolution!!, context.getString(R.string.custom_resolution_hint)
        ) { newValue ->
            scope.launch {
                preferencesStorage.setCustomScreenResolution( newValue)
            }
        }

        HorizontalDivider()
    }

    @Composable
    private fun DrawUserInterfaceSettings(scope: CoroutineScope) {
        val engineState by preferencesStorage.activeEngineAsFlowString.collectAsState(
            initial =
                EngineTypes.DefaultActiveEngine.toString()
        )
        val activeEngine = rememberSaveable(engineState) { enumValueOf<EngineTypes>(engineState!!) }
        var drawKeysEditor by rememberSaveable { mutableStateOf(false) }
        val isModelDownloaded by TranslationManager.isTranslationSupportedAsFlow().collectAsState(initial = true)

        TranslatedText(context.getString(R.string.user_interface_settings), style = MaterialTheme.typography.titleLarge)

        SwitchPreferenceItem(
            context.getString(R.string.use_sdl_ttf_for_rendering),
            checkedFlow = preferencesStorage.useSDLTTFForFontsRendering,
        ) { newValue ->
            scope.launch {
                preferencesStorage.setUseSDLTTFForFontsRenderingValue( newValue)
            }
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.use_ai_for_text_translations),
            checkedFlow = preferencesStorage.enableGameMachineTextTranslation,
            enabled = isModelDownloaded
        ) { newValue ->
            scope.launch {
                preferencesStorage.setEnableGameMachineTextTranslationValue( newValue)
            }
        }

        HorizontalDivider()

        PreferenceItem(context.getString(R.string.keys_editor)) {
            drawKeysEditor = true
        }

        HorizontalDivider()

        PreferenceItem(context.getString(R.string.configure_screen_controls)) {
            ScreenControlsEditorActivity.editControls( activeEngine)
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.show_custom_mouse_cursor),
            checkedFlow = preferencesStorage.showCustomMouseCursor,
        ) { newValue ->
            scope.launch {
                preferencesStorage.setShowCustomMouseCursorValue( newValue)
            }
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.allow_to_edit_controls_in_game),
            checkedFlow = preferencesStorage.editCustomScreenControlsInGame,
        ) { newValue ->
            scope.launch {
                preferencesStorage.setEditCustomScreenControlsInGameValue( newValue)
            }
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.hide_custom_screen_controls),
            checkedFlow = preferencesStorage.hideScreenControls,
        ) { newValue ->
            scope.launch {
                preferencesStorage.setHideControlsValue( newValue)
            }
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.controls_autohing),
            checkedFlow = preferencesStorage.autoHideScreenControls,
        ) { newValue ->
            scope.launch {
                preferencesStorage.setControlsAutoHidingValue( newValue)
            }
        }

        HorizontalDivider()

        if (drawKeysEditor) {
            val buttonsToDraw = enginesInfo[activeEngine]!!.buttonsToDraw
            KeysEditor(buttonsToDraw) {
                drawKeysEditor = false
            }
        }
    }

    @Composable
    private fun DrawWolfensteinRpgSettings(scope: CoroutineScope) {
        val previousPathToWolfensteinRpgIPa by preferencesStorage.pathToWolfensteinRpgIpaFile
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.wolfenstein_rpg_ipa_file), onPathSelected = { selectedPath ->
                scope.launch { preferencesStorage.setPathToWolfensteinRpgIpaFile( selectedPath) }
            },
            previousPathToWolfensteinRpgIPa
        )
    }

    @Composable
    private fun DrawDoomRpgSettings(scope: CoroutineScope) {
        val savedPathToDoomRpgZip by preferencesStorage.pathToDoomRpgZipFile
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.doom_rpg_zip_file),
            onPathSelected = { selectedPath ->
                scope.launch { preferencesStorage.setPathToDoomRpgZipFile( selectedPath) }
            },
            savedPathToDoomRpgZip,
        )
    }

    @Composable
    private fun DrawDoom2RpgSettings(scope: CoroutineScope) {
        val previousPathToDoom2RpgIpa by preferencesStorage.pathToDoom2RpgIpaFile
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.doom2_rpg_ipa_file),
            onPathSelected = { selectedPath ->
                scope.launch { preferencesStorage.setPathToDoom2RpgIpaFile( selectedPath) }
            },
            previousPathToDoom2RpgIpa
        )
    }

    @Composable
    private fun DrawDoom64Settings(scope: CoroutineScope) {
        val previousPathToDoom64WadsFolder by preferencesStorage.pathToDoom64MainWadsFolder
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.path_to_doom64_folder),
            onPathSelected = { selectedPath ->
                scope.launch { preferencesStorage.setPathToDoom64MainWadsFolder( selectedPath) }
            },
            previousPathToDoom64WadsFolder, requestOnlyDirectory = true
        )

        HorizontalDivider()

        val enableDoom64ModsFlow = preferencesStorage.enableDoom64Mods
        val enableDoom64Mods by enableDoom64ModsFlow.collectAsState(initial = false)

        SwitchPreferenceItem(
            context.getString(R.string.enable_doom64_mods),
            checkedFlow = enableDoom64ModsFlow,
        ) { newValue ->
            scope.launch {
                preferencesStorage.setEnableDoom64ModsValue( newValue)
            }
        }

        val previousPathToDoom64ModsFolder by preferencesStorage.pathToDoom64ModsFolder
            .collectAsState(initial = "")

        if (enableDoom64Mods) {
            HorizontalDivider()

            RequestPath(
                context.getString(R.string.path_to_doom64_mods_folder),
                onPathSelected = { selectedPath ->
                    scope.launch { preferencesStorage.setPathToDoom64ModsFolder( selectedPath) }
                },
                previousPathToDoom64ModsFolder, requestOnlyDirectory = true
            )
        }
    }
}

