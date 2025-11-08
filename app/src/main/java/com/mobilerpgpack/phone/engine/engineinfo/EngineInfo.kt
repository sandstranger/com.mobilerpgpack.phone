package com.mobilerpgpack.phone.engine.engineinfo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Process
import android.system.Os
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.databinding.GameLayoutBinding
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.main.buildFullLibraryName
import com.mobilerpgpack.phone.main.gl4esFullLibraryName
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.ScreenResolution
import com.mobilerpgpack.phone.utils.callAs
import com.mobilerpgpack.phone.utils.displayInSafeArea
import com.mobilerpgpack.phone.utils.getScreenResolution
import com.mobilerpgpack.phone.utils.hideSystemBars
import com.sun.jna.Function
import com.sun.jna.Native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

abstract class EngineInfo(
    protected val mainEngineLib: String,
    private val allLibs: Array<String>,
    private val viewsToDraw: Collection<IScreenControlsView>,
    activeEngineType: EngineTypes,
    pathToResourceFlow: Flow<String>) : KoinComponent, IEngineInfo {

    protected val preferencesStorage: PreferencesStorage by inject()

    protected val scope = CoroutineScope(Dispatchers.Default)

    protected lateinit var resolution: ScreenResolution
        private set

    protected var controlsOverlayUI: View? = null

    protected var virtualKeyboardView: View? = null

    protected var showVirtualKeyboardSavedState by mutableStateOf(false)

    protected lateinit var activity: ComponentActivity
        private set

    protected var needToShowControlsLastState: Boolean = false

    protected val pathToRootUserFolder: String = get(
        named(
            KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY
        )
    )

    protected abstract val screenController: IScreenController

    override val engineType: EngineTypes = activeEngineType

    override val pathToResource: Flow<String> = pathToResourceFlow

    override val mainSharedObject: String get() = buildFullLibraryName(mainEngineLib)

    override val nativeLibraries: Array<String> get() = allLibs

    private lateinit var needToShowScreenControlsNativeDelegate: Function

    private var hideScreenControls: Boolean = false
    private var showCustomMouseCursor: Boolean = false
    private var allowToEditScreenControlsInGame = false
    private var isCursorVisible by mutableIntStateOf(0)
    private var enableControlsAutoHidingFeature = false
    private var displayInSafeArea: Boolean = false

    private external fun pauseSound()

    private external fun resumeSound()

    override suspend fun initialize(activity: ComponentActivity) {
        this.activity = activity
        initJna()
        initializeCommonEngineData()
        resolution = activity.getScreenResolution()

        Os.setenv("PATH_TO_RESOURCES",
            File(pathToResource.first()).absolutePath, true)

        hideScreenControls = preferencesStorage.hideScreenControls.first()
        enableControlsAutoHidingFeature = preferencesStorage.autoHideScreenControls.first()
                && engineType != EngineTypes.DoomRpg && !hideScreenControls

        allowToEditScreenControlsInGame = preferencesStorage.editCustomScreenControlsInGame.first()
        showCustomMouseCursor = preferencesStorage.showCustomMouseCursor.first()
        displayInSafeArea = preferencesStorage.enableDisplayInSafeArea.first()
        val customAspectRatio = preferencesStorage.customAspectRatio.first()
        val customScreenResolution = preferencesStorage.customScreenResolution.first()
        val customScreenResolutionWasSet = setScreenResolution(customScreenResolution)

        if (!customAspectRatio.isEmpty() && !customScreenResolutionWasSet) {
            preserveCustomScreenAspectRatio(customAspectRatio)
        }
    }

    override fun onPause() {
        pauseSound()
    }

    override fun onResume() {
        resumeSound()
    }

    override fun onDestroy() {
        scope.cancel()
        killEngine()
    }

    override fun loadLayout(){
        activity.window.decorView.post {
            activity.enableEdgeToEdge()
            activity.hideSystemBars()
            if (displayInSafeArea) {
                activity.displayInSafeArea()
                onSafeAreaApplied(activity.getScreenResolution(true))
            }
            inflateControlsLayout()
        }
    }

    private fun inflateControlsLayout() {
        if (showCustomMouseCursor || !hideScreenControls) {
            val binding = GameLayoutBinding.inflate(activity.layoutInflater)

            activity.window.addContentView(
                binding.root,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            if (!showCustomMouseCursor) {
                binding.mouseOverlayUI.visibility = View.GONE
            }

            if (hideScreenControls) {
                binding.controlsOverlayUI.visibility = View.GONE
            } else {
                controlsOverlayUI = binding.controlsOverlayUI
                virtualKeyboardView = binding.keyboardView
            }

            binding.keyboardView.visibility = View.GONE

            binding.sdlContainer.post {
                binding.sdlContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {

                        if (showCustomMouseCursor) {
                            binding.mouseOverlayUI.setContent {
                                AutoMouseModeComposable()
                                if (isCursorVisible == 1) {
                                    DrawMouseIcon()
                                }
                            }
                        }

                        if (!hideScreenControls) {
                            binding.controlsOverlayUI.setContent {
                                screenController.DrawScreenControls(
                                    viewsToDraw,
                                    inGame = true,
                                    activeEngine = engineType,
                                    allowToEditControls = allowToEditScreenControlsInGame,
                                    drawInSafeArea = displayInSafeArea,
                                    showVirtualKeyboardEvent = { showVirtualKeyboard ->
                                        showVirtualKeyboardSavedState = showVirtualKeyboard
                                        updateVirtualKeyboardVisibility(showVirtualKeyboard)
                                    }
                                )
                            }

                            binding.keyboardView.setContent {
                                DrawVirtualKeyboard()
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

    protected abstract fun setScreenResolution(screenWidth: Int, screenHeight: Int)

    protected open fun isMouseShown(): Int = 1

    protected open fun onSafeAreaApplied (screenResolution : ScreenResolution){}

    @Composable
    protected open fun DrawVirtualKeyboard() {}

    @Composable
    protected open fun DrawMouseIcon() {}

    protected open fun initJna() {
        needToShowScreenControlsNativeDelegate = Function.getFunction(
            mainEngineLib,
            "needToShowScreenControls"
        )
        Native.register(EngineInfo::class.java, mainEngineLib)
    }

    protected suspend fun changeScreenControlsVisibility() {
        if (this@EngineInfo.controlsOverlayUI == null) {
            return
        }

        while (true) {
            val needToShowControls: Boolean = needToShowScreenControlsNativeDelegate
                .callAs(Boolean::class.java)

            if (needToShowControls != needToShowControlsLastState) {
                this@EngineInfo.activity.runOnUiThread {
                    if (needToShowControls) {
                        this@EngineInfo.controlsOverlayUI!!.visibility = View.VISIBLE
                    } else {
                        this@EngineInfo.controlsOverlayUI!!.visibility = View.GONE
                    }
                    updateVirtualKeyboardVisibility(needToShowControls)
                }
            }
            needToShowControlsLastState = needToShowControls
            delay(200)
        }
    }

    protected fun updateVirtualKeyboardVisibility(showVirtualKeyboard: Boolean) {
        virtualKeyboardView!!.visibility = if (showVirtualKeyboard && showVirtualKeyboardSavedState)
            View.VISIBLE else View.GONE
    }

    private fun preserveCustomScreenAspectRatio(customAspectRatio: String) {
        val aspectRatioData = parseString(customAspectRatio)
        if (aspectRatioData != null) {
            val screenWidth = resolution.screenWidth
            val screenHeight = resolution.screenHeight
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

    private fun parseString(input: String): Pair<Int, Int>? {
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
        if (screenResolutionData != null) {
            setScreenResolution(screenResolutionData.first, screenResolutionData.second)
            return true
        }

        return false
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

    private fun initializeCommonEngineData() {
        val pathToSDL2ControllerDB = "${pathToRootUserFolder}${File.separator}gamecontrollerdb.txt"
        val pathToPsaFolder = getPathToPsaFolder()
        val psaFolder = File(pathToPsaFolder)

        if (!psaFolder.exists()) {
            psaFolder.mkdirs()
        }

        Os.setenv("LIBGL_SIMPLE_SHADERCONV", "1", true)
        Os.setenv("LIBGL_DXTMIPMAP", "1", true)
        Os.setenv("LIBGL_ES", "3", true)
        Os.setenv("LIBGL_GL", "21", true)
        Os.setenv("LIBGL_DXT", "1", true)
        Os.setenv("LIBGL_NOTEXARRAY", "0", true)
        Os.setenv("LIBGL_NOPSA", "0", true)
        Os.setenv("LIBGL_PSA_FOLDER", pathToPsaFolder, true)
        Os.setenv("SDL_VIDEO_GL_DRIVER", gl4esFullLibraryName, true)
        Os.setenv("PATH_TO_SDL2_CONTROLLER_DB", pathToSDL2ControllerDB, true)
    }

    private fun getPathToPsaFolder() =
        pathToRootUserFolder + File.separator + if (BuildConfig.LEGACY_GLES2) "gles2" else "gles3"

    private fun killEngine() = Process.killProcess(Process.myPid())

    private companion object {
        private const val RESOLUTION_DELIMITER = "x"
    }
}