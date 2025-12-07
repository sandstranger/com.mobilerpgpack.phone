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
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.sun.jna.Function
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class UZDoomEngineInfo (mainEngineLib: String,
                        allLibs: Array<String>,
                        buttonsToDraw: Collection<IScreenControlsView>,
                        commandLineParamsFlow : Flow<String>) :
    SDL2EngineInfo (mainEngineLib, allLibs, buttonsToDraw,activeEngineType = EngineTypes.UZDoom,
        commandLineParamsFlow = commandLineParamsFlow) {

    private val lzDoomPreferencesStorage by inject <UZDoomPreferenceStorage>(named(
        EngineTypes.UZDoom.toString()))

    private val modsModel : UZDoomModsModel by inject ()

    private val pathToLZDoomUserFolder by lazy{
        super.pathToRootUserFolder + File.separator + "uzdoom"
    }

    private val pathToLZDoomConfigsFile by lazy {
        pathToLZDoomUserFolder + File.separator + "uzdoom.ini"
    }

    private val destroyVulkanSwapChainNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "DestroyVulkanSwapChain")
    }

    private val recreateVulkanSwapChainNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "RecreateVulkanSwapChain")
    }

    private val updateGLLiteShaderStateNativeDelegate  by lazy {
        Function.getFunction(mainEngineLib,
            "UpdateGLLiteShaderState")
    }

    private var enableLightShaders = false

    override val preferencesStorage = lzDoomPreferencesStorage

    override val pathToResource get() = runBlocking{ lzDoomPreferencesStorage.pathToUZDoomIWadFile.first() }

    override val requiredResourceExtension = ".wad"

    override val loadGL4ES = false

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            return mutableListOf<String>().let {
                it +=baseCommandLineArgs

                runBlocking {
                    val pathToWadFile = preferencesStorage.pathToUZDoomIWadFile.first()
                    if (pathToWadFile.isNotEmpty() && File(pathToWadFile).exists() &&
                        !baseCommandLineArgs.contains(IWAD_COMMAND)){
                        it += IWAD_COMMAND
                        it += pathToWadFile
                    }

                    if (!baseCommandLineArgs.contains(CONFIG_FILE_COMMAND)){
                        it += CONFIG_FILE_COMMAND
                        it += pathToLZDoomConfigsFile
                    }

                    if (!baseCommandLineArgs.contains(SAVE_DIR_COMMAND)){
                        it += SAVE_DIR_COMMAND
                        it += pathToLZDoomUserFolder
                    }

                    if (!baseCommandLineArgs.contains(FILE_COMMAND) && modsModel.modsCanBeUsed){
                        it +=FILE_COMMAND

                        modsModel.mods.forEach { mod : Mod ->
                            if (!mod.pathToMod.value.isNullOrEmpty() && File(mod.pathToMod.value!!).exists()){
                                it+=mod.pathToMod.value!!
                            }
                        }
                    }

                    if (!baseCommandLineArgs.contains(PLAY_DEMO_COMMAND) && modsModel.playingRecordsFileCanBeUsed){
                        it +=PLAY_DEMO_COMMAND
                        it +=modsModel.pathToDemoFile.value!!
                    }

                    if (!baseCommandLineArgs.contains(XLAT_FILE_COMMAND) && modsModel.xlatFileCanBeUsed){
                        it +=XLAT_FILE_COMMAND
                        it +=modsModel.pathToXLatFile.value!!
                    }

                    if (!baseCommandLineArgs.contains(DEH_COMMAND) && modsModel.dehFileCanBeUsed){
                        it +=DEH_COMMAND
                        it +=modsModel.pathToDehFile.value!!
                    }

                    if (!baseCommandLineArgs.contains(BEH_COMMAND) && modsModel.behFileCanBeUsed){
                        it +=BEH_COMMAND
                        it +=modsModel.pathToBehFile.value!!
                    }
                }

                it.toTypedArray()
            }
        }

    override suspend fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        Os.setenv("PATH_TO_UZDOOM_USER_FOLDER", pathToLZDoomUserFolder, true)
        enableLightShaders = preferencesStorage.enableLightShaders.first()
    }

    override fun onNativeLibrariesLoaded() = updateGLLiteShaderStateNativeDelegate
        .invokeVoid(arrayOf(enableLightShaders))

    override fun onResume() = recreateVulkanSwapChainNativeDelegate.invokeVoid(null)

    override fun onPause() = destroyVulkanSwapChainNativeDelegate.invokeVoid(null)

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