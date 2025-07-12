package com.mobilerpgpack.phone.translator

import android.util.Log
import com.mobilerpgpack.phone.engine.EngineTypes
import kotlin.math.roundToInt

class IntervalMarkerTranslator {

    private val symbolsRegex = Regex("[\\p{L}&&[^xX]]")
    private val pipeSpecialSymbol = "|"
    private val sentenceSpecialTypes = setOf('.', '!', '?', ',')

    suspend fun translateWithFixedInterval(
        sourceText: String,
        textCameFromDialog : Boolean,
        inGame : Boolean,
        engineTypes: EngineTypes,
        translateFn: suspend (String) -> String
    ): Pair <String, Boolean> {

        if (!symbolsRegex.containsMatchIn(sourceText)){
            return sourceText to true
        }

        if (!inGame){
            return translateFn(sourceText) to false
        }

        if (textCameFromDialog){
            val cleanedTextToTranslate = sourceText.replace("-$pipeSpecialSymbol","")
                .replace(pipeSpecialSymbol, " ").trim()

            val translatedText = translateFn(cleanedTextToTranslate)
            return if (translatedText == cleanedTextToTranslate) sourceText to false else
                insertSymbolsWithRules(translatedText, pipeSpecialSymbol, interval = 15) to false
        }

        val newLineSymbol = "\n"
        val newLineIndex = sourceText.indexOf(newLineSymbol)

        val cleanedTextToTranslate = sourceText.replace(" - $newLineSymbol","")
            .replace(" -$newLineSymbol", "").replace("-$newLineSymbol", "")
            .replace(newLineSymbol," ").trim()


        val translatedText = translateFn(cleanedTextToTranslate)

        if (newLineIndex>0){
            return insertSymbolsWithRules(translatedText, newLineSymbol,newLineIndex) to false
        }

        return translatedText to false
    }

    private fun insertSymbolsWithRules(text: String, symbolToInsert : String, interval: Int): String {
        val sb = StringBuilder((text.length * 1.5f).roundToInt())
        var count = 0
        var i = 0

        while (i < text.length) {
            val c = text[i]

            sb.append(c)
            if (c != ' ' && c != '\n') {
                count++
            }

            if (count >= interval) {
                var j = i + 1
                while (j < text.length && text[j] in sentenceSpecialTypes ) {
                    sb.append(text[j])
                    j++
                }

                if (sb.isNotEmpty() && sb.last() == ' ') {
                    sb.setLength(sb.length - 1)
                }

                sb.append(symbolToInsert)

                if (j < text.length && text[j] == ' ') {
                    j++
                }

                count = 0
                i = j - 1
            }

            i++
        }

        return sb.toString()
    }
}