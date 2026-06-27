package com.mobilerpgpack.phone.ui.screen.viewmodels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.copyFolder
import com.mobilerpgpack.phone.utils.startGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

internal class SettingsScreenViewModel : ViewModel(), KoinComponent {
    private val preferencesStorage : PreferencesStorage by inject ()
    private val context : Context by inject ()
    private val scope : CoroutineScope by inject (
        named(KoinModulesProvider.BACKGROUND_THREAD_COROUTINE_KEY))
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
                copyAssetsContentToInternalStorage(copyForced = true)
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

    fun changePathToUserFolderAndRestartApplication(sourceFolder : String){
        scope.launch {
            withContext(Dispatchers.Main){
                preferencesStorage.setBooleanValueAsync(preferencesStorage.allAssetsCopiedPrefsKey, false)
                preferencesStorage.setStringValueAsync(preferencesStorage.pathToRootUserFolderPrefsKey, sourceFolder)
                restartApplication()
            }
        }
    }

    fun restartApplication () = ProcessPhoenix.triggerRebirth(context)
}