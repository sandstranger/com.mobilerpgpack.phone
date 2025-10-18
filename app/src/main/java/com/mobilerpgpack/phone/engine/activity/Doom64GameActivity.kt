package com.mobilerpgpack.phone.engine.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.system.Os
import androidx.activity.enableEdgeToEdge
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.initializeCommonEngineData
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.utils.AssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.hideSystemBars
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.libsdl3.app.SDLActivity
import java.io.File

internal class Doom64GameActivity : SDLActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeGameData()
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
                .getPathToDoom64MainWadsFolder(this@Doom64GameActivity).first()
            pathToDoom64ModsFolder = PreferencesStorage
                .getPathToDoom64ModsFolder(this@Doom64GameActivity).first()
        }

        Os.setenv("PATH_TO_DOOM64_MAIN_WADS_FOLDER", pathToDoom64MainWadsFolder, true)
        Os.setenv("PATH_TO_DOOM64_MODS_FOLDER", pathToDoom64ModsFolder, true)
        Os.setenv("PATH_TO_DOOM_64_USER_FOLDER",getPathToDoom64UserFolder(), true)
        initializeCommonEngineData(this)
    }

    private fun getPathToDoom64UserFolder () = this.getExternalFilesDir("")!!.absolutePath + File.separator +
            AssetExtractor.GAME_FILES_ASSETS_FOLDER + File.separator + "doom64ex-plus"
}