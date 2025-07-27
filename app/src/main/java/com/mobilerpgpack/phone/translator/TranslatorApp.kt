package com.mobilerpgpack.phone.translator

import android.app.Application
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

class TranslatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        var activeTranslationModelType : TranslationType
        var allowDownloadingModelsOverMobile = false
        runBlocking {
            allowDownloadingModelsOverMobile = PreferencesStorage
                .getAllowDownloadingModelsOverMobileValue(this@TranslatorApp).first()
            activeTranslationModelType = enumValueOf<TranslationType>(
                PreferencesStorage.getTranslationModelTypeValue(this@TranslatorApp).first())
        }
        globalScope.launch {
            AssetExtractor.copyAssetsContentToInternalStorage(this@TranslatorApp)
        }
        TranslationManager.init(this,activeTranslationModelType, allowDownloadingModelsOverMobile)
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