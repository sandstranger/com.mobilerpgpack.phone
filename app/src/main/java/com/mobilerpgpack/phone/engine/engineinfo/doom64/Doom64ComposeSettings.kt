package com.mobilerpgpack.phone.engine.engineinfo.doom64

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.DrawModsSupport
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class Doom64ComposeSettings () :
    KoinComponent, IEngineUIController {

    private val scope : CoroutineScope by inject(named(KoinModulesProvider.COROUTINES_SCOPE))

    private val preferencesStorage : PreferencesStorage by inject()

    private val modsModel : ModsModel by inject (named(EngineTypes.Doom64ExPlus.toString()))

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val previousPathToDoom64WadsFolder = preferencesStorage.pathToDoom64MainWadsFolder
        DrawCommandLinePreferences(preferencesStorage.doom64CommandLineArgsString,
            preferencesStorage.doom64CommandLineArgsStringPrefsKey.name)

        DrawHorizontalDivider()

        RequestPath(
            stringResource(R.string.path_to_doom64_folder),
            previousPathToDoom64WadsFolder) { selectedPath ->
            preferencesStorage.setPathToDoom64MainWadsFolder(selectedPath)
        }

        DrawModsSupport(modsModel)

        val enableDoom64Mods = preferencesStorage.enableDoom64Mods

        SwitchPreferenceItem(
            stringResource(R.string.enable_doom64_mods),
            initialValue = enableDoom64Mods,
            preferencesStorage.enableDoom64ModsPrefsKey.name
        )

        val previousPathToDoom64ModsFolder = preferencesStorage.pathToDoom64ModsFolder

        if (enableDoom64Mods) {
            DrawHorizontalDivider()

            RequestPath(
                stringResource(R.string.path_to_doom64_mods_folder),
                previousPathToDoom64ModsFolder,
            ){ selectedPath ->
                 preferencesStorage.setPathToDoom64ModsFolder(selectedPath)
            }
        }
    }
}