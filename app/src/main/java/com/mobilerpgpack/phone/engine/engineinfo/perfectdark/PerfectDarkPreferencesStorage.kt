package com.mobilerpgpack.phone.engine.engineinfo.perfectdark

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.enumPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class PerfectDarkPreferencesStorage : PreferencesStorage() {
    val pathToNTSCRomPrefsKey = stringPreferencesKey("path_to_ntsc_rom")
    val pathToPalRomPrefsKey = stringPreferencesKey("path_to_pal_rom")
    val pathToJpnRomPrefsKey = stringPreferencesKey("path_to_jpn_rom")
    val skipIntroCutScenesPrefsKey = booleanPreferencesKey("skip_perfect_dark_intro")
    val romVersionPrefsKey = enumPreferencesKey<PerfectDarkRomVersions> ("perfect_dark_rom_version")
    val enablePerfectDarkModsSupportPrefsKey = booleanPreferencesKey("enable_perfect_dark_roms_support")
    val pathToPerfectDarkModsFolderPrefsKey = stringPreferencesKey("path_to_perfect_dark_mods_folder")
    val commandLineArgsPrefsKey = stringPreferencesKey("perfect_dark_command_line_args")

    val commandLineArgs get() = getStringValue(commandLineArgsPrefsKey)
    val pathToNTSCRom get() = getStringValue(pathToNTSCRomPrefsKey)
    val pathToPalRom get() = getStringValue(pathToPalRomPrefsKey)
    val pathToJpnRom get() = getStringValue(pathToJpnRomPrefsKey)
    val pathToPerfectDarkModsFolder get() = getStringValue(pathToPerfectDarkModsFolderPrefsKey)
    val enablePerfectDarkModsSupport get() = getBooleanValue(enablePerfectDarkModsSupportPrefsKey, false)
    val skipIntroCutScenes get() = getBooleanValue(skipIntroCutScenesPrefsKey, true)
    val romVersion get() = getEnumValue(romVersionPrefsKey, PerfectDarkRomVersions::class.java,
        PerfectDarkRomVersions.DefaultRomType)
}