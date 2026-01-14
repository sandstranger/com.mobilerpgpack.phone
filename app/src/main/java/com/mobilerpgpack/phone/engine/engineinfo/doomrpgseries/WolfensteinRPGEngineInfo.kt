package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import com.mobilerpgpack.phone.engine.EngineTypes

class WolfensteinRPGEngineInfo(mainEngineLib: String, allLibs: Array<String>) :
    DoomRPGSeriesEngineInfo(mainEngineLib, allLibs, EngineTypes.WolfensteinRpg) {

    override val pathToResource: String get() = preferencesStorage.pathToWolfensteinRpgIpaFile.value!!
}