package com.mobilerpgpack.phone.engine.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.isResourceCorrect
import com.mobilerpgpack.phone.engine.engineinfo.mainSharedObject
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.libsdl3.app.SDLActivity

internal class SDL3GameActivity : SDLActivity(), KoinComponent {

    private lateinit var engineInfo : IEngineInfo

    private var wasPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        var useDarkTheme = false
        val preferencesStorage : PreferencesStorage = get()
        runBlocking {
            useDarkTheme = preferencesStorage.getUseDarkThemeValue().first()
            val activeEngineType = preferencesStorage.activeEngineAsFlowString.first()
            engineInfo = get (named(activeEngineType))
        }
        gameResourcesFound = engineInfo.isResourceCorrect(this, onCloseDialogBox = { finish() })
        if (useDarkTheme){
            setTheme(R.style.AppFullScreenThemeDark)
        }
        if (!gameResourcesFound){
            super.onCreate(savedInstanceState)
            return
        }
        runBlocking { engineInfo.initialize(this@SDL3GameActivity) }
        super.onCreate(savedInstanceState)
        engineInfo.loadLayout()
        engineInfo.onNativeLibrariesLoaded()
    }

    override fun getMainSharedObject() = engineInfo.mainSharedObject

    override fun getLibraries() = engineInfo.nativeLibraries

    override fun getArguments(): Array<String>  {
        val args = engineInfo.commandLineArgs
        return if (args.isEmpty()) super.getArguments() else args
    }

    override fun onPause() {
        super.onPause()
        if (gameResourcesFound) {
            engineInfo.onPause()
        }
        wasPaused = true
    }

    override fun onResume() {
        super.onResume()
        if (gameResourcesFound && wasPaused) {
            engineInfo.onResume()
        }
        wasPaused = false
    }

    override fun onDestroy() {
        super.onDestroy()
        engineInfo.onDestroy()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (!engineInfo.onBackPressed()) {
            org.libsdl3.app.onKeyDown(KeyEvent.KEYCODE_ESCAPE, delayBeforeKeyRelease = 50L)
        }
    }
}