package com.mobilerpgpack.phone.translator.models

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.mobilerpgpack.ctranslate2proxy.M2M100Translator
import com.mobilerpgpack.ctranslate2proxy.Translator
import kotlinx.coroutines.runBlocking

class M2M100TranslationModel (
    private val context: Context,
    private val modelFile: String,
    private val spmFile: String,
    private val allowDownloadingOverMobile : Boolean = false
) : BaseM2M100TranslationModel(context, allowDownloadingOverMobile) {

    override val zipFileId: String = "1mUR8czA7-f-FK-Gw2orMNMTt9p0MgBYN"

    override val zipFileSha256: String = "86178730785f6f250fa60a1aa977585eaa361cc50ce7c3ea9fbe9ebe6016dbd1"

    override val zipFileName: String = "m2m100_ct2.zip"
    override val isModelDownloadedPrefsKey: Preferences.Key<Boolean> =
        booleanPreferencesKey("m2m100_model_downloaded")

    override val translator: Translator = M2M100Translator(modelFile,spmFile)

    override val translationType: TranslationType = TranslationType.M2M100

    init {
        runBlocking {
            isModelDownloaded = !needToDownloadModel()
        }
    }
}