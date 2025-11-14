package com.mobilerpgpack.phone.engine.engineinfo

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.utils.ScreenResolution
import com.sun.jna.Native
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import java.io.File

open class Doom64EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    buttonsToDraw: Collection<IScreenControlsView>,
    commandLineParamsFlow : Flow<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, buttonsToDraw,
        EngineTypes.Doom64ExPlus,emptyFlow(),commandLineParamsFlow) {

    override val pathToResource: Flow<String> = preferencesStorage.pathToDoom64MainWadsFolder

    private var customScreenResolutionWasApplied = false

    private external fun RecalculateScreenResolution (screenWidth : Int, screenHeight : Int)

    init {
        Native.register(Doom64EngineInfo::class.java, mainEngineLib)
    }

    override suspend fun initialize(activity: ComponentActivity) {
        super.initialize(activity)

        val pathToDoom64ModsFolder = getPathToDoom64ModsFolder()

        Os.setenv("PATH_TO_DOOM64_MODS_FOLDER", pathToDoom64ModsFolder, true)
        Os.setenv("PATH_TO_DOOM_64_USER_FOLDER", getPathToDoom64UserFolder(), true)
    }

    override fun setScreenResolution(screenResolution: ScreenResolution) {
        super.setScreenResolution(screenResolution)
        setupScreenResolutionToEnv(screenResolution)
        customScreenResolutionWasApplied = true
    }

    override fun onSafeAreaApplied(screenResolution: ScreenResolution) {
        super.onSafeAreaApplied(screenResolution)
        if (!customScreenResolutionWasApplied) {
            setupScreenResolutionToEnv(screenResolution)
            RecalculateScreenResolution(screenResolution.screenWidth, screenResolution.screenHeight)
        }
    }

    final override fun isMouseShown(): Int = if (super.mouseButtonsEventsCanBeInvoked) 1 else 0

    protected open fun getPathToDoom64UserFolder() =
        pathToRootUserFolder + File.separator + "doom64ex-plus" + File.separator

    private fun setupScreenResolutionToEnv (screenResolution: ScreenResolution){
        Os.setenv("SCREEN_WIDTH", screenResolution.screenWidth.toString(), true)
        Os.setenv("SCREEN_HEIGHT", screenResolution.screenHeight.toString(), true)
    }

    private suspend fun getPathToDoom64ModsFolder(): String {
        val enableDoom64Mods = preferencesStorage.enableDoom64Mods.first()

        if (!enableDoom64Mods) {
            return ""
        }

        var pathToDoom64ModsFolder = preferencesStorage.pathToDoom64ModsFolder.first()

        val pathToDoom64ModsFolderExists = File(pathToDoom64ModsFolder).exists()

        if (!pathToDoom64ModsFolderExists) {
            pathToDoom64ModsFolder = ""
        }

        return pathToDoom64ModsFolder
    }
}

