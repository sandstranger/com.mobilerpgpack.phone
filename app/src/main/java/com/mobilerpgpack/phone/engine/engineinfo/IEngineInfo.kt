package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import kotlinx.coroutines.flow.Flow

interface IEngineInfo {

    val engineType : EngineTypes

    val pathToResource: Flow<String>

    val mainSharedObject : String

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