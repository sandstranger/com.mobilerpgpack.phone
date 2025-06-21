package com.mobilerpgpack.ctranslate2proxy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpusMtTranslator (private val pathToTranslationModel : String,
                        private val pathToSourceProcessor : String,
                        private val pathToTargetProcessor : String) : Translator() {

    private external fun initializeFromJni (pathToTranslationModel : String, pathToSourceProcessor : String,
                                            pathToTargetProcessor : String)

    private external fun translateFromJni (text : String) : String

    private external fun releaseFromJni()

    @Synchronized
    override fun initialize() =
        initializeFromJni(pathToTranslationModel, pathToSourceProcessor, pathToTargetProcessor)

    override suspend fun translate(vararg params: String): String = withContext(Dispatchers.IO) {
        if (params.isEmpty()){
            return@withContext ""
        }
        return@withContext translateFromJni(params[0])
    }

    @Synchronized
    override fun release() {
        releaseFromJni()
    }
}