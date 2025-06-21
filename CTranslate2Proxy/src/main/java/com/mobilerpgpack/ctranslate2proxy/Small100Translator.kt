package com.mobilerpgpack.ctranslate2proxy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Small100Translator (private val modelFile: String, private val spmFile: String) : Translator() {
    private external fun initializeFromJni (modelFile : String, spmFile : String)

    private external fun translateFromJni (text : String, targetLocale: String) : String

    private external fun releaseFromJni()

    @Synchronized
    override fun initialize() {
        initializeFromJni(modelFile, spmFile)
    }

    override suspend fun translate(vararg params: String): String = withContext(Dispatchers.IO) {
        if (params.size< 2){
            return@withContext ""
        }
        return@withContext translateFromJni(params[0],params[1])
    }

    @Synchronized
    override fun release() {
        releaseFromJni()
    }
}