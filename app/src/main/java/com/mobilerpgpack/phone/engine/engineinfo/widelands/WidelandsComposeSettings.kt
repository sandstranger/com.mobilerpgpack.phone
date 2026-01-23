package com.mobilerpgpack.phone.engine.engineinfo.widelands

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.compose.koinInject

class WidelandsComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val preferencesStorage = koinInject<PreferencesStorage>()
        DrawCommandLinePreferences(preferencesStorage.widelandsCommandLineArgs,
            preferencesStorage.widelandsCommandLineArgsPrefsKey.name)
    }
}