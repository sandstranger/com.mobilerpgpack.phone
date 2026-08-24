package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import android.os.Build
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.utils.UZDoomModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.behFileCanBeUsed
import com.mobilerpgpack.phone.engine.engineinfo.utils.dehFileCanBeUsed
import com.mobilerpgpack.phone.engine.engineinfo.utils.modsCanBeUsed
import com.mobilerpgpack.phone.engine.engineinfo.utils.playingRecordsFileCanBeUsed
import com.mobilerpgpack.phone.engine.engineinfo.utils.xlatFileCanBeUsed
import com.mobilerpgpack.phone.utils.GpuProbe
import com.sun.jna.Native
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File
import kotlin.collections.contains

class UZDoomEngineInfo: SDL3EngineInfo(activeEngineType = EngineTypes.UZDoom) {
    private val gpuProbe by inject <GpuProbe>()
    private val currentEngineVersion get() = preferencesStorage.uzDoomEngineVersion.value!!
    private val allNativeLibraries : Array<String> by lazy { get (named(currentEngineVersion.name)) }
    private val mainNativeLibrary : String by lazy { get (named(currentEngineVersion.name)) }
    private val modsModel : UZDoomModsModel by inject (named(EngineTypes.UZDoom.toString()))
    private val uzDoomViewModel : UZDoomComposeSettingsViewModel by inject ()
    private val uzDoomCacheFolder : File by lazy {
        File(activity.cacheDir, if (currentEngineVersion == UZDoomEngineVersions.Dev)
            "dev_uzdoom_cache" else "legacy_uzdoom_cache")
    }
    private val pathToUZDoomUserFolder by lazy{
        super.pathToRootUserFolder + File.separator + if (currentEngineVersion == UZDoomEngineVersions.Dev)
            DEV_GZDOOM_USER_FOLDER_NAME else LEGACY_GZDOOM_USER_FOLDER_NAME
    }
    private val enableLightShaders get() = preferencesStorage.enableLightShaders.value!!
    private val useAngleLayerForced: Boolean by lazy { !uzDoomViewModel.useOpenGLESRender &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !gpuProbe.probe().isAdreno }

    private val pathToUZDoomConfigsFile by lazy { pathToUZDoomUserFolder + File.separator + "uzdoom.ini" }

    override val preferencesStorage by inject <UZDoomPreferenceStorage>(named(
        EngineTypes.UZDoom.toString()))
    override val commandLineParams: String get() = preferencesStorage.uZDoomCommandLineArgsString.value!!
    override val mainLibraryName: String get() = mainNativeLibrary
    override val nativeLibraries get() = allNativeLibraries
    override val pathToResource get() = preferencesStorage.pathToUZDoomIWadFile.value!!
    override val requiredResourceExtensions = listOf(".wad", ".WAD")
    override val loadGL4ES = false
    override val enableAngleSupport: Boolean get() = super.enableAngleSupport || useAngleLayerForced

    private external fun UpdateGLLiteShaderState(enableLightShaders : Boolean)
    private external fun UpdateHarmGLESVersion(glesVersion : Int)
    private external fun setPathsToFolders (pathToUserFolder : String, pathToCacheFolder : String)
    private external fun UpdateUseOpenGLESState(useOpenGLES : Boolean)
    private external fun setSpirvCrossState(enableSpirvCross : Boolean)
    private external fun setTargetFPS (targetFPS : Int)

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        File(pathToUZDoomUserFolder, "config").mkdirs()
        File(pathToUZDoomUserFolder, "share").mkdirs()
        File(uzDoomCacheFolder, "uzdoom_cache").mkdirs()
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        val glesVersion = enumValueOf<UZDoomGLESVersion>(preferencesStorage.uzDoomGLESVersion.value!!).value
        Native.register(UZDoomEngineInfo::class.java, mainLibraryName)
        UpdateHarmGLESVersion(glesVersion)
        UpdateGLLiteShaderState(enableLightShaders)
        setPathsToFolders(pathToUZDoomUserFolder,
            uzDoomCacheFolder.absolutePath)
        UpdateUseOpenGLESState(uzDoomViewModel.renderAPI == UZDoomRenderAPI.OpenGLES)
        preferencesStorage.apply {
            setSpirvCrossState(enableSpirvCross.value!!)
            setTargetFPS(framePacingTargetFPS.value!!)
        }
    }

    override fun buildCustomCommandLineArgs () : Collection<String>{
        return mutableListOf<String>().also {
            val pathToWadFile = preferencesStorage.pathToUZDoomIWadFile.value!!
            if (pathToWadFile.isNotEmpty() && File(pathToWadFile).exists()) {
                it += IWAD_COMMAND
                it += pathToWadFile
            }

            it += CONFIG_FILE_COMMAND
            it += pathToUZDoomConfigsFile

            it += SAVE_DIR_COMMAND
            it += pathToUZDoomUserFolder

            if (modsModel.modsCanBeUsed) {
                it += FILE_COMMAND

                modsModel.mods.forEach { mod: Mod ->
                    val pathToMod = mod.pathToMod.liveData.value
                    if (!pathToMod.isNullOrEmpty() && File(pathToMod).exists()) {
                        it += pathToMod
                    }
                }
            }

            if (modsModel.playingRecordsFileCanBeUsed) {
                it += PLAY_DEMO_COMMAND
                it += modsModel.pathToDemoFile.liveData.value!!
            }

            if (modsModel.xlatFileCanBeUsed) {
                it += XLAT_FILE_COMMAND
                it += modsModel.pathToXLatFile.liveData.value!!
            }

            if (modsModel.dehFileCanBeUsed) {
                it += DEH_COMMAND
                it += modsModel.pathToDehFile.liveData.value!!
            }

            if (modsModel.behFileCanBeUsed) {
                it += BEH_COMMAND
                it += modsModel.pathToBehFile.liveData.value!!
            }
        }
    }

    companion object {
        private const val IWAD_COMMAND = "-iwad"
        private const val FILE_COMMAND = "-file"
        private const val PLAY_DEMO_COMMAND = "-playdemo"
        private const val XLAT_FILE_COMMAND = "-xlat"
        private const val CONFIG_FILE_COMMAND = "-config"
        private const val SAVE_DIR_COMMAND = "-savedir"
        private const val DEH_COMMAND = "-deh"
        private const val BEH_COMMAND = "-bex"

        const val DEV_GZDOOM_USER_FOLDER_NAME = "uzdoom_dev"
        const val LEGACY_GZDOOM_USER_FOLDER_NAME = "uzdoom"
    }
}