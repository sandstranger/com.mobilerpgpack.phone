package com.mobilerpgpack.phone.engine.engineinfo.fteqw

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.main.FREETYPE_NATIVE_LIB_NAME
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class FTEQWEngineInfo(mainEngineLib: String, allLibs: Array<String>) :
    SDL2EngineInfo(mainEngineLib, allLibs, EngineTypes.FTEQW){

    private val homeDirFile : File by lazy {
        File(activity.getExternalFilesDir("")!!.absolutePath + File.separator + "FTEQW")
    }

    private val gameType get() = fteQWPrefsStorage.activeFTEQWGame

    private val fteQWPrefsStorage : FTEQWPreferencesStorage by inject (named(EngineTypes.FTEQW.name))

    private val pathToModsDirectory : String get() {
        return when (gameType) {
            FTEQWGames.Quake -> fteQWPrefsStorage.pathToQuake1ModsDir
            FTEQWGames.Quake2 -> fteQWPrefsStorage.pathToQuake2ModsDir
            FTEQWGames.Quake3 -> fteQWPrefsStorage.pathToQuake3ModsDir
            FTEQWGames.Hexen2 -> fteQWPrefsStorage.pathToHexen2ModsDir
            FTEQWGames.Halflife -> fteQWPrefsStorage.pathToHalfLifeModsDir
        }
    }

    private val pathToBaseGameDirectory : String get() {
        return when (gameType) {
            FTEQWGames.Quake -> fteQWPrefsStorage.pathToQuake1BaseDir
            FTEQWGames.Quake2 -> fteQWPrefsStorage.pathToQuake2BaseDir
            FTEQWGames.Quake3 -> fteQWPrefsStorage.pathToQuake3BaseDir
            FTEQWGames.Hexen2 -> fteQWPrefsStorage.pathToHexen2BaseDir
            FTEQWGames.Halflife -> fteQWPrefsStorage.pathToHalfLifeBaseDir
        }
    }

    private val pathToManifest : String get() {
        return when (gameType) {
            FTEQWGames.Quake -> fteQWPrefsStorage.pathToQuake1Manifest
            FTEQWGames.Quake2 -> fteQWPrefsStorage.pathToQuake2Manifest
            FTEQWGames.Quake3 -> fteQWPrefsStorage.pathToQuake3Manifest
            FTEQWGames.Hexen2 -> fteQWPrefsStorage.pathToHexen2Manifest
            FTEQWGames.Halflife -> fteQWPrefsStorage.pathToHalfLifeManifest
        }
    }

    override val loadGL4ES = false

    override val preferencesStorage get() = fteQWPrefsStorage

    override val commandLineParams: String get() = fteQWPrefsStorage.commandLineArgs

    override val pathToResource : String get() {
        return when (gameType) {
            FTEQWGames.Quake -> fteQWPrefsStorage.pathToQuake1
            FTEQWGames.Quake2 -> fteQWPrefsStorage.pathToQuake2
            FTEQWGames.Quake3 -> fteQWPrefsStorage.pathToQuake3
            FTEQWGames.Hexen2 -> fteQWPrefsStorage.pathToQuake3
            FTEQWGames.Halflife -> fteQWPrefsStorage.pathToHalfLife
        }
    }

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

                if (pathToBaseGameDirectory.isNotEmpty()) {
                    val pathToBaseGameDirectory = File(pathToBaseGameDirectory)
                    if (!baseCommandLineArgs.contains(BASE_GAME_COMMAND) && pathToBaseGameDirectory.exists()){
                        this += BASE_GAME_COMMAND
                        this += pathToBaseGameDirectory.name
                    }
                }

                if (pathToModsDirectory.isNotEmpty()) {
                    val pathToModsDirectory = File(pathToModsDirectory)
                    if (preferencesStorage.enableFTEQWModsSupport && !
                        baseCommandLineArgs.contains(GAME_COMMAND) && pathToModsDirectory.exists()
                    ) {
                        this += GAME_COMMAND
                        this += pathToModsDirectory.name
                    }
                }

                if (pathToManifest.isNotEmpty()) {
                    val pathToManifest = File(pathToManifest)
                    if (!baseCommandLineArgs.contains(MANIFEST_COMMAND) && pathToManifest.exists()){
                        this += MANIFEST_COMMAND
                        this += pathToManifest.name
                    }
                }

                toTypedArray()
            }
        }

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        if (!homeDirFile.exists()){
            homeDirFile.mkdirs()
        }
        Os.setenv("PATH_TO_HOME_DIRECTORY", homeDirFile.absolutePath, true)
        Os.setenv("FREETYPE_LIBRARY_NAME", FREETYPE_NATIVE_LIB_NAME, true)
        Os.setenv("DLL_DEFAULT_PATH", activity.applicationInfo.nativeLibraryDir, true)
    }

    private companion object {
        private const val HOME_DIR_COMMAND = "-homedir"
        private const val BASE_DIR_COMMAND = "-basedir"
        private const val BASE_GAME_COMMAND = "-basegame"
        private const val GAME_COMMAND = "-game"
        private const val MANIFEST_COMMAND = "-manifest"
    }
}