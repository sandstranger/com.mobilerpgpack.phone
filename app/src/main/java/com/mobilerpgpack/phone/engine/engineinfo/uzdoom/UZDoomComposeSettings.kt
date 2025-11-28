package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.SettingScreen
import com.mobilerpgpack.phone.ui.items.SwitchItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.psyDoomButtons
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.getValue

class UZDoomComposeSettings: IEngineUIController, KoinComponent {

    private val zDoomEngineInfo : IEngineInfo by inject (named(EngineTypes.UZDoom.toString()))

    private val preferencesStorage: UZDoomPreferenceStorage by inject(
        named(EngineTypes.UZDoom.toString()))

    override val screenViewsToDraw: Collection<IScreenControlsView> = psyDoomButtons

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val viewModel : UZDoomComposeSettingsViewModel = koinViewModel ()
        viewModel.initialize()

        if (!viewModel.showView){
            return
        }

        DrawCommandLinePreferences(
            preferencesStorage.uZDoomCommandLineArgsString,
            preferencesStorage.uZDoomCommandLineArgsPrefsKey.name
        )

        HorizontalDivider()

        RequestPath(
            stringResource(R.string.path_to_uzdoom_wad_file),
            preferencesStorage.pathToUZDoomIWadFile,
            preferencesStorage.pathToUZDoomIWadFilePrefsKey,
            RequestPathMode.File,
            requiredFileExtensions = arrayListOf(zDoomEngineInfo.requiredResourceExtension))

        HorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.enable_lzdoom_mods),
            initialValueFlow = preferencesStorage.enableUZDoomMods,
            preferencesStorage.enableUZDoomModsPrefsKey.name)

        HorizontalDivider()

        val enableMods by preferencesStorage.enableUZDoomMods.collectAsState(initial = false)

        if (enableMods) {
            RequestPath(
                stringResource(R.string.path_to_uzdoom_mods_folder),
                preferencesStorage.pathToUZDoomModsFolder,
                preferencesStorage.pathToUZDoomModsFolderPrefsKey
            )

            HorizontalDivider()
        }

        ListPreferenceItem(
            stringResource(R.string.uzdoom_rendering_api),
            viewModel.renderAPI.toString(),
            UZDoomRenderAPI.stringCollection
        ) {
            viewModel.renderAPI =  enumValueOf<UZDoomRenderAPI>(it)
        }

        HorizontalDivider()

        SwitchItem(
            stringResource(R.string.uzdoom_autoload_lights),
            viewModel.autoLoadLights) {
            viewModel.autoLoadLights = it
        }

        HorizontalDivider()

        SwitchItem(
            stringResource(R.string.uzdoom_autoload_brightmaps),
            viewModel.autoLoadBrightMaps) {
            viewModel.autoLoadBrightMaps = it
        }

        HorizontalDivider()

        SwitchItem(
            stringResource(R.string.uzdoom_autoload_widescreen),
            viewModel.autoLoadWideScreen) {
            viewModel.autoLoadWideScreen = it
        }

        HorizontalDivider()

        PreferenceItem(stringResource(R.string.more_uzdoom_settings)) {
            navController.navigate(MORE_SETTINGS_SCREEN)
        }
    }

    @Composable
    private fun DrawMoreSettings(){

    }

    data class UZDoomMoreSettingsScreen(private val composeSettings: UZDoomComposeSettings) :
        SettingScreen(MORE_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            composeSettings.DrawMoreSettings()
    }

    private companion object{
        private const val MORE_SETTINGS_SCREEN = "more_uzdoom_settings_screen"
    }
}