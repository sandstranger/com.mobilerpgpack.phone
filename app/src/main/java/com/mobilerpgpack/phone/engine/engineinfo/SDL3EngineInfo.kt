package com.mobilerpgpack.phone.engine.engineinfo

import com.mobilerpgpack.phone.engine.activity.SDL3GameActivity
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView

abstract class SDL3EngineInfo (private val mainEngineLib: String,
                      private val allLibs : Array<String>,
                      private val buttonsToDraw : Collection<IScreenControlsView>) :
    EngineInfo(mainEngineLib, allLibs, buttonsToDraw ) {

    override val gameActivityClazz: Class<*> = SDL3GameActivity::class.java

    override fun loadControlsLayout() {}
}