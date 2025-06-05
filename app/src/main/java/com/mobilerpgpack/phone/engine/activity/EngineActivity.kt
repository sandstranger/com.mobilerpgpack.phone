package com.mobilerpgpack.phone.engine.activity

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.system.Os
import android.util.Log
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.engine.setFullscreen
import com.mobilerpgpack.phone.ui.items.MouseIcon
import com.mobilerpgpack.phone.ui.screen.OnScreenController
import com.mobilerpgpack.phone.ui.views.TouchCamera
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.displayInSafeArea
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface
import java.io.File

private const val RESOLUTION_DELIMITER = "x"

class EngineActivity : SDLActivity() {
    private lateinit var activeEngineType: EngineTypes
    private lateinit var pathToLog: String
    private lateinit var logcatProcess: Process
    private lateinit var sdlView: View

    private var hideScreenControls: Boolean = false
    private var showCustomMouseCursor: Boolean = false
    private var allowToEditScreenControlsInGame = false
    private var isCursorVisible by mutableIntStateOf(0)

    private external fun pauseSound()

    private external fun resumeSound()

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        initializeEngineData()
        super.onCreate(savedInstanceState)
        loadControlsLayout()
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

    private fun initializeEngineData() {
        var pathToEngineResourceFile: File
        var needToPreserveScreenAspectRatio = false
        var displayInSafeArea = false
        var customScreenResolution = ""

        runBlocking {
            allowToEditScreenControlsInGame =
                PreferencesStorage.getEditCustomScreenControlsInGameValue(this@EngineActivity)
                    .first()!!
            showCustomMouseCursor =
                PreferencesStorage.getShowCustomMouseCursorValue(this@EngineActivity).first()!!
            hideScreenControls =
                PreferencesStorage.getHideScreenControlsValue(this@EngineActivity).first()!!
            activeEngineType = PreferencesStorage.getActiveEngineValue(this@EngineActivity)
            pathToLog = PreferencesStorage.getPathToLogFileValue(this@EngineActivity).first()!!
            customScreenResolution =
                PreferencesStorage.getCustomScreenResolutionValue(this@EngineActivity).first()!!
            displayInSafeArea =
                PreferencesStorage.getDisplayInSafeAreaValue(this@EngineActivity).first()!!
            needToPreserveScreenAspectRatio =
                PreferencesStorage.getPreserveAspectRatioValue(this@EngineActivity).first()!!
            pathToEngineResourceFile = File(
                enginesInfo[activeEngineType]!!.pathToResourcesCallback(this@EngineActivity)
                    .first()!!
            )
        }

        var customScreenResolutionWasSet = setScreenResolution(customScreenResolution)

        if (needToPreserveScreenAspectRatio && !customScreenResolutionWasSet) {
            preserve16x9ScreenAspectRatio()
        }

        if (displayInSafeArea) {
            this.displayInSafeArea()
        }

        logcatProcess = enableLogcat()

        Os.setenv("LIBGL_ES", "2", true)
        Os.setenv("SDL_VIDEO_GL_DRIVER", "libGL.so", true)

        if (pathToEngineResourceFile.isFile) {
            Os.setenv("ANDROID_GAME_PATH", pathToEngineResourceFile.parent, true)
            Os.setenv("RESOURCE_FILE_NAME", pathToEngineResourceFile.name, true)
        } else {
            Os.setenv("ANDROID_GAME_PATH", pathToEngineResourceFile.absolutePath, true)
        }
    }

    private fun enableLogcat(): Process {
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

    private fun setScreenResolution(savedScreenResolution: String): Boolean {
        if (savedScreenResolution.isNotEmpty() && savedScreenResolution.contains(
                RESOLUTION_DELIMITER
            )
        ) {
            try {
                val resolutionsArray = savedScreenResolution.split(RESOLUTION_DELIMITER)
                setScreenResolution(
                    Integer.parseInt(resolutionsArray[0]),
                    Integer.parseInt(resolutionsArray[1])
                )
                return true
            } catch (_: Exception) {
            }
        }

        return false
    }

    private fun setScreenResolution(screenWidth: Int, screenHeight: Int) {
        SDLSurface.fixedWidth = screenWidth
        SDLSurface.fixedHeight = screenHeight
    }

    private fun loadControlsLayout() {
        if (showCustomMouseCursor || !hideScreenControls) {
            setContentView(R.layout.engine_activity)

            if (!hideScreenControls) {
                sdlView = getContentView()
            } else {
                sdlView = View(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                }
            }

            // Add SDL view programmatically
            val sdlContainer = findViewById<FrameLayout>(R.id.sdl_container)
            (sdlView.parent as? ViewGroup)?.removeView(sdlView)
            sdlContainer.addView(sdlView) // Add SDL view to the sdl_container

            val touchCamera = findViewById<TouchCamera>(R.id.touchCamera)
            (touchCamera.parent as? ViewGroup)?.removeView(touchCamera)
            sdlContainer.addView(touchCamera)

            // Setup Compose overlay for buttons
            val composeViewUI = findViewById<ComposeView>(R.id.compose_overlayUI)
            (composeViewUI.parent as? ViewGroup)?.removeView(composeViewUI)
            sdlContainer.addView(composeViewUI)

            // Adding a Global Layout Listener to get container dimensions
            sdlContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // Adds Overlay menu for buttons and edit mode
                    composeViewUI.setContent {

                        if (showCustomMouseCursor) {
                            AutoMouseModeComposable()
                            if (isCursorVisible == 1) {
                                MouseIcon()
                            }
                        }

                        if (!hideScreenControls) {
                            OnScreenController(
                                enginesInfo[activeEngineType]!!.buttonsToDraw,
                                inGame = true,
                                allowToEditControls = allowToEditScreenControlsInGame,
                                sdlView = touchCamera
                            )
                        }
                    }
                    // Remove the Global Layout Listener to prevent multiple calls
                    sdlContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun AutoMouseModeComposable() {
        var isMouseShown by remember { mutableIntStateOf(isMouseShown()) }
        // Launch a Choreographer callback to update isMouseShown in real-time
        DisposableEffect(Unit) {
            val choreographer = Choreographer.getInstance()
            val frameCallback = object : Choreographer.FrameCallback {
                override fun doFrame(frameTimeNanos: Long) {
                    isMouseShown = isMouseShown()
                    isCursorVisible = isMouseShown
                    choreographer.postFrameCallback(this)
                }
            }
            choreographer.postFrameCallback(frameCallback)

            onDispose {
                choreographer.removeFrameCallback(frameCallback)
            }
        }
    }

}