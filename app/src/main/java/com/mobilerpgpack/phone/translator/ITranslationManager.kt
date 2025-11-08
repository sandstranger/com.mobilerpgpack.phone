package com.mobilerpgpack.phone.translator

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.models.ITranslationModel
import com.mobilerpgpack.phone.translator.models.TranslationType
import kotlinx.coroutines.flow.Flow

interface ITranslationManager{

    val translationModel : ITranslationModel

    var inGame : Boolean

    var activeEngine: EngineTypes

    var allowDownloadingOveMobile: Boolean

    var activeTranslationType : TranslationType

    fun terminate()

    fun isTargetLocaleSupported () : Boolean

    fun isTranslationSupportedAsFlow(): Flow<Boolean>
}