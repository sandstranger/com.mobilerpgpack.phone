package com.mobilerpgpack.ctranslate2proxy

abstract class Translator {

    abstract fun initialize()

    abstract suspend fun translate (vararg params: String) : String

    abstract fun release()

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