package com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.enumPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.intPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class VanillaConquerPreferencesStorage : PreferencesStorage () {
    val pathToRedAlertResourcesPrefsKey = stringPreferencesKey("path_to_red_alert_resources")
    val pathToTiberianDawnResourcesPrefsKey = stringPreferencesKey("path_to_tiberian_dawn_resources")
    val activeVanillaConquerGamePrefsKey = enumPreferencesKey<VanillaConquerGames>("active_vanilla_conquer_game")
    val vanillaConquerCommandLineArgsPrefsKey = stringPreferencesKey("vanilla_conquer_command_line_args")
    val vanillaConquerEnableVsyncPrefsKey = booleanPreferencesKey("vanilla_conquer_enable_vsync")
    val enableDosModePrefsKey = booleanPreferencesKey("vanilla_conquer_enable_dos_mode")
    val vanillaConquerFrameRateLimitPrefsKey = intPreferencesKey("vanilla_conquer_fps_limit")
    val vanillaConquerMouseSensitivityPrefsKey = intPreferencesKey("vanilla_conquer_mouse_sensitivity")
    val vanillaConquerControllerPointerSpeedPrefsKey = intPreferencesKey("vanilla_conquer_controller_speed")

    val pathToRedAlertResources = getStringValue(pathToRedAlertResourcesPrefsKey)
    val pathToTiberianDawnResources = getStringValue(pathToTiberianDawnResourcesPrefsKey)
    val activeVanillaConquerGame = getEnumValue(activeVanillaConquerGamePrefsKey,
        VanillaConquerGames::class.java, VanillaConquerGames.DefaultGame)

    val vanillaConquerCommandLineArgs = getStringValue(vanillaConquerCommandLineArgsPrefsKey)
    val vanillaConquerEnableVsync = getBooleanValue(vanillaConquerEnableVsyncPrefsKey, true)
    val enableDosMode = getBooleanValue(enableDosModePrefsKey, true)
    val vanillaConquerFrameRateLimit = getIntValue(vanillaConquerFrameRateLimitPrefsKey, 60)
    val vanillaConquerMouseSensitivity = getIntValue(vanillaConquerMouseSensitivityPrefsKey, 70)
    val vanillaConquerControllerPointerSpeed = getIntValue(vanillaConquerControllerPointerSpeedPrefsKey, 10)
}