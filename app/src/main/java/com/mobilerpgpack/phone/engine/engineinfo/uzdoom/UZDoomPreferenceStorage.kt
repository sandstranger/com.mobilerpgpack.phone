package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class UZDoomPreferenceStorage :PreferencesStorage() {

    val enableLightShadersPrefsKey = booleanPreferencesKey("enable_uzdoom_light_shaders")

    val pathToUZDoomIWadFilePrefsKey = stringPreferencesKey("path_to_uzdoom_iwad_file")

    val uZDoomCommandLineArgsPrefsKey = stringPreferencesKey("uzdoom_command_line_args")

    val uzDoomGLESVersionPrefsKey = stringPreferencesKey("uzdoom_gles_version")

    val pathToUZDoomIWadFile get() = getStringValue(pathToUZDoomIWadFilePrefsKey)

    val enableLightShaders get() = getBooleanValue(enableLightShadersPrefsKey)

    val uzDoomGLESVersion get() = getStringValue(uzDoomGLESVersionPrefsKey,
        UZDoomGLESVersion.OpenGLES_2_0.toString())

    val uZDoomCommandLineArgsString get() = getStringValue(uZDoomCommandLineArgsPrefsKey)
}