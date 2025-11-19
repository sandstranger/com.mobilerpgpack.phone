package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope

class PsyDoomPreferencesStorage (context : Context, scope : CoroutineScope) : PreferencesStorage(context, scope) {

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

    val pathToPsyDoomCueFile get() = getStringValue(pathToPsyDoomCueFilePrefsKey)

    val recordDemos get() = getBooleanValue(recordDemosPrefsKey)

    val forcePistolStart get() = getBooleanValue(forcePistolStartPrefsKey)

    val turboMode get() = getBooleanValue(turboModePrefsKey)

    val noMonsters get() = getBooleanValue(noMonstersPrefsKey)

    val nmBossFixUp get() = getBooleanValue(nmBossFixUpPrefsKey)

    val host get() = getStringValue(hostPrefsKey)

    val port get() = getIntValue(portPrefsKey, 0)

    val peerType get() = getStringValue(peerTypePrefsKey,PeerType.Client.toString())

    val pathToPsyDoomModsFolder get() = getStringValue(pathToPsyDoomModsFolderPrefsKey)

    val psyDoomCommandLineArgsString get() = getStringValue(psyDoomCommandLineArgsPrefsKey)

    val enablePsyDoomMods get() = getBooleanValue(enablePsyDoomModsPrefsKey)
}