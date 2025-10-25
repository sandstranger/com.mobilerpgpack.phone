package com.mobilerpgpack.ctranslate2proxy

import com.sun.jna.Native
import com.sun.jna.StringArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Small100Translator(private val modelFile: String, private val spmFile: String) :
    Translator() {
    private external fun Small100Translator_initializeFromJni(modelFile: String, spmFile: String)

    private external fun Small100Translator_translateFromJni(text: String,
                                          sentences : StringArray,
                                          targetLocale: String): String

    private external fun Small100Translator_releaseFromJni()

    override fun initialize() {
        synchronized(lockObject) {
            Small100Translator_initializeFromJni(modelFile, spmFile)
        }
    }

    override suspend fun translate(
        text: String,
        sourceLocale: String,
        targetLocale: String
    ): String = withContext(Dispatchers.IO) {
        synchronized(lockObject) {
            if (text.isEmpty()){
                return@withContext text
            }
            return@withContext Small100Translator_translateFromJni(text,splitTextIntoSentences(text), targetLocale)
        }
    }

    override fun release() {
        synchronized(lockObject) {
            Small100Translator_releaseFromJni()
        }
    }

    private companion object{
        init {
            Native.register(Small100Translator::class.java, C2TRANSLATE_PROXY_NATIVE_LIB_NAME)
        }
    }
}