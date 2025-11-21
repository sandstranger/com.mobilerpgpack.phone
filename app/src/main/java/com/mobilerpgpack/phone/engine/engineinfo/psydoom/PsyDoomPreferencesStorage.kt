package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope

class PsyDoomPreferencesStorage(context: Context, scope: CoroutineScope) :
    PreferencesStorage(context, scope) {

    val pathToPsyDoomCueFilePrefsKey = stringPreferencesKey("path_to_psydoom_cue_file")

    val pathToPsyDoomModsFolderPrefsKey = stringPreferencesKey("path_to_psydoom_mods_folder")

    val psyDoomCommandLineArgsPrefsKey = stringPreferencesKey("pdydoom_command_line_args")

    val recordDemosPrefsKey = booleanPreferencesKey("psy_doom_record_demos")

    val forcePistolStartPrefsKey = booleanPreferencesKey("psy_doom_force_pistol_start")

    val turboModePrefsKey = booleanPreferencesKey("psy_doom_turbo_mode")

    val noMonstersPrefsKey = booleanPreferencesKey("psy_doom_no_monsters")

    val nmBossFixUpPrefsKey = booleanPreferencesKey("psy_doom_nm_boss_fix_up")

    val hostPrefsKey = stringPreferencesKey("psy_doom_host")

    val portPrefsKey = intPreferencesKey("psy_doom_port")

    val peerTypePrefsKey = stringPreferencesKey("psy_doom_peer_type")

    val enablePsyDoomModsPrefsKey = booleanPreferencesKey("enable_psydoom_mods")

    val pathToPsyDoomCueFile = getStringValue(pathToPsyDoomCueFilePrefsKey)

    val recordDemos = getBooleanValue(recordDemosPrefsKey)

    val forcePistolStart = getBooleanValue(forcePistolStartPrefsKey)

    val turboMode = getBooleanValue(turboModePrefsKey)

    val noMonsters = getBooleanValue(noMonstersPrefsKey)

    val nmBossFixUp = getBooleanValue(nmBossFixUpPrefsKey)

    val host = getStringValue(hostPrefsKey)

    val port = getIntValue(portPrefsKey, 0)

    val peerType = getStringValue(peerTypePrefsKey, PeerType.Client.toString())

    val pathToPsyDoomModsFolder = getStringValue(pathToPsyDoomModsFolderPrefsKey)

    val psyDoomCommandLineArgsString = getStringValue(psyDoomCommandLineArgsPrefsKey)

    val enablePsyDoomMods = getBooleanValue(enablePsyDoomModsPrefsKey)
}