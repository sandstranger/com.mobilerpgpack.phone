package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class ArxLibertatisPreferenceStorage : PreferencesStorage() {
    val pathToArxFatalisFolderPrefsKey = stringPreferencesKey("path_to_arx_fatalis_folder")
    val arxLibertatisCommandLineArgsPrefsKey = stringPreferencesKey("arx_fatalis_command_line_args")

    val pathToArxFatalisFolder get() = getStringValue(pathToArxFatalisFolderPrefsKey)
    val arxLibertatisCommandLineArgs get() = getStringValue(arxLibertatisCommandLineArgsPrefsKey)
}
