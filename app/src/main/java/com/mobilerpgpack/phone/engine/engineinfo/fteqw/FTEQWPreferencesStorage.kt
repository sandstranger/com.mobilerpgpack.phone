package com.mobilerpgpack.phone.engine.engineinfo.fteqw

import com.mobilerpgpack.phone.engine.engineinfo.perfectdark.PerfectDarkRomVersions
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.enumPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.floatPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class FTEQWPreferencesStorage : PreferencesStorage() {
    val pathToQuake1PrefsKey = stringPreferencesKey("path_to_quake1")
    val pathToQuake1BaseDirPrefsKey = stringPreferencesKey("path_to_quake1_base_dir")
    val pathToQuake1ModsDirPrefsKey = stringPreferencesKey("path_to_quake1_mods_dir")
    val pathToQuake1ManifestPrefsKey = stringPreferencesKey("path_to_quake1_manifest")
    val pathToQuake2PrefsKey = stringPreferencesKey("path_to_quake2")
    val pathToQuake2BaseDirPrefsKey = stringPreferencesKey("path_to_quake2_base_dir")
    val pathToQuake2ModsDirPrefsKey = stringPreferencesKey("path_to_quake2_mods_dir")
    val pathToQuake2ManifestPrefsKey = stringPreferencesKey("path_to_quake2_manifest")
    val pathToQuake3PrefsKey = stringPreferencesKey("path_to_quake3")
    val pathToQuake3BaseDirPrefsKey = stringPreferencesKey("path_to_quake3_base_dir")
    val pathToQuake3ModsDirPrefsKey = stringPreferencesKey("path_to_quake3_mods_dir")
    val pathToQuake3ManifestPrefsKey = stringPreferencesKey("path_to_quake3_manifest")
    val pathToHexen2PrefsKey = stringPreferencesKey("path_to_hexen2")
    val pathToHexen2BaseDirPrefsKey = stringPreferencesKey("path_to_hexen2_base_dir")
    val pathToHexen2ModsDirPrefsKey = stringPreferencesKey("path_to_hexen2_mods_dir")
    val pathToHexen2ManifestPrefsKey = stringPreferencesKey("path_to_hexen2_manifest")
    val commandLineArgsPrefsKey = stringPreferencesKey("fteqw_command_line_args")
    val enableFTEQWModsPrefsKey = booleanPreferencesKey("enable_fteqw_mods")
    val enableManifestSupportPrefsKey = booleanPreferencesKey("enable_fteqw_manifest")
    val activeFTEQWGamePrefsKey = enumPreferencesKey<FTEQWGames>("active_fteqw_game")
    val fteqwUIScalePrefsKey = floatPreferencesKey("fteqw_ui_scale")
    val quake2GameTypePrefsKey = enumPreferencesKey<Quake2Games>("quake2_game_type")

    val pathToQuake1 = getStringValue(pathToQuake1PrefsKey)
    val pathToQuake1BaseDir = getStringValue(pathToQuake1BaseDirPrefsKey)
    val pathToQuake1ModsDir = getStringValue(pathToQuake1ModsDirPrefsKey)
    val pathToQuake1Manifest = getStringValue(pathToQuake1ManifestPrefsKey)
    val quake2GameType = getEnumValue(quake2GameTypePrefsKey, Quake2Games::class.java,
        Quake2Games.DefaultGame)
    val pathToQuake2 = getStringValue(pathToQuake2PrefsKey)
    val pathToQuake2BaseDir = getStringValue(pathToQuake2BaseDirPrefsKey)
    val pathToQuake2ModsDir = getStringValue(pathToQuake2ModsDirPrefsKey)
    val pathToQuake2Manifest = getStringValue(pathToQuake2ManifestPrefsKey)

    val pathToQuake3 = getStringValue(pathToQuake3PrefsKey)
    val pathToQuake3BaseDir = getStringValue(pathToQuake3BaseDirPrefsKey)
    val pathToQuake3ModsDir = getStringValue(pathToQuake3ModsDirPrefsKey)
    val pathToQuake3Manifest = getStringValue(pathToQuake3ManifestPrefsKey)

    val pathToHexen2 = getStringValue(pathToHexen2PrefsKey)
    val pathToHexen2BaseDir = getStringValue(pathToHexen2BaseDirPrefsKey)
    val pathToHexen2ModsDir = getStringValue(pathToHexen2ModsDirPrefsKey)
    val pathToHexen2Manifest = getStringValue(pathToHexen2ManifestPrefsKey)

    val enableFTEQWModsSupport = getBooleanValue(enableFTEQWModsPrefsKey)
    val enableManifestSupport = getBooleanValue(enableManifestSupportPrefsKey)
    val activeFTEQWGame = getEnumValue(
        activeFTEQWGamePrefsKey, FTEQWGames::class.java,
        FTEQWGames.Quake
    )
    val commandLineArgs = getStringValue(commandLineArgsPrefsKey)
    val fteqwUIScale = getFloatValue(fteqwUIScalePrefsKey, 3.5f)
}