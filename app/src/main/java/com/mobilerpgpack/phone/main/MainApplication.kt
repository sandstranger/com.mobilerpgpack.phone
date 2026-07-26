package com.mobilerpgpack.phone.main

import android.app.Application
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.opentouchgaming.saffal.UtilsSAF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UtilsSAF.setContext(applicationContext, true)
        UtilsSAF.loadTreeRoots()
        setupJna()
        initializeKoin()
    }

    override fun onTerminate() {
        super.onTerminate()
        val translationManager : TranslationManager = getKoin().get ()
        translationManager.terminate()
    }

    private fun initializeKoin(){
        val koinModulesProvider = KoinModulesProvider(this@MainApplication)
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(koinModulesProvider.allModules)
        }
    }

    private companion object{
        private fun setupJna(){
            System.setProperty("jna.nosys", "true")
            System.setProperty("jna.nounpack", "true")
            System.setProperty("jna.encoding", "UTF-8")
        }
    }
}