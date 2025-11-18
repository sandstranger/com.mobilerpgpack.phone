package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.screencontrols.wolfensteinButtons
import com.mobilerpgpack.phone.utils.IAssetExtractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class PsyDoomComposeSettings : IEngineUIController, KoinComponent {

    private val assetsExtractor : IAssetExtractor by inject ()

    private val preferencesStorage : PsyDoomPreferencesStorage by inject (
        named(EngineTypes.PsyDoom.toString()))

    override val screenViewsToDraw = wolfensteinButtons

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        if (assetsExtractor.assetsCopied){
            DrawPsyDoomCommonSettings(navController)
        }
    }

    @Composable
    private fun DrawPsyDoomCommonSettings(navController: NavHostController){
        RequestPath(
            stringResource(R.string.path_to_psydoom_cue_file),
            preferencesStorage.pathToPsyDoomCueFile,
            preferencesStorage.pathToPsyDoomCueFilePrefsKey,
            RequestPathMode.Cue)

        HorizontalDivider()

        RequestPath(
            stringResource(R.string.path_to_psydoom_mods_folder),
            preferencesStorage.pathToPsyDoomModsFolder,
            preferencesStorage.pathToPsyDoomModsFolderPrefsKey)

        HorizontalDivider()

        DrawCommandLinePreferences(preferencesStorage.psyDoomCommandLineArgsString,
            preferencesStorage.psyDoomCommandLineArgsPrefsKey.name)

        HorizontalDivider()

        PreferenceItem(stringResource(R.string.psydoom_more_settings)){
            navController.navigate(MORE_SETTINGS_SCREEN)
        }
    }

    @Composable
    private fun DrawMoreSettings(navController: NavHostController){
        HorizontalDivider()
        PreferenceItem(stringResource(R.string.psydoom_launcher_settings)){
            navController.navigate(LAUNCHER_SETTINGS_SCREEN)
        }
        HorizontalDivider()
    }

    @Composable
    private fun DrawLauncherSettings(){
        DrawTitleText(stringResource(R.string.psydoom_game_options_title))

        SwitchPreferenceItem(stringResource(R.string.psydoom_record_demos),
            preferencesStorage.recordDemos, preferencesStorage.recordDemosPrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(stringResource(R.string.psydoom_force_pistol_start),
            preferencesStorage.forcePistolStart, preferencesStorage.forcePistolStartPrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(stringResource(R.string.psydoom_force_turbo_mode),
            preferencesStorage.turboMode, preferencesStorage.turboModePrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(stringResource(R.string.psydoom_force_no_monsters),
            preferencesStorage.noMonsters, preferencesStorage.noMonstersPrefsKey.name)

        HorizontalDivider()

        SwitchPreferenceItem(stringResource(R.string.psydoom_nm_boss_fixup),
            preferencesStorage.nmBossFixUp, preferencesStorage.nmBossFixUpPrefsKey.name)

        HorizontalDivider()

        DrawNetworkSettings()
    }

    @Composable
    private fun DrawNetworkSettings (){
        DrawTitleText(stringResource(R.string.psydoom_network_settings_title))

        EditTextPreferenceItem(stringResource(R.string.psydoom_host),
            preferencesStorage.host, preferencesStorage.hostPrefsKey.name)

        HorizontalDivider()

        val port by preferencesStorage.port.collectAsState(initial = 0)

        EditTextPreferenceItem(stringResource(R.string.psydoom_port),
            port.toString()){
            val port = it.toIntOrNull() ?: 0
            preferencesStorage.setIntValue(preferencesStorage.portPrefsKey, port)
        }

        HorizontalDivider()

        val peerType by preferencesStorage.peerType.collectAsState(initial = PeerType.Client.toString())

        ListPreferenceItem(stringResource(R.string.psydoom_peer_type),
            peerType,
            enumValues<PeerType>().map { it.toString() }.toList()){
            preferencesStorage.setStringValue(preferencesStorage.peerTypePrefsKey, it)
        }

        HorizontalDivider()
    }

    data class PsyDoomLauncherSettingsScreen (private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        PsyDoomSettingScreen (LAUNCHER_SETTINGS_SCREEN){

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawLauncherSettings()
    }

   data class PsyDoomMoreSettingsScreen (private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        PsyDoomSettingScreen (MORE_SETTINGS_SCREEN){

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawMoreSettings(navController)
    }

    private companion object{
        private const val LAUNCHER_SETTINGS_SCREEN = "launcher_settings_screen"
        private const val MORE_SETTINGS_SCREEN = "more_settings_screen"
    }
}


