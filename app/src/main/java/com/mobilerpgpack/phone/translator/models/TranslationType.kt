package com.mobilerpgpack.phone.translator.models

enum class TranslationType {
    MLKit,
    OpusMt,
    M2M100,
    Small100,
    GoogleTranslate,
    BingTranslate;

    companion object{
        val DefaultTranslationType = MLKit

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