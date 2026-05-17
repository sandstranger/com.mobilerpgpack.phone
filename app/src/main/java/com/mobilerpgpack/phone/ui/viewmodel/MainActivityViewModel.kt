package com.mobilerpgpack.phone.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.main.ONE_FRAME_DELAY
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class MainActivityViewModel : ViewModel(), KoinComponent {
    private val scope : CoroutineScope by inject ()
    private val preferencesStorage : PreferencesStorage by inject()
    @Volatile
    private var assetsStartedToCopy = false

    fun copyAllAssetsFromApk(){
        if (!assetsStartedToCopy) {
            assetsStartedToCopy = true
            scope.launch { copyAllAssetsFromApkAsync() }
        }
    }

    private suspend fun copyAllAssetsFromApkAsync(){
        while (!preferencesStorage.prefsWasLoaded){
            delay(ONE_FRAME_DELAY)
        }
        val rootUserDirectory : File = getKoin().get ( named (
            KoinModulesProvider.ROOT_USER_DIRECTORY_KEY))
        rootUserDirectory.mkdirs()
        val assetExtractor : IAssetExtractor = getKoin().get ()
        assetExtractor.copyAssetsContentToInternalStorage()
    }
}