package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import android.os.Build
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
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class UZDoomEngineInfo (mainEngineLib: String,
                        allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, activeEngineType = EngineTypes.UZDoom) {
    private val gpuProbe by inject <GpuProbe>()
    private val uzDoomIniProvider by inject <UzDoomIniProvider>()
    private val modsModel : UZDoomModsModel by inject (named(EngineTypes.UZDoom.toString()))
    private val uzDoomViewModel : UZDoomComposeSettingsViewModel by inject ()
    private val pathToUZDoomUserFolder by lazy{
        super.pathToRootUserFolder + File.separator + "uzdoom"
    }
    private val pathToUZDoomConfigsFile by lazy {
        pathToUZDoomUserFolder + File.separator + "uzdoom.ini"
    }
    private val enableLightShaders get() = preferencesStorage.enableLightShaders.value!!
    private val useAngleLayerForced: Boolean by lazy {
        if (uzDoomIniProvider.useOpenGLESRender) {
            false
        } else {
            gpuProbe.probe().run { !this.isAdreno && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O }
        }
    }

    override val preferencesStorage by inject <UZDoomPreferenceStorage>(named(
        EngineTypes.UZDoom.toString()))
    override val commandLineParams: String get() = preferencesStorage.uZDoomCommandLineArgsString.value!!
    override val pathToResource get() = preferencesStorage.pathToUZDoomIWadFile.value!!
    override val requiredResourceExtensions = listOf(".wad", ".WAD")
    override val loadGL4ES = false
    override val enableAngleSupport: Boolean get() = super.enableAngleSupport || useAngleLayerForced
    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            return mutableListOf<String>().let {
                it += baseCommandLineArgs

                val pathToWadFile = preferencesStorage.pathToUZDoomIWadFile.value!!
                if (pathToWadFile.isNotEmpty() && File(pathToWadFile).exists() &&
                    !baseCommandLineArgs.contains(IWAD_COMMAND)
                ) {
                    it += IWAD_COMMAND
                    it += pathToWadFile
                }

                if (!baseCommandLineArgs.contains(CONFIG_FILE_COMMAND)) {
                    it += CONFIG_FILE_COMMAND
                    it += pathToUZDoomConfigsFile
                }

                if (!baseCommandLineArgs.contains(SAVE_DIR_COMMAND)) {
                    it += SAVE_DIR_COMMAND
                    it += pathToUZDoomUserFolder
                }

                if (!baseCommandLineArgs.contains(FILE_COMMAND) && modsModel.modsCanBeUsed) {
                    it += FILE_COMMAND

                    modsModel.mods.forEach { mod: Mod ->
                        val pathToMod = mod.pathToMod.liveData.value
                        if (!pathToMod.isNullOrEmpty() && File(pathToMod).exists()) {
                            it += pathToMod
                        }
                    }
                }

                if (!baseCommandLineArgs.contains(PLAY_DEMO_COMMAND) && modsModel.playingRecordsFileCanBeUsed) {
                    it += PLAY_DEMO_COMMAND
                    it += modsModel.pathToDemoFile.liveData.value!!
                }

                if (!baseCommandLineArgs.contains(XLAT_FILE_COMMAND) && modsModel.xlatFileCanBeUsed) {
                    it += XLAT_FILE_COMMAND
                    it += modsModel.pathToXLatFile.liveData.value!!
                }

                if (!baseCommandLineArgs.contains(DEH_COMMAND) && modsModel.dehFileCanBeUsed) {
                    it += DEH_COMMAND
                    it += modsModel.pathToDehFile.liveData.value!!
                }

                if (!baseCommandLineArgs.contains(BEH_COMMAND) && modsModel.behFileCanBeUsed) {
                    it += BEH_COMMAND
                    it += modsModel.pathToBehFile.liveData.value!!
                }

                it.toTypedArray()
            }
        }

    private external fun DestroyVulkanSwapChain()
    private external fun RecreateVulkanSwapChain()
    private external fun UpdateGLLiteShaderState(enableLightShaders : Boolean)
    private external fun UpdateHarmGLESVersion(glesVersion : Int)
    private external fun setPathToUserFolder (pathToUserFolder : String)
    private external fun UpdateUseOpenGLESState(useOpenGLES : Boolean)

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        val glesVersion = enumValueOf<UZDoomGLESVersion>(preferencesStorage.uzDoomGLESVersion.value!!).value
        Native.register(UZDoomEngineInfo::class.java, mainLibraryName)
        UpdateHarmGLESVersion(glesVersion)
        UpdateGLLiteShaderState(enableLightShaders)
        setPathToUserFolder(pathToUZDoomUserFolder)
        UpdateUseOpenGLESState(uzDoomViewModel.renderAPI == UZDoomRenderAPI.OpenGLES)
    }

    override fun onResume() {
        super.onResume()
        RecreateVulkanSwapChain()
    }

    override fun onPause() {
        super.onPause()
        DestroyVulkanSwapChain()
    }

    private companion object {
        private const val IWAD_COMMAND = "-iwad"
        private const val FILE_COMMAND = "-file"
        private const val PLAY_DEMO_COMMAND = "-playdemo"
        private const val XLAT_FILE_COMMAND = "-xlat"
        private const val CONFIG_FILE_COMMAND = "-config"
        private const val SAVE_DIR_COMMAND = "-savedir"
        private const val DEH_COMMAND = "-deh"
        private const val BEH_COMMAND = "-bex"
    }
}