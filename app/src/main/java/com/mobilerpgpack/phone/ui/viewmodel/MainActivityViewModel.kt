package com.mobilerpgpack.phone.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.main.ONE_FRAME_DELAY
import com.mobilerpgpack.phone.ui.screen.ComposeScreen
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class MainActivityViewModel : ViewModel(), KoinComponent {
    private val preferencesStorage : PreferencesStorage by inject()
    private val scope : CoroutineScope by inject (
        named(KoinModulesProvider.BACKGROUND_THREAD_COROUTINE_KEY))
    @Volatile
    private var assetsStartedToLoaded = false

    fun loadAllAssets(){
        if (!assetsStartedToLoaded) {
            assetsStartedToLoaded = true
            scope.launch {
                preferencesStorage.loadAllEntriesAsync()
                copyAllAssetsFromApkAsync()
            }
        }
    }

    fun destroyAllComposeScreens(){
        val composeScreens = get <Collection<ComposeScreen>> (
            named(KoinModulesProvider.ALL_COMPOSE_SCREENS))
        composeScreens.forEach { it.onMainActivityFinish() }
        get <IAssetExtractor> ().clearSubscribers()
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