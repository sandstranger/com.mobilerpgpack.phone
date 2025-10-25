package com.mobilerpgpack.phone.translator.models

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.mobilerpgpack.ctranslate2proxy.M2M100Translator
import com.mobilerpgpack.ctranslate2proxy.Translator
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.java.KoinJavaComponent.get

class M2M100TranslationModel (
    private val context: Context,
    private val modelFile: String,
    private val spmFile: String,
    private val allowDownloadingOverMobile : Boolean = false
) : BaseM2M100TranslationModel(context,modelFile,spmFile, allowDownloadingOverMobile) {

    override val zipFileId: String = "1mUR8czA7-f-FK-Gw2orMNMTt9p0MgBYN"

    override val zipFileSha256: String = "86178730785f6f250fa60a1aa977585eaa361cc50ce7c3ea9fbe9ebe6016dbd1"

    override val translator: Translator = get(M2M100Translator::class.java)

    override val translationType: TranslationType = TranslationType.M2M100
}