package com.mobilerpgpack.phone.ui.screen.utils

import android.content.Context
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.translator.models.TranslationType

fun buildTranslationsDescription (context: Context) : Collection<String>{
    val result : MutableList<String> = mutableListOf()

    for (type in TranslationType.entries) {
        if (BuildConfig.FDROID_BUILD && type == TranslationType.MLKit){
            continue
        }

        when (type) {
            TranslationType.MLKit ->
                result.add("${TranslationType.MLKit} ${context.getString(R.string.mlkit_description)}")
            TranslationType.OpusMt ->
                result.add("${TranslationType.OpusMt} ${context.getString(R.string.opus_mt_description)}")
            TranslationType.M2M100 ->
                result.add("${TranslationType.M2M100} ${context.getString(R.string.m2m_mt_description)}")
            TranslationType.Small100 ->
                result.add("${TranslationType.Small100} ${context.getString(R.string.small100_mt_description)}")
            TranslationType.GoogleTranslate ->
                result.add("${TranslationType.GoogleTranslate} ${context.getString(R.string.google_translate_description)}")
            TranslationType.BingTranslate ->
                result.add("${TranslationType.BingTranslate} ${context.getString(R.string.bing_translate_description)}")
            TranslationType.NLLB200 ->
                result.add("${TranslationType.NLLB200} ${context.getString(R.string.nllb200_translate_description)}")
        }
    }

    return result
}
