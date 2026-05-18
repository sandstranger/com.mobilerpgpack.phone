package com.mobilerpgpack.phone.engine.engineinfo.perfectdark

import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.sun.jna.Native
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class PerfectDarkEngineInfo : SDL3EngineInfo
    ("", emptyArray(), EngineTypes.PerfectDark) {

    private val homeDirectory : File by inject { parametersOf(PERFECT_DARK_FOLDER_NAME) }

    private val savesDirectory : File by inject { parametersOf(PERFECT_DARK_FOLDER_NAME +
            File.separator + SAVE_FOLDER_NAME) }

    private val perfectDarkPreferencesStorage : PerfectDarkPreferencesStorage by inject (
        named(EngineTypes.PerfectDark.name))

    private val pathToModsDirectory by lazy {
        if (perfectDarkPreferencesStorage.enablePerfectDarkModsSupport.value!!) perfectDarkPreferencesStorage
            .pathToPerfectDarkModsFolder.value!! else ""
    }

    private val romVersion get() = perfectDarkPreferencesStorage.romVersion.value!!

    override val preferencesStorage: PreferencesStorage get() = perfectDarkPreferencesStorage

    override val commandLineParams by lazy { perfectDarkPreferencesStorage.commandLineArgs.value!! }

    override val requiredResourceExtensions = listOf(".z64", ".Z64")

    override val mainLibraryName: String by lazy { romVersion.mainLibraryName }

    override val nativeLibraries by lazy {
        mutableListOf<String>().run {
            this += super.nativeLibraries
            this += get<Array<String>> (named(romVersion.name))
            toTypedArray()
        }
    }

    override val pathToResource get() =
        when (romVersion) {
            PerfectDarkRomVersions.NTSC -> perfectDarkPreferencesStorage.pathToNTSCRom.value!!
            PerfectDarkRomVersions.PAL -> perfectDarkPreferencesStorage.pathToPalRom.value!!
            PerfectDarkRomVersions.JPN -> perfectDarkPreferencesStorage.pathToJpnRom.value!!
        }

    override val loadGL4ES = false

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs
            return with(mutableListOf<String>()){
                this += baseCommandLineArgs

                val pathToRom = pathToResource
                if (!baseCommandLineArgs.contains(ROM_FILE_COMMAND) &&
                    pathToRom.isNotEmpty() && File(pathToRom).exists()){
                    this +=ROM_FILE_COMMAND
                    this +=pathToRom
                }

                if (!baseCommandLineArgs.contains(PORTABLE_COMMAND)){
                    this +=PORTABLE_COMMAND
                }

                val enableMods = perfectDarkPreferencesStorage.enablePerfectDarkModsSupport.value!!
                        && pathToModsDirectory.isNotEmpty() && File(pathToModsDirectory).exists()

                if (!baseCommandLineArgs.contains(MODS_DIR_COMMAND) && enableMods){
                    this += MODS_DIR_COMMAND
                    this += pathToModsDirectory
                }

                if (!baseCommandLineArgs.contains(BASE_DIR_COMMAND)){
                    this += BASE_DIR_COMMAND
                    this += homeDirectory.absolutePath
                }

                if (!baseCommandLineArgs.contains(SAVE_DIR_COMMAND)){
                    this += SAVE_DIR_COMMAND
                    this += savesDirectory.absolutePath
                }

                if (!baseCommandLineArgs.contains(SKIP_INTROS_COMMAND) &&
                    perfectDarkPreferencesStorage.skipIntroCutScenes.value!!){
                    this += SKIP_INTROS_COMMAND
                }

                this += GL_VERSION_COMMAND
                this + OPENGL_ES_3_0_VERSION

                this.toTypedArray()
            }
        }

    private external fun setPathToHomeDirectory (pathToHomeDirectory : String)

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        homeDirectory.mkdirs()
        savesDirectory.mkdirs()
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(PerfectDarkEngineInfo::class.java, mainLibraryName)
        setPathToHomeDirectory(homeDirectory.absolutePath)
    }

    private companion object{
        private const val OPENGL_ES_3_0_VERSION = "3.0es"
        private const val GL_VERSION_COMMAND = "--gl-version"
        private const val SKIP_INTROS_COMMAND = "--skip-intro"
        private const val PORTABLE_COMMAND = "--portable"
        private const val ROM_FILE_COMMAND = "--rom-file"
        private const val BASE_DIR_COMMAND = "--basedir"
        private const val SAVE_DIR_COMMAND = "--savedir"
        private const val MODS_DIR_COMMAND = "--moddir"
        private const val PERFECT_DARK_FOLDER_NAME = "perfect_dark"
        private const val SAVE_FOLDER_NAME = "saves"
    }
}