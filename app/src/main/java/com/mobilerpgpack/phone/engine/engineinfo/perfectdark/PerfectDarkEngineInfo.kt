package com.mobilerpgpack.phone.engine.engineinfo.perfectdark

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class PerfectDarkEngineInfo : SDL2EngineInfo
    ("", emptyArray(), EngineTypes.PerfectDark) {

    private val pathToHomeDirectory = pathToRootUserFolder + File.separator +"perfect_dark"

    private val pathToSavesDirectory = pathToHomeDirectory + File.separator + "saves"

    private val perfectDarkPreferencesStorage : PerfectDarkPreferencesStorage by inject (
        named(EngineTypes.PerfectDark.name))

    private val pathToModsDirectory by lazy {
        if (perfectDarkPreferencesStorage.enablePerfectDarkModsSupport) perfectDarkPreferencesStorage
            .pathToPerfectDarkModsFolder else ""
    }

    private val romType get() = perfectDarkPreferencesStorage.romType

    override val preferencesStorage: PreferencesStorage get() = perfectDarkPreferencesStorage

    override val commandLineParams by lazy { perfectDarkPreferencesStorage.commandLineArgs }

    override val requiredResourceExtensions = listOf(".z64", ".Z64")

    override val mainLibraryName: String by lazy { romType.mainLibraryName }

    override val nativeLibraries by lazy { get<Array<String>> (named(romType.name)) }

    override val pathToResource get() =
        when (romType) {
            PerfectDarkRomTypes.NTSC -> perfectDarkPreferencesStorage.pathToNTSCRom
            PerfectDarkRomTypes.PAL -> perfectDarkPreferencesStorage.pathToPalRom
            PerfectDarkRomTypes.JPN -> perfectDarkPreferencesStorage.pathToJpnRom
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

                val enableMods = perfectDarkPreferencesStorage.enablePerfectDarkModsSupport
                        && pathToModsDirectory.isNotEmpty() && File(pathToModsDirectory).exists()

                if (!baseCommandLineArgs.contains(MODS_DIR_COMMAND) && enableMods){
                    this += MODS_DIR_COMMAND
                    this += pathToModsDirectory
                }

                if (!baseCommandLineArgs.contains(BASE_DIR_COMMAND)){
                    this += BASE_DIR_COMMAND
                    this += pathToHomeDirectory
                }

                if (!baseCommandLineArgs.contains(SAVE_DIR_COMMAND)){
                    this += SAVE_DIR_COMMAND
                    this += pathToSavesDirectory
                }

                if (!baseCommandLineArgs.contains(SKIP_INTROS_COMMAND) &&
                    perfectDarkPreferencesStorage.skipIntroCutScenes){
                    this += SKIP_INTROS_COMMAND
                }

                this += GL_VERSION_COMMAND
                this + OPENGL_2_1_VERSION

                this.toTypedArray()
            }
        }

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        val homeDirectory = File(pathToHomeDirectory)
        val savesDirectory = File (pathToSavesDirectory)
        if (!homeDirectory.exists()){
            homeDirectory.mkdirs()
        }
        if (!savesDirectory.exists()){
            savesDirectory.mkdirs()
        }
        Os.setenv("HOME_DIRECTORY", pathToHomeDirectory, true)
    }

    private companion object{
        private const val OPENGL_2_1_VERSION = "3.0es"
        private const val GL_VERSION_COMMAND = "--gl-version"
        private const val SKIP_INTROS_COMMAND = "--skip-intro"
        private const val PORTABLE_COMMAND = "--portable"
        private const val ROM_FILE_COMMAND = "--rom-file"
        private const val BASE_DIR_COMMAND = "--basedir"
        private const val SAVE_DIR_COMMAND = "--savedir"
        private const val MODS_DIR_COMMAND = "--moddir"
    }
}