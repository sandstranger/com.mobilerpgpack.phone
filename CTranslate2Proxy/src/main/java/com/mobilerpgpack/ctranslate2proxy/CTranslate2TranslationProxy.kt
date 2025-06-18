package com.mobilerpgpack.ctranslate2proxy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CTranslate2TranslationProxy (private val pathToTranslationModel : String,
                                   private val pathToSourceProcessor : String,
                                   private val pathToTargetProcessor : String) {

    private external fun initializeTranslation (pathToTranslationModel : String, pathToSourceProcessor : String,
                                                pathToTargetProcessor : String)

    private external fun translate (text : String) : String

    init {
        initializeTranslation(pathToTranslationModel, pathToSourceProcessor, pathToTargetProcessor)
    }

    suspend fun translateAsync (text: String) : String = withContext(Dispatchers.IO){
        translate(text)
    }

    private companion object{
        init {
            System.loadLibrary("omp")
            System.loadLibrary("spdlogd")
            System.loadLibrary("ctranslate2")
            System.loadLibrary("sentencepiece_train")
            System.loadLibrary("sentencepiece")
            System.loadLibrary("TranslationProxy")
        }
    }
}