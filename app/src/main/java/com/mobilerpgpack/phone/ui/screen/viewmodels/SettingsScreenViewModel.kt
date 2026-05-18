package com.mobilerpgpack.phone.ui.screen.viewmodels

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.copyFolder
import com.mobilerpgpack.phone.utils.startGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

internal class SettingsScreenViewModel : ViewModel(), KoinComponent {
    private val context : Context by inject ()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val assetsExtractor : IAssetExtractor by inject ()
    private val sourceFolder = context.filesDir
    private val rootUserDirectory : File by inject ( named(KoinModulesProvider.ROOT_USER_DIRECTORY_KEY))

    @Volatile
    private var contentCopied = true
    private var wasInit = false

    val allAssetsCopied = MutableLiveData (true)

    fun initialize (){
        if (wasInit){
            return
        }
        wasInit = true
        assetsExtractor.apply {
            allAssetsCopied.value = assetsCopied
            assetsStartedCopyListeners += { allAssetsCopied.value = false }
            assetsFinishCopyListeners += { allAssetsCopied.value = true }
        }
    }

    fun onResetResourcesClicked(){
        assetsExtractor.apply {
            if (!assetsCopied || !allAssetsCopied.value!!){
                return
            }
            allAssetsCopied.value = false
            scope.launch {
                rootUserDirectory.mkdirs()
                resetAssetsInfo()
                copyAssetsContentToInternalStorage()
            }
        }
    }

    fun onStartGameClicked(activeEngine : EngineTypes,activity: Activity) =
        startGame(activity, activeEngine)

    fun copyContentFromInternalStorage () {
        if (!contentCopied || sourceFolder.absolutePath == rootUserDirectory.absolutePath) {
            return
        }
        contentCopied = false
        allAssetsCopied.value = false
        scope.launch {
            copyFolder(sourceFolder, rootUserDirectory)
            contentCopied = true
            allAssetsCopied.postValue(true)
        }
    }

    fun restartApplication () = ProcessPhoenix.triggerRebirth(context)
}