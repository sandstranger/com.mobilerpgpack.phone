package com.mobilerpgpack.phone.engine.engineinfo.doom64

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.modsCanBeUsed
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File
import kotlin.collections.contains

open class Doom64EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, EngineTypes.Doom64ExPlus) {
    private val modsModel : ModsModel by inject (named(EngineTypes.Doom64ExPlus.toString()))

    override val gl4esShaderCacheFolderName = "doom64_gl4es_cache"
    final override val commandLineParams: String get() = preferencesStorage.doom64CommandLineArgsString.value!!
    final override val pathToResource get() = preferencesStorage.pathToDoom64MainWadsFolder.value!!
    final override val touchFullScreenModeCanBeUsed = false
    final override val enableNGGL4ESSimpleShaderConv = false

    private external fun MouseCursorCanBeDrawn() : Boolean
    private external fun setPathsToResources (pathToWadsFolder : String, pathToUserFolder : String)
    private external fun updateMaxAnisotropyValue (targetAnisotropyValue : Int)
    private external fun updateOnScreenControlsState (onScreenControlsActive : Boolean)

    final override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(Doom64EngineInfo::class.java, mainLibraryName)
        setPathsToResources(pathToResource, getPathToDoom64UserFolder())
        preferencesStorage.apply {
            updateMaxAnisotropyValue(doom64AnisotropyTexturesValue.value!!)
            updateOnScreenControlsState(!hideScreenControls.value!!)
        }
    }

    final override fun isMouseShown() = MouseCursorCanBeDrawn()

    final override fun buildCustomCommandLineArgs () : Collection<String>{
        return mutableListOf<String>().also {
            if (modsModel.modsCanBeUsed){
                it += FILE_COMMAND
                modsModel.mods.forEach { mod : Mod ->
                    val pathToMode = mod.pathToMod.liveData.value
                    if (!pathToMode.isNullOrEmpty() && File(pathToMode).exists()){
                        it+=pathToMode
                    }
                }
            }

            val pathToDoom64ModsFolder = getPathToDoom64ModsFolder()

            if (pathToDoom64ModsFolder.isNotEmpty()){
                it +=MODS_COMMAND
                it +=pathToDoom64ModsFolder
            }
        }
    }

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

        val pathToDoom64ModsFolderExists = File(pathToDoom64ModsFolder).exists()

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

