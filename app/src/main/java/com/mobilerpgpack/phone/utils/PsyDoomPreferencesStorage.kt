package com.mobilerpgpack.phone.utils

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey

class PsyDoomPreferencesStorage (context : Context) : PreferencesStorage (context) {

    val pathToPsyDoomResourcesPrefsKey = stringPreferencesKey("path_to_psydoom_resources")

    val psyDoomCommandLineArgsPrefsKey = stringPreferencesKey("pdydoom_command_line_args")

    val pathToPsyDoomResources get() = getStringValue(psyDoomCommandLineArgsPrefsKey)

    val psyDoomCommandLineArgsString get() = getStringValue(psyDoomCommandLineArgsPrefsKey)
}