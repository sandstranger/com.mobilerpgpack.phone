package com.mobilerpgpack.phone.engine.engineinfo

import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.buildFullLibraryName
import kotlinx.coroutines.flow.Flow

interface IEngineInfo {

    val engineType : EngineTypes

    val pathToResource: Flow<String>

    val mainLibraryName : String

    val nativeLibraries : Array<String>

    val gameActivityClazz: Class<*>

    val commandLineArgs : Array<String>

    val mouseButtonsEventsCanBeInvoked : Boolean

    suspend fun initialize(activity: ComponentActivity)

    fun loadLayout()

    fun onPause()

    fun onResume()

    fun onDestroy()
}

val IEngineInfo.mainSharedObject get() = buildFullLibraryName(this.mainLibraryName)