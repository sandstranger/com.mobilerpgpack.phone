package com.mobilerpgpack.phone.engine.engineinfo.lzdoom

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.getValue

class LZDoomEngineInfo (mainEngineLib: String,
                        allLibs: Array<String>,
                        buttonsToDraw: Collection<IScreenControlsView>,
                        commandLineParamsFlow : Flow<String>) :
    SDL2EngineInfo (mainEngineLib, allLibs, buttonsToDraw,activeEngineType = EngineTypes.LZDoom,
        commandLineParamsFlow = commandLineParamsFlow) {

    private val lzDoomPreferencesStorage by inject <LZDoomPreferenceStorage>(named(
        EngineTypes.LZDoom.toString()))

    override val preferencesStorage = lzDoomPreferencesStorage

    override val pathToResource get() = runBlocking{ lzDoomPreferencesStorage.pathToLZDoomIWadFile.first() }

    override val requiredResourceExtension = ".wad"
}