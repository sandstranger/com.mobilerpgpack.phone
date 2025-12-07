package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope

class UZDoomPreferenceStorage (context: Context, scope: CoroutineScope) :
    PreferencesStorage(context, scope) {

    val enableLightShadersPrefsKey = booleanPreferencesKey("enable_uzdoom_light_shaders")

    val pathToUZDoomIWadFilePrefsKey = stringPreferencesKey("path_to_uzdoom_iwad_file")

    val uZDoomCommandLineArgsPrefsKey = stringPreferencesKey("uzdoom_command_line_args")

    val pathToUZDoomIWadFile = getStringValue(pathToUZDoomIWadFilePrefsKey)

    val enableLightShaders = getBooleanValue(enableLightShadersPrefsKey)

    val uZDoomCommandLineArgsString = getStringValue(uZDoomCommandLineArgsPrefsKey)
}