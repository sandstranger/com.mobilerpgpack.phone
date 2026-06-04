package com.mobilerpgpack.phone.engine.engineinfo.doom64

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.flow.Flow
import java.io.File

class Doom64EnhancedEngineInfo(mainEngineLib: String,
                               allLibs: Array<String>
) : Doom64EngineInfo(mainEngineLib,allLibs) {

    override val engineType: EngineTypes = EngineTypes.Doom64ExPlusEnhanced
    override val gl4esShaderCacheFolderName = "doom64_enhanced_gl4es_cache"
    override fun getPathToDoom64UserFolder() =
        pathToRootUserFolder + File.separator + "doom64ex-plus-enhanced" + File.separator

}