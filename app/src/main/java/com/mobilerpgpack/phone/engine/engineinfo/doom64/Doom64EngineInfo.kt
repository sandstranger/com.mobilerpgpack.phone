package com.mobilerpgpack.phone.engine.engineinfo.doom64

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.modsCanBeUsed
import com.mobilerpgpack.phone.utils.ScreenResolution
import com.opentouchgaming.saffal.FileSAF
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

open class Doom64EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, EngineTypes.Doom64ExPlus) {

    private var savedScreenResolution : ScreenResolution? = null
    private var customScreenResolutionWasApplied = false
    private val modsModel : ModsModel by inject (named(EngineTypes.Doom64ExPlus.toString()))

    override val gl4esShaderCacheFolderName = "doom64_gl4es_cache"
    final override val commandLineParams: String get() = preferencesStorage.doom64CommandLineArgsString.value!!
    final override val pathToResource get() = preferencesStorage.pathToDoom64MainWadsFolder.value!!
    final override val touchFullScreenModeCanBeUsed = false
    final override val enableNGGL4ESSimpleShaderConv = false
    final override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            return mutableListOf<String>().let {
                it += baseCommandLineArgs

                if (!baseCommandLineArgs.contains(FILE_COMMAND) && modsModel.modsCanBeUsed){
                    it += FILE_COMMAND
                    modsModel.mods.forEach { mod : Mod ->
                        val pathToMode = mod.pathToMod.liveData.value
                        if (!pathToMode.isNullOrEmpty() && FileSAF(pathToMode).exists()){
                            it+=pathToMode
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
    private external fun setScreenResolution (screenWidth : Int, screenHeight : Int)
    private external fun RecalculateScreenResolution(screenWidth : Int, screenHeight : Int)
    private external fun setPathsToResources (pathToWadsFolder : String, pathToUserFolder : String)
    private external fun updateMaxAnisotropyValue (targetAnisotropyValue : Int)

    final override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(Doom64EngineInfo::class.java, mainLibraryName)
        setPathsToResources(pathToResource, getPathToDoom64UserFolder())
        updateMaxAnisotropyValue(preferencesStorage.doom64AnisotropyTexturesValue.value!!)
        savedScreenResolution?.run {
            setScreenResolution(screenWidth, screenHeight)
        }
    }

    final override fun setScreenResolution(screenResolution: ScreenResolution) {
        super.setScreenResolution(screenResolution)
        savedScreenResolution = screenResolution
        customScreenResolutionWasApplied = true
    }

    final override fun onSafeAreaApplied(screenResolution: ScreenResolution) {
        super.onSafeAreaApplied(screenResolution)
        if (!customScreenResolutionWasApplied) {
            setScreenResolution(screenResolution.screenWidth, screenResolution.screenHeight)
            RecalculateScreenResolution(screenResolution.screenWidth, screenResolution.screenHeight)
        }
    }

    final override fun isMouseShown() = MouseCursorCanBeDrawn()

    protected open fun getPathToDoom64UserFolder() =
        pathToRootUserFolder + File.separator + "doom64ex-plus" + File.separator

    private fun getPathToDoom64ModsFolder(): String {
        val enableDoom64Mods = preferencesStorage.enableDoom64Mods.value!!

        if (!enableDoom64Mods) {
            return ""
        }

        var pathToDoom64ModsFolder = preferencesStorage.pathToDoom64ModsFolder.value!!

        if (pathToDoom64ModsFolder.isEmpty()){
            return ""
        }

        val pathToDoom64ModsFolderExists = FileSAF(pathToDoom64ModsFolder).exists()

        if (!pathToDoom64ModsFolderExists) {
            pathToDoom64ModsFolder = ""
        }

        return pathToDoom64ModsFolder
    }

    private companion object{
        private const val FILE_COMMAND = "-file"
        private const val MODS_COMMAND = "-mod"
    }
}

