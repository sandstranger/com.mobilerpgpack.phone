package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mobilerpgpack.phone.utils.PreferencesStorage

class PsyDoomPreferencesStorage (context : Context) : PreferencesStorage(context) {

    val pathToPsyDoomCueFilePrefsKey = stringPreferencesKey("path_to_psydoom_cue_file")

    val pathToPsyDoomModsFolderPrefsKey = stringPreferencesKey("path_to_psydoom_mods_folder")

    val psyDoomCommandLineArgsPrefsKey = stringPreferencesKey("pdydoom_command_line_args")

    val pathToPsyDoomCueFile get() = getStringValue(pathToPsyDoomCueFilePrefsKey)

    val pathToPsyDoomModsFolder get() = getStringValue(pathToPsyDoomModsFolderPrefsKey)

    val psyDoomCommandLineArgsString get() = getStringValue(psyDoomCommandLineArgsPrefsKey)
}