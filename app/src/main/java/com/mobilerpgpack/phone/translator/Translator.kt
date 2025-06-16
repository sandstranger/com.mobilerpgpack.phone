package com.mobilerpgpack.phone.translator

import android.content.Context
import android.content.res.Resources
import android.os.Build

object Translator {

    fun init (context: Context){

    }

    fun setLocale (newLocale : String){

    }

    private fun getSystemLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales.get(0).language
        } else {
            Resources.getSystem().configuration.locale.language
        }
    }
}