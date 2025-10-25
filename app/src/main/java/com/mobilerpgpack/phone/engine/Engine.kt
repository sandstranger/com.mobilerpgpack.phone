package com.mobilerpgpack.phone.engine

import android.app.Activity
import android.content.Context
import android.os.Process
import android.system.Os
import com.afollestad.materialdialogs.MaterialDialog
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.activity.Doom64GameActivity
import com.mobilerpgpack.phone.engine.activity.DoomRpgSeriesGameActivity
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.ui.screen.doom2RPGButtons
import com.mobilerpgpack.phone.ui.screen.doomRPGButtons
import com.mobilerpgpack.phone.ui.screen.wolfensteinButtons
import com.mobilerpgpack.phone.utils.AssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.startActivity
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import java.io.File

internal val enginesInfo : HashMap<EngineTypes, EngineInfo> = hashMapOf(
    EngineTypes.Doom64ExPlus to EngineInfo("libDOOM64.so","DOOM64",
        arrayOf("ng_gl4es","SDL3",if (BuildConfig.DEBUG) "png16d" else "png16",
            "fmod","DOOM64"), wolfensteinButtons,
        pathToResourcesCallback = { context -> PreferencesStorage.getPathToDoom64MainWadsFolder (context) }),
    EngineTypes.WolfensteinRpg to EngineInfo("libWolfensteinRPG.so","WolfensteinRPG",
        arrayOf("ng_gl4es","SDL2","openal","SDL2_ttf","c++_shared","fbjni","Translator","WolfensteinRPG"), wolfensteinButtons, pathToResourcesCallback =
            { context -> PreferencesStorage.getPathToWolfensteinRpgIpaFileValue (context) } ),
    EngineTypes.DoomRpg to EngineInfo("libDoomRPG.so","DoomRPG", arrayOf("fluidsynth","ng_gl4es","SDL2","gme","SDL2_mixer",
        "SDL2_ttf","c++_shared","fbjni","Translator","DoomRPG"), doomRPGButtons,
        pathToResourcesCallback = { context -> PreferencesStorage.getPathToDoomRpgZipFileValue (context) }),
    EngineTypes.Doom2Rpg to EngineInfo("libDoomIIRPG.so","DoomIIRPG",
        arrayOf("ng_gl4es","SDL2","openal","SDL2_ttf","c++_shared","fbjni","Translator","DoomIIRPG"),
        doom2RPGButtons, pathToResourcesCallback = { context -> PreferencesStorage.getPathToDoom2RpgIpaFile (context) })
)

class Engine : KoinComponent {

    private val pathToRootUserFolder : String = get { parametersOf(
        KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY) }

    private val context : Context = get ()

    private val assetsExtractor : AssetExtractor = get()

    private val pathToSDL2ControllerDB = "${pathToRootUserFolder}${File.separator}gamecontrollerdb.txt"

    suspend fun startEngine(engineToPlay : EngineTypes) {
        if (assetsExtractor.assetsCopied){
            return
        }

        if (enginesInfo[engineToPlay]!!.pathToResourcesCallback(context).first()!!.isEmpty()){
            MaterialDialog(context).show {
                title(R.string.error)
                message(R.string.can_not_start_engine)
                positiveButton(R.string.ok_text)
            }
            return
        }

        when (engineToPlay) {
            EngineTypes.Doom64ExPlus -> startEngineActivity<Doom64GameActivity>(engineToPlay)
            else -> startEngineActivity<DoomRpgSeriesGameActivity>(engineToPlay)
        }
    }

    internal fun initializeCommonEngineData (){
        Os.setenv("LIBGL_SIMPLE_SHADERCONV", "1", true)
        Os.setenv("LIBGL_DXTMIPMAP", "1", true)
        Os.setenv("LIBGL_ES","3",true)
        Os.setenv("LIBGL_GL","21", true)
        Os.setenv("LIBGL_DXT", "1", true)
        Os.setenv("LIBGL_NOTEXARRAY","0",true)
        Os.setenv("LIBGL_NOPSA", "0",true)
        Os.setenv("LIBGL_PSA_FOLDER",pathToRootUserFolder,true)
        Os.setenv("SDL_VIDEO_GL_DRIVER", "libng_gl4es.so", true)
        Os.setenv("PATH_TO_SDL2_CONTROLLER_DB", pathToSDL2ControllerDB,true)
    }

    fun killEngine() = Process.killProcess(Process.myPid())

    private suspend inline fun <reified T> startEngineActivity ( engineToPlay : EngineTypes) where T : Activity {
        if (isDoomRpgSeriesResourceReady(engineToPlay)){
            context.startActivity<T>()
        }
    }

    private suspend fun isDoomRpgSeriesResourceReady (engineToPlay : EngineTypes) : Boolean{
        if (enginesInfo[engineToPlay]!!.pathToResourcesCallback(context).first()!!.isEmpty()){
            MaterialDialog(context).show {
                title(R.string.error)
                message(R.string.can_not_start_engine)
                positiveButton(R.string.ok_text)
            }
            return false
        }
        return true
    }
}