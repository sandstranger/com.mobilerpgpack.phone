package com.mobilerpgpack.phone.main

import android.content.Context
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translator
import com.mobilerpgpack.ctranslate2proxy.M2M100Translator
import com.mobilerpgpack.ctranslate2proxy.NLLB200Translator
import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator
import com.mobilerpgpack.ctranslate2proxy.Small100Translator
import com.mobilerpgpack.phone.net.DriveDownloader
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.translator.models.BingTranslatorModel
import com.mobilerpgpack.phone.translator.models.GoogleTranslateV2
import com.mobilerpgpack.phone.translator.models.ITranslationModel
import com.mobilerpgpack.phone.translator.models.M2M100TranslationModel
import com.mobilerpgpack.phone.translator.models.MLKitTranslationModel
import com.mobilerpgpack.phone.translator.models.NLLB200TranslationModel
import com.mobilerpgpack.phone.translator.models.OpusMtTranslationModel
import com.mobilerpgpack.phone.translator.models.Small100TranslationModel
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.translator.sql.TranslationDatabase
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.zxw.bingtranslateapi.BingTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent
import java.io.File
import kotlin.collections.set
import org.koin.java.KoinJavaComponent.get
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.Boolean

class KoinModulesProvider(private val context: Context, private val scope: CoroutineScope) {

    private val pathToUserFolder = context.getExternalFilesDir("")!!.absolutePath
    private val preferencesStorage: PreferencesStorage = PreferencesStorage(context)

    val mainModule = module {
        single<Context> { context }.withOptions { createdAtStart() }
        single<CoroutineScope> { scope }.withOptions { createdAtStart() }
        single<PreferencesStorage> { preferencesStorage }.withOptions { createdAtStart() }
        single <TranslationDatabase> { TranslationDatabase.createInstance(context) }
        single<String> { pathToUserFolder }.withOptions {
            named(USER_ROOT_FOLDER_NAMED_KEY)
            createdAtStart()
        }
    }

    val httpModule = module {
        factory { (retrofitKey : String) -> Retrofit.Builder()
            .baseUrl(retrofitKey )
            .addConverterFactory(GsonConverterFactory.create())
            .build() }

        factory { OkHttpClient }
        factory { (apiKey: String) -> DriveDownloader(apiKey) }
    }

    val translationModule = module {
        var activeTranslationModelType : TranslationType
        var allowDownloadingModelsOverMobile = false

        runBlocking {
            allowDownloadingModelsOverMobile = preferencesStorage.allowDownloadingModelsOverMobile.first()
            activeTranslationModelType = enumValueOf<TranslationType>(preferencesStorage.translationModelType.first())
        }

        val targetLocale = TranslationManager.getSystemLocale()

        single { targetLocale }.withOptions {
            named(TARGET_LOCALE_NAMES_KEY)
            createdAtStart()
        }

        factory { CoroutineScope(Dispatchers.IO) }.withOptions { named(COROUTINES_TRANSLATION_SCOPE) }

        factory { (sourceLocale: String, targetLocale: String) ->
            MLKitTranslationModel.buildMlkitTranslator(sourceLocale, targetLocale) }

        factory { (allowDownloadingOveMobile: Boolean) -> MLKitTranslationModel.buildConditions(allowDownloadingOveMobile) }

        factory { (modelCache : MutableMap<String, TranslateRemoteModel>,langCode: String) ->
            MLKitTranslationModel.getRemoteModel(modelCache,langCode) }

        single <MLKitTranslationModel> {  MLKitTranslationModel(context,
            TranslationManager.sourceLocale, targetLocale, allowDownloadingModelsOverMobile) }

        val pathToOptModel = "${pathToUserFolder}${File.separator}opus-ct2-en-ru"
        val optModelSourceProcessor = "${pathToOptModel}${File.separator}source.spm"
        val optModelTargetProcessor = "${pathToOptModel}${File.separator}target.spm"

        single<OpusMtTranslator> { OpusMtTranslator(pathToOptModel,
            optModelSourceProcessor, optModelTargetProcessor) }

        singleOf(::OpusMtTranslationModel).bind()

        val pathToM2M100Model = "${pathToUserFolder}${File.separator}m2m100_ct2"
        val m2m100smpFile = "${pathToM2M100Model}${File.separator}sentencepiece.model"

        single <M2M100Translator> { M2M100Translator(pathToM2M100Model,m2m100smpFile) }

        single<M2M100TranslationModel> { M2M100TranslationModel (context, pathToM2M100Model, m2m100smpFile,
            allowDownloadingModelsOverMobile) }

        val pathToSmall100Model = "${pathToUserFolder}${File.separator}small100_ct2"
        val small100SmpFile = "${pathToSmall100Model}${File.separator}sentencepiece.model"

        single<Small100Translator> { Small100Translator(pathToSmall100Model,small100SmpFile) }

        single <Small100TranslationModel> { Small100TranslationModel (context, pathToSmall100Model, small100SmpFile, allowDownloadingOveMobile) }

        single <BingTranslator> { BingTranslator(get ()) }
        singleOf(::BingTranslatorModel).bind()

        val pathToNLLB200Model = "${pathToUserFolder}${File.separator}nllb-200-distilled-600M"
        val nLLB200SmpFile = "${pathToNLLB200Model}${File.separator}sentencepiece.model"

        single<NLLB200Translator> { NLLB200Translator(pathToNLLB200Model,nLLB200SmpFile) }

        single<NLLB200TranslationModel> { NLLB200TranslationModel (context, pathToNLLB200Model, nLLB200SmpFile,
            allowDownloadingModelsOverMobile) }

        singleOf(::GoogleTranslateV2).bind()

        val translationModels = HashMap<TranslationType, ITranslationModel>()
        translationModels[TranslationType.MLKit] = get(MLKitTranslationModel::class.java)
        translationModels[TranslationType.OpusMt] = get(OpusMtTranslationModel::class.java)
        translationModels[TranslationType.M2M100] = get(M2M100TranslationModel::class.java)
        translationModels[TranslationType.Small100] = get(Small100TranslationModel::class.java)
        translationModels[TranslationType.NLLB200] = get(NLLB200TranslationModel::class.java)
        translationModels[TranslationType.BingTranslate] = get(BingTranslatorModel::class.java)
        translationModels[TranslationType.GoogleTranslate] = get(GoogleTranslateV2::class.java)

        single <ITranslationModel> { translationModels[activeTranslationModelType]!! }.withOptions {
            named(ACTIVE_TRANSLATION_MODEL_KEY)
        }
        single { translationModels }.bind()
        singleOf(::TranslationManager).bind()
    }

    companion object{
        const val USER_ROOT_FOLDER_NAMED_KEY = "user_root_folder"

        const val TARGET_LOCALE_NAMES_KEY = "target_locale"

        const val COROUTINES_TRANSLATION_SCOPE = "courotines_translation_scope"

        const val ACTIVE_TRANSLATION_MODEL_KEY = "active_translation_model"
    }
}

