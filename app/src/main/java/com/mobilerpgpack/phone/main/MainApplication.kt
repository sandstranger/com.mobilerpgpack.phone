package com.mobilerpgpack.phone.main

import android.app.Application
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.qualifier.named
import java.io.File

class MainApplication : Application() {
    private lateinit var preferencesStorage : PreferencesStorage

    override fun onCreate() {
        super.onCreate()
        setupJna()
        initializeKoin()
        preferencesStorage = getKoin().get ()
        preferencesStorage.loadAllEntries()
        globalScope.launch { copyAllAssetsFromApk() }
    }

    override fun onTerminate() {
        super.onTerminate()
        globalScope.coroutineContext.cancelChildren()
        val translationManager : TranslationManager = getKoin().get ()
        translationManager.terminate()
    }

    private fun initializeKoin(){
        val koinModulesProvider = KoinModulesProvider(this@MainApplication,globalScope)
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(koinModulesProvider.allModules)
        }
    }

    private suspend fun copyAllAssetsFromApk(){
        while (!preferencesStorage.prefsWasLoaded){
            delay(ONE_FRAME_DELAY)
        }
        val rootUserDirectory : File = getKoin().get ( named (
            KoinModulesProvider.ROOT_USER_DIRECTORY_KEY))
        rootUserDirectory.mkdirs()
        val assetExtractor : IAssetExtractor = getKoin().get ()
        assetExtractor.copyAssetsContentToInternalStorage()
    }

    private companion object{
        val globalScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        private fun setupJna(){
            System.setProperty("jna.nosys", "true")
            System.setProperty("jna.nounpack", "true")
            System.setProperty("jna.encoding", "UTF-8")
        }
    }
}