package com.mobilerpgpack.phone.engine.activity

import android.os.Bundle
import android.os.Environment
import android.system.Os
import android.util.Log
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.engine.setFullscreen
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.libsdl.app.SDLActivity

@InternalCoroutinesApi
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

    override fun onDestroy() {
        super.onDestroy()
        killEngine()
    }

    override fun onPause() {
        super.onPause()
        pauseSound()
    }

    override fun onResume() {
        super.onResume()
        resumeSound()
    }

    private fun initializeEngineData(){
        runBlocking {
            activeEngineType = PreferencesStorage.getActiveEngineValue(this@EngineActivity)
        }

        Os.setenv("LIBGL_ES", "2", true)
        Os.setenv("SDL_VIDEO_GL_DRIVER", "libGL.so", true)
        Os.setenv("ANDROID_GAME_PATH","${Environment.getExternalStorageDirectory().absolutePath}/wolfenstein",true)
        Os.setenv("WOLF_IPA_FILE_NAME","Wolfenstein RPG.ipa",true)
    }
}