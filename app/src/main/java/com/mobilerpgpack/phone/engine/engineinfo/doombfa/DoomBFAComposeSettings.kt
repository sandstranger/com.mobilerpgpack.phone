package com.mobilerpgpack.phone.engine.engineinfo.doombfa

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class DoomBFAComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val prefsStorage : DoomBFAPreferencesStorage = koinInject(
            named(EngineTypes.Classic_RBDOOM_3_BFG.name))
        prefsStorage.apply {
            DrawCommandLinePreferences(commandLineArgs,
                commandLineArgsPrefsKey.name)
            DrawHorizontalDivider()
            RequestPath(stringResource(R.string.path_to_doom3_resources),
                pathToDoom3Resources,
                pathToDoom3ResourcesPreferenceKey)
        }
    }
}