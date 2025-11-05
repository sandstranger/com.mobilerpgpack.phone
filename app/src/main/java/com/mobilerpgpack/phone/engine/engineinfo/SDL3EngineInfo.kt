package com.mobilerpgpack.phone.engine.engineinfo

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.activity.SDL3GameActivity
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import kotlinx.coroutines.flow.Flow

abstract class SDL3EngineInfo (private val mainEngineLib: String,
                      private val allLibs : Array<String>,
                      private val buttonsToDraw : Collection<ButtonState>,
                      private val activeEngineType : EngineTypes) :
    EngineInfo(mainEngineLib, allLibs, buttonsToDraw, activeEngineType ) {

    override val gameActivityClazz: Class<*> = SDL3GameActivity::class.java

    override fun loadControlsLayout() {}
}