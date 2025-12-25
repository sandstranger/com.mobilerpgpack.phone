package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import com.mobilerpgpack.phone.engine.EngineTypes

class Doom2RPGEngineInfo(mainEngineLib: String, allLibs: Array<String>) :
    DoomRPGSeriesEngineInfo(mainEngineLib, allLibs, EngineTypes.Doom2Rpg) {

    override val pathToResource: String get() = preferencesStorage.pathToDoom2RpgIpaFile
}