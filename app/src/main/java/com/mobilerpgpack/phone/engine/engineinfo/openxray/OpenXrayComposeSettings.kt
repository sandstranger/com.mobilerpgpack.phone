package com.mobilerpgpack.phone.engine.engineinfo.openxray

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class OpenXrayComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val prefsStorage : OpenXrayPreferencesStorage = koinInject(
            named(EngineTypes.OpenXRAY.name))
        prefsStorage.apply {
            val activeGame = activeOpenXrayGame.getComposableValue(OpenXrayGames.DefaultGame)

            DrawCommandLinePreferences(openXrayCommandLineArgs,
                openXrayCommandLineArgsPrefsKey.name)
            DrawHorizontalDivider()
            ListPreferenceItem(stringResource(R.string.openxray_active_game),
                activeGame){
                setEnumValue(activeOpenXrayGamePrefsKey,it)
            }
            DrawHorizontalDivider()
            when (activeGame) {
                OpenXrayGames.ClearSky -> {
                    RequestPath(stringResource(R.string.path_to_clear_sky_resources),
                        pathToClearSkyResources,
                        pathToClearSkyResourcesPrefsKey)
                }
                OpenXrayGames.CallOfPripyat -> {
                    RequestPath(stringResource(R.string.path_to_call_of_pripyat_resources),
                        pathToCallOfPripyatResources,
                        pathToCallOfPripyatResourcesPrefsKey)
                }
            }
        }
    }
}