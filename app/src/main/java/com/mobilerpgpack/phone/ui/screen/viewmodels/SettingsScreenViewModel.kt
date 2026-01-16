package com.mobilerpgpack.phone.ui.screen.viewmodels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.copyFolder
import com.mobilerpgpack.phone.utils.startGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

internal class SettingsScreenViewModel : ViewModel(), KoinComponent {

    private val context : Context by inject ()

    private val preferencesStorage : PreferencesStorage by inject ()

    private val scope : CoroutineScope by inject ()

    private val assetsExtractor : IAssetExtractor by inject ()

    private val sourceFolder = context.getExternalFilesDir("")!!.absolutePath
    
    @Volatile
    private var contentCopied = true

    fun onResetResourcesClicked(){
        if (!assetsExtractor.assetsCopied){
            return
        }
        val rootUserFolder = File(preferencesStorage.pathToRootUserFolder.value!!)
        rootUserFolder.deleteRecursively()
        rootUserFolder.mkdirs()
        scope.launch { assetsExtractor.copyAssetsContentToInternalStorage() }
    }

    fun onStartGameClicked(activeEngine : EngineTypes,activity: Activity) =
        startGame(activity, activeEngine)

    fun copyContentFromInternalStorage () {
        val targetFolder = preferencesStorage.pathToRootUserFolder.value!!
        if (!contentCopied || sourceFolder == targetFolder) {
            return
        }
        contentCopied = false
        scope.launch {
            copyFolder(sourceFolder, targetFolder)
            contentCopied = true
        }
    }

    fun restartApplication () = ProcessPhoenix.triggerRebirth(context)
}