package com.mobilerpgpack.phone.engine.engineinfo.fteqw

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo

class FTEQWEngineInfo(mainEngineLib: String, allLibs: Array<String>) :
    SDL2EngineInfo(mainEngineLib, allLibs, EngineTypes.FTEQW){

    override val pathToResource = ""
}