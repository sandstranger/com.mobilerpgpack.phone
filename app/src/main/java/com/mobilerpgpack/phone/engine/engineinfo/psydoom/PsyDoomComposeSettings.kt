package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import CustomTopBar
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.getBackgroundColor
import com.mobilerpgpack.phone.ui.getTextColor
import com.mobilerpgpack.phone.ui.getTopBarColor
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.SetupNavigationBar
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.ComposeScreen
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

        PreferenceItem("More laucnher settings"){
            navController.navigate(LAUNCHER_SETTINGS_SCREEN)
        }
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

    data class PsyDoomLauncherSettings (private val composeSettings: PsyDoomComposeSettings) :
        ComposeScreen(LAUNCHER_SETTINGS_SCREEN){

        @Composable
        override fun DrawScreenContent(
            innerPadding: PaddingValues,
            navController: NavHostController,
            backgroundColor: Color,
            textColor: Color,
            isSystemInDarkTheme: Boolean
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)){
                composeSettings.DrawLauncherSettings()
            }
        }
    }

    private companion object{
        private const val LAUNCHER_SETTINGS_SCREEN = "launcher_settings"
    }
}

