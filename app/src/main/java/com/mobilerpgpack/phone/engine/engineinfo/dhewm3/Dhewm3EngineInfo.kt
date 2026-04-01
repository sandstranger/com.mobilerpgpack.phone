package com.mobilerpgpack.phone.engine.engineinfo.dhewm3

import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.main.DHWEM3_NATIVE_LIB_NAME
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class Dhewm3EngineInfo : SDL2EngineInfo (EngineTypes.Dhewm3) {
    private val homeDirectoryFolder : File by inject { parametersOf(DHWEM3_HOME_DIRECTORY_NAME) }

    private val dhewm3PreferenceStorage by inject<Dhewm3PreferenceStorage>(named(EngineTypes.Dhewm3.name))

    private val baseNativeLibs by inject <Array<String>>(named(EngineTypes.Dhewm3.name))

    override val pathToResource: String get() = dhewm3PreferenceStorage.pathToDoom3Resources.value!!

    override val mainLibraryName: String = DHWEM3_NATIVE_LIB_NAME

    override val nativeLibraries: Array<String> get() = baseNativeLibs

    override val preferencesStorage get() = dhewm3PreferenceStorage

    override val commandLineParams: String get() = dhewm3PreferenceStorage.commandLineArgs.value!!

    override val commandLineArgs: Array<String>
        get() {
            return with(mutableListOf<String>()) {
                this += super.commandLineArgs
                this += SET_COMMAND
                this += RENDER_COMMAND
                this += "vulkan"
                toTypedArray()
            }
        }

    private external fun setPathsToResources (pathToHomeFolder : String, pathToResourcesFolder : String)

    private external fun setPathToNativeLibsFolder (pathToNativeLibsFolder : String)

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        if (!homeDirectoryFolder.exists()){
            homeDirectoryFolder.mkdirs()
        }
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(Dhewm3EngineInfo::class.java, mainLibraryName)
        setPathsToResources(homeDirectoryFolder.absolutePath,
            pathToResource)
        setPathToNativeLibsFolder (activity.applicationInfo.nativeLibraryDir)
    }

    private companion object{
        private const val DHWEM3_HOME_DIRECTORY_NAME = "dhewm3"
        private const val RENDER_COMMAND = "r_renderBackend"
        private const val SET_COMMAND = "+set"
    }
}