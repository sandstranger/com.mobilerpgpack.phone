package com.mobilerpgpack.phone.engine.engineinfo.utils.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

abstract class IniViewModel : ViewModel(), KoinComponent {

    private val assetsExtractor : IAssetExtractor = get()

    private val preferencesStorage : PreferencesStorage = get ()

    protected val pathToRootUserFolder get() = preferencesStorage.pathToRootUserFolder

    private var iniFilesLoaded by mutableStateOf(false)

    private var wasInitialized = false

    val showView get() = iniFilesLoaded && assetsExtractor.assetsCopied

    fun initialize() {
        if (wasInitialized){
            return
        }
        wasInitialized = true

        if (assetsExtractor.assetsCopied){
            reloadIniFiles()
        }

        assetsExtractor.assetsStartedCopyListeners += { unloadIniFiles() }
        assetsExtractor.assetsFinishCopyListeners += { reloadIniFiles() }
    }

    protected open fun unloadIniFiles(){
        iniFilesLoaded = false
    }

    protected open fun reloadIniFiles (){
        iniFilesLoaded = true
    }
}