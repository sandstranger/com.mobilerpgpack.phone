package com.mobilerpgpack.phone.engine.engineinfo

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.flow.Flow
import java.io.File

class Doom64EnhancedEngineInfo(mainEngineLib: String,
                               allLibs: Array<String>,
                               buttonsToDraw: Collection<IScreenControlsView>,
                               commandLineParamsFlow : Flow<String>
) : Doom64EngineInfo(mainEngineLib,allLibs, buttonsToDraw,commandLineParamsFlow) {

    override val engineType: EngineTypes = EngineTypes.Doom64ExPlusEnhanced

    override fun getPathToDoom64UserFolder() =
        pathToRootUserFolder + File.separator + "doom64ex-plus-enhanced" + File.separator

}