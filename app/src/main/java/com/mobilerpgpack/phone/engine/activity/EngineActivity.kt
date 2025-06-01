package com.mobilerpgpack.phone.engine.activity

import android.os.Bundle
import com.mobilerpgpack.phone.engine.MAIN_ENGINE_NATIVE_LIB
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.engine.libsArray
import com.mobilerpgpack.phone.engine.setFullscreen
import org.libsdl.app.SDLActivity

class EngineActivity : SDLActivity() {
    private external fun pauseSound()

    private external fun resumeSound()

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)
    }

    override fun getMainSharedObject() = MAIN_ENGINE_NATIVE_LIB

    override fun getLibraries() = libsArray

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
}