package com.mobilerpgpack.phone.engine.activity

import android.os.Bundle
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
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

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferencesStorage : PreferencesStorage = get()
        runBlocking {
            val activeEngineType = preferencesStorage.activeEngineAsFlowString.first()
            engineInfo = get (named(activeEngineType))
            engineInfo.initialize(this@SDL3GameActivity)
        }
        super.onCreate(savedInstanceState)
        engineInfo.loadLayout()
    }

    override fun getMainSharedObject() = engineInfo.mainSharedObject

    override fun getLibraries() = engineInfo.nativeLibraries

    override fun getArguments(): Array<String>  {
        val args = engineInfo.commandLineArgs
        return if (args.isEmpty()) super.getArguments() else args
    }

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