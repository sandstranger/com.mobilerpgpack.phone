package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.buildFullLibraryName
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.utils.showErrorDialogBox
import kotlinx.coroutines.flow.Flow

interface IEngineInfo {
    val screenController : IScreenController
    val engineType : EngineTypes
    val engineReadyToStart : Boolean
    val supportRenderChanges : Boolean
    val useGyroscope : Boolean
    val pathToResourceIsCorrect : Boolean
    val pathToResourceExists : Boolean
    val needToReInitGameControllers : Boolean
    val requiredResourceExtensions : Collection<String>
    val mouseButtonsEventsCanBeInvokedAsFlow : Flow<Boolean>
    val mainLibraryName : String
    val callExitProcessOnDestroy : Boolean
    val nativeLibraries : Array<String>
    val gameActivityClazz: Class<*>
    val commandLineArgs : Array<String>
    val mouseButtonsEventsCanBeInvoked : Boolean
    val touchFullScreenModeCanBeUsed : Boolean
    fun initialize(activity: ComponentActivity)
    fun loadLayout()
    fun onPause()
    fun onResume()
    fun onNativeTrimMemory(aggressive : Boolean)
    fun onDestroy()
    fun onBackPressed() : Boolean
    fun onNativeLibrariesLoaded()
}

val IEngineInfo.mainSharedObject get() = buildFullLibraryName(this.mainLibraryName)

fun IEngineInfo.isResourceCorrect (activity: Activity, onCloseDialogBox :(()-> Unit)? =null) : Boolean{
    if (!pathToResourceExists) {
        activity.showErrorDialogBox(R.string.resource_not_found_error, onCloseDialogBox)
        return false
    }

    if (!pathToResourceIsCorrect) {
        val errorMessage = activity.getString(R.string.resource_not_correct_error,
            this.requiredResourceExtensions)
        activity.showErrorDialogBox(errorMessage, onCloseDialogBox)
        return false
    }

    return true
}