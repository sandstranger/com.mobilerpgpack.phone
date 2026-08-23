package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.enumPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class UZDoomPreferenceStorage : PreferencesStorage() {
    val enableLightShadersPrefsKey = booleanPreferencesKey("enable_uzdoom_light_shaders")
    val pathToUZDoomIWadFilePrefsKey = stringPreferencesKey("path_to_uzdoom_iwad_file")
    val uZDoomCommandLineArgsPrefsKey = stringPreferencesKey("uzdoom_command_line_args")
    val uzDoomGLESVersionPrefsKey = stringPreferencesKey("uzdoom_new_gles_version")
    val enableSpirvCrossPrefsKey = booleanPreferencesKey("enable_spirv_cross")
    val uzDoomEngineVersionPrefsKey = enumPreferencesKey<UZDoomEngineVersions>(
        "uzdoom_engine_version")

    val enableSpirvCross = getBooleanValue(enableSpirvCrossPrefsKey);
    val pathToUZDoomIWadFile = getStringValue(pathToUZDoomIWadFilePrefsKey)
    val enableLightShaders = getBooleanValue(enableLightShadersPrefsKey)
    val uzDoomGLESVersion = getStringValue(
        uzDoomGLESVersionPrefsKey,
        UZDoomGLESVersion.OpenGLES_3_0.toString()
    )
    val uZDoomCommandLineArgsString = getStringValue(uZDoomCommandLineArgsPrefsKey)
    val uzDoomEngineVersion= getEnumValue(uzDoomEngineVersionPrefsKey,
        UZDoomEngineVersions::class.java, UZDoomEngineVersions.Dev)
}