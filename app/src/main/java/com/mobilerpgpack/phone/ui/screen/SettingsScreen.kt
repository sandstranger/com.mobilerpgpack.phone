package com.mobilerpgpack.phone.ui.screen

import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.activity.ScreenControlsEditorActivity
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.ShowYesNoDialog
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.viewmodels.SettingsScreenViewModel
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.isTelevision
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.component.get
import org.koin.core.qualifier.named

class SettingsScreen : ComposeScreen(SCREEN_NAME) {

    private val preferencesStorage : PreferencesStorage = get ()

    private val context : Context = get ()

    override val drawFloatingActionButton: Boolean = !context.isTelevision

    @Composable
    override fun DrawScreenContent(
        innerPadding: PaddingValues,
        navController: NavHostController,
        textColor: Color,
        isSystemInDarkTheme: Boolean
    ) {
        val activity = LocalActivity.current!!
        val scope = rememberCoroutineScope()
        val activeEngineString by preferencesStorage.activeEngineAsFlowString
            .collectAsState(initial = EngineTypes.DefaultActiveEngine.toString())

        val activeEngine = rememberSaveable(activeEngineString) {
            enumValueOf<EngineTypes>(activeEngineString)
        }

        val settingsScreenViewModel : SettingsScreenViewModel = koinViewModel ()

        super.onFloatingActionButtonClickedDelegate = {
            settingsScreenViewModel.onStartGameClicked(activeEngine, activity)
        }

        if (!drawFloatingActionButton) {
            DrawTelevisionSettings(innerPadding,
                scope, activeEngine,
                navController, settingsScreenViewModel
            )
        } else {
            DrawAllSettings( innerPadding, scope, activeEngine,navController, settingsScreenViewModel)
        }
    }

    @Composable
    private fun DrawTelevisionSettings(
        innerPadding: PaddingValues,
        scope: CoroutineScope,
        activeEngine: EngineTypes,
        navController: NavHostController,
        viewModel: SettingsScreenViewModel
    ) {
        val activity = LocalActivity.current!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Button(
                onClick = { viewModel.onStartGameClicked(activeEngine, activity) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.start_game), textAlign = TextAlign.Center, fontSize = 22.sp)
            }

            DrawAllSettings( innerPadding,scope, activeEngine,navController, viewModel)
        }
    }

    @Composable
    private fun DrawAllSettings(
        innerPadding: PaddingValues,
        scope: CoroutineScope,
        activeEngine: EngineTypes,
        navController: NavHostController,
        viewModel: SettingsScreenViewModel) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            DrawCommonSettings( scope, activeEngine, viewModel, navController)
            DrawGraphicsSettings()
            DrawUserInterfaceSettings(scope)
        }
    }

    @Composable
    private fun DrawCommonSettings(scope: CoroutineScope, activeEngine: EngineTypes,
                                   viewModel: SettingsScreenViewModel, navController: NavHostController) {
        DrawTitleText(stringResource(R.string.common_settings))

        ListPreferenceItem(
            stringResource(R.string.active_engine),
            activeEngine) { newValue ->
            preferencesStorage.setActiveEngineValue(newValue)
        }

        HorizontalDivider()
        DrawResetResourcesDialog(viewModel)
        HorizontalDivider()

        val engineInfo : IEngineUIController = koinInject(named(activeEngine.toString()))
        engineInfo.DrawSettings(navController)

        HorizontalDivider()
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

        DrawTitleText(stringResource(R.string.graphics_settings))

        val customScreenResolution by preferencesStorage.customScreenResolution
            .collectAsState(initial = "")

        val customAspectRatio by preferencesStorage.customAspectRatio
            .collectAsState(initial = "")

        SwitchPreferenceItem(
            stringResource(R.string.dark_theme),
            preferencesStorage.getUseDarkThemeValue( isSystemInDarkTheme()),
            preferencesStorage.useDarkThemePrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.display_in_safe_area),
            preferencesStorage.enableDisplayInSafeArea,
            preferencesStorage.displayInSafeAreaPrefsKey.name)

        HorizontalDivider()

        EditTextPreferenceItem(stringResource(R.string.custom_aspect_ratio), customAspectRatio,
            preferencesStorage.customAspectRatioPrefsKey.name, stringResource(R.string.custom_aspect_ratio_hint))

        HorizontalDivider()

        EditTextPreferenceItem(stringResource(R.string.custom_resolution),
            customScreenResolution,preferencesStorage.customScreenResolutionPrefsKey.name, stringResource(R.string.custom_resolution_hint))

        HorizontalDivider()
    }

    @Composable
    private fun DrawUserInterfaceSettings(scope: CoroutineScope) {
        val useStandardSDLTextInput by preferencesStorage.useStandardSDLTextInput
            .collectAsState(initial = false)

        DrawTitleText(stringResource(R.string.user_interface_settings))
        DrawEditScreenControlsSettings()
        HorizontalDivider()

        SwitchPreferenceItem(stringResource(R.string.use_standard_sdl_text_input),
            useStandardSDLTextInput,
            preferencesStorage.useStandardSDLTextInputPrefsKey.name)

        HorizontalDivider()

        DrawMouseCustomCursorSettings()
        HorizontalDivider()
    }

    @Composable
    private fun DrawEditScreenControlsSettings (){
        val activity = LocalActivity.current!!
        val engineState by preferencesStorage.activeEngineAsFlowString.collectAsState(
            initial =
                EngineTypes.DefaultActiveEngine.toString()
        )
        val activeEngine = rememberSaveable(engineState) { enumValueOf<EngineTypes>(engineState!!) }
        var drawKeysEditor by rememberSaveable { mutableStateOf(false) }

        PreferenceItem(stringResource(R.string.keys_editor)) {
            drawKeysEditor = true
        }

        HorizontalDivider()

        PreferenceItem(stringResource(R.string.configure_screen_controls)) {
            ScreenControlsEditorActivity.editControls( activity,activeEngine)
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.allow_to_edit_controls_in_game),
            preferencesStorage.editCustomScreenControlsInGame,
            preferencesStorage.editCustomScreenControlsInGamePrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.hide_custom_screen_controls),
            preferencesStorage.hideScreenControls,
            preferencesStorage.hideScreenControlsPrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.controls_autohing),
            preferencesStorage.autoHideScreenControls,
            preferencesStorage.enableControlsAutoHiding.name)

        HorizontalDivider()

        EditTextPreferenceItem(
            stringResource(R.string.custom_keyboard_transparency),
            preferencesStorage.customOnScreenKeyboardTransparency){
            preferencesStorage.setFloatValue(preferencesStorage.customOnScreenKeyboardTransparencyPrefsKey,
                it.coerceIn(0f, 1.0f))
        }

        if (drawKeysEditor) {
            val engineInfo : IEngineUIController = get (named(activeEngine.toString()))
            KeysEditor(engineInfo.screenViewsToDraw) {
                drawKeysEditor = false
            }
        }
    }

    @Composable
    private fun DrawMouseCustomCursorSettings (){

        SwitchPreferenceItem(
            stringResource(R.string.show_custom_mouse_cursor),
            preferencesStorage.showCustomMouseCursor,
            preferencesStorage.showCustomMouseCursorPrefsKey.name)

        HorizontalDivider()

        EditTextPreferenceItem(stringResource(R.string.custom_mouse_cursor_horizontal_offset),
            preferencesStorage.offsetXMouse, preferencesStorage.OFFSET_X_MOUSE.name)

        HorizontalDivider()

        EditTextPreferenceItem(stringResource(R.string.custom_mouse_cursor_vertical_offset),
            preferencesStorage.offsetYMouse, preferencesStorage.OFFSET_Y_MOUSE.name)
    }

    companion object{
        const val SCREEN_NAME = "settings"
    }
}

