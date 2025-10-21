package com.mobilerpgpack.ctranslate2proxy

import com.ibm.icu.text.BreakIterator
import java.util.Locale

abstract class Translator {

    protected val lockObject = Any()
    
    abstract fun initialize()

    abstract suspend fun translate (text: String, sourceLocale: String, targetLocale : String) : String

    abstract fun release()

    protected fun splitTextIntoSentences(text: String): List<String> {
        val replacedText = text.replace(dotsWithSpacingRegex,"$0 ")
        val iterator = BreakIterator.getSentenceInstance(Locale.US)
        iterator.setText(text)
        val sentences = mutableListOf<String>()
        var start = iterator.first()
        var end = iterator.next()

        while (end != BreakIterator.DONE) {
            val sentence = replacedText.substring(start, end).trim()
            if (sentence.isNotEmpty()) {
                sentences.addAll(splitSentencesRegex(sentence))
            }
            start = end
            end = iterator.next()
        }

        if (sentences.isEmpty()){
            sentences.add(text)
        }
        return sentences
    }

    private fun splitSentencesRegex(text: String): List<String> {
        return text.split(dotsRegex).map { it.trim() }.filter { it.isNotEmpty() }
    }

    private companion object{
        private val dotsRegex = Regex("(?<=[.!?])[\"')\\]]*\\s+(?=[A-Z0-9\\-])")
        private val dotsWithSpacingRegex = Regex("[.!?](?=\\p{L})")

        init {
            System.loadLibrary("omp")
            System.loadLibrary(if (BuildConfig.DEBUG) "spdlogd" else "spdlog")
            System.loadLibrary("ctranslate2")
            System.loadLibrary("sentencepiece_train")
            System.loadLibrary("sentencepiece")
            System.loadLibrary("CTranslate2Proxy")
        }
    }
}