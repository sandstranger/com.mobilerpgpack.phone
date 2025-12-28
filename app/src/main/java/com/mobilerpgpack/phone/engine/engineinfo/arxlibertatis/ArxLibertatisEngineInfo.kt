package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class ArxLibertatisEngineInfo (mainEngineLib: String, allLibs: Array<String>):
    SDL2EngineInfo(mainEngineLib, allLibs, EngineTypes.ArxLibertatis) {

    private val arxPreferenceStorage by inject <ArxLibertatisPreferenceStorage> (
        named(EngineTypes.ArxLibertatis.name))

    override val pathToResource: String get() = arxPreferenceStorage.pathToArxFatalisFolder
}