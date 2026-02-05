package com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
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

            DrawCommandLinePreferences(vanillaConquerCommandLineArgs,
                vanillaConquerCommandLineArgsPrefsKey.name)

            DrawHorizontalDivider()

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
            DrawHorizontalDivider()

            SwitchPreferenceItem(stringResource(R.string.psydoom_enable_vsync),
                vanillaConquerEnableVsync,vanillaConquerEnableVsyncPrefsKey.name)

            DrawHorizontalDivider()

            SwitchPreferenceItem(stringResource(R.string.enable_dos_mode),
                enableDosMode,enableDosModePrefsKey.name)

            DrawHorizontalDivider()

            EditTextItem(stringResource(R.string.framerate_limit), vanillaConquerFrameRateLimit){
                setIntValue(vanillaConquerFrameRateLimitPrefsKey, it.coerceAtLeast(10))
            }

            DrawHorizontalDivider()

            EditTextItem(stringResource(R.string.mouse_sensitivity), vanillaConquerMouseSensitivity){
                setIntValue(vanillaConquerMouseSensitivityPrefsKey, it.coerceAtLeast(10))
            }

            DrawHorizontalDivider()

            EditTextItem(stringResource(R.string.controller_pointer_speed), vanillaConquerControllerPointerSpeed){
                setIntValue(vanillaConquerControllerPointerSpeedPrefsKey, it.coerceAtLeast(1))
            }
        }
    }
}