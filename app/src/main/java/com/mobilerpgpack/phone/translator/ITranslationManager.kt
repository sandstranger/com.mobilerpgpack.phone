package com.mobilerpgpack.phone.translator

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.models.TranslationType
import kotlinx.coroutines.flow.Flow

interface ITranslationManager{
    var inGame : Boolean

    var activeEngine: EngineTypes

    var allowDownloadingOveMobile: Boolean

    var activeTranslationType : TranslationType

    fun terminate()

    fun isTranslationSupportedAsFlow(): Flow<Boolean>
}