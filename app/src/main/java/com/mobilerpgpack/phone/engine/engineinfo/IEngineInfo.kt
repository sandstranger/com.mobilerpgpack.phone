package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.buildFullLibraryName
import com.mobilerpgpack.phone.utils.showErrorDialogBox

interface IEngineInfo {

    val engineType : EngineTypes

    val pathToResourceIsCorrect : Boolean

    val pathToResourceExists : Boolean

    val requiredResourceExtension : String

    val mainLibraryName : String

    val nativeLibraries : Array<String>

    val gameActivityClazz: Class<*>

    val commandLineArgs : Array<String>

    val mouseButtonsEventsCanBeInvoked : Boolean

    suspend fun initialize(activity: ComponentActivity)

    fun loadLayout()

    fun onPause()

    fun onResume()

    fun onNativeLibrariesLoaded() {}

    fun onDestroy()
}

val IEngineInfo.mainSharedObject get() = buildFullLibraryName(this.mainLibraryName)

fun IEngineInfo.isResourceCorrect (activity: Activity, onCloseDialogBox :(()-> Unit)? =null) : Boolean{
    if (!pathToResourceExists) {
        activity.showErrorDialogBox(R.string.resource_not_found_error, onCloseDialogBox)
        return false
    }

    if (!pathToResourceIsCorrect) {
        val errorMessage = activity.getString(R.string.resource_not_correct_error,
            this.requiredResourceExtension)
        activity.showErrorDialogBox(errorMessage, onCloseDialogBox)
        return false
    }

    return true
}