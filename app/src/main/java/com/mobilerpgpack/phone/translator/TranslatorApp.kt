package com.mobilerpgpack.phone.translator

import android.app.Application
import android.content.res.Configuration
import android.os.Build

class TranslatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Translator.init(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newLang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            newConfig.locales[0].language
        else
            newConfig.locale.language

        Translator.setLocale(newLang)
    }
}