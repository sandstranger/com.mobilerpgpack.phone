package com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class VanillaConquerComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val prefsStorage : VanillaConquerPreferencesStorage = koinInject(named(EngineTypes.VanillaConquer.name))
        prefsStorage.run {
            val activeVanillaConquerGame = prefsStorage.activeVanillaConquerGame.getComposableValue(
                VanillaConquerGames.DefaultGame)
            ListPreferenceItem(stringResource(R.string.vanilla_conquer_active_game),
                activeVanillaConquerGame){
                setEnumValue(activeVanillaConquerGamePrefsKey,it)
            }
            DrawHorizontalDivider()
            when (activeVanillaConquerGame) {
                VanillaConquerGames.TiberianDawn -> {
                    RequestPath(stringResource(R.string.path_to_tiberian_dawn_resources),
                        pathToTiberianDawnResources,
                        pathToTiberianDawnResourcesPrefsKey)
                }
                VanillaConquerGames.RedAlert -> {
                    RequestPath(stringResource(R.string.path_to_red_alert_resources),
                        pathToRedAlertResources,
                        pathToRedAlertResourcesPrefsKey)
                }
            }
        }
    }
}