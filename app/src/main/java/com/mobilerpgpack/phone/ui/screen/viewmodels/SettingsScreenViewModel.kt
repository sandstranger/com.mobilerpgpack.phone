package com.mobilerpgpack.phone.ui.screen.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.startGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

internal class SettingsScreenViewModel : ViewModel(), KoinComponent {

    private val scope : CoroutineScope by inject ()

    private val pathToRootUserFolder: String = get(
        named(KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY))

    private val assetsExtractor : IAssetExtractor by inject ()

    private val rootUserFolder = File(pathToRootUserFolder)

    fun onResetResourcesClicked(){
        if (!assetsExtractor.assetsCopied){
            return
        }
        rootUserFolder.deleteRecursively()
        rootUserFolder.mkdirs()
        scope.launch { assetsExtractor.copyAssetsContentToInternalStorage() }
    }

    fun onStartGameClicked(activeEngine : EngineTypes,activity: Activity) =
        scope.launch { startGame(activity, activeEngine) }
}