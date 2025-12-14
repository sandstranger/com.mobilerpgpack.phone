package com.mobilerpgpack.phone.engine.engineinfo

import android.annotation.SuppressLint
import android.os.Process
import android.system.Os
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.databinding.GameLayoutBinding
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.main.gl4esFullLibraryName
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.ScreenResolution
import com.mobilerpgpack.phone.utils.displayInSafeArea
import com.mobilerpgpack.phone.utils.getScreenResolution
import com.mobilerpgpack.phone.utils.hideSystemBarsAndWait
import com.mobilerpgpack.phone.utils.invokeBool
import com.sun.jna.Function
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

abstract class EngineInfo(
    mainEngineLib: String,
    private val allLibs: Array<String>,
    activeEngineType: EngineTypes,
    private val pathToResourceFlow: Flow<String>,
    private val commandLineParamsFlow : Flow<String> = emptyFlow()) : KoinComponent, IEngineInfo {

    private var controlsOverlayUI: View? = null
    private var layoutBinding : GameLayoutBinding? = null

    protected open val preferencesStorage: PreferencesStorage by inject()

    protected open val blockTouchCameraEvents get() = controlsProvider.blockTouchCameraEventsWhenOnScreenStickActive

    protected val scope = CoroutineScope(Dispatchers.Default)

    protected lateinit var resolution: ScreenResolution
        private set

    protected lateinit var activity: ComponentActivity
        private set

    protected val controlsProvider : ControlsProvider = get (named(activeEngineType.name))

    protected val pathToRootUserFolder: String = get(
        named(
            KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY
        )
    )

    final override val mainLibraryName: String = mainEngineLib

    override val engineType: EngineTypes = activeEngineType

    final override val pathToResourceExists : Boolean
        get() {
            val pathToResource = this.pathToResource
            return pathToResource.isNotEmpty() &&
                    File(pathToResource).exists()
        }

    override val pathToResourceIsCorrect: Boolean get() = pathToResourceExists &&
            (requiredResourceExtensions.isEmpty() || requiredResourceExtensions.any { pathToResource.endsWith(it) })

    override val requiredResourceExtensions = listOf<String>()

    final override val nativeLibraries: Array<String> get() = allLibs

    override val mouseButtonsEventsCanBeInvoked: Boolean get() = needToInvokeMouseButtonsEventsDelegate.invokeBool()

    protected open val needToShowScreenControls : Boolean get() = needToShowScreenControlsNativeDelegate.invokeBool()

    protected open val pathToResource : String get() = runBlocking { pathToResourceFlow.first() }

    protected open val loadGL4ES : Boolean = true

    private var wasInit = false
    private var safeAreaWasApplied = false
    private var needToShowControlsLastState: Boolean = false
    private var hideScreenControls: Boolean = false
    private var showCustomMouseCursor: Boolean = false
    private var allowToEditScreenControlsInGame = false
    private var isCursorVisible by mutableStateOf(false)
    private var enableControlsAutoHidingFeature = false
    private var displayInSafeArea: Boolean = false

    private var commandLineParams : String? = ""

    private val needToShowScreenControlsNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "needToShowScreenControls")
    }

    private val needToInvokeMouseButtonsEventsDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "needToInvokeMouseButtonsEvents")
    }

    private val pauseSoundNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "pauseSound")
    }

    private val resumeSoundNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "resumeSound")
    }

    final override val rootView get() = layoutBinding?.sdlContainer

    final override val keyboardView get() = layoutBinding?.customKeyboard

    final override val keyboardInputField: TextView? get() = layoutBinding?.keyboardEditText

    override val commandLineArgs: Array<String>
        get() {
            if (commandLineParams.isNullOrEmpty() || !commandLineParams!!.contains("-")) {
                return emptyArray()
            }

            try {
                val args = arrayListOf<String>()

                commandLineParams!!.split(" ".toRegex()).forEach {
                    val trimmedString = it.trim()
                    if (trimmedString.isNotBlank() && trimmedString.isNotEmpty()) {
                        args += trimmedString
                    }
                }

                return args.toTypedArray()
            } catch (_: Exception) {
                return emptyArray()
            }
        }

    override suspend fun initialize(activity: ComponentActivity) {
        if (wasInit){
            return
        }

        wasInit = true
        this.activity = activity
        initializeCommonEngineData()
        resolution = activity.getScreenResolution()

        Os.setenv("PATH_TO_RESOURCES",
            File(pathToResource).absolutePath, true)

        hideScreenControls = preferencesStorage.hideScreenControls.first()
        enableControlsAutoHidingFeature = preferencesStorage.autoHideScreenControls.first()
                && engineType != EngineTypes.DoomRpg && !hideScreenControls

        allowToEditScreenControlsInGame = preferencesStorage.editCustomScreenControlsInGame.first()
        showCustomMouseCursor = preferencesStorage.showCustomMouseCursor.first()
        displayInSafeArea = preferencesStorage.enableDisplayInSafeArea.first()
        commandLineParams = commandLineParamsFlow.firstOrNull()

        onUseSdlStandardTextInputValueChanged(preferencesStorage.useStandardSDLTextInput.first())

        val customAspectRatio = preferencesStorage.customAspectRatio.first()
        val customScreenResolution = preferencesStorage.customScreenResolution.first()
        val customScreenResolutionWasSet = setScreenResolution(customScreenResolution)

        if (!customAspectRatio.isEmpty() && !customScreenResolutionWasSet) {
            preserveCustomScreenAspectRatio(customAspectRatio)
        }
    }

    override fun onPause() {
        pauseSoundNativeDelegate.invokeVoid(null)
    }

    override fun onResume() {
        resumeSoundNativeDelegate.invokeVoid(null)
    }

    override fun onDestroy() {
        scope.cancel()
        killEngine()
    }

    override fun onBackPressed(): Boolean {
        if (layoutBinding == null || !layoutBinding!!.customKeyboard.isExpanded){
            return false
        }
        layoutBinding!!.customKeyboard.translateLayout()
        return true
    }

    final override fun loadLayout(){
        activity.enableEdgeToEdge()
        activity.hideSystemBarsAndWait  {
            if (displayInSafeArea && !safeAreaWasApplied) {
                activity.displayInSafeArea()
                onSafeAreaApplied(activity.getScreenResolution(true))
                safeAreaWasApplied = true
            }
        }
        inflateControlsLayout()
    }

    protected abstract fun onUseSdlStandardTextInputValueChanged(useSdlTextStandardInput : Boolean)

    protected abstract fun setScreenResolution(screenResolution: ScreenResolution)

    protected open fun isMouseShown(): Boolean = true

    protected open fun onSafeAreaApplied (screenResolution : ScreenResolution){}

    @Composable
    protected open fun DrawMouseIcon() {}

    private fun inflateControlsLayout() {
        if (showCustomMouseCursor || !hideScreenControls) {
            layoutBinding = GameLayoutBinding.inflate(activity.layoutInflater)
            layoutBinding?.apply {
                activity.window.addContentView(
                    root,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )

                if (!showCustomMouseCursor) {
                    mouseOverlayUI.visibility = View.GONE
                }

                if (hideScreenControls) {
                    controlsOverlayUI.visibility = View.GONE
                } else {
                    controlsOverlayUI = controlsOverlayUI
                }

                customKeyboard.alpha = runBlocking { preferencesStorage.customOnScreenKeyboardTransparency.first() }

                sdlContainer.post {
                    sdlContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {

                            if (showCustomMouseCursor) {
                                mouseOverlayUI.setContent {
                                    AutoMouseModeComposable(layoutBinding!!)
                                    if (isCursorVisible) {
                                        DrawMouseIcon()
                                    }
                                }
                            }

                            if (!hideScreenControls) {
                                controlsOverlayUI.setContent {
                                    MaterialTheme {
                                        val isSystemInDarkTheme = isSystemInDarkTheme()
                                        val useDarkTheme by preferencesStorage.getUseDarkThemeValue(isSystemInDarkTheme)
                                            .collectAsState(initial = isSystemInDarkTheme)

                                        Theme(darkTheme = useDarkTheme) {
                                            screenController.DrawScreenControls(
                                                inGame = true,
                                                blockTouchCameraEvents = blockTouchCameraEvents,
                                                activeEngine = engineType,
                                                allowToEditControls = allowToEditScreenControlsInGame,
                                                drawInSafeArea = displayInSafeArea)
                                        }
                                    }
                                }
                            }

                            sdlContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
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
    }

    private suspend fun changeScreenControlsVisibility() {
        if (this@EngineInfo.controlsOverlayUI == null) {
            return
        }

        while (true) {
            val needToShowControls: Boolean = this.needToShowScreenControls

            if (needToShowControls != needToShowControlsLastState) {
                this@EngineInfo.activity.runOnUiThread {
                    if (needToShowControls) {
                        this@EngineInfo.controlsOverlayUI!!.visibility = View.VISIBLE
                    } else {
                        this@EngineInfo.controlsOverlayUI!!.visibility = View.GONE
                    }
                }
            }
            needToShowControlsLastState = needToShowControls
            delay(200)
        }
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
                setScreenResolution(ScreenResolution(newWidth, screenHeight))
            } else {
                val newHeight = (screenWidth / targetRatio).toInt()
                setScreenResolution(ScreenResolution(screenWidth, newHeight))
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
            setScreenResolution(ScreenResolution(screenResolutionData.first, screenResolutionData.second))
            return true
        }

        return false
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun AutoMouseModeComposable(binding : GameLayoutBinding) {
        var isMouseShown by remember { mutableStateOf(isMouseShown()) }
        DisposableEffect(Unit) {
            val choreographer = Choreographer.getInstance()
            val frameCallback = object : Choreographer.FrameCallback {
                override fun doFrame(frameTimeNanos: Long) {
                    isMouseShown = isMouseShown()
                    isCursorVisible = isMouseShown
                    binding.mouseOverlayUI.visibility = if(isCursorVisible) View.VISIBLE else View.GONE
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
        if (loadGL4ES) {
            val pathToPsaFolder = getPathToPsaFolder()
            val psaFolder = File(pathToPsaFolder)

            if (!psaFolder.exists()) {
                psaFolder.mkdirs()
            }

            Os.setenv("LIBGL_SIMPLE_SHADERCONV", "1", true)
            Os.setenv("LIBGL_DXTMIPMAP", "1", true)
            Os.setenv("LIBGL_ES", if (!BuildConfig.LEGACY_GLES2) "3" else "2", true)
            Os.setenv("LIBGL_GL", "21", true)
            Os.setenv("LIBGL_DXT", "1", true)
            Os.setenv("LIBGL_NOTEXARRAY", "0", true)
            Os.setenv("LIBGL_NOPSA", "0", true)
            Os.setenv("LIBGL_PSA_FOLDER", pathToPsaFolder, true)
            Os.setenv("SDL_VIDEO_GL_DRIVER", gl4esFullLibraryName, true)
        }

        val pathToSDL2ControllerDB = "${pathToRootUserFolder}${File.separator}gamecontrollerdb.txt"
        Os.setenv("PATH_TO_SDL2_CONTROLLER_DB", pathToSDL2ControllerDB, true)
    }

    private fun getPathToPsaFolder() =
        pathToRootUserFolder + File.separator + if (BuildConfig.LEGACY_GLES2) "gles2" else "gles3"

    private fun killEngine() = Process.killProcess(Process.myPid())

    private companion object {
        private const val RESOLUTION_DELIMITER = "x"
    }
}