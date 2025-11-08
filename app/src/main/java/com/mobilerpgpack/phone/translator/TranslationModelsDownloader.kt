package com.mobilerpgpack.phone.translator

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TranslationModelsDownloader : ITranslationModelsDownloader, KoinComponent {

    private val translationManager : ITranslationManager by inject()

    override suspend fun downloadModelIfNeeded(onProgress: (String) -> Unit) {
        if (translationManager.isTargetLocaleSupported()){
            translationManager.translationModel.downloadModelIfNeeded(onProgress)
        }
    }

    override fun cancelDownloadModel() = translationManager.translationModel.cancelDownloadingModel()
}