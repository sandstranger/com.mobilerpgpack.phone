package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import androidx.viewbinding.ViewBinding
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState

interface IEngineInfo {

    val engineType : EngineTypes

    val pathToResource: String

    val mainSharedObject : String

    val nativeLibraries : Array<String>

    val gameActivityClazz: Class<*>

    val screenButtonsToDraw : Collection<ButtonState>

    suspend fun initialize(activity: Activity)

    fun loadControlsLayout()

    fun onPause()

    fun onResume()

    fun onDestroy()
}