package com.mobilerpgpack.phone.main

import android.app.Application
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.utils.AssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

@OptIn(KoinExperimentalAPI::class)
class KoinApplication : Application(), KoinStartup {

    override fun onCreate() {
        super.onCreate()
        var activeTranslationModelType : TranslationType
        var allowDownloadingModelsOverMobile = false
        runBlocking {
            allowDownloadingModelsOverMobile = PreferencesStorage
                .getAllowDownloadingModelsOverMobileValue(this@KoinApplication).first()
            activeTranslationModelType = enumValueOf<TranslationType>(
                PreferencesStorage.getTranslationModelTypeValue(this@KoinApplication).first()
            )
        }
        globalScope.launch {
            AssetExtractor.copyAssetsContentToInternalStorage(this@KoinApplication)
        }
        TranslationManager.init(this,activeTranslationModelType, allowDownloadingModelsOverMobile)
    }

    override fun onKoinStartup(): KoinConfiguration {
        TODO("Not yet implemented")
    }

    override fun onTerminate() {
        super.onTerminate()
        globalScope.coroutineContext.cancelChildren()
        TranslationManager.terminate()
    }

    companion object{
        val globalScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
}