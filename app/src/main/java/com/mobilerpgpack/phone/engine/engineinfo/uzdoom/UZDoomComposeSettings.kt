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
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.psyDoomButtons
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.getValue

class UZDoomComposeSettings: IEngineUIController, KoinComponent {

    private val lzDoomEngineInfo : IEngineInfo by inject (named(EngineTypes.UZDoom.toString()))

    private val preferencesStorage: UZDoomPreferenceStorage by inject(
        named(EngineTypes.UZDoom.toString()))

    override val screenViewsToDraw: Collection<IScreenControlsView> = psyDoomButtons

    @Composable
    override fun DrawSettings(navController: NavHostController) {
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
            requiredFileExtensions = arrayListOf(lzDoomEngineInfo.requiredResourceExtension))

        HorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.enable_lzdoom_mods),
            initialValueFlow = preferencesStorage.enableUZDoomMods,
            preferencesStorage.enableUZDoomModsPrefsKey.name)

        val enableMods by preferencesStorage.enableUZDoomMods.collectAsState(initial = false)

        if (enableMods) {
            HorizontalDivider()
            RequestPath(
                stringResource(R.string.path_to_uzdoom_mods_folder),
                preferencesStorage.pathToUZDoomModsFolder,
                preferencesStorage.pathToUZDoomModsFolderPrefsKey
            )
        }
    }
}