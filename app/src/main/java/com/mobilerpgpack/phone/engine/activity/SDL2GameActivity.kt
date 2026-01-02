package com.mobilerpgpack.phone.engine.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.isResourceCorrect
import com.mobilerpgpack.phone.engine.engineinfo.mainSharedObject
import com.mobilerpgpack.phone.main.ONE_FRAME_DELAY
import com.mobilerpgpack.phone.ui.activity.MainActivity
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.waitUntil
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.libsdl.app.SDLActivity

class SDL2GameActivity : SDLActivity(), KoinComponent {
    private lateinit var engineInfo : IEngineInfo

    private var wasPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        MainActivity.gameActivityStarted = true
        enableEdgeToEdge()
        val preferencesStorage : PreferencesStorage = get()
        runBlocking {
            waitUntil { !preferencesStorage.prefsWasLoaded }
        }
        engineInfo = get <IEngineInfo> (named(preferencesStorage.activeEngineString)).apply {
            gameResourcesFound = isResourceCorrect(this@SDL2GameActivity, onCloseDialogBox = { finish() })
            if (!gameResourcesFound) {
                super.onCreate(savedInstanceState)
                return
            }
            initialize(this@SDL2GameActivity)
            super.onCreate(savedInstanceState)
            loadLayout()
            onNativeLibrariesLoaded()
            if (Build.VERSION.SDK_INT >= 33) {
                onBackInvokedDispatcher.registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT
                ) {
                    onBackPressed()
                }
            }
        }
    }

    override fun getMainSharedObject() = engineInfo.mainSharedObject

    override fun getLibraries() = engineInfo.nativeLibraries

    override fun getArguments(): Array<String>  {
        val args = engineInfo.commandLineArgs
        return if (args.isEmpty()) super.getArguments() else args
    }

    override fun onPause() {
        super.onPause()
        if (gameResourcesFound){
            engineInfo.onPause()
            wasPaused = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (gameResourcesFound && wasPaused){
            engineInfo.onResume()
        }
        wasPaused = false
    }

    override fun onDestroy() {
        super.onDestroy()
        engineInfo.onDestroy()
    }

    @SuppressLint("MissingSuperCall", "GestureBackNavigation")
    override fun onBackPressed() {
        engineInfo.onBackPressed()
    }
}