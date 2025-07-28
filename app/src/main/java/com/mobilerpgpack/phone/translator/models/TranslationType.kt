package com.mobilerpgpack.phone.translator.models

import com.mobilerpgpack.phone.BuildConfig

enum class TranslationType {
    MLKit,
    OpusMt,
    M2M100,
    Small100,
    GoogleTranslate,
    BingTranslate,
    NLLB200;

    companion object{
        val DefaultTranslationType = if (!BuildConfig.FDROID_BUILD) MLKit else Small100

        fun getTranslationType (inputText : String) : TranslationType{
            TranslationType.entries.forEach {
                if (inputText.startsWith(it.toString())){
                    return it
                }
            }

            return DefaultTranslationType
        }
    }
}