package com.mobilerpgpack.phone.main

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

class KoinModules (private val context: Context, private val scope : CoroutineScope){
    val mainModule = module {
        single <Context> { context }
        single<CoroutineScope> { scope }
            .withOptions {
                createdAtStart()
            }
    }
}

