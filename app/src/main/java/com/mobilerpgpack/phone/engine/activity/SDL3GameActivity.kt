package com.mobilerpgpack.phone.engine.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.isResourceCorrect
import com.mobilerpgpack.phone.engine.engineinfo.mainSharedObject
import com.mobilerpgpack.phone.ui.activity.MainActivity
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.forceLandscapeOrientation
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.libsdl3.app.SDLActivity
import kotlin.system.exitProcess

internal class SDL3GameActivity : SDLActivity(), KoinComponent {
    private lateinit var engineInfo : IEngineInfo
    private var callNativeEvents = false

    override fun onCreate(savedInstanceState: Bundle?) {
        MainActivity.gameActivityStarted = true
        enableEdgeToEdge()
        val preferencesStorage : PreferencesStorage = get()
        engineInfo = get (named(preferencesStorage.activeEngineString.value!!))
        engineInfo.apply {
            gameResourcesFound = isResourceCorrect(this@SDL3GameActivity, onCloseDialogBox = { finish() })
            if (!gameResourcesFound) {
                super.onCreate(savedInstanceState)
                return
            }
            initialize(this@SDL3GameActivity)
            super.onCreate(savedInstanceState)
            loadLayout()
            onNativeLibrariesLoaded()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                onBackInvokedDispatcher.registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT
                ) {
                    onBackPressed()
                }
            }
        }
        forceLandscapeOrientation()
    }

    override fun getMainSharedObject() = engineInfo.mainSharedObject

    override fun getLibraries() = engineInfo.nativeLibraries

    override fun getArguments(): Array<String>  {
        val args = engineInfo.commandLineArgs
        return if (args.isEmpty()) super.getArguments() else args
    }

    override fun onPause() {
        super.onPause()
        if (gameResourcesFound && callNativeEvents) {
            engineInfo.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (gameResourcesFound && callNativeEvents) {
            engineInfo.onResume()
        }
        forceLandscapeOrientation()
        callNativeEvents = true
    }

    override fun onLowMemory() {
        super.onLowMemory()
        engineInfo.onNativeTrimMemory(true)
    }

    override fun onDestroy() {
        engineInfo.onDestroy()
        super.onDestroy()
        if (engineInfo.callExitProcessOnDestroy) {
            exitProcess(0)
        }
    }

    @SuppressLint("MissingSuperCall", "GestureBackNavigation")
    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            engineInfo.onBackPressed()
        }
    }
}