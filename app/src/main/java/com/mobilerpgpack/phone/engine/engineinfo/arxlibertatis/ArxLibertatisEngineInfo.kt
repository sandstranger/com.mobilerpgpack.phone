package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo

class ArxLibertatisEngineInfo (mainEngineLib: String, allLibs: Array<String>): SDL2EngineInfo(mainEngineLib, allLibs,
    EngineTypes.ArxLibertatis) {

    override val pathToResource: String
        get() = TODO("Not yet implemented")
}