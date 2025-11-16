package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.main.FREETYPE_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.PSYDOOM_MAIN_ENGINE_LIB
import com.mobilerpgpack.phone.main.SDL2_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.ui.screen.screencontrols.wolfensteinButtons
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class PsyDoomEngineInfo : SDL2EngineInfo (activeEngineType = EngineTypes.PsyDoom) {

    private val psyDoomPreferencesStorage by inject <PsyDoomPreferencesStorage>(named(
        EngineTypes.PsyDoom.toString()))

    override val preferencesStorage = psyDoomPreferencesStorage

    override val commandLineParamsFlow get() = psyDoomPreferencesStorage.psyDoomCommandLineArgsString

    override val pathToResource get() = psyDoomPreferencesStorage.pathToPsyDoomResources

    override val mainEngineLib = PSYDOOM_MAIN_ENGINE_LIB

    override val allLibs = arrayOf(FREETYPE_NATIVE_LIB_NAME, SDL2_NATIVE_LIB_NAME, PSYDOOM_MAIN_ENGINE_LIB)

    override val needToShowScreenControls = true

    override val mouseButtonsEventsCanBeInvoked = false

    override val buttonsToDraw = wolfensteinButtons

    override fun isMouseShown() = false
}