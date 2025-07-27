package com.mobilerpgpack.phone.translator

import android.util.Log
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.models.TranslationResult
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
        translateFn: suspend (String) -> TranslationResult
    ): TranslationResult {

        if (!symbolsRegex.containsMatchIn(sourceText)){
            return TranslationResult(sourceText,true)
        }

        if (!inGame){
            return translateFn(sourceText)
        }

        return when (engineTypes) {
            EngineTypes.WolfensteinRpg -> translateWolfensteinRpgText(sourceText, translateFn)
            EngineTypes.DoomRpg -> translateDoomRpgText(sourceText, textCameFromDialog, translateFn)
            EngineTypes.Doom2Rpg -> translateDoomRpg2Text(sourceText, translateFn)
        }
    }

    private suspend fun translateWolfensteinRpgText(
        sourceText: String,
        translateFn: suspend (String) -> TranslationResult
    ): TranslationResult {
        val defaultPipeSymbolIndex = 30

        val dialogBoxText = sourceText.contains(pipeSpecialSymbol)
        if (dialogBoxText) {
            val firstPipeSymbolIndex = sourceText.indexOf(pipeSpecialSymbol)
            val splittedTexts = sourceText.split(pipeSpecialSymbol)

            if (firstPipeSymbolIndex <= defaultPipeSymbolIndex) {
                val title = splittedTexts[0].trim()
                val cleanedTextToTranslate = sourceText.replace("-$pipeSpecialSymbol", "")
                    .replace(pipeSpecialSymbol, " ").replace(title, "").trim()

                val translatedTitleResult = translateFn(title)
                val translatedTextResult = translateFn(cleanedTextToTranslate)

                if (translatedTitleResult.translated && translatedTextResult.translated) {
                    val result = "${translatedTitleResult.text}$pipeSpecialSymbol${
                        insertSymbolsWithRulesDoom2Rpg(
                            translatedTextResult.text,
                            pipeSpecialSymbol, interval = splittedTexts.maxBy { it.length }.length
                        )
                    }"
                    return TranslationResult(result, true)
                } else {
                    return TranslationResult(sourceText, false)
                }
            } else {
                val cleanedTextToTranslate = sourceText.replace("-$pipeSpecialSymbol", "")
                    .replace(pipeSpecialSymbol, " ").trim()
                val translatedResult = translateFn(cleanedTextToTranslate)
                if (!translatedResult.translated) {
                    return translatedResult
                }
                val spacingSymbol = "     "
                val sourceTextContainsSpacing = sourceText.startsWith(spacingSymbol)
                val result = if (sourceTextContainsSpacing) "${spacingSymbol}${translatedResult.text}" else translatedResult.text
                val symbolToInsert = if (sourceTextContainsSpacing) "$pipeSpecialSymbol$spacingSymbol" else pipeSpecialSymbol
                return TranslationResult(insertSymbolsWithRulesDoom2Rpg(result, symbolToInsert,
                            interval = splittedTexts.maxBy { it.length }.length), true)
            }
        }

        val newLineSymbol = "\n"
        val newLineIndex = sourceText.indexOf(newLineSymbol)

        val cleanedTextToTranslate = sourceText.replace(" - $newLineSymbol", "")
            .replace(" -$newLineSymbol", "").replace("-$newLineSymbol", "")
            .replace(newLineSymbol, " ").trim()

        val translatedResult = translateFn(cleanedTextToTranslate)

        if (translatedResult.translated && newLineIndex > 0) {
            return TranslationResult(insertSymbolsWithRulesDoom2Rpg(translatedResult.text, newLineSymbol, newLineIndex),
                true)
        }

        return translatedResult
    }

    private suspend fun translateDoomRpg2Text(
        sourceText: String,
        translateFn: suspend (String) -> TranslationResult
    ): TranslationResult {
        val defaultPipeSymbolIndex = 30

        var lootMenuText = false;
        var counter = 0

        if (sourceText.count { it == pipeSpecialSymbol[0] } > 1) {
            for (i in sourceText.indices) {
                counter++
                if (sourceText[i] == pipeSpecialSymbol[0]) {
                    lootMenuText = counter <= defaultPipeSymbolIndex
                    counter = 0
                }
            }
        }

        if (lootMenuText) {
            val splittedTexts = sourceText.split(pipeSpecialSymbol)
            val buffer = StringBuilder (sourceText.length * 2)
            for (text in splittedTexts) {
                val translatedResult = translateFn(text.trim())

                if (!translatedResult.translated){
                    return TranslationResult(sourceText, false)
                }
                buffer.append(translatedResult.text).append(pipeSpecialSymbol)
            }
            return TranslationResult(buffer.toString(), true)
        } else {
            val dialogBoxText = sourceText.contains(pipeSpecialSymbol)
            if (dialogBoxText) {
                val firstPipeSymbolIndex = sourceText.indexOf(pipeSpecialSymbol)
                val splittedTexts = sourceText.split(pipeSpecialSymbol)

                if (firstPipeSymbolIndex <= defaultPipeSymbolIndex) {
                    val title = splittedTexts[0].trim()
                    val cleanedTextToTranslate = sourceText.replace("-$pipeSpecialSymbol", "")
                        .replace(pipeSpecialSymbol, " ").replace(title, "").trim()

                    val translatedTitleResult = translateFn(title)
                    val translatedTextResult = translateFn(cleanedTextToTranslate)

                    if (translatedTitleResult.translated && translatedTextResult.translated) {
                        val result = "${translatedTitleResult.text}$pipeSpecialSymbol${insertSymbolsWithRulesDoom2Rpg(                             translatedTextResult.text,
                                pipeSpecialSymbol, interval = splittedTexts.maxBy { it.length }.length)}"
                        return TranslationResult(result, true)
                    } else {
                        return TranslationResult(sourceText, false)
                    }
                } else {
                    val cleanedTextToTranslate = sourceText.replace("-$pipeSpecialSymbol", "")
                        .replace(pipeSpecialSymbol, " ").trim()
                    val translatedResult = translateFn(cleanedTextToTranslate)
                    return if (!translatedResult.translated) translatedResult else
                        TranslationResult(
                            insertSymbolsWithRulesDoom2Rpg(
                                translatedResult.text, pipeSpecialSymbol,
                                interval = splittedTexts.maxBy { it.length }.length
                            ), true
                        )
                }
            }
        }

        val newLineSymbol = "\n"
        val newLineIndex = sourceText.indexOf(newLineSymbol)

        val cleanedTextToTranslate = sourceText.replace(" - $newLineSymbol", "")
            .replace(" -$newLineSymbol", "").replace("-$newLineSymbol", "")
            .replace(newLineSymbol, " ").trim()

        val translatedResult = translateFn(cleanedTextToTranslate)

        if (translatedResult.translated && newLineIndex > 0) {
            return TranslationResult(insertSymbolsWithRulesDoom2Rpg(translatedResult.text,newLineSymbol, newLineIndex),
                true)
        }

        return translatedResult
    }

    private suspend fun translateDoomRpgText(
        sourceText: String, textCameFromDialog: Boolean,
        translateFn: suspend (String) -> TranslationResult
    ): TranslationResult {
        if (textCameFromDialog) {
            val cleanedTextToTranslate = sourceText.replace("-$pipeSpecialSymbol", "")
                .replace(pipeSpecialSymbol, " ").trim()

            val translatedResult = translateFn(cleanedTextToTranslate)
            return if (!translatedResult.translated) translatedResult else
                TranslationResult(insertSymbolsWithRulesDoomRpg(translatedResult.text, pipeSpecialSymbol,
                    interval = 15), true)
        }

        val newLineSymbol = "\n"
        val newLineIndex = sourceText.indexOf(newLineSymbol)

        val cleanedTextToTranslate = sourceText.replace(" - $newLineSymbol", "")
            .replace(" -$newLineSymbol", "").replace("-$newLineSymbol", "")
            .replace(newLineSymbol, " ").trim()

        val translatedResult = translateFn(cleanedTextToTranslate)

        if (newLineIndex > 0) {
            return TranslationResult(insertSymbolsWithRulesDoomRpg( translatedResult.text,
                newLineSymbol, newLineIndex), translatedResult.translated)
        }

        return translatedResult
    }

    private fun insertSymbolsWithRulesDoomRpg(text: String, symbolToInsert : String, interval: Int): String {
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

    private fun insertSymbolsWithRulesDoom2Rpg(text: String, symbolToInsert : String, interval: Int): String {
        val sb = StringBuilder((text.length * 1.5f).roundToInt())
        var count = 0
        var i = 0

        while (i < text.length) {
            sb.append(text[i])
            count++
            if (count >= interval) {
                sb.append(symbolToInsert)
                count = 0
            }
            i++
        }

        return sb.toString()
    }
}