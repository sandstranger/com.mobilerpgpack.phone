package com.mobilerpgpack.phone.engine

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.os.Process
import android.system.Os
import com.afollestad.materialdialogs.MaterialDialog
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.activity.Doom64GameActivity
import com.mobilerpgpack.phone.engine.activity.DoomRpgSeriesGameActivity
import com.mobilerpgpack.phone.ui.screen.doom2RPGButtons
import com.mobilerpgpack.phone.ui.screen.doomRPGButtons
import com.mobilerpgpack.phone.ui.screen.wolfensteinButtons
import com.mobilerpgpack.phone.utils.AssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.startActivity
import kotlinx.coroutines.flow.first
import java.io.File

const val logcatFileName = "wolfenstein_doom_rpg_log.log"

val gl4esLibraryName : String
    get() {
        return if (BuildConfig.LEGACY_GLES2) return "gl4es" else return "ng_gl4es"
    }

val gl4esFullLibraryName get() = "lib${gl4esLibraryName}.so"

val pngLibraryName get() = if (BuildConfig.DEBUG) "png18d" else "png18"

internal val enginesInfo : HashMap<EngineTypes, EngineInfo> = if (BuildConfig.LEGACY_GLES2) hashMapOf(
    EngineTypes.Doom64ExPlus to EngineInfo("libDOOM64.so","DOOM64",
        arrayOf(gl4esLibraryName,"SDL3",pngLibraryName,
            "fmod","DOOM64"), wolfensteinButtons,
        pathToResourcesCallback = { context -> PreferencesStorage.getPathToDoom64MainWadsFolder (context) }),
    EngineTypes.WolfensteinRpg to EngineInfo("libWolfensteinRPG.so","WolfensteinRPG",
        arrayOf(gl4esLibraryName,"SDL2","openal","SDL2_ttf","Translator","WolfensteinRPG"), wolfensteinButtons, pathToResourcesCallback =
            { context -> PreferencesStorage.getPathToWolfensteinRpgIpaFileValue (context) } ),
    EngineTypes.DoomRpg to EngineInfo("libDoomRPG.so","DoomRPG", arrayOf(gl4esLibraryName,"fluidsynth","SDL2","gme","SDL2_mixer",
        "SDL2_ttf","Translator","DoomRPG"), doomRPGButtons,
        pathToResourcesCallback = { context -> PreferencesStorage.getPathToDoomRpgZipFileValue (context) }),
    EngineTypes.Doom2Rpg to EngineInfo("libDoomIIRPG.so","DoomIIRPG",
        arrayOf(gl4esLibraryName,"SDL2","openal","SDL2_ttf","Translator","DoomIIRPG"),
        doom2RPGButtons, pathToResourcesCallback = { context -> PreferencesStorage.getPathToDoom2RpgIpaFile (context) })
)
else
    hashMapOf(
        EngineTypes.Doom64ExPlus to EngineInfo("libDOOM64.so","DOOM64",
            arrayOf("spirv-cross-c-shared",gl4esLibraryName,"SDL3",pngLibraryName,
                "fmod","DOOM64"), wolfensteinButtons,
            pathToResourcesCallback = { context -> PreferencesStorage.getPathToDoom64MainWadsFolder (context) }),
        EngineTypes.WolfensteinRpg to EngineInfo("libWolfensteinRPG.so","WolfensteinRPG",
            arrayOf("spirv-cross-c-shared",gl4esLibraryName,"SDL2","openal","SDL2_ttf","Translator","WolfensteinRPG"), wolfensteinButtons, pathToResourcesCallback =
                { context -> PreferencesStorage.getPathToWolfensteinRpgIpaFileValue (context) } ),
        EngineTypes.DoomRpg to EngineInfo("libDoomRPG.so","DoomRPG", arrayOf("spirv-cross-c-shared",gl4esLibraryName,"fluidsynth","SDL2","gme","SDL2_mixer",
            "SDL2_ttf","Translator","DoomRPG"), doomRPGButtons,
            pathToResourcesCallback = { context -> PreferencesStorage.getPathToDoomRpgZipFileValue (context) }),
        EngineTypes.Doom2Rpg to EngineInfo("libDoomIIRPG.so","DoomIIRPG",
            arrayOf("spirv-cross-c-shared",gl4esLibraryName,"SDL2","openal","SDL2_ttf","Translator","DoomIIRPG"),
            doom2RPGButtons, pathToResourcesCallback = { context -> PreferencesStorage.getPathToDoom2RpgIpaFile (context) })
    )

internal val defaultPathToLogcatFile: String = "${Environment.getExternalStorageDirectory().absolutePath}" +
        "${File.separator}$logcatFileName"

fun killEngine() = Process.killProcess(Process.myPid())

suspend fun startEngine(context: Context, engineToPlay : EngineTypes) {
    if (!AssetExtractor.assetsCopied){
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
        EngineTypes.Doom64ExPlus -> startEngineActivity<Doom64GameActivity>(context, engineToPlay)
        else -> startEngineActivity<DoomRpgSeriesGameActivity>(context, engineToPlay)
    }
}

internal fun initializeCommonEngineData (context: Context){
    Os.setenv("LIBGL_SIMPLE_SHADERCONV", "0", true)
    Os.setenv("LIBGL_DXTMIPMAP", "1", true)
    Os.setenv("ENABLE_ANGLE", "0", true)
    Os.setenv("LIBGL_ES",if (BuildConfig.LEGACY_GLES2) "2" else "3",true)
    Os.setenv("LIBGL_GL","21", true)
    Os.setenv("LIBGL_DXT", "1", true)
    Os.setenv("LIBGL_NOTEXARRAY","0",true)
    Os.setenv("LIBGL_NOPSA", "0",true)
    Os.setenv("LIBGL_PSA_FOLDER",context.getExternalFilesDir("")!!.absolutePath,true)
    Os.setenv("SDL_VIDEO_GL_DRIVER", gl4esFullLibraryName, true)
    Os.setenv("PATH_TO_SDL2_CONTROLLER_DB", getPathToSDL2ControllerDB(context),true)
}

private suspend inline fun <reified T> startEngineActivity (context: Context, engineToPlay : EngineTypes) where T : Activity {
    if (isDoomRpgSeriesResourceReady(context, engineToPlay)){
        context.startActivity<T>()
    }
}

private suspend fun isDoomRpgSeriesResourceReady (context: Context,engineToPlay : EngineTypes) : Boolean{
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

private fun getPathToSDL2ControllerDB (context: Context) =
    "${context.getExternalFilesDir("")!!.absolutePath}${File.separator}gamecontrollerdb.txt"
