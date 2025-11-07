package com.mobilerpgpack.phone.engine.engineinfo

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.flow.Flow

class Doom2RpgEngineInfo(private val mainEngineLib: String,
    private val allLibs: Array<String>,
    private val buttonsToDraw: Collection<IScreenControlsView>) :
    DoomRPGSeriesEngineInfo(mainEngineLib, allLibs, buttonsToDraw) {

    override val pathToResource: Flow<String> = preferencesStorage.pathToDoom2RpgIpaFile

    override val engineType: EngineTypes = EngineTypes.Doom2Rpg
}

