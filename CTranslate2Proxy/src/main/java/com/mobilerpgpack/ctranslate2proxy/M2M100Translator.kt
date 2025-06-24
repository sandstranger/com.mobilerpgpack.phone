package com.mobilerpgpack.ctranslate2proxy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class M2M100Translator(private val modelFile: String, private val spmFile: String) : Translator() {

    private external fun initializeFromJni(modelFile: String, spmFile: String)

    private external fun translateFromJni(
        text: String,
        sentences : List<String>,
        sourceLocale: String,
        targetLocale: String
    ): String

    private external fun releaseFromJni()

    override fun initialize() {
        synchronized(lockObject) {
            initializeFromJni(modelFile, spmFile)
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
            return@withContext translateFromJni(text,splitSentencesRegex(text), sourceLocale, targetLocale)
        }
    }

    override fun release() {
        synchronized(lockObject) {
            releaseFromJni()
        }
    }
}