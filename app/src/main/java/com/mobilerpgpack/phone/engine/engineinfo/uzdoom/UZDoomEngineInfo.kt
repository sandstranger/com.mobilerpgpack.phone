package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.sun.jna.Function
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File
import kotlin.getValue

class UZDoomEngineInfo (mainEngineLib: String,
                        allLibs: Array<String>,
                        buttonsToDraw: Collection<IScreenControlsView>,
                        commandLineParamsFlow : Flow<String>) :
    SDL2EngineInfo (mainEngineLib, allLibs, buttonsToDraw,activeEngineType = EngineTypes.UZDoom,
        commandLineParamsFlow = commandLineParamsFlow) {

    private val lzDoomPreferencesStorage by inject <UZDoomPreferenceStorage>(named(
        EngineTypes.UZDoom.toString()))

    private val pathToLZDoomUserFolder by lazy{
        super.pathToRootUserFolder + File.separator + "uzdoom"
    }

    private val pathToLZDoomConfigsFile by lazy {
        pathToLZDoomUserFolder + File.separator + "uzdoom.ini"
    }

    private val destroyVulkanSwapChainNativeDelegate by lazy {
        com.sun.jna.Function.getFunction(mainEngineLib,
            "DestroyVulkanSwapChain")
    }

    private val recreateVulkanSwapChainNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "RecreateVulkanSwapChain")
    }

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
                }

                it.toTypedArray()
            }
        }

    override suspend fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        Os.setenv("PATH_TO_UZDOOM_USER_FOLDER", pathToLZDoomUserFolder, true)
        Os.setenv("PATH_TO_UZDOOM_MODS_FOLDER", getPathToLZDoomModsFolder(), true)
    }

    override fun onResume() = recreateVulkanSwapChainNativeDelegate.invokeVoid(null) 

    override fun onPause() = destroyVulkanSwapChainNativeDelegate.invokeVoid(null)

    private suspend fun getPathToLZDoomModsFolder(): String {
        val enableMods = preferencesStorage.enableUZDoomMods.first()

        if (!enableMods) {
            return ""
        }

        var pathToModsFolder = preferencesStorage.pathToUZDoomModsFolder.first()

        if (pathToModsFolder.isEmpty()){
            return ""
        }

        val pathToDoom64ModsFolderExists = File(pathToModsFolder).exists()

        if (!pathToDoom64ModsFolderExists) {
            pathToModsFolder = ""
        }

        return pathToModsFolder
    }

    private companion object {
        private const val IWAD_COMMAND = "-iwad"
        private const val FILE_COMMAND = "-file"
        private const val PLAY_DEMO_COMMAND = "-playdemo"
        private const val XLAT_FILE_COMMAND = "-xlat"
        private const val CONFIG_FILE_COMMAND = "-config"
        private const val SAVE_DIR_COMMAND = "-savedir"
    }
}