package com.mobilerpgpack.phone.ui.screen.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.res.stringResource
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.translator.models.TranslationType

@Composable
fun buildTranslationsDescription () : Collection<String>{
    val result : MutableList<String> = mutableStateListOf()

    for (type in TranslationType.entries) {
        when (type) {
            TranslationType.MLKit ->
                result.add("${TranslationType.MLKit} ${stringResource(R.string.mlkit_description)}")
            TranslationType.OpusMt ->
                result.add("${TranslationType.OpusMt} ${stringResource(R.string.opus_mt_description)}")
            TranslationType.M2M100 ->
                result.add("${TranslationType.M2M100} ${stringResource(R.string.m2m_mt_description)}")
            TranslationType.Small100 ->
                result.add("${TranslationType.Small100} ${stringResource(R.string.small100_mt_description)}")
            TranslationType.GoogleTranslate ->
                result.add("${TranslationType.GoogleTranslate} ${stringResource(R.string.google_translate_description)}")
            TranslationType.BingTranslate ->
                result.add("${TranslationType.BingTranslate} ${stringResource(R.string.bing_translate_description)}")
            TranslationType.NLLB200 ->
                result.add("${TranslationType.NLLB200} ${stringResource(R.string.nllb200_translate_description)}")
        }
    }

    return result
}
