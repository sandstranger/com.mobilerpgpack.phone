package com.mobilerpgpack.phone.main

import android.app.Application
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.utils.AssetExtractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import java.nio.charset.Charset

class MainApplication : Application() {

    private lateinit var assetExtractor: AssetExtractor

    override fun onCreate() {
        super.onCreate()
        setupJna()
        copyAllAssetsFromApk()
        initializeKoin()
    }

    override fun onTerminate() {
        super.onTerminate()
        globalScope.coroutineContext.cancelChildren()
        val translationManager : TranslationManager = getKoin().get ()
        translationManager.terminate()
    }

    private fun initializeKoin(){
        val koinModulesProvider = KoinModulesProvider(this@MainApplication, assetExtractor,globalScope)
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(koinModulesProvider.allModules)
        }
    }

    private fun copyAllAssetsFromApk(){
        assetExtractor = AssetExtractor(this)
        globalScope.launch {
            assetExtractor.copyAssetsContentToInternalStorage()
        }
    }

    private companion object{
        val globalScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

        private fun setupJna(){
            System.setProperty("jna.nosys", "true")
            System.setProperty("jna.nounpack", "true")
            System.setProperty("jna.encoding", "UTF-8")
        }
    }
}