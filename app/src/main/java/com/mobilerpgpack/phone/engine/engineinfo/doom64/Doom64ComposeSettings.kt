package com.mobilerpgpack.phone.engine.engineinfo.doom64

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.DrawModsSupport
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named

class Doom64ComposeSettings () :
    KoinComponent, IEngineUIController {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val preferencesStorage : PreferencesStorage = koinInject()
        val modsModel : ModsModel = koinInject (named(EngineTypes.Doom64ExPlus.name))
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

        val enableDoom64Mods = preferencesStorage.enableDoom64Mods.getComposableValue()

        SwitchPreferenceItem(
            stringResource(R.string.enable_doom64_mods),
            initialValue = preferencesStorage.enableDoom64Mods,
            preferencesStorage.enableDoom64ModsPrefsKey.name
        )

        if (enableDoom64Mods) {
            DrawHorizontalDivider()

            RequestPath(
                stringResource(R.string.path_to_doom64_mods_folder),
                preferencesStorage.pathToDoom64ModsFolder,
            ){ selectedPath ->
                 preferencesStorage.setPathToDoom64ModsFolder(selectedPath)
            }
        }
    }
}