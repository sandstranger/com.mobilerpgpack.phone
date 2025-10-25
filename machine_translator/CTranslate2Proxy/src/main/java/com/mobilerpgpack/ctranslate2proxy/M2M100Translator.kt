package com.mobilerpgpack.ctranslate2proxy

import com.sun.jna.Native
import com.sun.jna.StringArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class M2M100Translator(private val modelFile: String, private val spmFile: String) : Translator() {

    private external fun M2M100Translator_initializeFromJni(modelFile: String, spmFile: String)

    private external fun M2M100Translator_translateFromJni(
        text: String,
        sentences : StringArray,
        sourceLocale: String,
        targetLocale: String
    ): String

    private external fun M2M100Translator_releaseFromJni()

    override fun initialize() {
        synchronized(lockObject) {
            M2M100Translator_initializeFromJni(modelFile, spmFile)
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
            return@withContext M2M100Translator_translateFromJni(text,splitTextIntoSentences(text), sourceLocale, targetLocale)
        }
    }

    override fun release() {
        synchronized(lockObject) {
            M2M100Translator_releaseFromJni()
        }
    }

    private companion object{
        init {
            Native.register(M2M100Translator::class.java, C2TRANSLATE_PROXY_NATIVE_LIB_NAME)
        }
    }
}