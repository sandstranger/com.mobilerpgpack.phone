package com.mobilerpgpack.phone.engine.engineinfo.openxray

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.enumPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class OpenXrayPreferencesStorage : PreferencesStorage() {
    val pathToClearSkyResourcesPrefsKey = stringPreferencesKey("path_to_clear_sky_resources")
    val pathToCallOfPripyatResourcesPrefsKey = stringPreferencesKey("path_to_call_of_pripyat_resources")
    val activeOpenXrayGamePrefsKey = enumPreferencesKey<OpenXrayGames>("active_open_xray_game")
    val openXrayCommandLineArgsPrefsKey = stringPreferencesKey("open_xray_command_line_args")

    val pathToClearSkyResources = getStringValue(pathToClearSkyResourcesPrefsKey)
    val pathToCallOfPripyatResources = getStringValue(pathToCallOfPripyatResourcesPrefsKey)
    val activeOpenXrayGame = getEnumValue(activeOpenXrayGamePrefsKey,
        OpenXrayGames::class.java, OpenXrayGames.DefaultGame)
    val openXrayCommandLineArgs = getStringValue(openXrayCommandLineArgsPrefsKey)
}