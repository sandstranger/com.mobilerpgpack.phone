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

    override val callExitProcessOnDestroy: Boolean = false

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
                    File(pathToBaseGameDirectory).apply {
                        if (!baseCommandLineArgs.contains(BASE_GAME_COMMAND) && exists()){
                            this@with += BASE_GAME_COMMAND
                            this@with += name
                        }
                    }
                }

                if (pathToModsDirectory.isNotEmpty()) {
                    File(pathToModsDirectory).apply {
                        if (preferencesStorage.enableFTEQWModsSupport &&
                            !baseCommandLineArgs.contains(GAME_COMMAND) && exists()
                        ) {
                            this@with += GAME_COMMAND
                            this@with += name
                        }
                    }
                }

                if (pathToManifest.isNotEmpty()) {
                    File(pathToManifest).apply {
                        if (!baseCommandLineArgs.contains(MANIFEST_COMMAND) && exists()){
                            this@with += MANIFEST_COMMAND
                            this@with += name
                        }
                    }
                }

                this += gameType.name

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
        Os.setenv("PATH_TO_BASE_DIRECTORY", pathToBaseGameDirectory, true)
        Os.setenv("RENDER_TYPE", preferencesStorage.fteQWRenderType.jniRenderName, true)
    }

    private companion object {
        private const val HOME_DIR_COMMAND = "-homedir"
        private const val BASE_DIR_COMMAND = "-basedir"
        private const val BASE_GAME_COMMAND = "-basegame"
        private const val GAME_COMMAND = "-game"
        private const val MANIFEST_COMMAND = "-manifest"
    }
}