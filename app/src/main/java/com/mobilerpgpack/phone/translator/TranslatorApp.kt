package com.mobilerpgpack.phone.translator

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.copyAssetsFolderToInternalStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.first
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
        copyAssetsContentToInternalStorage()
        TranslationManager.init(this,activeTranslationModelType, allowDownloadingModelsOverMobile)
    }

    override fun onTerminate() {
        super.onTerminate()
        globalScope.coroutineContext.cancelChildren()
        TranslationManager.terminate()
    }

    private fun copyAssetsContentToInternalStorage (){
        copyAssetsFolderToInternalStorage(this, "game_files", this.getExternalFilesDir("")!!)
    }

    companion object{
        val globalScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
}