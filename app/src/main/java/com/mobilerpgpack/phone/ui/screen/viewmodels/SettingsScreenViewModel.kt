package com.mobilerpgpack.phone.ui.screen.viewmodels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.copyFolder
import com.mobilerpgpack.phone.utils.startGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.io.File

internal class SettingsScreenViewModel : ViewModel(), KoinComponent {

    private val context : Context by inject ()

    private val scope : CoroutineScope by inject ()

    private val assetsExtractor : IAssetExtractor by inject ()

    private val sourceFolder = context.getExternalFilesDir("")!!

    private val rootUserDirectory : File by inject { parametersOf(KoinModulesProvider.ROOT_USER_DIRECTORY_KEY) }

    @Volatile
    private var contentCopied = true

    fun onResetResourcesClicked(){
        if (!assetsExtractor.assetsCopied){
            return
        }
        rootUserDirectory.apply {
            deleteRecursively()
            mkdirs()
        }
        scope.launch { assetsExtractor.copyAssetsContentToInternalStorage() }
    }

    fun onStartGameClicked(activeEngine : EngineTypes,activity: Activity) =
        startGame(activity, activeEngine)

    fun copyContentFromInternalStorage () {
        if (!contentCopied || sourceFolder.absolutePath == rootUserDirectory.absolutePath) {
            return
        }
        contentCopied = false
        scope.launch {
            copyFolder(sourceFolder, rootUserDirectory)
            contentCopied = true
        }
    }

    fun restartApplication () = ProcessPhoenix.triggerRebirth(context)
}