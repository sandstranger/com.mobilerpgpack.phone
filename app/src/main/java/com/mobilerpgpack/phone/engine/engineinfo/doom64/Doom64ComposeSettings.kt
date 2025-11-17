package com.mobilerpgpack.phone.engine.engineinfo.doom64

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class Doom64ComposeSettings (override val screenViewsToDraw: Collection<IScreenControlsView>) :
    KoinComponent, IEngineUIController {

    private val scope : CoroutineScope by inject(named(KoinModulesProvider.COROUTINES_SCOPE))

    private val preferencesStorage : PreferencesStorage by inject()

    @Composable
    override fun DrawSettings() {
        val previousPathToDoom64WadsFolder by preferencesStorage.pathToDoom64MainWadsFolder
            .collectAsState(initial = "")
        RequestPath(
            stringResource(R.string.path_to_doom64_folder),
            previousPathToDoom64WadsFolder) { selectedPath ->
            scope.launch { preferencesStorage.setPathToDoom64MainWadsFolder(selectedPath) }
        }

        HorizontalDivider()

        DrawCommandLinePreferences(preferencesStorage.doom64CommandLineArgsString,
            preferencesStorage.doom64CommandLineArgsStringPrefsKey.name)

        HorizontalDivider()

        val enableDoom64ModsFlow = preferencesStorage.enableDoom64Mods
        val enableDoom64Mods by enableDoom64ModsFlow.collectAsState(initial = false)

        SwitchPreferenceItem(
            stringResource(R.string.enable_doom64_mods),
            initialValue = enableDoom64Mods,
            preferencesStorage.enableDoom64ModsPrefsKey.name
        )

        val previousPathToDoom64ModsFolder by preferencesStorage.pathToDoom64ModsFolder
            .collectAsState(initial = "")

        if (enableDoom64Mods) {
            HorizontalDivider()

            RequestPath(
                stringResource(R.string.path_to_doom64_mods_folder),
                previousPathToDoom64ModsFolder
            ){ selectedPath ->
                scope.launch { preferencesStorage.setPathToDoom64ModsFolder(selectedPath) }
            }
        }
    }
}