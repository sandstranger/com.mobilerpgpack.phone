package com.mobilerpgpack.phone.engine

import android.content.Context
import android.os.Environment
import android.os.Process
import android.system.Os
import android.view.View
import com.mobilerpgpack.phone.engine.activity.EngineActivity
import com.mobilerpgpack.phone.utils.startActivity

internal const val MAIN_ENGINE_NATIVE_LIB = "libWolfensteinRPG.so"
internal val libsArray= arrayOf("GL","SDL2","openal","WolfensteinRPG")

@Suppress("DEPRECATION")
internal fun setFullscreen(decorView: View) {
    val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    decorView.systemUiVisibility = uiOptions
}

fun killEngine() = Process.killProcess(Process.myPid())

fun startEngine(context: Context) {
    Os.setenv("LIBGL_ES", "2", true)
    Os.setenv("SDL_VIDEO_GL_DRIVER", "libGL.so", true)
    Os.setenv("ANDROID_GAME_PATH","${Environment.getExternalStorageDirectory().absolutePath}/wolfenstein",true)
    Os.setenv("WOLF_IPA_FILE_NAME","Wolfenstein RPG.ipa",true)
    context.startActivity<EngineActivity>()
}