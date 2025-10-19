package com.mobilerpgpack.phone.engine.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.system.Os
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.databinding.EngineActivityBinding
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.initializeCommonEngineData
import com.mobilerpgpack.phone.engine.killEngine
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.ui.items.BoxGrid2
import com.mobilerpgpack.phone.ui.items.MouseIcon
import com.mobilerpgpack.phone.ui.screen.OnScreenController
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.displayInSafeArea
import com.mobilerpgpack.phone.utils.hideSystemBars
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
private const val AndroidGamePathEnvName = "ANDROID_GAME_PATH"
private const val ResourceFileNameEnvName = "RESOURCE_FILE_NAME"

class DoomRpgSeriesGameActivity : SDLActivity() {
    private val scope = CoroutineScope(Dispatchers.Default)

    private lateinit var activeEngineType: EngineTypes
    private lateinit var pathToLog: String
    private var controlsOverlayUI : View? = null
    private var virtualKeyboardView : View? = null
    private var showVirtualKeyboardSavedState by mutableStateOf(false)

    private var hideScreenControls: Boolean = false
    private var showCustomMouseCursor: Boolean = false
    private var allowToEditScreenControlsInGame = false
    private var isCursorVisible by mutableIntStateOf(0)
    private var enableControlsAutoHidingFeature = false
    private var needToShowControlsLastState : Boolean = false
    private var displayInSafeArea : Boolean = false
    private lateinit var resolution: Pair<Int, Int>
    private var savedDoomRpgScreenWidth : Int = 0
    private var savedDoomRpgScreenHeight : Int = 0

    private external fun pauseSound()

    private external fun resumeSound()

    private external fun needToShowScreenControls () : Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeEngineData()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBars()
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
        scope.cancel()
        killEngine()
    }

    private fun initializeEngineData() {
        var pathToEngineResourceFile: File
        var customAspectRatio = ""
        var customScreenResolution = ""
        var useSdlTTFForTextRendering = false
        var enableMachineTranslation = false

        runBlocking {
            useSdlTTFForTextRendering = PreferencesStorage.getUseSDLTTFForFontsRenderingValue(this@DoomRpgSeriesGameActivity).first()!!
            enableMachineTranslation = PreferencesStorage.getEnableGameMachineTextTranslationValue(this@DoomRpgSeriesGameActivity).first()!!
            savedDoomRpgScreenWidth = PreferencesStorage.getIntValue(this@DoomRpgSeriesGameActivity,
                PreferencesStorage.savedDoomRpgScreenWidthPrefsKey).first()!!
            savedDoomRpgScreenHeight= PreferencesStorage.getIntValue(this@DoomRpgSeriesGameActivity,
                PreferencesStorage.savedDoomRpgScreenHeightPrefsKey).first()!!
            hideScreenControls =
                PreferencesStorage.getHideScreenControlsValue(this@DoomRpgSeriesGameActivity).first()!!
            activeEngineType = PreferencesStorage.getActiveEngineValue(this@DoomRpgSeriesGameActivity)
            enableControlsAutoHidingFeature = PreferencesStorage.getControlsAutoHidingValue(this@DoomRpgSeriesGameActivity)
                .first()!! && activeEngineType!= EngineTypes.DoomRpg && !hideScreenControls
            allowToEditScreenControlsInGame =
                PreferencesStorage.getEditCustomScreenControlsInGameValue(this@DoomRpgSeriesGameActivity)
                    .first()!!
            showCustomMouseCursor =
                PreferencesStorage.getShowCustomMouseCursorValue(this@DoomRpgSeriesGameActivity).first()!!
            pathToLog = PreferencesStorage.getPathToLogFileValue(this@DoomRpgSeriesGameActivity).first()!!
            customScreenResolution =
                PreferencesStorage.getCustomScreenResolutionValue(this@DoomRpgSeriesGameActivity).first()!!
            displayInSafeArea =
                PreferencesStorage.getDisplayInSafeAreaValue(this@DoomRpgSeriesGameActivity).first()!!
            customAspectRatio = PreferencesStorage.getCustomAspectRatioValue(this@DoomRpgSeriesGameActivity).first()
            pathToEngineResourceFile = File(
                enginesInfo[activeEngineType]!!.pathToResourcesCallback(this@DoomRpgSeriesGameActivity)
                    .first()!!
            )
        }

        TranslationManager.inGame = true
        TranslationManager.activeEngine = activeEngineType

        resolution = getRealScreenResolution()

        val customScreenResolutionWasSet = setScreenResolution(customScreenResolution)

        if (!customAspectRatio.isEmpty() && !customScreenResolutionWasSet) {
            preserveCustomScreenAspectRatio(customAspectRatio)
        }

        if (displayInSafeArea) {
            this.displayInSafeArea()
        }

        initializeCommonEngineData(this)
        Os.setenv("ENABLE_SDL_TTF", useSdlTTFForTextRendering.toString().lowercase(),true)
        Os.setenv("ENABLE_TEXTS_MACHINE_TRANSLATION", enableMachineTranslation.toString().lowercase(),true)

        if (activeEngineType == EngineTypes.DoomRpg){
            val (width, height) = getDefaultDoomRpgResolution()
            if (savedDoomRpgScreenWidth!=width && savedDoomRpgScreenHeight!=height){
                scope.launch {
                    PreferencesStorage.setIntValue(this@DoomRpgSeriesGameActivity,PreferencesStorage.savedDoomRpgScreenWidthPrefsKey,
                        width)
                    PreferencesStorage.setIntValue(this@DoomRpgSeriesGameActivity,PreferencesStorage.savedDoomRpgScreenHeightPrefsKey,
                        height)
                }

                Os.setenv("RECALCULATE_RESOLUTION_INDEX","true",true)
            }
            else{
                Os.setenv("RECALCULATE_RESOLUTION_INDEX","false",true)
            }
            Os.setenv("SCREEN_WIDTH", width.toString(), true)
            Os.setenv("SCREEN_HEIGHT", height.toString(), true)
            Os.setenv("FORCE_FILE_PATH", "true", true)
        }

        if (pathToEngineResourceFile.isFile) {
            Os.setenv(AndroidGamePathEnvName, this@DoomRpgSeriesGameActivity.getExternalFilesDir("")!!.absolutePath, true)
            Os.setenv(ResourceFileNameEnvName, pathToEngineResourceFile.absolutePath, true)
        } else {
            Os.setenv(AndroidGamePathEnvName, pathToEngineResourceFile.absolutePath, true)
        }
    }

    private fun preserveCustomScreenAspectRatio(customAspectRatio : String) {
        val aspectRatioData = parseString(customAspectRatio)
        if (aspectRatioData!=null) {
            val screenWidth = resolution.first
            val screenHeight = resolution.second
            val targetRatio = aspectRatioData.first.toFloat() / aspectRatioData.second.toFloat()
            val screenRatio = screenWidth.toFloat() / screenHeight

            if (screenRatio > targetRatio) {
                val newWidth = (screenHeight * targetRatio).toInt()
                setScreenResolution(newWidth, screenHeight)
            } else {
                val newHeight = (screenWidth / targetRatio).toInt()
                setScreenResolution(screenWidth, newHeight)
            }
        }
    }

    private fun parseString (input: String) : Pair<Int, Int>?{
        if (input.isNotEmpty() && input.contains(RESOLUTION_DELIMITER)) {
            try {
                val array = input.split(RESOLUTION_DELIMITER)
                return Integer.parseInt(array[0]) to Integer.parseInt(array[1])
            } catch (_: Exception) {
            }
        }
        return null
    }

    private fun setScreenResolution(savedScreenResolution: String): Boolean {
        val screenResolutionData = parseString(savedScreenResolution)
        if (screenResolutionData!=null) {
            setScreenResolution(screenResolutionData.first,screenResolutionData.second)
            return true
        }

        return false
    }

    private fun setScreenResolution(screenWidth: Int, screenHeight: Int) {
        SDLSurface.fixedWidth = screenWidth
        SDLSurface.fixedHeight = screenHeight
    }

    private fun getDefaultDoomRpgResolution() : Pair<Int, Int>{
        if (SDLSurface.fixedWidth > 0 && SDLSurface.fixedHeight >0){
            return SDLSurface.fixedWidth to SDLSurface.fixedHeight
        }

        return resolution
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
                virtualKeyboardView = binding.keyboardView
            }

            binding.keyboardView.visibility = View.GONE

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
                                    drawInSafeArea = displayInSafeArea,
                                    showVirtualKeyboardEvent = { showVirtualKeyboard ->
                                        showVirtualKeyboardSavedState = showVirtualKeyboard
                                        updateVirtualKeyboardVisibility(showVirtualKeyboard)
                                    }
                                )
                            }

                            binding.keyboardView.setContent {
                                BoxGrid2()
                            }
                        }

                        binding.sdlContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })

                if (enableControlsAutoHidingFeature) {
                    needToShowControlsLastState = true
                    scope.launch {
                        changeScreenControlsVisibility()
                    }
                }
            }
        }
    }

    private fun getRealScreenResolution(): Pair<Int, Int> {
        val wm = this.getSystemService(WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val realSize = android.graphics.Point()
        display.getRealSize(realSize)
        return realSize.x to realSize.y
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
        if (this@DoomRpgSeriesGameActivity.controlsOverlayUI == null){
            return
        }

        while (true){
            val needToShowControls = needToShowScreenControls()
            if (needToShowControls != needToShowControlsLastState){
                this@DoomRpgSeriesGameActivity.runOnUiThread {
                    if (needToShowControls) {
                        this@DoomRpgSeriesGameActivity.controlsOverlayUI!!.visibility = View.VISIBLE
                    } else {
                        this@DoomRpgSeriesGameActivity.controlsOverlayUI!!.visibility = View.GONE
                    }
                    updateVirtualKeyboardVisibility(needToShowControls)
                }
            }
            needToShowControlsLastState = needToShowControls
            delay(200)
        }
    }

    private fun updateVirtualKeyboardVisibility (showVirtualKeyboard: Boolean){
        virtualKeyboardView!!.visibility = if (showVirtualKeyboard && showVirtualKeyboardSavedState)
            View.VISIBLE else View.GONE
    }
}