package com.mobilerpgpack.phone

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

class CustomApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupJna()
        var activeTranslationModelType : TranslationType
        var allowDownloadingModelsOverMobile = false
        runBlocking {
            allowDownloadingModelsOverMobile = PreferencesStorage
                .getAllowDownloadingModelsOverMobileValue(this@CustomApp).first()
            activeTranslationModelType = enumValueOf<TranslationType>(
                PreferencesStorage.getTranslationModelTypeValue(this@CustomApp).first()
            )
        }
        globalScope.launch {
            AssetExtractor.copyAssetsContentToInternalStorage(this@CustomApp)
        }
        TranslationManager.init(this,activeTranslationModelType, allowDownloadingModelsOverMobile)
    }

    override fun onTerminate() {
        super.onTerminate()
        globalScope.coroutineContext.cancelChildren()
        TranslationManager.terminate()
    }

    private fun setupJna(){
        System.setProperty("jna.nosys", "true")
        System.setProperty("jna.nounpack", "true")
    }

    companion object{
        val globalScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
}