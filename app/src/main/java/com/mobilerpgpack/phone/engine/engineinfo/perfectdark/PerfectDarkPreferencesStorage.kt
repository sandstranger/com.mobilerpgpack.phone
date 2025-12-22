package com.mobilerpgpack.phone.engine.engineinfo.perfectdark

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class PerfectDarkPreferencesStorage : PreferencesStorage() {

    val pathToNTSCRomPrefsKey = stringPreferencesKey("path_to_ntsc_rom")
    val pathToPalRomPrefsKey = stringPreferencesKey("path_to_pal_rom")
    val pathToJpnRomPrefsKey = stringPreferencesKey("path_to_jpn_rom")


}