package com.mobilerpgpack.phone.engine.activity

import android.os.Bundle
import android.system.Os
import androidx.activity.enableEdgeToEdge
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.initializeCommonEngineData
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.hideSystemBars
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.libsdl3.app.SDLActivity
import java.io.File

internal class SDL3GameActivity : SDLActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeGameData()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBars()
    }

    override fun getMainSharedObject() = enginesInfo[EngineTypes.Doom64ExPlus]!!.mainEngineLib

    override fun getLibraries() = enginesInfo[EngineTypes.Doom64ExPlus]!!.allLibs

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        killEngine()
    }

    private fun initializeGameData(){
        var pathToDoom64MainWadsFolder : String
        var pathToDoom64ModsFolder : String

        runBlocking {
            pathToDoom64MainWadsFolder = PreferencesStorage
                .getPathToDoom64MainWadsFolder(this@SDL3GameActivity).first()
            pathToDoom64ModsFolder = getPathToDoom64ModsFolder()
        }

        Os.setenv("PATH_TO_DOOM64_MAIN_WADS_FOLDER", pathToDoom64MainWadsFolder, true)
        Os.setenv("PATH_TO_DOOM64_MODS_FOLDER", pathToDoom64ModsFolder, true)
        Os.setenv("PATH_TO_DOOM_64_USER_FOLDER",getPathToDoom64UserFolder(), true)
        Os.setenv("PATH_TO_ROOT_USER_FOLDER",getRootPathToUserFolder(), true)
        initializeCommonEngineData(this)
    }

    private fun getRootPathToUserFolder() = this.getExternalFilesDir("")!!.absolutePath

    private fun getPathToDoom64UserFolder () = getRootPathToUserFolder() + File.separator + "doom64ex-plus" + File.separator

    private suspend fun getPathToDoom64ModsFolder () : String {
        val enableDoom64Mods = PreferencesStorage
            .getEnableDoom64ModsValue(this@SDL3GameActivity).first()

        if (!enableDoom64Mods){
            return ""
        }

        var pathToDoom64ModsFolder = PreferencesStorage
            .getPathToDoom64ModsFolder(this@SDL3GameActivity).first()

        val pathToDoom64ModsFolderExists = File(pathToDoom64ModsFolder).exists()

        if (!pathToDoom64ModsFolderExists){
            pathToDoom64ModsFolder = ""
        }

        return pathToDoom64ModsFolder
    }
}