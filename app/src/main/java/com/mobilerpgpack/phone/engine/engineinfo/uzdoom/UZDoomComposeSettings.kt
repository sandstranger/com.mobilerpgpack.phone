package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.DrawModsSupport
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.SettingScreen
import com.mobilerpgpack.phone.ui.items.SwitchItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class UZDoomComposeSettings : IEngineUIController, KoinComponent {

    private val zDoomEngineInfo: IEngineInfo by inject(named(EngineTypes.UZDoom.toString()))

    private val preferencesStorage: UZDoomPreferenceStorage by inject(
        named(EngineTypes.UZDoom.toString())
    )

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val viewModel: UZDoomComposeSettingsViewModel = koinViewModel()
        viewModel.initialize()

        if (!viewModel.showView) {
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
            requiredFileExtensions = zDoomEngineInfo.requiredResourceExtensions
        )

        HorizontalDivider()

        ListPreferenceItem(
            stringResource(R.string.uzdoom_rendering_api),
            viewModel.renderAPI) {
            viewModel.renderAPI = it
        }

        HorizontalDivider()

        ListPreferenceItem(
            stringResource(R.string.uzdoom_rendering_gles_version),
            preferencesStorage.uzDoomGLESVersion,
            UZDoomGLESVersion.stringCollection
        ) {
            preferencesStorage.setStringValue(preferencesStorage.uzDoomGLESVersionPrefsKey, it)
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.uzdoom_enable_light_shaders),
            preferencesStorage.enableLightShaders,
            preferencesStorage.enableLightShadersPrefsKey.name)

        HorizontalDivider()

        SwitchItem(
            stringResource(R.string.uzdoom_autoload_lights),
            viewModel.autoLoadLights
        ) {
            viewModel.autoLoadLights = it
        }

        HorizontalDivider()

        SwitchItem(
            stringResource(R.string.uzdoom_autoload_brightmaps),
            viewModel.autoLoadBrightMaps
        ) {
            viewModel.autoLoadBrightMaps = it
        }

        HorizontalDivider()

        SwitchItem(
            stringResource(R.string.uzdoom_autoload_widescreen),
            viewModel.autoLoadWideScreen
        ) {
            viewModel.autoLoadWideScreen = it
        }

        HorizontalDivider()

        PreferenceItem(stringResource(R.string.more_uzdoom_settings)) {
            navController.navigate(MORE_SETTINGS_SCREEN)
        }
    }

    @Composable
    private fun DrawMoreSettings() {
        val viewModel: UZDoomComposeSettingsViewModel = koinViewModel()
        DrawModsSupport(viewModel.uzDoomMods)

        SwitchItem(
            stringResource(R.string.enable_uzdoom_playing_records),
            viewModel.uzDoomMods.enableDemoPlayingSupport.value!!
        ) {
            viewModel.uzDoomMods.enableDemoPlayingSupport.value = it
            viewModel.uzDoomMods.save()
        }

        HorizontalDivider()

        if (viewModel.uzDoomMods.enableDemoPlayingSupport.value!!){
            RequestPath(stringResource(R.string.uzdoom_path_to_demo_file),
                viewModel.uzDoomMods.pathToDemoFile.value!!,
                requestMode = RequestPathMode.File,
                requiredFileExtensions = listOf(".lmp")
            ) {
                viewModel.uzDoomMods.pathToDemoFile.value = it
                viewModel.uzDoomMods.save()
            }

            HorizontalDivider()
        }

        SwitchItem(
            stringResource(R.string.enable_uzdoom_beh_support),
            viewModel.uzDoomMods.enableBehSupport.value!!
        ) {
            viewModel.uzDoomMods.enableBehSupport.value = it
            viewModel.uzDoomMods.save()
        }

        HorizontalDivider()

        if (viewModel.uzDoomMods.enableBehSupport.value!!){
            RequestPath(stringResource(R.string.uzdoom_path_to_beh_file),
                viewModel.uzDoomMods.pathToBehFile.value!!,
                requestMode = RequestPathMode.File,
                requiredFileExtensions = listOf(".beh")
            ) {
                viewModel.uzDoomMods.pathToBehFile.value = it
                viewModel.uzDoomMods.save()
            }

            HorizontalDivider()
        }

        SwitchItem(
            stringResource(R.string.enable_uzdoom_deh_support),
            viewModel.uzDoomMods.enableDehSupport.value!!
        ) {
            viewModel.uzDoomMods.enableDehSupport.value = it
            viewModel.uzDoomMods.save()
        }

        HorizontalDivider()

        if (viewModel.uzDoomMods.enableDehSupport.value!!){
            RequestPath(stringResource(R.string.uzdoom_path_to_deh_file),
                viewModel.uzDoomMods.pathToDehFile.value!!,
                requestMode = RequestPathMode.File,
                requiredFileExtensions = listOf(".deh")
            ) {
                viewModel.uzDoomMods.pathToDehFile.value = it
                viewModel.uzDoomMods.save()
            }

            HorizontalDivider()
        }

        SwitchItem(
            stringResource(R.string.enable_uzdoom_xlat_support),
            viewModel.uzDoomMods.enableXLatSupport.value!!
        ) {
            viewModel.uzDoomMods.enableXLatSupport.value = it
            viewModel.uzDoomMods.save()
        }

        HorizontalDivider()

        if (viewModel.uzDoomMods.enableXLatSupport.value!!){
            RequestPath(stringResource(R.string.uzdoom_path_to_xlat_file),
                viewModel.uzDoomMods.pathToXLatFile.value!!,
                requestMode = RequestPathMode.File
            ) {
                viewModel.uzDoomMods.pathToXLatFile.value = it
                viewModel.uzDoomMods.save()
            }

            HorizontalDivider()
        }
    }

    data class UZDoomMoreSettingsScreen(private val composeSettings: UZDoomComposeSettings) :
        SettingScreen(MORE_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            composeSettings.DrawMoreSettings()
    }

    private companion object {
        private const val MORE_SETTINGS_SCREEN = "more_uzdoom_settings_screen"
    }
}