package com.mobilerpgpack.phone.engine.engineinfo.doom64

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.modsCanBeUsed
import com.mobilerpgpack.phone.utils.ScreenResolution
import com.sun.jna.Native
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

open class Doom64EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, EngineTypes.Doom64ExPlus) {

    private val mutex = Mutex()

    private var customScreenResolutionWasApplied = false

    private val modsModel : ModsModel by inject (named(EngineTypes.Doom64ExPlus.toString()))

    override val commandLineParams: String get() = preferencesStorage.doom64CommandLineArgsString

    override val pathToResource get() = preferencesStorage.pathToDoom64MainWadsFolder

    override val touchFullScreenModeCanBeUsed = false

    final override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            return mutableListOf<String>().let {
                it += baseCommandLineArgs

                if (!baseCommandLineArgs.contains(FILE_COMMAND) && modsModel.modsCanBeUsed){
                    it += FILE_COMMAND
                    modsModel.mods.forEach { mod : Mod ->
                        if (!mod.pathToMod.value.isNullOrEmpty() && File(mod.pathToMod.value!!).exists()){
                            it+=mod.pathToMod.value!!
                        }
                    }
                }

                val pathToDoom64ModsFolder = getPathToDoom64ModsFolder()

                if (!baseCommandLineArgs.contains(MODS_COMMAND) && pathToDoom64ModsFolder.isNotEmpty()){
                    it +=MODS_COMMAND
                    it +=pathToDoom64ModsFolder
                }

                it.toTypedArray()
            }
        }

    private external fun MouseCursorCanBeDrawn() : Boolean

    private external fun RecalculateScreenResolution(screenWidth : Int, screenHeight : Int)

    final override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        Os.setenv("PATH_TO_DOOM_64_USER_FOLDER", getPathToDoom64UserFolder(), true)
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(Doom64EngineInfo::class.java, mainLibraryName)
    }

    final override fun setScreenResolution(screenResolution: ScreenResolution) {
        super.setScreenResolution(screenResolution)
        setupScreenResolutionToEnv(screenResolution)
        customScreenResolutionWasApplied = true
    }

    final override fun onSafeAreaApplied(screenResolution: ScreenResolution) {
        super.onSafeAreaApplied(screenResolution)
        if (!customScreenResolutionWasApplied) {
            backgroundScope.launch { updateScreenResolution(screenResolution) }
        }
    }

    final override fun isMouseShown() = MouseCursorCanBeDrawn()

    protected open fun getPathToDoom64UserFolder() =
        pathToRootUserFolder + File.separator + "doom64ex-plus" + File.separator

    private suspend fun updateScreenResolution (screenResolution : ScreenResolution){
        mutex.withLock {
            setupScreenResolutionToEnv(screenResolution)
            RecalculateScreenResolution(screenResolution.screenWidth, screenResolution.screenHeight)
        }
    }

    private fun getPathToDoom64ModsFolder(): String {
        val enableDoom64Mods = preferencesStorage.enableDoom64Mods

        if (!enableDoom64Mods) {
            return ""
        }

        var pathToDoom64ModsFolder = preferencesStorage.pathToDoom64ModsFolder

        if (pathToDoom64ModsFolder.isEmpty()){
            return ""
        }

        val pathToDoom64ModsFolderExists = File(pathToDoom64ModsFolder).exists()

        if (!pathToDoom64ModsFolderExists) {
            pathToDoom64ModsFolder = ""
        }

        return pathToDoom64ModsFolder
    }

    private fun setupScreenResolutionToEnv (screenResolution: ScreenResolution){
        Os.setenv("SCREEN_WIDTH", screenResolution.screenWidth.toString(), true)
        Os.setenv("SCREEN_HEIGHT", screenResolution.screenHeight.toString(), true)
    }
    
    private companion object{
        private const val FILE_COMMAND = "-file"
        private const val MODS_COMMAND = "-mod"
    }
}

