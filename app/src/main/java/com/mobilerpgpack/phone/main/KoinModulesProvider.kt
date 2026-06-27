package com.mobilerpgpack.phone.main

import android.app.Activity
import android.content.Context
import com.codekidlabs.storagechooser.StorageChooser
import com.google.mlkit.common.model.RemoteModel
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.mobilerpgpack.ctranslate2proxy.M2M100Translator
import com.mobilerpgpack.ctranslate2proxy.NLLB200Translator
import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator
import com.mobilerpgpack.ctranslate2proxy.Small100Translator
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ArxLibertatisEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ArxLibertatisPreferenceStorage
import com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ui.ArxLibertatisComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ui.ArxLibertatisComposeSettingsViewModel
import com.mobilerpgpack.phone.engine.engineinfo.doom64.Doom64ComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.doom64.Doom64EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.doom64.Doom64EnhancedEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.DoomBFAEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.DoomBFAPreferencesStorage
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.ui.DoomBFAComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.ui.DoomBFAComposeSettings.DoomBFAGraphicsScreen
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.ui.DoomBFAViewModel
import com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries.Doom2RPGEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries.Doom2RpgComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries.DoomRpgComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries.DoomRpgEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries.WolfensteinRPGEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries.WolfensteinRpgComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.fteqw.FTEQWComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.fteqw.FTEQWEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.fteqw.FTEQWPreferencesStorage
import com.mobilerpgpack.phone.engine.engineinfo.fteqw.Quake2Games
import com.mobilerpgpack.phone.engine.engineinfo.perfectdark.PerfectDarkComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.perfectdark.PerfectDarkEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.perfectdark.PerfectDarkPreferencesStorage
import com.mobilerpgpack.phone.engine.engineinfo.perfectdark.PerfectDarkRomVersions
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings.PsyDoomAudioSettingsScreen
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings.PsyDoomCheatsSettingsScreen
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings.PsyDoomGameSettingsScreen
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings.PsyDoomGraphicsSettingsScreen
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings.PsyDoomInputSettingsScreen
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings.PsyDoomLauncherSettingsScreen
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings.PsyDoomMoreSettingsScreen
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings.PsyDoomMultiplayerSettingsScreen
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettingsViewModel
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomPreferencesStorage
import com.mobilerpgpack.phone.engine.engineinfo.utils.Doom64ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsFilesUpdater.Companion.updateFiles
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.PsyDoomModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.UZDoomModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.ModsExporterViewModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.SettingScreen
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomComposeSettings.UZDoomMoreSettingsScreen
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomComposeSettingsViewModel
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomPreferenceStorage
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UzDoomIniProvider
import com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer.VanillaConquerComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer.VanillaConquerEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer.VanillaConquerGames
import com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer.VanillaConquerPreferencesStorage
import com.mobilerpgpack.phone.engine.engineinfo.widelands.WidelandsComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.widelands.WidelandsEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.widelands.WidelandsViewModel
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
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.viewmodel.FileExplorerViewModel
import com.mobilerpgpack.phone.ui.screen.ComposeScreen
import com.mobilerpgpack.phone.ui.screen.PermissionScreen
import com.mobilerpgpack.phone.ui.screen.SettingsScreen
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.arxLibertatisOnScreenStickControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.doom2RPGControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.doom64AbsoluteTouchControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.doom64OnScreenStickControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.doomBFAScreenStickControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.doomRPGControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.psyDoomAbsoluteTouchControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.psyDoomOnScreenStickControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.uzDoomAbsoluteTouchControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.uzDoomOnScreenStickControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.doomseries.wolfensteinRpgLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.fteQWOnScreenStickControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.perfectDarkAbsoluteTouchControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.perfectDarkOnScreenStickControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.vanillaConquerOnScreenStickControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.layout.widelandsAbsoluteControlsLayout
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLKeyboard
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3Keyboard
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ScreenController
import com.mobilerpgpack.phone.ui.screen.viewmodels.DownloadViewModel
import com.mobilerpgpack.phone.ui.screen.viewmodels.SettingsScreenViewModel
import com.mobilerpgpack.phone.ui.viewmodel.MainActivityViewModel
import com.mobilerpgpack.phone.utils.AssetExtractor
import com.mobilerpgpack.phone.utils.GpuProbe
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.IKeyCodesProvider
import com.mobilerpgpack.phone.utils.KeyCodesProvider
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.SDL3GyroInput
import com.mobilerpgpack.phone.utils.SwappyJNILayer
import com.mobilerpgpack.phone.utils.VirtualControllerJnaLayer
import com.mobilerpgpack.phone.utils.buildFinalPathToUserFolder
import com.mobilerpgpack.phone.utils.sharesprefs.SharedPrefsDao
import com.mobilerpgpack.phone.utils.sharesprefs.SharedPrefsDatabase
import com.zxw.bingtranslateapi.BingTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class KoinModulesProvider(private val context: Context) : KoinComponent  {
    val allModules : List<Module>

    private val mainModule = module {
        single<Context> { context }.withOptions { createdAtStart() }
        single<PreferencesStorage> { PreferencesStorage() }.withOptions { createdAtStart() }
        single <TranslationDatabase> { TranslationDatabase.createInstance(get()) }
        singleOf <IAssetExtractor> (::AssetExtractor)
        singleOf(::SwappyJNILayer)
        single <SharedPrefsDao> { SharedPrefsDatabase.createInstance().dao() }
        singleOf(::SDL3Keyboard).withOptions {
            named(KeyboardType.SDL3Keyboard.name)
            bind<SDLKeyboard>()
        }
        single <File> {
            val prefsStorage : PreferencesStorage = get()
            val pathToUserFolder = prefsStorage.pathToRootUserFolder.value!!
            context.buildFinalPathToUserFolder(pathToUserFolder)
        }.withOptions {
            named(ROOT_USER_DIRECTORY_KEY)
        }
        factory <SDL3GyroInput> { (ctx: Context, engineInfo: IEngineInfo) -> SDL3GyroInput(ctx, engineInfo) }
        factory <CoroutineScope> { CoroutineScope(Dispatchers.IO + SupervisorJob()) }.withOptions {
            named(BACKGROUND_THREAD_COROUTINE_KEY)
        }
        factory <CoroutineScope> { CoroutineScope(Dispatchers.Main + SupervisorJob()) }.withOptions {
            named(MAIN_THREAD_COROUTINE_KEY)
        }
        factory <File>{ (pathToFile : String) -> File(get<File>(named(ROOT_USER_DIRECTORY_KEY)),
            pathToFile) }
        singleOf(::VirtualControllerJnaLayer)
        singleOf(::GpuProbe)
    }

    private val httpModule = module {
        factory <Retrofit> { (retrofitKey : String) -> Retrofit.Builder()
            .baseUrl(retrofitKey )
            .addConverterFactory(GsonConverterFactory.create())
            .build() }

        factory <OkHttpClient> { OkHttpClient() }
        factory <IDriveDownloader> { (apiKey: String) -> DriveDownloader(apiKey) }
    }

    private val allowDownloadingModelsOverMobile : Boolean
        get() {
            val preferencesStorage : PreferencesStorage = get ()
            return preferencesStorage.allowDownloadingModelsOverMobile.value!!
        }

    private val translationModule = module {
        val targetLocale = TranslationManager.getSystemLocale()

        single { targetLocale }.withOptions {
            named(TARGET_LOCALE_NAMES_KEY)
            createdAtStart()
        }

        factory { (sourceLocale: String, targetLocale: String) ->
            MLKitTranslationModel.buildMlkitTranslator(sourceLocale, targetLocale) }

        factory { (allowDownloadingOveMobile: Boolean) -> MLKitTranslationModel.buildConditions(allowDownloadingOveMobile) }

        factory<RemoteModel> { (modelCache : MutableMap<String, TranslateRemoteModel>,langCode: String) ->
            MLKitTranslationModel.getRemoteModel(modelCache,langCode) }

        single <MLKitTranslationModel> {  MLKitTranslationModel(get(),
            TranslationManager.SOURCE_LOCALE, targetLocale, allowDownloadingModelsOverMobile) }

        val pathToOptModel = "opus-ct2-en-ru"
        val optModelSourceProcessor = "${pathToOptModel}${File.separator}source.spm"
        val optModelTargetProcessor = "${pathToOptModel}${File.separator}target.spm"
        val optModelSMPFILE = "${pathToOptModel}${File.separator}model.bin"

        single<OpusMtTranslator> {
            OpusMtTranslator(pathToOptModel,
            optModelSourceProcessor, optModelTargetProcessor) }

        single<OpusMtTranslationModel> {
            OpusMtTranslationModel (get(), pathToOptModel, optModelSMPFILE,
                allowDownloadingModelsOverMobile) }
        single <M2M100Translator> {
            val pathToM2M100Model = "m2m100_ct2"
            val m2m100smpFile = "${pathToM2M100Model}${File.separator}sentencepiece.model"

            M2M100Translator(pathToM2M100Model,m2m100smpFile) }

        single<M2M100TranslationModel> {
            val pathToM2M100Model = "m2m100_ct2"
            val m2m100smpFile = "${pathToM2M100Model}${File.separator}sentencepiece.model"
            M2M100TranslationModel (get(), pathToM2M100Model, m2m100smpFile,
            allowDownloadingModelsOverMobile) }


        single<Small100Translator> {
            val pathToSmall100Model = "small100_ct2"
            val small100SmpFile = "${pathToSmall100Model}${File.separator}sentencepiece.model"
            Small100Translator(pathToSmall100Model,small100SmpFile) }

        single <Small100TranslationModel> {
            val pathToSmall100Model = "small100_ct2"
            val small100SmpFile = "${pathToSmall100Model}${File.separator}sentencepiece.model"
            Small100TranslationModel (get(), pathToSmall100Model, small100SmpFile,
            allowDownloadingModelsOverMobile) }

        single <BingTranslator> { BingTranslator(get ()) }
        singleOf(::BingTranslatorEndPoint).bind()
        singleOf(::BingTranslatorModel).bind()

        single<NLLB200Translator> {
            val pathToNLLB200Model = "nllb-200-distilled-600M"
            val nLLB200SmpFile = "${pathToNLLB200Model}${File.separator}sentencepiece.model"
            NLLB200Translator(pathToNLLB200Model,nLLB200SmpFile) }

        single<NLLB200TranslationModel> {
            val pathToNLLB200Model = "nllb-200-distilled-600M"
            val nLLB200SmpFile = "${pathToNLLB200Model}${File.separator}sentencepiece.model"
            NLLB200TranslationModel (get(), pathToNLLB200Model, nLLB200SmpFile,
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

        single <ITranslationModel> {
            val preferencesStorage : PreferencesStorage = get ()
            var activeTranslationModelType = enumValueOf<TranslationType>(preferencesStorage.translationModelType.value!!)
            get<Map<TranslationType, ITranslationModel>>()[activeTranslationModelType]!! }
            .withOptions {
            named(ACTIVE_TRANSLATION_MODEL_KEY)
        }
        singleOf(::IntervalMarkerTranslator).bind()
        singleOf<ITranslationManager>(::TranslationManager)
        singleOf<ITranslationModelsDownloader>(::TranslationModelsDownloader)
    }

    @Suppress("DEPRECATION")
    private val composeModule = module {
        viewModelOf(::FileExplorerViewModel)

        factory <StorageChooser> { (requestMode: RequestPathMode, predefinedPath : String, activity: Activity) ->
            StorageChooser.Builder()
                .withActivity(activity)
                .withFragmentManager(activity.fragmentManager)
                .withMemoryBar(false)
                .allowCustomPath(true)
                .setType( if (requestMode == RequestPathMode.Directory)
                StorageChooser.DIRECTORY_CHOOSER else StorageChooser.FILE_PICKER)
                .apply {
                    if (predefinedPath.isNotEmpty()) {
                        withPredefinedPath(predefinedPath)
                    }
                }
                .build()
        }

        viewModelOf(::ModsExporterViewModel)
        viewModelOf(::DownloadViewModel)
        viewModelOf(::SettingsScreenViewModel)
        viewModelOf(::MainActivityViewModel)
        singleOf(::SettingsScreen).bind<ComposeScreen>()
        singleOf <IScreenController>(::SDL3ScreenController).withOptions {
            named(SDL3ScreenController.SDL3_SCREEN_CONTROLLER_NAME)
        }

        singleOf<SDL3MouseIcon>(::SDL3MouseIcon)
        singleOf<IKeyCodesProvider>(::KeyCodesProvider)
        singleOf(::PermissionScreen).bind<ComposeScreen>()
        single<Collection<ComposeScreen>> { getAll<ComposeScreen>() }.withOptions {
            named(ALL_COMPOSE_SCREENS)
        }
    }

    private val doomRpgSeriesModule = module {

        single<ControlsProvider> { ControlsProvider(EngineTypes.DoomRpg, hashMapOf(
            ControlsType.AbsoluteTouchControls to doomRPGControlsLayout
        )) }.withOptions {
            named(EngineTypes.DoomRpg.name) }

        single {
            val nativeLibs = arrayOf(OBOE_NATIVE_LUB_NAME,
                FLUIDSYNTH_NATIVE_LIB_NAME,
                SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                MPG123_NATIVE_LIB_NAME,
                GME_NATIVE_LIB_NAME,
                SDL2_MIXER_NATIVE_LIB_NAME,
                SDL2_TTF_NATIVE_LIB_NAME,
                TRANSLATOR_NATIVE_LIB_NAME,
                DOOMRPG_MAIN_ENGINE_LIB)

            DoomRpgEngineInfo(DOOMRPG_MAIN_ENGINE_LIB, nativeLibs) }.withOptions {
            named(EngineTypes.DoomRpg.toString())
            bind<IEngineInfo>()
        }

        single<IEngineUIController> { DoomRpgComposeSettings() }
            .withOptions { named(EngineTypes.DoomRpg.toString()) }

        single<ControlsProvider> { ControlsProvider(EngineTypes.Doom2Rpg, hashMapOf(
            ControlsType.AbsoluteTouchControls to doom2RPGControlsLayout
        )) }.withOptions {
            named(EngineTypes.Doom2Rpg.name) }

        single {
            val nativeLibs = arrayOf(OBOE_NATIVE_LUB_NAME,
                OPENAL_NATIVE_LIB_NAME,
                SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                SDL2_TTF_NATIVE_LIB_NAME,
                TRANSLATOR_NATIVE_LIB_NAME,
                DOOM2RPG_MAIN_ENGINE_LIB)

            Doom2RPGEngineInfo(DOOM2RPG_MAIN_ENGINE_LIB, nativeLibs)
        }.withOptions {
            named(EngineTypes.Doom2Rpg.toString())
            bind<IEngineInfo>()
        }

        single<IEngineUIController> { Doom2RpgComposeSettings() }
            .withOptions { named(EngineTypes.Doom2Rpg.toString()) }

        single<ControlsProvider> { ControlsProvider(EngineTypes.WolfensteinRpg, hashMapOf(
            ControlsType.AbsoluteTouchControls to wolfensteinRpgLayout
        )) }.withOptions {
            named(EngineTypes.WolfensteinRpg.name) }

        single {
            val nativeLibs = arrayOf(OBOE_NATIVE_LUB_NAME,
                OPENAL_NATIVE_LIB_NAME,
                SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                SDL2_TTF_NATIVE_LIB_NAME,
                TRANSLATOR_NATIVE_LIB_NAME,
                WOLFENSTEINRPG_MAIN_ENGINE_LIB)

            WolfensteinRPGEngineInfo(WOLFENSTEINRPG_MAIN_ENGINE_LIB, nativeLibs)
        }.withOptions {
            named(EngineTypes.WolfensteinRpg.toString())
            bind<IEngineInfo>()
        }

        single<IEngineUIController> { WolfensteinRpgComposeSettings() }
            .withOptions { named(EngineTypes.WolfensteinRpg.toString()) }
    }

    private val doom64RegisterModule = module {
        single<ControlsProvider> { ControlsProvider(EngineTypes.Doom64ExPlus, hashMapOf(
            ControlsType.AbsoluteTouchControls to doom64AbsoluteTouchControlsLayout,
            ControlsType.OnScreenStick to doom64OnScreenStickControlsLayout
        )) }.withOptions {
            named(EngineTypes.Doom64ExPlus.name)
            }.withOptions {
                named(EngineTypes.Doom64ExPlusEnhanced.name)
            }

        single  {
            val nativeLibs = arrayOf(SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                PNG_NATIVE_LIB_NAME,
                FMOD_NATIVE_LIB_NAME,
                DOOM64_MAIN_ENGINE_LIB)

            Doom64EngineInfo(DOOM64_MAIN_ENGINE_LIB,
                nativeLibs) }.withOptions {
            named(EngineTypes.Doom64ExPlus.toString())
            bind<IEngineInfo>()
        }

        single  {
            val nativeLibs = arrayOf(SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                PNG_NATIVE_LIB_NAME,
                FMOD_NATIVE_LIB_NAME,
                DOOM64_ENHANCED_MAIN_ENGINE_LIB)

            Doom64EnhancedEngineInfo(DOOM64_ENHANCED_MAIN_ENGINE_LIB,
                nativeLibs) }.withOptions {
            named(EngineTypes.Doom64ExPlusEnhanced.toString())
            bind<IEngineInfo>()
        }

        single<IEngineUIController> { Doom64ComposeSettings() }
            .withOptions {
                named(EngineTypes.Doom64ExPlus.toString())
            }.withOptions {
                named(EngineTypes.Doom64ExPlusEnhanced.toString())
            }

        single<ModsModel> { Doom64ModsModel.load().updateFiles() }.withOptions {
            named(EngineTypes.Doom64ExPlus.toString())
        }.withOptions {
            named(EngineTypes.Doom64ExPlusEnhanced.toString())
        }
    }

    private val uZDoomRegisterModule = module {
        singleOf<UzDoomIniProvider>(::UzDoomIniProvider).bind()
        single { UZDoomPreferenceStorage() }.withOptions {
            named(EngineTypes.UZDoom.toString())
            bind<UZDoomPreferenceStorage>()
        }

        single<ControlsProvider> { ControlsProvider(EngineTypes.UZDoom, hashMapOf(
            ControlsType.AbsoluteTouchControls to uzDoomAbsoluteTouchControlsLayout,
            ControlsType.OnScreenStick to uzDoomOnScreenStickControlsLayout)) }.withOptions {
            named(EngineTypes.UZDoom.name) }

        single  {
            val nativeLibs = arrayOf(SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                OBOE_NATIVE_LUB_NAME,
                FLUIDSYNTH_NATIVE_LIB_NAME,
                OPENAL_NATIVE_LIB_NAME,
                MPG123_NATIVE_LIB_NAME,
                MP3LAME_NATIVE_LIB_NAME,
                OGG_NATIVE_LIB_NAME,
                VORBIS_NATIVE_LIB_NAME,
                VORBIS_FILE_NATIVE_LIB_NAME,
                VORBIS_ENC_NATIVE_LIB_NAME,
                FLAC_NATIVE_LIB_NAME,
                OPUS_NATIVE_LIB_NAME,
                SND_FILE_NATIVE_LIB_NAME,
                ZMUSIC_NATIVE_LIB_NAME,
                UZDOOM_MAIN_ENGINE_LIB)

            UZDoomEngineInfo(UZDOOM_MAIN_ENGINE_LIB,
                nativeLibs) }.withOptions {
            named(EngineTypes.UZDoom.toString())
            bind<IEngineInfo>()
        }

        singleOf(::UZDoomComposeSettings)
            .withOptions {
                named(EngineTypes.UZDoom.toString())
                bind<IEngineUIController>()
            }

        viewModel { UZDoomComposeSettingsViewModel().also { it.initialize() } }
        singleOf(::UZDoomMoreSettingsScreen).bind()

        single<UZDoomModsModel> { UZDoomModsModel.load().updateFiles() }.withOptions {
            named(EngineTypes.UZDoom.toString())
        }
    }

    private val psyDoomRegisterModule = module {
        single { PsyDoomPreferencesStorage() }.withOptions {
            named(EngineTypes.PsyDoom.toString())
            bind<PsyDoomPreferencesStorage>()
        }

        single<ControlsProvider> { ControlsProvider(EngineTypes.PsyDoom, hashMapOf(
            ControlsType.AbsoluteTouchControls to psyDoomAbsoluteTouchControlsLayout,
            ControlsType.OnScreenStick to psyDoomOnScreenStickControlsLayout)) }.withOptions {
            named(EngineTypes.PsyDoom.name) }

        single {
            val nativeLibs = arrayOf(FREETYPE_NATIVE_LIB_NAME,
                SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                PSYDOOM_MAIN_ENGINE_LIB)

            PsyDoomEngineInfo(PSYDOOM_MAIN_ENGINE_LIB, nativeLibs)
        }.withOptions {
            named(EngineTypes.PsyDoom.toString())
            bind<IEngineInfo>()
        }

        singleOf(::PsyDoomComposeSettings).withOptions {
            named(EngineTypes.PsyDoom.toString())
            bind<IEngineUIController>()
        }

        singleOf(::PsyDoomLauncherSettingsScreen).bind()
        singleOf(::PsyDoomMoreSettingsScreen).bind()
        singleOf(::PsyDoomGraphicsSettingsScreen).bind()
        singleOf(::PsyDoomGameSettingsScreen).bind()
        singleOf(::PsyDoomInputSettingsScreen).bind()
        singleOf(::PsyDoomAudioSettingsScreen).bind()
        singleOf(::PsyDoomCheatsSettingsScreen).bind()
        singleOf(::PsyDoomMultiplayerSettingsScreen).bind()

        single {
            val launcherSettings = get <PsyDoomLauncherSettingsScreen>()
            val moreSettings = get <PsyDoomMoreSettingsScreen>()
            val graphicsSettings = get <PsyDoomGraphicsSettingsScreen>()
            val gameSettings = get <PsyDoomGameSettingsScreen>()
            val inputSettings = get <PsyDoomInputSettingsScreen>()
            val audioSettings = get <PsyDoomAudioSettingsScreen>()
            val cheatsSettings = get <PsyDoomCheatsSettingsScreen>()
            val multiplayerSettings = get <PsyDoomMultiplayerSettingsScreen>()
            listOf(launcherSettings,moreSettings, graphicsSettings, gameSettings,
                inputSettings, audioSettings,cheatsSettings,multiplayerSettings)
        }.withOptions { bind<Collection<SettingScreen>>() }

        viewModel { PsyDoomComposeSettingsViewModel().also { it.initialize() } }
        singleOf(::PsyDoomMoreSettingsScreen).bind()
        single <ModsModel> { PsyDoomModsModel.load().updateFiles() }.withOptions {
            named(EngineTypes.PsyDoom.toString())
        }
    }

    private val perfectDarkKoinModule = module{
        single { arrayOf(SDL3_NATIVE_LIB_NAME,
            ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
            SDL2_NATIVE_LIB_NAME,
            PerfectDarkRomVersions.NTSC.mainLibraryName,
        ) }.withOptions {
            named(PerfectDarkRomVersions.NTSC.name)
        }

        single { arrayOf(SDL3_NATIVE_LIB_NAME,
            ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
            SDL2_NATIVE_LIB_NAME,
            PerfectDarkRomVersions.PAL.mainLibraryName,
        ) }.withOptions {
            named(PerfectDarkRomVersions.PAL.name)
        }

        single { arrayOf(SDL3_NATIVE_LIB_NAME,
            ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
            SDL2_NATIVE_LIB_NAME,
            PerfectDarkRomVersions.JPN.mainLibraryName,
        ) }.withOptions {
            named(PerfectDarkRomVersions.JPN.name)
        }

        singleOf(::PerfectDarkPreferencesStorage).withOptions {
            createdAtStart()
            named(EngineTypes.PerfectDark.name)
            bind<PerfectDarkPreferencesStorage>()
        }

        single<ControlsProvider> { ControlsProvider(EngineTypes.PerfectDark, hashMapOf(
            ControlsType.AbsoluteTouchControls to perfectDarkAbsoluteTouchControlsLayout,
            ControlsType.OnScreenStick to perfectDarkOnScreenStickControlsLayout)) }.withOptions {
            named(EngineTypes.PerfectDark.name) }

        singleOf(::PerfectDarkComposeSettings).withOptions {
            named(EngineTypes.PerfectDark.name)
            bind<IEngineUIController>()
        }

        singleOf(::PerfectDarkEngineInfo).withOptions {
            named(EngineTypes.PerfectDark.name)
            bind<IEngineInfo>()
        }
    }

    private val arxLibertatisKoinModule = module{
        single<ControlsProvider> { ControlsProvider(EngineTypes.ArxLibertatis, hashMapOf(
            ControlsType.OnScreenStick to arxLibertatisOnScreenStickControlsLayout)) }.withOptions {
            named(EngineTypes.ArxLibertatis.name) }

        singleOf(::ArxLibertatisPreferenceStorage)
            .withOptions {
                named(EngineTypes.ArxLibertatis.name)
            }

        single {
            val allLibs = arrayOf(SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                FREETYPE_NATIVE_LIB_NAME,
                OBOE_NATIVE_LUB_NAME,
                OPENAL_NATIVE_LIB_NAME,
                ARX_LIBERTATIS_MAIN_ENGINE_LIB,
            )
            ArxLibertatisEngineInfo(ARX_LIBERTATIS_MAIN_ENGINE_LIB, allLibs)
        }.withOptions {
            named(EngineTypes.ArxLibertatis.name)
            bind<IEngineInfo>()
        }

        viewModel { ArxLibertatisComposeSettingsViewModel().also { it.initialize() } }

        singleOf(::ArxLibertatisComposeSettings)
            .withOptions {
                named(EngineTypes.ArxLibertatis.name)
                bind <IEngineUIController>()
            }
    }

    private val fteQWKoinModule = module {
        single<ControlsProvider> { ControlsProvider(EngineTypes.FTEQW, hashMapOf(
            ControlsType.OnScreenStick to fteQWOnScreenStickControlsLayout)) }.withOptions {
            named(EngineTypes.FTEQW.name) }

        singleOf(::FTEQWPreferencesStorage)
            .withOptions {
                named(EngineTypes.FTEQW.name)
            }

        Quake2Games.entries.forEach { quake2GameType ->
            single {
                with(mutableListOf(SDL3_NATIVE_LIB_NAME,
                    ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                    SDL2_NATIVE_LIB_NAME,
                    FREETYPE_NATIVE_LIB_NAME,
                    BZ2_NATIVE_LIB_NAME,
                    ODE_NATIVE_LIB_NAME,
                    PNG_NATIVE_LIB_NAME,
                    JPEG_NATIVE_LIB_NAME,
                    OGG_NATIVE_LIB_NAME,
                    VORBIS_NATIVE_LIB_NAME,
                    VORBIS_FILE_NATIVE_LIB_NAME,
                    VORBIS_ENC_NATIVE_LIB_NAME,
                    OPUS_NATIVE_LIB_NAME,
                )){
                    add(quake2GameType.nativeLibraryName)
                    addAll(getFMPEGLibs(get()))
                    addAll(opensslLibs)
                    addAll(bulletLibs)
                    addAll(fteQWNativePlugins)
                    add(FTEQW_MAIN_ENGINE_LIB)
                    toTypedArray()
                }
            }.withOptions {
                named(quake2GameType.name)
            }
        }

        singleOf(::FTEQWEngineInfo).withOptions {
            named(EngineTypes.FTEQW.name)
            bind<IEngineInfo>()
        }

        singleOf(::FTEQWComposeSettings)
            .withOptions {
                named(EngineTypes.FTEQW.name)
                bind <IEngineUIController>()
            }
    }

    private val widelandsKoinModule = module {
        val widelandsName = EngineTypes.Widelands.name
        single<ControlsProvider> { ControlsProvider(EngineTypes.Widelands, hashMapOf(
            ControlsType.AbsoluteTouchControls to widelandsAbsoluteControlsLayout)) }.withOptions {
            named(widelandsName) }

        single {
            val allLibs = arrayOf(GLBINDING_NATIVE_LIB_NAME,
                TIFF_NATIVE_LIB_NAME,
                TIFFXX_NATIVE_LIB_NAME,
                PNG_NATIVE_LIB_NAME,
                MPG123_NATIVE_LIB_NAME,
                SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                SDL2_MIXER_NATIVE_LIB_NAME,
                SDL2_IMAGE_NATIVE_LIB_NAME,
                GLOB_NATIVE_LIB_NAME,
                WIDELANDS_MAIN_ENGINE_LIB
            )
            WidelandsEngineInfo(WIDELANDS_MAIN_ENGINE_LIB, allLibs)
        }.withOptions {
            named(widelandsName)
            bind<IEngineInfo>()
        }.withOptions {
            bind<WidelandsEngineInfo>()
        }
        viewModelOf(::WidelandsViewModel)
        singleOf <IEngineUIController>(::WidelandsComposeSettings).withOptions {
            named(widelandsName)
        }
    }

    private val vanillaConquerKoinModule = module {
        val engineName = EngineTypes.VanillaConquer.name
        single<ControlsProvider> { ControlsProvider(EngineTypes.VanillaConquer, hashMapOf(
            ControlsType.OnScreenStick to vanillaConquerOnScreenStickControlsLayout)) }.withOptions {
            named(engineName) }
        singleOf<VanillaConquerPreferencesStorage>(::VanillaConquerPreferencesStorage).withOptions {
            named(engineName)
        }
        single { arrayOf(OBOE_NATIVE_LUB_NAME,
                OPENAL_NATIVE_LIB_NAME,
                SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                RED_ALERT_NATIVE_LIB_NAME)
        }.withOptions {
            named(VanillaConquerGames.RedAlert.name)
        }
        single { arrayOf(OBOE_NATIVE_LUB_NAME,
                OPENAL_NATIVE_LIB_NAME,
                SDL3_NATIVE_LIB_NAME,
                ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME,
                SDL2_NATIVE_LIB_NAME,
                TIBERIAN_DAWN_NATIVE_LIB_NAME)
        }.withOptions {
            named(VanillaConquerGames.TiberianDawn.name)
        }
        singleOf<IEngineInfo>(::VanillaConquerEngineInfo).withOptions {
            named(engineName)
        }
        singleOf <IEngineUIController>(::VanillaConquerComposeSettings).withOptions {
            named(engineName)
        }
    }

    private val doomBFAKoinModule = module {
        val engineName = EngineTypes.Classic_RBDOOM_3_BFG.name
        single<ControlsProvider> { ControlsProvider(EngineTypes.Classic_RBDOOM_3_BFG, hashMapOf(
            ControlsType.OnScreenStick to doomBFAScreenStickControlsLayout)) }.withOptions {
            named(engineName) }
        singleOf<DoomBFAPreferencesStorage>(::DoomBFAPreferencesStorage).withOptions {
            named(engineName)
        }
        single<DoomBFAEngineInfo> {
            val libs = mutableListOf<String>().run {
                addAll(getFMPEGLibs(get()))
                addAll(opensslLibs)
                add(OBOE_NATIVE_LUB_NAME)
                add(OPENAL_NATIVE_LIB_NAME)
                add(JPEG_NATIVE_LIB_NAME)
                add(SDL3_NATIVE_LIB_NAME)
                add(ANDROID_GRAPHICS_LAYER_NATIVE_LIB_NAME)
                add(DOOM_BFA_MAIN_LIB)
                toTypedArray()
            }
            DoomBFAEngineInfo(DOOM_BFA_MAIN_LIB, libs)
        }.withOptions {
            named(engineName)
            bind<IEngineInfo>()
        }.withOptions {
            bind<DoomBFAEngineInfo>()
        }
        singleOf(::DoomBFAComposeSettings).withOptions {
            named(engineName)
            bind<IEngineUIController>()
        }
        singleOf(::DoomBFAGraphicsScreen).bind()
        viewModelOf(::DoomBFAViewModel)
    }

    init {
        allModules = listOf(mainModule,httpModule,translationModule,
            composeModule, doomRpgSeriesModule, doom64RegisterModule,
            psyDoomRegisterModule,uZDoomRegisterModule, perfectDarkKoinModule,
            arxLibertatisKoinModule, fteQWKoinModule,widelandsKoinModule,
            vanillaConquerKoinModule,doomBFAKoinModule)
    }

    companion object{
        const val ROOT_USER_DIRECTORY_KEY = "root_user_directory"
        const val ALL_COMPOSE_SCREENS = "all_compose_screens"
        const val TARGET_LOCALE_NAMES_KEY = "target_locale"
        const val ACTIVE_TRANSLATION_MODEL_KEY = "active_translation_model"
        const val BACKGROUND_THREAD_COROUTINE_KEY = "background_thread_coroutine"
        const val MAIN_THREAD_COROUTINE_KEY = "main_thread_coroutine"
    }
}

