package com.mobilerpgpack.phone.main

import android.content.Context
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

class KoinModulesProvider(private val context: Context, private val scope: CoroutineScope) {

    private val preferencesStorage: PreferencesStorage = PreferencesStorage(context)

    val mainModule = module {
        single<Context> { context }.withOptions { createdAtStart() }
        single<CoroutineScope> { scope }.withOptions { createdAtStart() }
        single<PreferencesStorage> { preferencesStorage }.withOptions { createdAtStart() }
    }
}

