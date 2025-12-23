package com.mobilerpgpack.phone.engine.engineinfo.perfectdark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class PerfectDarkComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val preferencesStorage : PerfectDarkPreferencesStorage =
            koinInject(named(EngineTypes.PerfectDark.name))
        val engineInfo : IEngineInfo = koinInject(named(EngineTypes.PerfectDark.name))
        val requiredFileExtensions = rememberSaveable {engineInfo.requiredResourceExtensions}
        var romType by rememberSaveable(preferencesStorage.romType) {
            mutableStateOf(preferencesStorage.romType)
        }
        var enableModsSupport by rememberSaveable(preferencesStorage.enablePerfectDarkModsSupport) {
            mutableStateOf(preferencesStorage.enablePerfectDarkModsSupport)
        }

        ListPreferenceItem(stringResource(R.string.perfect_dark_rom_type),
            romType){
            preferencesStorage.setEnumValue(preferencesStorage.romTypePrefsKey,it)
            romType = it
        }

        DrawHorizontalDivider()

        when (romType) {
            PerfectDarkRomTypes.NTSC -> {
                RequestPath(stringResource(R.string.path_to_ntsc_perfect_dark_rom),
                    preferencesStorage.pathToNTSCRom,
                    preferencesStorage.pathToNTSCRomPrefsKey, requestMode = RequestPathMode.File,
                    requiredFileExtensions = requiredFileExtensions)
            }
            PerfectDarkRomTypes.PAL -> {
                RequestPath(stringResource(R.string.path_to_pal_perfect_dark_rom),
                    preferencesStorage.pathToPalRom,
                    preferencesStorage.pathToPalRomPrefsKey, requestMode = RequestPathMode.File,
                    requiredFileExtensions = requiredFileExtensions)
            }
            PerfectDarkRomTypes.JPN -> {
                RequestPath(stringResource(R.string.path_to_jpn_perfect_dark_rom),
                    preferencesStorage.pathToJpnRom,
                    preferencesStorage.pathToJpnRomPrefsKey, requestMode = RequestPathMode.File,
                    requiredFileExtensions = requiredFileExtensions)
            }
        }

        DrawHorizontalDivider()

        SwitchPreferenceItem(stringResource(R.string.enable_mods_support),
            enableModsSupport, preferencesStorage.enablePerfectDarkModsSupportPrefsKey.name){
            enableModsSupport = it
        }

        DrawHorizontalDivider()

        if (enableModsSupport){
            RequestPath(stringResource(R.string.path_to_common_mods_folder),
                preferencesStorage.pathToPerfectDarkModsFolder,
                preferencesStorage.pathToPerfectDarkModsFolderPrefsKey, requestMode = RequestPathMode.Directory)
            DrawHorizontalDivider()
        }

        SwitchPreferenceItem(stringResource(R.string.skip_perfect_dark_intro_cutscenes),
            preferencesStorage.skipIntroCutScenes,
            preferencesStorage.skipIntroCutScenesPrefsKey.name)
    }
}