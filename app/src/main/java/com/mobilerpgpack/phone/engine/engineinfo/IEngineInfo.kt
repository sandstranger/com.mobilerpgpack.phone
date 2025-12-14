package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.buildFullLibraryName
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.utils.showErrorDialogBox
import com.quantuminventions.customkeyboard.components.keyboard.CustomisedKeyboardView

interface IEngineInfo {
    val rootView : View?
    val screenController : IScreenController
    val keyboardView : CustomisedKeyboardView?
    val keyboardInputField : TextView?
    val engineType : EngineTypes
    val pathToResourceIsCorrect : Boolean
    val pathToResourceExists : Boolean
    val requiredResourceExtensions : Collection<String>
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
    fun onBackPressed() : Boolean
    fun onNativeLibrariesLoaded() {}
    fun registerJoysticks() {}
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