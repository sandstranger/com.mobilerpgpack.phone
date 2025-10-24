package com.mobilerpgpack.ctranslate2proxy

import com.sun.jna.Native
import com.sun.jna.StringArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpusMtTranslator(
    private val pathToTranslationModel: String,
    private val pathToSourceProcessor: String,
    private val pathToTargetProcessor: String
) : Translator() {

    private external fun OpusMtTranslator_initializeFromJni(
        pathToTranslationModel: String, pathToSourceProcessor: String,
        pathToTargetProcessor: String
    )

    private external fun OpusMtTranslator_translateFromJni( sourceText : String, sentences: StringArray): String

    private external fun OpusMtTranslator_releaseFromJni()

    override fun initialize() {
        synchronized(lockObject) {
            OpusMtTranslator_initializeFromJni(pathToTranslationModel, pathToSourceProcessor, pathToTargetProcessor)
        }
    }

    override suspend fun translate(
        text: String, sourceLocale: String,
        targetLocale: String
    ): String = withContext(Dispatchers.IO) {
        synchronized(lockObject) {
            if (text.isEmpty()){
                return@withContext text
            }
            return@withContext OpusMtTranslator_translateFromJni(text, splitTextIntoSentences(text))
        }
    }

    override fun release() {
        synchronized(lockObject) {
            OpusMtTranslator_releaseFromJni()
        }
    }

    private companion object{
        init {
            Native.register(OpusMtTranslator::class.java,"CTranslate2Proxy")
            // System.loadLibrary("omp")
            //  System.loadLibrary(if (BuildConfig.DEBUG) "spdlogd" else "spdlog")
            // System.loadLibrary("ctranslate2")
            //System.loadLibrary("sentencepiece_train")
            // System.loadLibrary("sentencepiece")
            //   System.loadLibrary("CTranslate2Proxy")
        }
    }
}