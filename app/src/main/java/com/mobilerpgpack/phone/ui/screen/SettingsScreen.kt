package com.mobilerpgpack.phone.ui.screen

import CustomTopBar
import android.content.Context
import android.util.Log
import androidx.activity.compose.LocalActivity
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.github.sproctor.composepreferences.LocalPreferenceHandler
import com.github.sproctor.composepreferences.PreferenceHandler
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.activity.ScreenControlsEditorActivity
import com.mobilerpgpack.phone.ui.getBackgroundColor
import com.mobilerpgpack.phone.ui.getTopBarColor
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.PreferenceItem
import com.mobilerpgpack.phone.ui.items.SetupNavigationBar
import com.mobilerpgpack.phone.ui.items.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.isTelevision
import com.mobilerpgpack.phone.utils.startGame
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.datastore.DataStoreSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parameterSetOf
import org.koin.core.qualifier.named

@OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
class SettingsScreen : KoinComponent {

    private val context : Context by inject()
    private val preferencesStorage : PreferencesStorage = get ()
    private val settings = DataStoreSettings(preferencesStorage.dataStore)

    @Composable
    fun DrawSettingsScreen() {
        val scope = rememberCoroutineScope()
        val isSystemInDarkTheme = isSystemInDarkTheme()
        val useDarkTheme by preferencesStorage.getUseDarkThemeValue(isSystemInDarkTheme)
            .collectAsState(initial = isSystemInDarkTheme)
        val backgroundColor = getBackgroundColor(useDarkTheme)
        val topBarColor = getTopBarColor(useDarkTheme)
        val activeEngineString by preferencesStorage.activeEngineAsFlowString
            .collectAsState(initial = EngineTypes.DefaultActiveEngine.toString())

        val activeEngine = rememberSaveable(activeEngineString) {
            enumValueOf<EngineTypes>(activeEngineString)
        }

        val prerefencesHandler : PreferenceHandler = koinInject {parameterSetOf(settings) }

        Theme(darkTheme = useDarkTheme) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(topBarColor)
                    .systemBarsPadding()
            ) {
                CustomTopBar(title = context.getString(R.string.app_name), useDarkTheme)

                CompositionLocalProvider(LocalPreferenceHandler provides prerefencesHandler) {
                    if (context.isTelevision) {
                        DrawTelevisionSettings( scope, backgroundColor, activeEngine)
                    } else {
                        DrawPhoneSettings( scope, backgroundColor, activeEngine)
                    }
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
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
        ) {
            Button(
                onClick = { scope.launch { startGame( context,activeEngine) } },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(context.getString(R.string.start_game), textAlign = TextAlign.Center, fontSize = 22.sp)
            }

            DrawAllSettings( scope, activeEngine)
        }
    }

    @Composable
    private fun DrawPhoneSettings(
        scope: CoroutineScope,
        backgroundColor: Color, activeEngine: EngineTypes
    ) {
        val context = LocalContext.current
        Scaffold(
            modifier = Modifier.background(backgroundColor),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { scope.launch { startGame( context,activeEngine) } }
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
            DrawGraphicsSettings()
            DrawUserInterfaceSettings(scope)
        }
    }

    @Composable
    private fun DrawCommonSettings(scope: CoroutineScope, activeEngine: EngineTypes) {
        DrawTitleText(context.getString(R.string.common_settings))

        ListPreferenceItem(
            context.getString(R.string.active_engine),
            activeEngine.toString(),
            EngineTypes.entries.map { it.toString() }.toList()
        ) { newValue ->
            scope.launch {
                preferencesStorage.setActiveEngineValue(
                    enumValueOf<EngineTypes>(newValue)
                )
            }
        }

        HorizontalDivider()

        val engineInfo : IEngineUIController = koinInject(named(activeEngine.toString()))
        engineInfo.DrawSettings()

        HorizontalDivider()
    }

    @Composable
    private fun DrawGraphicsSettings() {

        DrawTitleText(context.getString(R.string.graphics_settings))

        val customScreenResolution by preferencesStorage.customScreenResolution
            .collectAsState(initial = "")

        val customAspectRatio by preferencesStorage.customAspectRatio
            .collectAsState(initial = "")

        SwitchPreferenceItem(
            context.getString(R.string.dark_theme),
            preferencesStorage.getUseDarkThemeValue( isSystemInDarkTheme()),
            preferencesStorage.useDarkThemePrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.display_in_safe_area),
            preferencesStorage.enableDisplayInSafeArea,
            preferencesStorage.displayInSafeAreaPrefsKey.name)

        HorizontalDivider()

        EditTextPreferenceItem(context.getString(R.string.custom_aspect_ratio), customAspectRatio,
            preferencesStorage.customAspectRatioPrefsKey.name, context.getString(R.string.custom_aspect_ratio_hint))

        HorizontalDivider()

        EditTextPreferenceItem(context.getString(R.string.custom_resolution),
            customScreenResolution,preferencesStorage.customScreenResolutionPrefsKey.name, context.getString(R.string.custom_resolution_hint))

        HorizontalDivider()
    }

    @Composable
    private fun DrawUserInterfaceSettings(scope: CoroutineScope) {
        val activity = LocalActivity.current!!
        val engineState by preferencesStorage.activeEngineAsFlowString.collectAsState(
            initial =
                EngineTypes.DefaultActiveEngine.toString()
        )
        val activeEngine = rememberSaveable(engineState) { enumValueOf<EngineTypes>(engineState!!) }
        var drawKeysEditor by rememberSaveable { mutableStateOf(false) }
        val useStandardSDLTextInput by preferencesStorage.useStandardSDLTextInput
            .collectAsState(initial = false)

        val horizontalMouseIconOffset by preferencesStorage.offsetXMouse
            .collectAsState(initial = 0f)

        val verticalMouseIconOffset by preferencesStorage.offsetYMouse
            .collectAsState(initial = 0f)

        DrawTitleText(context.getString(R.string.user_interface_settings))

        SwitchPreferenceItem(context.getString(R.string.use_standard_sdl_text_input),
            useStandardSDLTextInput,
            preferencesStorage.useStandardSDLTextInputPrefsKey.name)

        HorizontalDivider()

        PreferenceItem(context.getString(R.string.keys_editor)) {
            drawKeysEditor = true
        }

        HorizontalDivider()

        PreferenceItem(context.getString(R.string.configure_screen_controls)) {
            ScreenControlsEditorActivity.editControls( activity,activeEngine)
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.show_custom_mouse_cursor),
             preferencesStorage.showCustomMouseCursor,
            preferencesStorage.showCustomMouseCursorPrefsKey.name)

        HorizontalDivider()

        EditTextPreferenceItem(
            context.getString(R.string.custom_mouse_cursor_horizontal_offset),
            horizontalMouseIconOffset.toString()){
            val floatValue = it.toFloatOrNull() ?: 0.0f
                scope.launch {
                    preferencesStorage.setOffsetXMouse(floatValue)
                }
            }

        HorizontalDivider()

        EditTextPreferenceItem(
            context.getString(R.string.custom_mouse_cursor_vertical_offset),
             verticalMouseIconOffset.toString()){
            val floatValue = it.toFloatOrNull() ?: 0.0f
            scope.launch {
                preferencesStorage.setOffsetYMouse(floatValue)
            }
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.allow_to_edit_controls_in_game),
            preferencesStorage.editCustomScreenControlsInGame,
            preferencesStorage.editCustomScreenControlsInGamePrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.hide_custom_screen_controls),
            preferencesStorage.hideScreenControls,
            preferencesStorage.hideScreenControlsPrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.controls_autohing),
            preferencesStorage.autoHideScreenControls,
            preferencesStorage.enableControlsAutoHiding.name)

        HorizontalDivider()

        if (drawKeysEditor) {
            val engineInfo : IEngineUIController = get (named(activeEngine.toString()))
            KeysEditor(engineInfo.screenViewsToDraw) {
                drawKeysEditor = false
            }
        }
    }
}

