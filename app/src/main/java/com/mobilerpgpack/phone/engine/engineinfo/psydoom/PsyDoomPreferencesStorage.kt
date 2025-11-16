package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mobilerpgpack.phone.utils.PreferencesStorage

class PsyDoomPreferencesStorage (context : Context) : PreferencesStorage(context) {

    val pathToPsyDoomResourcesPrefsKey = stringPreferencesKey("path_to_psydoom_resources")

    val psyDoomCommandLineArgsPrefsKey = stringPreferencesKey("pdydoom_command_line_args")

    val pathToPsyDoomResources get() = getStringValue(pathToPsyDoomResourcesPrefsKey)

    val psyDoomCommandLineArgsString get() = getStringValue(psyDoomCommandLineArgsPrefsKey)
}