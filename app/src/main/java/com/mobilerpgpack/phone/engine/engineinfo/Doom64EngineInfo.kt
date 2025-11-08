package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import android.system.Os
import android.util.Log
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.utils.ScreenResolution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import java.io.File

class Doom64EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    buttonsToDraw: Collection<IScreenControlsView>) :
    SDL3EngineInfo(mainEngineLib, allLibs, buttonsToDraw,
        EngineTypes.Doom64ExPlus,
        emptyFlow()) {

    override val pathToResource: Flow<String> = preferencesStorage.pathToDoom64MainWadsFolder

    override val engineType: EngineTypes = EngineTypes.Doom64ExPlus

    override suspend fun initialize(activity: Activity) {
        super.initialize(activity)

        val pathToDoom64ModsFolder = getPathToDoom64ModsFolder()

        Os.setenv("PATH_TO_DOOM64_MODS_FOLDER", pathToDoom64ModsFolder, true)
        Os.setenv("PATH_TO_DOOM_64_USER_FOLDER", getPathToDoom64UserFolder(), true)
    }

    override fun onSafeAreaApplied(screenResolution: ScreenResolution) {
        super.onSafeAreaApplied(screenResolution)
    }

    private fun getPathToDoom64UserFolder() =
        pathToRootUserFolder + File.separator + "doom64ex-plus" + File.separator

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

