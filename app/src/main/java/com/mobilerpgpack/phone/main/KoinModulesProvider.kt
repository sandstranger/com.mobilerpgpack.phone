package com.mobilerpgpack.phone.main

import android.app.Activity
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.codekidlabs.storagechooser.StorageChooser
import com.github.sproctor.composepreferences.PreferenceHandler
import com.google.mlkit.common.model.RemoteModel
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.mobilerpgpack.ctranslate2proxy.M2M100Translator
import com.mobilerpgpack.ctranslate2proxy.NLLB200Translator
import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator
import com.mobilerpgpack.ctranslate2proxy.Small100Translator
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.Doom2RpgComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.Doom64ComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.Doom64EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.DoomRPGSeriesEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.DoomRpgComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.DoomRpgEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.WolfensteinRpgComposeSettings
import com.mobilerpgpack.phone.net.DriveDownloader
import com.mobilerpgpack.phone.net.IDriveDownloader
import com.mobilerpgpack.phone.translator.ITranslationManager
import com.mobilerpgpack.phone.translator.ITranslationModelsDownloader
import com.mobilerpgpack.phone.translator.IntervalMarkerTranslator
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.translator.TranslationModelsDownloader
import com.mobilerpgpack.phone.translator.models.BingTranslatorEndPoint
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
import com.mobilerpgpack.phone.ui.screen.SettingsScreen
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL2MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL2ScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL3MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL3ScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.ScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.doom2RPGButtons
import com.mobilerpgpack.phone.ui.screen.screencontrols.doom64Buttons
import com.mobilerpgpack.phone.ui.screen.screencontrols.doomRPGButtons
import com.mobilerpgpack.phone.ui.screen.screencontrols.wolfensteinButtons
import com.mobilerpgpack.phone.ui.screen.viewmodels.DownloadViewModel
import com.mobilerpgpack.phone.utils.CustomPreferenceHandler
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.IKeyCodesProvider
import com.mobilerpgpack.phone.utils.KeyCodesProvider
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.datastore.DataStoreSettings
import com.zxw.bingtranslateapi.BingTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class KoinModulesProvider(private val context: Context,
                          private val assetExtractor: IAssetExtractor,
                          private val scope: CoroutineScope) : KoinComponent  {

    private val clampButtonsMap = HashMap<EngineTypes, Preferences.Key<Boolean>>()
    private val pathToUserFolder = context.getExternalFilesDir("")!!.absolutePath
    private val preferencesStorage: PreferencesStorage = PreferencesStorage(context)

    val allModules : List<Module>

    private val mainModule = module {
        single<Context> { context }.withOptions { createdAtStart() }
        single<CoroutineScope> { scope }.withOptions {
            createdAtStart()
            named(COROUTINES_SCOPE)
        }
        single<PreferencesStorage> { preferencesStorage }.withOptions { createdAtStart() }
        single <TranslationDatabase> { TranslationDatabase.createInstance(get()) }
        single<String> { pathToUserFolder }.withOptions {
            named(USER_ROOT_FOLDER_NAMED_KEY)
            createdAtStart()
        }
        single { assetExtractor }.bind()
    }

    private val httpModule = module {
        factory <Retrofit> { (retrofitKey : String) -> Retrofit.Builder()
            .baseUrl(retrofitKey )
            .addConverterFactory(GsonConverterFactory.create())
            .build() }

        factory <OkHttpClient> { OkHttpClient() }
        factory <IDriveDownloader> { (apiKey: String) -> DriveDownloader(apiKey) }
    }

    private val translationModule = module {
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

        factory { CoroutineScope(Dispatchers.IO) }.withOptions { named(COROUTINES_SCOPE) }

        factory { (sourceLocale: String, targetLocale: String) ->
            MLKitTranslationModel.buildMlkitTranslator(sourceLocale, targetLocale) }

        factory { (allowDownloadingOveMobile: Boolean) -> MLKitTranslationModel.buildConditions(allowDownloadingOveMobile) }

        factory<RemoteModel> { (modelCache : MutableMap<String, TranslateRemoteModel>,langCode: String) ->
            MLKitTranslationModel.getRemoteModel(modelCache,langCode) }

        single <MLKitTranslationModel> {  MLKitTranslationModel(get(),
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

        single<M2M100TranslationModel> { M2M100TranslationModel (get(), pathToM2M100Model, m2m100smpFile,
            allowDownloadingModelsOverMobile) }

        val pathToSmall100Model = "${pathToUserFolder}${File.separator}small100_ct2"
        val small100SmpFile = "${pathToSmall100Model}${File.separator}sentencepiece.model"

        single<Small100Translator> { Small100Translator(pathToSmall100Model,small100SmpFile) }

        single <Small100TranslationModel> { Small100TranslationModel (get(), pathToSmall100Model, small100SmpFile,
            allowDownloadingModelsOverMobile) }

        single <BingTranslator> { BingTranslator(get ()) }
        singleOf(::BingTranslatorEndPoint).bind()
        singleOf(::BingTranslatorModel).bind()

        val pathToNLLB200Model = "${pathToUserFolder}${File.separator}nllb-200-distilled-600M"
        val nLLB200SmpFile = "${pathToNLLB200Model}${File.separator}sentencepiece.model"

        single<NLLB200Translator> { NLLB200Translator(pathToNLLB200Model,nLLB200SmpFile) }

        single<NLLB200TranslationModel> { NLLB200TranslationModel (get(), pathToNLLB200Model, nLLB200SmpFile,
            allowDownloadingModelsOverMobile) }

        singleOf(::GoogleTranslateV2).bind()

        single<Map<TranslationType, ITranslationModel>> {
            mutableMapOf<TranslationType, ITranslationModel>().apply {
                this[TranslationType.MLKit] = get<MLKitTranslationModel>()
                this[TranslationType.OpusMt] = get<OpusMtTranslationModel>()
                this[TranslationType.M2M100] = get<M2M100TranslationModel>()
                this[TranslationType.Small100] = get<Small100TranslationModel>()
                this[TranslationType.NLLB200] = get<NLLB200TranslationModel>()
                this[TranslationType.BingTranslate] = get<BingTranslatorModel>()
                this[TranslationType.GoogleTranslate] = get<GoogleTranslateV2>()
            }
        }

        single <ITranslationModel> { get<Map<TranslationType, ITranslationModel>>()[activeTranslationModelType]!! }
            .withOptions {
            named(ACTIVE_TRANSLATION_MODEL_KEY)
        }
        singleOf(::IntervalMarkerTranslator).bind()
        singleOf<ITranslationManager>(::TranslationManager)
        singleOf<ITranslationModelsDownloader>(::TranslationModelsDownloader)
    }

    @OptIn(ExperimentalSettingsApi::class, ExperimentalSettingsImplementation::class)
    val composeModule = module {
        factory <StorageChooser> { (requestOnlyDirectory: Boolean, activity: Activity) ->
            val builder = StorageChooser.Builder()
                .withActivity(activity)
                .withFragmentManager(activity.fragmentManager)
                .withMemoryBar(true)
                .allowCustomPath(true)

            if (requestOnlyDirectory) {
                builder.setType(StorageChooser.DIRECTORY_CHOOSER)
            } else {
                builder.setType(StorageChooser.FILE_PICKER)
                    .filter(StorageChooser.FileType.ARCHIVE)
            }

            builder.build()
        }

        factory { (engineType : EngineTypes) -> getClampButtonPrefsKey(engineType) }

        viewModelOf(::DownloadViewModel)
        singleOf(::SettingsScreen)
        singleOf <IScreenController>(::SDL2ScreenController).withOptions {
            named(SDL2ScreenController.SDL2_SCREEN_CONTROLLER_NAME)
        }
        singleOf <IScreenController>(::ScreenController).withOptions {
            named(ScreenController.COMMON_SCREEN_CONTROLLER_NAME)
        }
        singleOf <IScreenController>(::SDL3ScreenController).withOptions {
            named(SDL3ScreenController.SDL3_SCREEN_CONTROLLER_NAME)
        }

        singleOf<SDL2MouseIcon>(::SDL2MouseIcon)
        singleOf<SDL3MouseIcon>(::SDL3MouseIcon)
        singleOf<IKeyCodesProvider>(::KeyCodesProvider)
        factory <PreferenceHandler> { (settings : DataStoreSettings) -> CustomPreferenceHandler(settings) }
    }

    val enginesModule = module {
        single  {
            val nativeLibs = arrayOf(gl4esLibraryName,
                SDL3_NATIVE_LIB_NAME,
                PNG_NATIVE_LIB_NAME,
                FMOD_NATIVE_LIB_NAME,
                DOOM64_MAIN_ENGINE_LIB)

            Doom64EngineInfo(DOOM64_MAIN_ENGINE_LIB,
                nativeLibs,
                doom64Buttons,
                preferencesStorage.doom64CommandLineArgsString) }.withOptions {
            named(EngineTypes.Doom64ExPlus.toString())
            bind<IEngineInfo>()
        }

        single<IEngineUIController> { Doom64ComposeSettings(doom64Buttons) }
            .withOptions { named(EngineTypes.Doom64ExPlus.toString()) }

        single {
            val nativeLibs = arrayOf(gl4esLibraryName,
                OBOE_NATIVE_LUB_NAME,
                FLUIDSYNTH_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                GME_NATIVE_LIB_NAME,
                SDL2_MIXER_NATIVE_LIB_NAME,
                SDL2_TTF_NATIVE_LIB_NAME,
                TRANSLATOR_NATIVE_LIB_NAME,
                DOOMRPG_MAIN_ENGINE_LIB)

            DoomRpgEngineInfo(DOOMRPG_MAIN_ENGINE_LIB,
                nativeLibs,
                doomRPGButtons
        ) }.withOptions {
            named(EngineTypes.DoomRpg.toString())
            bind<IEngineInfo>()
        }

        single<IEngineUIController> { DoomRpgComposeSettings(wolfensteinButtons) }
            .withOptions { named(EngineTypes.DoomRpg.toString()) }

        single {
            val nativeLibs = arrayOf(gl4esLibraryName,
                OBOE_NATIVE_LUB_NAME,
                OPENAL_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                SDL2_TTF_NATIVE_LIB_NAME,
                TRANSLATOR_NATIVE_LIB_NAME,
                DOOM2RPG_MAIN_ENGINE_LIB)

            DoomRPGSeriesEngineInfo(DOOM2RPG_MAIN_ENGINE_LIB,
                nativeLibs,
                doom2RPGButtons,
                EngineTypes.Doom2Rpg,
                preferencesStorage.pathToDoom2RpgIpaFile)
        }.withOptions {
            named(EngineTypes.Doom2Rpg.toString())
            bind<IEngineInfo>()
        }

        single<IEngineUIController> { Doom2RpgComposeSettings(doom2RPGButtons) }
            .withOptions { named(EngineTypes.Doom2Rpg.toString()) }

        single {
            val nativeLibs = arrayOf(gl4esLibraryName,
                OBOE_NATIVE_LUB_NAME,
                OPENAL_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                SDL2_TTF_NATIVE_LIB_NAME,
                TRANSLATOR_NATIVE_LIB_NAME,
                WOLFENSTEINRPG_MAIN_ENGINE_LIB)

            DoomRPGSeriesEngineInfo(WOLFENSTEINRPG_MAIN_ENGINE_LIB,
                nativeLibs,
                wolfensteinButtons,
                EngineTypes.WolfensteinRpg,
                preferencesStorage.pathToWolfensteinRpgIpaFile)
        }.withOptions {
            named(EngineTypes.WolfensteinRpg.toString())
            bind<IEngineInfo>()
        }

        single<IEngineUIController> { WolfensteinRpgComposeSettings(wolfensteinButtons) }
            .withOptions { named(EngineTypes.WolfensteinRpg.toString()) }
    }

    init {
        allModules = listOf<Module>(mainModule,httpModule,translationModule, composeModule, enginesModule)
    }

    private fun getClampButtonPrefsKey (engineType: EngineTypes) = clampButtonsMap.getOrPut(engineType) {
        booleanPreferencesKey("clamp_${engineType.toString().lowercase()}_buttons")
    }

    companion object{
        const val USER_ROOT_FOLDER_NAMED_KEY = "user_root_folder"
        const val TARGET_LOCALE_NAMES_KEY = "target_locale"
        const val COROUTINES_SCOPE = "courotines_scope"
        const val ACTIVE_TRANSLATION_MODEL_KEY = "active_translation_model"
    }
}

