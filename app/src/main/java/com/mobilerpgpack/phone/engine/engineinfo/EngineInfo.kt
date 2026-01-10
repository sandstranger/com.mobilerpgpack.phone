package com.mobilerpgpack.phone.engine.engineinfo

import android.annotation.SuppressLint
import android.system.Os
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.databinding.GameLayoutBinding
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.ONE_FRAME_DELAY
import com.mobilerpgpack.phone.main.gl4esFullLibraryName
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLKeyboard
import com.mobilerpgpack.phone.utils.GyroInput
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.ScreenResolution
import com.mobilerpgpack.phone.utils.displayInSafeArea
import com.mobilerpgpack.phone.utils.getScreenResolution
import com.mobilerpgpack.phone.utils.hideSystemBarsAndWait
import com.mobilerpgpack.phone.utils.invokeBool
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import com.sun.jna.Function
import com.sun.jna.Native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File
import kotlin.system.exitProcess

abstract class EngineInfo(
    mainEngineLib: String,
    private val allLibs: Array<String>,
    activeEngineType: EngineTypes) : KoinComponent, IEngineInfo {

    private var layoutBinding : GameLayoutBinding? = null

    protected val controlsProvider : ControlsProvider = get (named(activeEngineType.name))

    protected open val preferencesStorage: PreferencesStorage by inject()

    protected open val blockTouchCameraEvents : Boolean get() = controlsProvider.run {
        blockTouchCameraEventsWhenOnScreenStickActive && activeControlsType == ControlsType.OnScreenStick }

    protected abstract val sdlKeyboard : SDLKeyboard

    protected open val keyboardInputType : CustomKeyboardView.KeyboardType =
        SDLKeyboard.DEFAULT_KEYBOARD_INPUT_TYPE

    protected val scope = CoroutineScope(Dispatchers.IO)

    protected lateinit var resolution: ScreenResolution
        private set

    protected lateinit var activity: ComponentActivity
        private set

    protected val pathToRootUserFolder: String get() = preferencesStorage.pathToRootUserFolder

    protected open val needToShowScreenControls : Boolean get() = needToShowScreenControls()

    protected open val commandLineParams : String = ""

    protected abstract val pathToResource : String

    protected open val loadGL4ES : Boolean = true

    protected open val enableGyroscope : Boolean get() = preferencesStorage.enableGyroscope

    protected open val callExitProcessOnDestroy : Boolean = true

    protected abstract val gyroInput : GyroInput

    private var wasInit = false
    private var hideScreenControls: Boolean = false
    private var showCustomMouseCursor: Boolean = false
    private var isCursorVisible by mutableStateOf(false)
    private var displayInSafeArea: Boolean = false
    private var hideOnScreenControlsMutableState by mutableStateOf(false)

    private external fun needToShowScreenControls() : Boolean

    protected external fun needToInvokeMouseButtonsEvents() : Boolean

    private external fun pauseSound()

    private external fun resumeSound()

    private external fun rescanGameControllersForced()

    private external fun needToReInitGameControllers() : Boolean

    final override val needToReInitGameControllers: Boolean get() = needToReInitGameControllers()

    final override val mouseButtonsEventsCanBeInvokedAsFlow : Flow<Boolean> by lazy{
        flow {
            while (currentCoroutineContext().isActive) {
                emit(mouseButtonsEventsCanBeInvoked)
                delay(ONE_FRAME_DELAY)
            }
        }.distinctUntilChanged()
    }

    override val mainLibraryName: String = mainEngineLib

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

    override val nativeLibraries: Array<String> get() = allLibs

    override val mouseButtonsEventsCanBeInvoked: Boolean get() = needToInvokeMouseButtonsEvents()

    override val touchFullScreenModeCanBeUsed: Boolean = true

    override val commandLineArgs: Array<String>
        get() {
            if (commandLineParams.isEmpty() || !commandLineParams.contains("-")) {
                return emptyArray()
            }

            try {
                val args = arrayListOf<String>()

                commandLineParams.split(" ".toRegex()).forEach {
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

    override fun onNativeLibrariesLoaded() = Native.register(EngineInfo::class.java, mainLibraryName)

    final override fun rescanGameControllers() = rescanGameControllersForced()

    override fun initialize(activity: ComponentActivity) {
        if (wasInit){
            return
        }

        wasInit = true

        this.activity = activity
        initializeCommonEngineData()
        resolution = activity.getScreenResolution()

        Os.setenv("PATH_TO_RESOURCES",
            File(pathToResource).absolutePath, true)

        hideScreenControls = preferencesStorage.hideScreenControls
        showCustomMouseCursor = preferencesStorage.showCustomMouseCursor
        displayInSafeArea = preferencesStorage.enableDisplayInSafeArea

        val customAspectRatio = preferencesStorage.customAspectRatio
        val customScreenResolution = preferencesStorage.customScreenResolution
        val customScreenResolutionWasSet = setScreenResolution(customScreenResolution)

        if (!customAspectRatio.isEmpty() && !customScreenResolutionWasSet) {
            preserveCustomScreenAspectRatio(customAspectRatio)
        }
    }

    override fun onPause() {
        scope.launch { pauseSound() }
        if (enableGyroscope) {
            gyroInput.stop()
        }
    }

    override fun onResume() {
        scope.launch { resumeSound() }
        if (enableGyroscope) {
            gyroInput.start()
        }
    }

    override fun onDestroy() {
        scope.coroutineContext.cancelChildren()
        if (callExitProcessOnDestroy) {
            exitProcess(0)
        }
    }

    override fun onBackPressed(): Boolean {
        if (layoutBinding == null || !layoutBinding!!.customKeyboard.isExpanded){
            return false
        }
        layoutBinding!!.customKeyboard.translateLayout()
        return true
    }

    final override fun loadLayout(){
        activity.hideSystemBarsAndWait  {
            if (displayInSafeArea){
                activity.displayInSafeArea()
                onSafeAreaApplied(activity.getScreenResolution(true))
            }
        }
        inflateControlsLayout()
    }

    protected abstract fun setScreenResolution(screenResolution: ScreenResolution)

    protected open fun isMouseShown(): Boolean = true

    protected open fun onSafeAreaApplied (screenResolution : ScreenResolution){}

    protected abstract fun updateUseStandardSDLInputState (useStandardSDLInput : Boolean)

    @Composable
    protected open fun DrawMouseIcon() {}

    private fun inflateControlsLayout() {
        layoutBinding = GameLayoutBinding.inflate(activity.layoutInflater)
        layoutBinding?.apply {
            activity.window.addContentView(
                root,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            this@EngineInfo.sdlKeyboard.initialize(keyboardEditText, customKeyboard)

            if (!showCustomMouseCursor) {
                mouseOverlayUI.visibility = View.GONE
            }

            updateUseStandardSDLInputState(useStandardSDLInput = hideScreenControls &&
                    !preferencesStorage.alwaysShowKeyboardButton)

            customKeyboard.alpha = preferencesStorage.customOnScreenKeyboardTransparency

            sdlContainer.post {
                sdlContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (showCustomMouseCursor) {
                            mouseOverlayUI.setContent {
                                val binding = remember { layoutBinding!! }
                                val isCursorVisible by rememberSaveable(isCursorVisible) {
                                    mutableStateOf(isCursorVisible)}
                                AutoMouseModeComposable(binding)
                                if (isCursorVisible) {
                                    DrawMouseIcon()
                                }
                            }
                        }

                        controlsOverlayUI.setContent {
                            Theme {
                                val hideScreenControls = rememberSaveable { hideScreenControls }
                                val alwaysShowKeyboard =
                                    rememberSaveable { preferencesStorage.alwaysShowKeyboardButton }
                                if (!hideScreenControls) {
                                    screenController.DrawScreenControls(
                                        inGame = true,
                                        blockTouchCameraEvents = blockTouchCameraEvents,
                                        activeEngine = engineType,
                                        drawInSafeArea = displayInSafeArea,
                                        hideOnScreenControls = hideOnScreenControlsMutableState,
                                        keyboardInputType = keyboardInputType
                                    )
                                } else if (alwaysShowKeyboard) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Image(
                                            painter = painterResource(R.drawable.keyboard),
                                            contentDescription = "keyboard_button",
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .size(70.dp)
                                                .alpha(0.5f)
                                                .minimumInteractiveComponentSize()
                                                .padding(8.dp)
                                                .clickable(
                                                    indication = null, interactionSource = null
                                                ) {
                                                    sdlKeyboard.showKeyboard(
                                                        useReturnButton = true,
                                                        keyboardInputType
                                                    )
                                                }
                                        )
                                    }
                                }
                            }
                        }
                        sdlContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })

                val enableControlsAutoHidingFeature = preferencesStorage.autoHideScreenControls && !hideScreenControls

                if (enableControlsAutoHidingFeature) {
                    hideOnScreenControlsMutableState = false
                    scope.launch { changeScreenControlsVisibility() }
                }
            }
        }
    }

    private suspend fun changeScreenControlsVisibility() {
        while (currentCoroutineContext().isActive) {
            val needToHideOnScreenControls: Boolean = !this.needToShowScreenControls
            if (needToHideOnScreenControls != hideOnScreenControlsMutableState) {
                hideOnScreenControlsMutableState = needToHideOnScreenControls
            }
            delay(ONE_FRAME_DELAY)
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
            Os.setenv("LIBGL_GL", "21", true)
            Os.setenv("LIBGL_DXT", "1", true)
            Os.setenv("LIBGL_NOTEXARRAY", "0", true)
            Os.setenv("LIBGL_NOPSA", "0", true)
            Os.setenv("LIBGL_PSA_FOLDER", pathToPsaFolder, true)
            Os.setenv("SDL_VIDEO_GL_DRIVER", gl4esFullLibraryName, true)
            Os.setenv("LIBGL_VABGRA", "1",true)
        }

        Os.setenv("LIBGL_ES", if (!BuildConfig.LEGACY_GLES2) "3" else "2", true)

        val pathToSDL2ControllerDB = "${pathToRootUserFolder}${File.separator}gamecontrollerdb.txt"
        Os.setenv("PATH_TO_SDL2_CONTROLLER_DB", pathToSDL2ControllerDB, true)
    }

    private fun getPathToPsaFolder() =
        pathToRootUserFolder + File.separator + if (BuildConfig.LEGACY_GLES2) "gles2" else "gles3"

    private companion object {
        private const val RESOLUTION_DELIMITER = "x"
    }
}