package com.mobilerpgpack.phone.engine.activity

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.system.Os
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.databinding.EngineActivityBinding
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.engine.setFullscreen
import com.mobilerpgpack.phone.ui.items.MouseIcon
import com.mobilerpgpack.phone.ui.screen.OnScreenController
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.displayInSafeArea
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface
import java.io.File

private const val RESOLUTION_DELIMITER = "x"

class EngineActivity : SDLActivity() {
    private val screenControlsVisibilityUpdater = CoroutineScope(Dispatchers.Default)

    private lateinit var activeEngineType: EngineTypes
    private lateinit var pathToLog: String
    private lateinit var logcatProcess: Process
    private var controlsOverlayUI : View? = null

    private var hideScreenControls: Boolean = false
    private var showCustomMouseCursor: Boolean = false
    private var allowToEditScreenControlsInGame = false
    private var isCursorVisible by mutableIntStateOf(0)
    private var enableControlsAutoHidingFeature = false
    private var needToShowControlsLastState : Boolean = false
    private var displayInSafeArea : Boolean = false

    private external fun pauseSound()

    private external fun resumeSound()

    private external fun needToShowScreenControls () : Boolean

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
        screenControlsVisibilityUpdater.cancel()
        killEngine()
    }

    private fun initializeEngineData() {
        var pathToEngineResourceFile: File
        var needToPreserveScreenAspectRatio = false
        var customScreenResolution = ""

        runBlocking {
            hideScreenControls =
                PreferencesStorage.getHideScreenControlsValue(this@EngineActivity).first()!!
            activeEngineType = PreferencesStorage.getActiveEngineValue(this@EngineActivity)
            enableControlsAutoHidingFeature = PreferencesStorage.getControlsAutoHidingValue(this@EngineActivity)
                .first()!! && activeEngineType!= EngineTypes.DoomRpg && !hideScreenControls
            allowToEditScreenControlsInGame =
                PreferencesStorage.getEditCustomScreenControlsInGameValue(this@EngineActivity)
                    .first()!!
            showCustomMouseCursor =
                PreferencesStorage.getShowCustomMouseCursorValue(this@EngineActivity).first()!!
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
            val binding = EngineActivityBinding.inflate(layoutInflater)

            window.addContentView(
                binding.root,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            if (!showCustomMouseCursor){
                binding.mouseOverlayUI.visibility = View.GONE
            }

            if (hideScreenControls){
                binding.controlsOverlayUI.visibility = View.GONE
            }
            else{
                controlsOverlayUI = binding.controlsOverlayUI
            }

            binding.sdlContainer.post {
                binding.sdlContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {

                        if (showCustomMouseCursor){
                            binding.mouseOverlayUI.setContent {
                                AutoMouseModeComposable()
                                if (isCursorVisible == 1) {
                                    MouseIcon()
                                }
                            }
                        }

                        if (!hideScreenControls) {
                            binding.controlsOverlayUI.setContent {
                                OnScreenController(
                                    enginesInfo[activeEngineType]!!.buttonsToDraw,
                                    inGame = true,
                                    activeEngine = activeEngineType,
                                    allowToEditControls = allowToEditScreenControlsInGame,
                                    drawInSafeArea = displayInSafeArea
                                )
                            }
                        }

                        binding.sdlContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })

                if (enableControlsAutoHidingFeature) {
                    needToShowControlsLastState = true
                    screenControlsVisibilityUpdater.launch {
                        changeScreenControlsVisibility()
                    }
                }
            }
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

    private suspend fun changeScreenControlsVisibility(){
        if (this@EngineActivity.controlsOverlayUI == null){
            return
        }

        while (true){
            val needToShowControls = needToShowScreenControls()
            if (needToShowControls != needToShowControlsLastState){
                this@EngineActivity.runOnUiThread {
                    if (needToShowControls) {
                        this@EngineActivity.controlsOverlayUI!!.visibility = View.VISIBLE
                    } else {
                        this@EngineActivity.controlsOverlayUI!!.visibility = View.GONE
                    }
                }
            }
            needToShowControlsLastState = needToShowControls
            delay(200)
        }
    }
}