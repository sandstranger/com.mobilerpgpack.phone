package com.mobilerpgpack.phone.ui.screen.viewmodels

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.copyFolder
import com.mobilerpgpack.phone.utils.startGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

internal class SettingsScreenViewModel : ViewModel(), KoinComponent {

    private val context : Context by inject ()

    private val preferencesStorage : PreferencesStorage by inject ()

    private val scope : CoroutineScope by inject ()

    private val assetsExtractor : IAssetExtractor by inject ()

    @Volatile
    private var contentCopied = true

    fun onResetResourcesClicked(){
        if (!assetsExtractor.assetsCopied){
            return
        }
        val rootUserFolder = File(preferencesStorage.pathToRootUserFolder)
        rootUserFolder.deleteRecursively()
        rootUserFolder.mkdirs()
        scope.launch { assetsExtractor.copyAssetsContentToInternalStorage() }
    }

    fun onStartGameClicked(activeEngine : EngineTypes,activity: Activity) =
        startGame(activity, activeEngine)

    fun copyContentFromInternalStorage () {
        val sourceFolder = context.getExternalFilesDir("")!!.absolutePath
        val targetFolder = preferencesStorage.pathToRootUserFolder
        if (!contentCopied || sourceFolder == targetFolder) {
            return
        }
        contentCopied = false
        scope.launch {
            copyFolder(sourceFolder, targetFolder)
            contentCopied = true
        }
    }
}