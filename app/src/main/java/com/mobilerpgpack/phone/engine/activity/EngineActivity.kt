package com.mobilerpgpack.phone.engine.activity

import android.os.Bundle
import android.system.Os
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.getEngineResourcePath
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.engine.preserveScreenAspectRatio
import com.mobilerpgpack.phone.engine.setFullscreen
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.displayInSafeArea
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.libsdl.app.SDLActivity
import java.io.File

class EngineActivity : SDLActivity() {
    private lateinit var activeEngineType : EngineTypes

    private external fun pauseSound()

    private external fun resumeSound()

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        initializeEngineData()
        super.onCreate(savedInstanceState)
    }

    override fun getMainSharedObject() = enginesInfo[activeEngineType]!!.mainEngineLib

    override fun getLibraries() = enginesInfo[activeEngineType]!!.allLibs

    override fun onPause() {
        super.onPause()
        pauseSound()
    }

    override fun onResume() {
        super.onResume()
        resumeSound()
    }

    override fun onDestroy() {
        super.onDestroy()
        killEngine()
    }

    private fun initializeEngineData(){
        var pathToEngineResourceFile : File
        var needToPreserveScreenAspectRatio = false
        var displayInSafeArea = false

        runBlocking {
            activeEngineType = PreferencesStorage.getActiveEngineValue(this@EngineActivity)
            displayInSafeArea = PreferencesStorage.getDisplayInSafeAreaValue(this@EngineActivity).first()!!
            needToPreserveScreenAspectRatio = PreferencesStorage.getPreserveAspectRatioValue(this@EngineActivity).first()!!
            pathToEngineResourceFile = File(getEngineResourcePath(this@EngineActivity,activeEngineType))
        }

        if (needToPreserveScreenAspectRatio){
            preserveScreenAspectRatio()
        }

        if (displayInSafeArea){
            this.displayInSafeArea()
        }

        Os.setenv("LIBGL_ES", "2", true)
        Os.setenv("SDL_VIDEO_GL_DRIVER", "libGL.so", true)
        Os.setenv("ANDROID_GAME_PATH",pathToEngineResourceFile.parent,true)
        Os.setenv("RESOURCE_FILE_NAME",pathToEngineResourceFile.name,true)
    }
}