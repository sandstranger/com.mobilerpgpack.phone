package com.mobilerpgpack.phone.engine.engineinfo.fteqw

import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.main.FTEQW_MAIN_ENGINE_LIB
import com.sun.jna.Native
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class FTEQWEngineInfo : SDL2EngineInfo( EngineTypes.FTEQW){

    private val homeDirFile : File by inject { parametersOf(FTEQW_CONFIGS_DIR) }

    private val gameType get() = fteQWPrefsStorage.activeFTEQWGame.value!!

    private val fteQWPrefsStorage : FTEQWPreferencesStorage by inject (named(EngineTypes.FTEQW.name))

    private val quake2GameType get() = preferencesStorage.quake2GameType.value!!

    private val pathToModsDirectory : String get() {
        return when (gameType) {
            FTEQWGames.Quake -> fteQWPrefsStorage.pathToQuake1ModsDir.value!!
            FTEQWGames.Quake2 -> fteQWPrefsStorage.pathToQuake2ModsDir.value!!
            FTEQWGames.Quake3 -> fteQWPrefsStorage.pathToQuake3ModsDir.value!!
            FTEQWGames.Hexen2 -> fteQWPrefsStorage.pathToHexen2ModsDir.value!!
        }
    }

    private val pathToBaseGameDirectory : String get() {
        return when (gameType) {
            FTEQWGames.Quake -> fteQWPrefsStorage.pathToQuake1BaseDir.value!!
            FTEQWGames.Quake2 -> fteQWPrefsStorage.pathToQuake2BaseDir.value!!
            FTEQWGames.Quake3 -> fteQWPrefsStorage.pathToQuake3BaseDir.value!!
            FTEQWGames.Hexen2 -> fteQWPrefsStorage.pathToHexen2BaseDir.value!!
        }
    }

    private val pathToManifest : String get() {
        return when (gameType) {
            FTEQWGames.Quake -> fteQWPrefsStorage.pathToQuake1Manifest.value!!
            FTEQWGames.Quake2 -> fteQWPrefsStorage.pathToQuake2Manifest.value!!
            FTEQWGames.Quake3 -> fteQWPrefsStorage.pathToQuake3Manifest.value!!
            FTEQWGames.Hexen2 -> fteQWPrefsStorage.pathToHexen2Manifest.value!!
        }
    }

    override val mainLibraryName: String = FTEQW_MAIN_ENGINE_LIB

    override val supportRenderChanges = true

    override val nativeLibraries: Array<String> by lazy {
        mutableListOf<String>().run {
            this += super.nativeLibraries
            this += get <Array<String>>(named(preferencesStorage.quake2GameType.value!!.name))
            toTypedArray()
        }
    }

    override val mouseButtonsEventsCanBeInvoked = false

    override val loadGL4ES = false

    override val preferencesStorage get() = fteQWPrefsStorage

    override val commandLineParams: String get() = fteQWPrefsStorage.commandLineArgs.value!!

    override val useGyroscope: Boolean get() = !needToInvokeMouseButtonsEvents()

    override val pathToResource : String get() {
        return when (gameType) {
            FTEQWGames.Quake -> fteQWPrefsStorage.pathToQuake1.value!!
            FTEQWGames.Quake2 -> fteQWPrefsStorage.pathToQuake2.value!!
            FTEQWGames.Quake3 -> fteQWPrefsStorage.pathToQuake3.value!!
            FTEQWGames.Hexen2 -> fteQWPrefsStorage.pathToHexen2.value!!
        }
    }

    override val touchFullScreenModeCanBeUsed: Boolean get() = gameType != FTEQWGames.Quake3

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs
            return with(mutableListOf<String>()) {
                this += baseCommandLineArgs

                if (!baseCommandLineArgs.contains(HOME_DIR_COMMAND)){
                    this += HOME_DIR_COMMAND
                    this += homeDirFile.absolutePath
                }

                val pathToResources = pathToResource

                if (!baseCommandLineArgs.contains(BASE_DIR_COMMAND) && pathToResources.isNotEmpty() &&
                    File(pathToResources).exists()){
                    this += BASE_DIR_COMMAND
                    this += pathToResources
                }

                val quake2GameType = preferencesStorage.quake2GameType.value!!

                if (gameType == FTEQWGames.Quake2 &&
                    quake2GameType != Quake2Games.Quake2 && quake2GameType.isPathToGameDirectoryExists()){
                    this@with += BASE_GAME_COMMAND
                    this@with += quake2GameType.directoryName
                }
                else if (pathToBaseGameDirectory.isNotEmpty()) {
                    File(pathToBaseGameDirectory).apply {
                        if (!baseCommandLineArgs.contains(BASE_GAME_COMMAND) && exists()){
                            this@with += BASE_GAME_COMMAND
                            this@with += name
                        }
                    }
                }

                val pathToModsDirectory = pathToModsDirectory

                if (pathToModsDirectory.isNotEmpty() && pathToModsDirectory.startsWith(pathToResource)) {
                    File(pathToModsDirectory).apply {
                        if (preferencesStorage.enableFTEQWModsSupport.value!! &&
                            !baseCommandLineArgs.contains(GAME_COMMAND) && exists()
                        ) {
                            this@with += GAME_COMMAND
                            this@with += name
                        }
                    }
                }

                val pathToManifest = pathToManifest

                if (pathToManifest.isNotEmpty() && pathToManifest.startsWith(pathToResource)) {
                    File(pathToManifest).apply {
                        if (!baseCommandLineArgs.contains(MANIFEST_COMMAND) && exists()){
                            this@with += MANIFEST_COMMAND
                            this@with += name
                        }
                    }
                }
                this += gameType.commandLineArg
                toTypedArray()
            }
        }

    private external fun setPathsToResources (pathToHomeDirectory : String, pathToBaseDirectory : String,
                                              dllDefaultPath : String)
    private external fun setUIScale (uiScale : Float)
    private external fun setQuake2LibraryName (targetLibraryName : String)

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        if (!homeDirFile.exists()){
            homeDirFile.mkdirs()
        }
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(FTEQWEngineInfo::class.java, mainLibraryName)
        setPathsToResources(homeDirFile.absolutePath,pathToBaseGameDirectory,
            activity.applicationInfo.nativeLibraryDir)
        setUIScale(preferencesStorage.fteqwUIScale.value!!)
        setQuake2LibraryName(getQuake2NativeLibraryName() + ".so")
    }

    private fun Quake2Games.isPathToGameDirectoryExists () =
        File(pathToResource + File.separator + directoryName).exists()

    private fun getQuake2NativeLibraryName () : String {
        return quake2GameType.run {
            if (this != Quake2Games.Quake2 && this.isPathToGameDirectoryExists()) this.nativeLibraryName else
                Quake2Games.Quake2.nativeLibraryName
        }
    }

    private companion object {
        private const val HOME_DIR_COMMAND = "-homedir"
        private const val BASE_DIR_COMMAND = "-basedir"
        private const val BASE_GAME_COMMAND = "-basegame"
        private const val GAME_COMMAND = "-game"
        private const val MANIFEST_COMMAND = "-manifest"
        private const val FTEQW_CONFIGS_DIR = "FTEQW"
    }
}