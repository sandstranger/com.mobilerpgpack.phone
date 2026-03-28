package com.mobilerpgpack.phone.engine.engineinfo.dhewm3

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class Dhewm3PreferenceStorage : PreferencesStorage() {
    val pathToDoom3ResourcesPreferenceKey = stringPreferencesKey("path_to_doom3_resources")
    val commandLineArgsPrefsKey = stringPreferencesKey("doom3_command_line_args")

    val commandLineArgs = getStringValue(commandLineArgsPrefsKey)
    val pathToDoom3Resources = getStringValue(pathToDoom3ResourcesPreferenceKey)
}