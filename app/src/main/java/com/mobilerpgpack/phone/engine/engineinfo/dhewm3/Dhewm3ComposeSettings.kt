package com.mobilerpgpack.phone.engine.engineinfo.dhewm3

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

class Dhewm3ComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val prefsStorage : Dhewm3PreferenceStorage = koinInject(
            named(EngineTypes.Dhewm3.name))
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