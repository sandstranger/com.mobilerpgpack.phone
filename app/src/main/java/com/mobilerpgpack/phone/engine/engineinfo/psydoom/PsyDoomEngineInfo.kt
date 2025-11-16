package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.SDL2EngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.PsyDoomPreferencesStorage
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class PsyDoomEngineInfo (
    mainEngineLib: String,
    allLibs: Array<String>,
    buttonsToDraw: Collection<IScreenControlsView>) :
    SDL2EngineInfo (mainEngineLib, allLibs, buttonsToDraw, EngineTypes.PsyDoom) {

    private val psyDoomPreferencesStorage by inject <PsyDoomPreferencesStorage>(named(
        EngineTypes.PsyDoom.toString()))

    override val preferencesStorage: PreferencesStorage = psyDoomPreferencesStorage

    override val commandLineParamsFlow: Flow<String> get() = psyDoomPreferencesStorage.psyDoomCommandLineArgsString

    override val pathToResource: Flow<String> get() = psyDoomPreferencesStorage.pathToPsyDoomResources
}