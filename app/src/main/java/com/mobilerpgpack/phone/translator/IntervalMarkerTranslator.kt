package com.mobilerpgpack.phone.translator

import com.mobilerpgpack.phone.engine.EngineTypes
import kotlin.math.roundToInt

class IntervalMarkerTranslator {

    private val pipeSpecialSymbol = "|"

    suspend fun translateWithFixedInterval(
        sourceText: String,
        textCameFromDialog : Boolean,
        engineTypes: EngineTypes,
        translateFn: suspend (String) -> String
    ): String {

        if (textCameFromDialog){
            val cleanedTextToTranslate = sourceText.replace("-$pipeSpecialSymbol","")
                .replace(pipeSpecialSymbol, " ").trim()

            val translatedText = translateFn(cleanedTextToTranslate)
            return insertSymbolsWithRules(translatedText, pipeSpecialSymbol, interval = 13)
        }

        val tokens = tokenizePreserveAll(sourceText)
        val (withPh, all) = makePlaceholders(tokens)
        val translatedWithPh = translateFn(withPh)
        return reinjectPlaceholders(translatedWithPh, all)
    }

    private fun tokenizePreserveAll(text: String): List<String> {
        val regex = Regex("""(\s+|\|+|\S+)""")
        return regex.findAll(text).map { it.value }.toList()
    }

    private fun makePlaceholders(tokens: List<String>):
            Pair<String /*joinedWithPlaceholders*/, List<String> /*allTokens*/> {
        val placeholders = mutableListOf<String>()
        val sb = StringBuilder()
        tokens.forEachIndexed { i, tok ->
            val ph = if (tok.all { it.isWhitespace() }) "##WS$i##" else "##T$i##"
            sb.append(ph)
            placeholders += tok
        }
        return sb.toString() to placeholders
    }

    private fun reinjectPlaceholders(translatedWithPh: String, tokens: List<String>): String {
        var result = translatedWithPh
        tokens.forEachIndexed { i, tok ->
            val ph = if (tok.all { it.isWhitespace() }) "##WS$i##" else "##T$i##"
            result = result.replace(ph, tok)
        }
        return result
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
                while (j < text.length && text[j] in setOf('.', '!', '?')) {
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