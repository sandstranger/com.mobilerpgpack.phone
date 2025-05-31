package com.mobilerpgpack.phone.engine.activity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.mobilerpgpack.phone.engine.MAIN_ENGINE_NATIVE_LIB
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.engine.libsArray
import com.mobilerpgpack.phone.engine.setFullscreen
import com.mobilerpgpack.phone.utils.displayInSafeArea
import org.libsdl.app.SDLActivity

class EngineActivity : SDLActivity() {
    private lateinit var prefsManager : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)
        prefsManager = PreferenceManager.getDefaultSharedPreferences(this)
        displayInSafeArea(prefsManager)
    }

    override fun getMainSharedObject() = MAIN_ENGINE_NATIVE_LIB

    override fun getLibraries() = libsArray

    override fun onDestroy() {
        super.onDestroy()
        killEngine()
    }
}