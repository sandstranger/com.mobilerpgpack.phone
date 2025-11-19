package com.mobilerpgpack.phone.translator

import com.mobilerpgpack.phone.translator.models.ITranslationModel
import com.mobilerpgpack.phone.translator.models.TranslationType
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class TranslationModelsDownloader : ITranslationModelsDownloader, KoinComponent {

    private val translationManager : ITranslationManager by inject()

    private val translationModels : Map<TranslationType, ITranslationModel> = get()

    override var allowDownloadingOveMobile: Boolean = false
        set(value) {
            translationModels.values.forEach {
                it.allowDownloadingOveMobile = value
            }
        }

    override suspend fun downloadModelIfNeeded(onProgress: (String) -> Unit) {
        if (translationManager.isTargetLocaleSupported()){
            translationManager.translationModel.downloadModelIfNeeded(onProgress)
        }
    }

    override fun cancelDownloadModel() = translationManager.translationModel.cancelDownloadingModel()
}