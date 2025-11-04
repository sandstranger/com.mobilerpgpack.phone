package com.mobilerpgpack.phone.engine.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.hideSystemBars
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.libsdl.app.SDLActivity

class SDL2GameActivity : SDLActivity(), KoinComponent {
    private lateinit var engineInfo : IEngineInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        runBlocking {
            val preferencesStorage : PreferencesStorage = get()
            val activeEngineType = preferencesStorage.activeEngineAsFlowString.first()
            engineInfo = get (named(activeEngineType))
            engineInfo.initialize(this@SDL2GameActivity)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBars()
        engineInfo.loadControlsLayout()
    }

    override fun getMainSharedObject() = engineInfo.mainSharedObject

    override fun getLibraries() = engineInfo.nativeLibraries

    override fun onPause() {
        super.onPause()
        engineInfo.onPause()
    }

    override fun onResume() {
        super.onResume()
        engineInfo.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        engineInfo.onDestroy()
    }
}