package com.mobilerpgpack.phone.engine.engineinfo.fteqw

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.enumPreferencesKey
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
    val fteQWRenderTypePrefsKey = enumPreferencesKey<FTEQWRenderTypes>("fteqw_render_type")

    val pathToQuake1 get() = getStringValue(pathToQuake1PrefsKey)
    val pathToQuake1BaseDir get() = getStringValue(pathToQuake1BaseDirPrefsKey)
    val pathToQuake1ModsDir get() = getStringValue(pathToQuake1ModsDirPrefsKey)
    val pathToQuake1Manifest get() = getStringValue(pathToQuake1ManifestPrefsKey)

    val pathToQuake2 get() = getStringValue(pathToQuake2PrefsKey)
    val pathToQuake2BaseDir get() = getStringValue(pathToQuake2BaseDirPrefsKey)
    val pathToQuake2ModsDir get() = getStringValue(pathToQuake2ModsDirPrefsKey)
    val pathToQuake2Manifest get() = getStringValue(pathToQuake2ManifestPrefsKey)

    val pathToQuake3 get() = getStringValue(pathToQuake3PrefsKey)
    val pathToQuake3BaseDir get() = getStringValue(pathToQuake3BaseDirPrefsKey)
    val pathToQuake3ModsDir get() = getStringValue(pathToQuake3ModsDirPrefsKey)
    val pathToQuake3Manifest get() = getStringValue(pathToQuake3ManifestPrefsKey)

    val pathToHexen2 get() = getStringValue(pathToHexen2PrefsKey)
    val pathToHexen2BaseDir get() = getStringValue(pathToHexen2BaseDirPrefsKey)
    val pathToHexen2ModsDir get() = getStringValue(pathToHexen2ModsDirPrefsKey)
    val pathToHexen2Manifest get() = getStringValue(pathToHexen2ManifestPrefsKey)

    val enableFTEQWModsSupport get() = getBooleanValue(enableFTEQWModsPrefsKey)
    val enableManifestSupport get() = getBooleanValue(enableManifestSupportPrefsKey)
    val activeFTEQWGame get() = getEnumValue(activeFTEQWGamePrefsKey, FTEQWGames::class.java,
        FTEQWGames.Quake)
    val commandLineArgs get() = getStringValue(commandLineArgsPrefsKey)
    val fteQWRenderType get() = getEnumValue(fteQWRenderTypePrefsKey,
        FTEQWRenderTypes::class.java, FTEQWRenderTypes.OpenGL_ES)
}