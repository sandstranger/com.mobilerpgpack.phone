package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.utils.UZDoomModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.behFileCanBeUsed
import com.mobilerpgpack.phone.engine.engineinfo.utils.dehFileCanBeUsed
import com.mobilerpgpack.phone.engine.engineinfo.utils.modsCanBeUsed
import com.mobilerpgpack.phone.engine.engineinfo.utils.playingRecordsFileCanBeUsed
import com.mobilerpgpack.phone.engine.engineinfo.utils.xlatFileCanBeUsed
import com.sun.jna.Native
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class UZDoomEngineInfo (mainEngineLib: String,
                        allLibs: Array<String>) :
    SDL2EngineInfo (mainEngineLib, allLibs, activeEngineType = EngineTypes.UZDoom) {

    private val modsModel : UZDoomModsModel by inject (named(EngineTypes.UZDoom.toString()))

    private val pathToUZDoomUserFolder by lazy{
        super.pathToRootUserFolder + File.separator + "uzdoom"
    }

    private val pathToUZDoomConfigsFile by lazy {
        pathToUZDoomUserFolder + File.separator + "uzdoom.ini"
    }

    private val enableLightShaders get() = preferencesStorage.enableLightShaders

    override val preferencesStorage by inject <UZDoomPreferenceStorage>(named(
        EngineTypes.UZDoom.toString()))

    override val commandLineParams: String get() = preferencesStorage.uZDoomCommandLineArgsString

    override val pathToResource get() = preferencesStorage.pathToUZDoomIWadFile

    override val requiredResourceExtensions = listOf(".wad", ".WAD")

    override val loadGL4ES = false

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            return mutableListOf<String>().let {
                it += baseCommandLineArgs

                val pathToWadFile = preferencesStorage.pathToUZDoomIWadFile
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
                        if (!mod.pathToMod.value.isNullOrEmpty() && File(mod.pathToMod.value!!).exists()) {
                            it += mod.pathToMod.value!!
                        }
                    }
                }

                if (!baseCommandLineArgs.contains(PLAY_DEMO_COMMAND) && modsModel.playingRecordsFileCanBeUsed) {
                    it += PLAY_DEMO_COMMAND
                    it += modsModel.pathToDemoFile.value!!
                }

                if (!baseCommandLineArgs.contains(XLAT_FILE_COMMAND) && modsModel.xlatFileCanBeUsed) {
                    it += XLAT_FILE_COMMAND
                    it += modsModel.pathToXLatFile.value!!
                }

                if (!baseCommandLineArgs.contains(DEH_COMMAND) && modsModel.dehFileCanBeUsed) {
                    it += DEH_COMMAND
                    it += modsModel.pathToDehFile.value!!
                }

                if (!baseCommandLineArgs.contains(BEH_COMMAND) && modsModel.behFileCanBeUsed) {
                    it += BEH_COMMAND
                    it += modsModel.pathToBehFile.value!!
                }

                it.toTypedArray()
            }
        }

    private external fun DestroyVulkanSwapChain()

    private external fun RecreateVulkanSwapChain()

    private external fun UpdateGLLiteShaderState(enableLightShaders : Boolean)

    private external fun UpdateHarmGLESVersion(glesVersion : Int)

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        Os.setenv("PATH_TO_UZDOOM_USER_FOLDER", pathToUZDoomUserFolder, true)
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        val glesVersion = enumValueOf<UZDoomGLESVersion>(preferencesStorage.uzDoomGLESVersion).value
        Native.register(UZDoomEngineInfo::class.java, mainLibraryName)
        UpdateHarmGLESVersion(glesVersion)
        UpdateGLLiteShaderState(enableLightShaders)
    }

    override fun onResume() {
        super.onResume()
        backgroundScope.launch { RecreateVulkanSwapChain() }
    }

    override fun onPause() {
        super.onPause()
        backgroundScope.launch { DestroyVulkanSwapChain() }
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