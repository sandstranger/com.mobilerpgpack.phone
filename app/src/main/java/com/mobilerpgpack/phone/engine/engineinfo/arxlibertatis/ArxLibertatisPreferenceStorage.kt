package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class ArxLibertatisPreferenceStorage : PreferencesStorage() {
    val pathToArxFatalisFolderPrefsKey = stringPreferencesKey("path_to_arx_fatalis_folder")
    val arxLibertatisCommandLineArgsPrefsKey = stringPreferencesKey("arx_fatalis_command_line_args")
    val enableEtc2TextureSupportPrefsKey = booleanPreferencesKey("enable_etc2_texture_support")

    val enableEtc2TextureSupport = getBooleanValue(enableEtc2TextureSupportPrefsKey, true)
    val pathToArxFatalisFolder = getStringValue(pathToArxFatalisFolderPrefsKey)
    val arxLibertatisCommandLineArgs = getStringValue(arxLibertatisCommandLineArgsPrefsKey)
}
