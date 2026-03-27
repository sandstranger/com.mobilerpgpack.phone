package com.mobilerpgpack.phone.engine.engineinfo.dhewm3

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.main.DHWEM3_NATIVE_LIB_NAME
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class Dhewm3EngineInfo : SDL2EngineInfo (EngineTypes.Dhewm3) {
    private val preferenceStorage by inject<Dhewm3PreferenceStorage>(named(EngineTypes.Dhewm3.name))

    private val baseNativeLibs by inject <Array<String>>(named(EngineTypes.Dhewm3.name))

    override val pathToResource: String get() = preferenceStorage.pathToDoom3Resources.value!!

    override val mainLibraryName: String = DHWEM3_NATIVE_LIB_NAME

    override val nativeLibraries: Array<String> get() = baseNativeLibs
}