package com.mobilerpgpack.phone.engine.engineinfo.lzdoom

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope

class LZDoomPreferenceStorage (context: Context, scope: CoroutineScope) :
    PreferencesStorage(context, scope) {

    val pathToLZDoomIWadFilePrefsKey = stringPreferencesKey("path_to_lzdoom_iwad_file")

    val pathToLZDoomModsFolderPrefsKey = stringPreferencesKey("path_to_lzdoom_mods_folder")

    val lZDoomCommandLineArgsPrefsKey = stringPreferencesKey("lzdoom_command_line_args")

    val enableLZDoomModsPrefsKey = booleanPreferencesKey("enable_lzdoom_mods")

    val pathToLZDoomIWadFile = getStringValue(pathToLZDoomIWadFilePrefsKey)

    val pathToLZDoomModsFolder = getStringValue(pathToLZDoomModsFolderPrefsKey)

    val lZDoomCommandLineArgsString = getStringValue(lZDoomCommandLineArgsPrefsKey)

    val enableLZDoomMods = getBooleanValue(enableLZDoomModsPrefsKey)
}