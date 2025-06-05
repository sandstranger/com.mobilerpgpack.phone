package com.mobilerpgpack.phone.engine.activity

import android.content.res.Resources
import android.os.Bundle
import android.system.Os
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.engine.setFullscreen
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.displayInSafeArea
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface
import java.io.File

private const val RESOLUTION_DELIMITER = "x"

class EngineActivity : SDLActivity() {
    private lateinit var activeEngineType : EngineTypes
    private lateinit var pathToLog : String
    private lateinit var logcatProcess : Process

    private external fun pauseSound()

    private external fun resumeSound()

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        initializeEngineData()
        super.onCreate(savedInstanceState)
    }

    override fun getMainSharedObject() = enginesInfo[activeEngineType]!!.mainEngineLib

    override fun getLibraries() = enginesInfo[activeEngineType]!!.allLibs

    override fun onPause() {
        super.onPause()
        pauseSound()
    }

    override fun onResume() {
        super.onResume()
        resumeSound()
    }

    override fun onDestroy() {
        super.onDestroy()
        logcatProcess.destroy()
        killEngine()
    }

    private fun initializeEngineData(){
        var pathToEngineResourceFile : File
        var needToPreserveScreenAspectRatio = false
        var displayInSafeArea = false
        var customScreenResolution = ""

        runBlocking {
            activeEngineType = PreferencesStorage.getActiveEngineValue(this@EngineActivity)
            pathToLog = PreferencesStorage.getPathToLogFileValue(this@EngineActivity).first()!!
            customScreenResolution = PreferencesStorage.getCustomScreenResolutionValue(this@EngineActivity).first()!!
            displayInSafeArea = PreferencesStorage.getDisplayInSafeAreaValue(this@EngineActivity).first()!!
            needToPreserveScreenAspectRatio = PreferencesStorage.getPreserveAspectRatioValue(this@EngineActivity).first()!!
            pathToEngineResourceFile = File(enginesInfo[activeEngineType]!!.pathToResourcesCallback(this@EngineActivity).first()!!)
        }

        var customScreenResolutionWasSet = setScreenResolution(customScreenResolution)

        if (needToPreserveScreenAspectRatio && !customScreenResolutionWasSet){
            preserve16x9ScreenAspectRatio()
        }

        if (displayInSafeArea){
            this.displayInSafeArea()
        }

        logcatProcess = enableLogcat()

        Os.setenv("LIBGL_ES", "2", true)
        Os.setenv("SDL_VIDEO_GL_DRIVER", "libGL.so", true)

        if (pathToEngineResourceFile.isFile){
            Os.setenv("ANDROID_GAME_PATH",pathToEngineResourceFile.parent,true)
            Os.setenv("RESOURCE_FILE_NAME",pathToEngineResourceFile.name,true)
        }
        else{
            Os.setenv("ANDROID_GAME_PATH",pathToEngineResourceFile.absolutePath,true)
        }
    }

    private fun enableLogcat() : Process {
        val logcatFile = File(pathToLog)
        if (logcatFile.exists()) {
            logcatFile.delete()
        }

        val processBuilder = ProcessBuilder()
        val commandToExecute = arrayOf("/system/bin/sh", "-c", "logcat *:W -d -f $pathToLog")
        processBuilder.command(*commandToExecute)
        processBuilder.redirectErrorStream(true)
        return processBuilder.start()
    }

    private fun preserve16x9ScreenAspectRatio() {
        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val targetRatio = 16f / 9f
        val screenRatio = screenWidth.toFloat() / screenHeight

        if (screenRatio > targetRatio) {
            val newWidth = (screenHeight * targetRatio).toInt()
            setScreenResolution(newWidth, screenHeight)
        } else {
            val newHeight = (screenWidth / targetRatio).toInt()
            setScreenResolution(screenWidth, newHeight)
        }
    }

    private fun setScreenResolution ( savedScreenResolution : String ) : Boolean {
        if (savedScreenResolution.isNotEmpty() && savedScreenResolution.contains(RESOLUTION_DELIMITER)) {
            try {
                val resolutionsArray = savedScreenResolution.split(RESOLUTION_DELIMITER)
                setScreenResolution(Integer.parseInt(resolutionsArray[0]), Integer.parseInt(resolutionsArray[1]))
                return true
            } catch (_: Exception) {
            }
        }

        return false
    }

    private fun setScreenResolution (screenWidth : Int, screenHeight: Int ){
        SDLSurface.fixedWidth = screenWidth
        SDLSurface.fixedHeight = screenHeight
    }
}