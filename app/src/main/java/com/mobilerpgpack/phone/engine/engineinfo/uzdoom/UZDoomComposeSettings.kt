package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.getComposableNullableValue
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class UZDoomComposeSettings : IEngineUIController, KoinComponent {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val zDoomEngineInfo : IEngineInfo = koinInject(named(EngineTypes.UZDoom.name))
        val preferencesStorage : UZDoomPreferenceStorage = koinInject(named(EngineTypes.UZDoom.name))
        val viewModel: UZDoomComposeSettingsViewModel = koinViewModel()
        val showView = viewModel.showView.getComposableValue()

        if (!showView) {
            return
        }

        DrawCommandLinePreferences(
            preferencesStorage.uZDoomCommandLineArgsString,
            preferencesStorage.uZDoomCommandLineArgsPrefsKey.name
        )

        DrawHorizontalDivider()

        val engineVersionStream = preferencesStorage.uzDoomEngineVersion.getComposableValue(
            UZDoomEngineVersions.Dev)
        val engineVersion by rememberSaveable(engineVersionStream) {
            mutableStateOf(engineVersionStream) }

        ListPreferenceItem(
            stringResource(R.string.uzdoom_engine_version),
            engineVersion) {
            viewModel.onEngineVersionChanged(it)
        }

        DrawHorizontalDivider()

        RequestPath(
            stringResource(R.string.path_to_uzdoom_wad_file),
            preferencesStorage.pathToUZDoomIWadFile,
            preferencesStorage.pathToUZDoomIWadFilePrefsKey,
            RequestPathMode.File,
            requiredFileExtensions = zDoomEngineInfo.requiredResourceExtensions
        )

        DrawHorizontalDivider()

        val renderApiStream = viewModel.renderAPIAsLiveData.getComposableValue(UZDoomRenderAPI.OpenGLES.value)
        val renderApi by rememberSaveable(renderApiStream) {
            mutableStateOf(UZDoomRenderAPI.fromValue(renderApiStream)) }

        ListPreferenceItem(
            stringResource(R.string.uzdoom_rendering_api),
            renderApi) {
            viewModel.renderAPI = it
        }

        DrawHorizontalDivider()

        val stringCollection = rememberSaveable { UZDoomGLESVersion.stringCollection }

        ListPreferenceItem(
            stringResource(R.string.uzdoom_rendering_gles_version),
            preferencesStorage.uzDoomGLESVersion,
            stringCollection
        ) {
            preferencesStorage.setStringValue(preferencesStorage.uzDoomGLESVersionPrefsKey, it)
        }
        DrawHorizontalDivider()
        SwitchPreferenceItem(stringResource(R.string.enable_spirv_cross),
            preferencesStorage.enableSpirvCross,
            preferencesStorage.enableSpirvCrossPrefsKey.name)
        DrawHorizontalDivider()
        SwitchPreferenceItem(
            stringResource(R.string.uzdoom_enable_light_shaders),
            preferencesStorage.enableLightShaders,
            preferencesStorage.enableLightShadersPrefsKey.name)
        DrawHorizontalDivider()

        SwitchItem(
            stringResource(R.string.uzdoom_autoload_lights),
            viewModel.autoLoadLightsAsLiveData
        ) {
            viewModel.autoLoadLights = it
        }

        DrawHorizontalDivider()

        SwitchItem(
            stringResource(R.string.uzdoom_autoload_brightmaps),
            viewModel.autoLoadBrightMapsAsLiveData
        ) {
            viewModel.autoLoadBrightMaps = it
        }

        DrawHorizontalDivider()

        SwitchItem(
            stringResource(R.string.uzdoom_autoload_widescreen),
            viewModel.autoLoadWideScreenAsLiveData
        ) {
            viewModel.autoLoadWideScreen = it
        }

        DrawHorizontalDivider()

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
            viewModel.uzDoomMods.enableDemoPlayingSupport.liveData
        ) {
            viewModel.uzDoomMods.enableDemoPlayingSupport.value = it
            viewModel.uzDoomMods.save()
        }

        DrawHorizontalDivider()

        val enableDemoPlayingSupport = viewModel.uzDoomMods.enableDemoPlayingSupport.liveData.getComposableNullableValue()

        if (enableDemoPlayingSupport!!){
            RequestPath(stringResource(R.string.uzdoom_path_to_demo_file),
                viewModel.uzDoomMods.pathToDemoFile.liveData,
                requestMode = RequestPathMode.File,
                requiredFileExtensions = listOf(".lmp")
            ) {
                viewModel.uzDoomMods.pathToDemoFile.value = it
                viewModel.uzDoomMods.save()
            }

            DrawHorizontalDivider()
        }

        SwitchItem(
            stringResource(R.string.enable_uzdoom_beh_support),
            viewModel.uzDoomMods.enableBehSupport.liveData
        ) {
            viewModel.uzDoomMods.enableBehSupport.value = it
            viewModel.uzDoomMods.save()
        }

        DrawHorizontalDivider()

        val enableBehSupport = viewModel.uzDoomMods.enableBehSupport.liveData.getComposableNullableValue()

        if (enableBehSupport!!){
            RequestPath(stringResource(R.string.uzdoom_path_to_beh_file),
                viewModel.uzDoomMods.pathToBehFile.liveData,
                requestMode = RequestPathMode.File,
                requiredFileExtensions = listOf(".beh")
            ) {
                viewModel.uzDoomMods.pathToBehFile.value = it
                viewModel.uzDoomMods.save()
            }

            DrawHorizontalDivider()
        }

        SwitchItem(
            stringResource(R.string.enable_uzdoom_deh_support),
            viewModel.uzDoomMods.enableDehSupport.liveData
        ) {
            viewModel.uzDoomMods.enableDehSupport.value = it
            viewModel.uzDoomMods.save()
        }

        DrawHorizontalDivider()

        val enableDehSupport = viewModel.uzDoomMods.enableDehSupport.liveData.getComposableNullableValue()

        if (enableDehSupport!!){
            RequestPath(stringResource(R.string.uzdoom_path_to_deh_file),
                viewModel.uzDoomMods.pathToDehFile.liveData,
                requestMode = RequestPathMode.File,
                requiredFileExtensions = listOf(".deh")
            ) {
                viewModel.uzDoomMods.pathToDehFile.value = it
                viewModel.uzDoomMods.save()
            }

            DrawHorizontalDivider()
        }

        SwitchItem(
            stringResource(R.string.enable_uzdoom_xlat_support),
            viewModel.uzDoomMods.enableXLatSupport.liveData
        ) {
            viewModel.uzDoomMods.enableXLatSupport.value = it
            viewModel.uzDoomMods.save()
        }

        DrawHorizontalDivider()

        val enableXLatSupport = viewModel.uzDoomMods.enableXLatSupport.liveData.getComposableNullableValue()

        if (enableXLatSupport!!){
            RequestPath(stringResource(R.string.uzdoom_path_to_xlat_file),
                viewModel.uzDoomMods.pathToXLatFile.liveData,
                requestMode = RequestPathMode.File
            ) {
                viewModel.uzDoomMods.pathToXLatFile.value = it
                viewModel.uzDoomMods.save()
            }

            DrawHorizontalDivider()
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