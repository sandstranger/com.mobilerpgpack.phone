package com.mobilerpgpack.phone.engine.engineinfo.utils.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

abstract class IniViewModel : ViewModel(), KoinComponent {

    private val assetsExtractor : IAssetExtractor = get()

    private val preferencesStorage : PreferencesStorage = get ()

    protected val pathToRootUserFolder get() = preferencesStorage.pathToRootUserFolder.value!!

    private val iniFilesLoaded = MutableLiveData(false)

    private var wasInitialized = false

    val showView get() : LiveData<Boolean> = iniFilesLoaded

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
        iniFilesLoaded.value = false
    }

    protected open fun reloadIniFiles (){
        iniFilesLoaded.value = true
    }
}