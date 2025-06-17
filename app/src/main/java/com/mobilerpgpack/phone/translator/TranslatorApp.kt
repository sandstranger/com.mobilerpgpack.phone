package com.mobilerpgpack.phone.translator

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TranslatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        var allowDownloadingModelsOverMobile = false
        runBlocking {
            allowDownloadingModelsOverMobile = PreferencesStorage
                .getAllowDownloadingModelsOverMobileValue(this@TranslatorApp).first()!!
        }
        TranslationManager.init(this, allowDownloadingModelsOverMobile)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newLang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            newConfig.locales[0].language
        else
            newConfig.locale.language

        TranslationManager.setLocale(newLang)
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