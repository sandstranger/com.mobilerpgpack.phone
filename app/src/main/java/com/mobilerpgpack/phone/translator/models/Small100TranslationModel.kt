package com.mobilerpgpack.phone.translator.models

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.mobilerpgpack.ctranslate2proxy.Small100Translator
import com.mobilerpgpack.ctranslate2proxy.Translator
import com.mobilerpgpack.phone.translator.models.TranslationType

class Small100TranslationModel (
    private val context: Context,
    private val modelFile: String,
    private val spmFile: String,
    private val allowDownloadingOverMobile : Boolean = false
) : BaseM2M100TranslationModel(context, allowDownloadingOverMobile) {

    override val zipFileId: String = "1Adl4YxrLdSq_sn7kpeixneaof88YAfle"

    override val zipFileSha256: String =
        "b102ebb66e70654d7982b8fd09715baf341b28bb0216ae6b51553c0deb76811b"

    override val zipFileName: String = "small100_ct2.zip"
    override val isModelDownloadedPrefsKey: Preferences.Key<Boolean> =
        booleanPreferencesKey("small100_model_downloaded")

    override val translator: Translator = Small100Translator(modelFile, spmFile)

    override val translationType: TranslationType = TranslationType.Small100
}