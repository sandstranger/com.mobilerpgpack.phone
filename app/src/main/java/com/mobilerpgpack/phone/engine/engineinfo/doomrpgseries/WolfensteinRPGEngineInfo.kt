package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import com.mobilerpgpack.phone.engine.EngineTypes

class WolfensteinRPGEngineInfo(mainEngineLib: String, allLibs: Array<String>) :
    DoomRPGSeriesEngineInfo(mainEngineLib, allLibs, EngineTypes.WolfensteinRpg) {

    override val gl4esShaderCacheFolderName = "wolfenstein_rpg_gl4es_cache"
    override val pathToResource: String get() = preferencesStorage.pathToWolfensteinRpgIpaFile.value!!
}