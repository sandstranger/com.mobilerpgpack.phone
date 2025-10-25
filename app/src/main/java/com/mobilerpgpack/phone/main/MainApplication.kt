package com.mobilerpgpack.phone.main

import android.app.Application
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.utils.AssetExtractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        copyAllAssetsFromApk()
        initializeKoin()
    }

    override fun onTerminate() {
        super.onTerminate()
        globalScope.coroutineContext.cancelChildren()
        val translationManager : TranslationManager = get()
        translationManager.terminate()
    }

    private fun initializeKoin(){
        val koinModulesProvider = KoinModulesProvider(this@MainApplication, globalScope)
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(koinModulesProvider.allModules)
        }
    }

    private fun copyAllAssetsFromApk(){
        val assetsExtractor = get <AssetExtractor>()
        globalScope.launch {
            assetsExtractor.copyAssetsContentToInternalStorage()
        }
    }

    private companion object{
        val globalScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
}