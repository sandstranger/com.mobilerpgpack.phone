package com.mobilerpgpack.phone.translator.models

import android.content.Context
import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator
import com.mobilerpgpack.ctranslate2proxy.Translator
import com.mobilerpgpack.phone.translator.TranslationManager
import org.koin.core.component.inject

class OpusMtTranslationModel (context: Context,
                              pathToModelFolder: String,
                              spmFile: String,
                              allowDownloadingOverMobile: Boolean = false):
    BaseM2M100TranslationModel(context,pathToModelFolder,spmFile, allowDownloadingOverMobile) {
    override val supportedLocales = listOf("ru")
    override val translationType = TranslationType.OpusMt
    override val zipFileId = "10db2umxImLHep0BoNllpJzdhqzryex1N"
    override val zipFileSha256 = "3f50481e2da47aeffd72278b1427561614002dbd8154a8cd0fe2896b5e12d57c"
    override val translator: Translator by inject<OpusMtTranslator>()
    override fun isLocaleSupported(locale: String) = locale == TranslationManager.RUSSIAN_LOCALE
}