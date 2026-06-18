package com.mobilerpgpack.phone.engine.engineinfo

import android.annotation.SuppressLint
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
import androidx.lifecycle.MutableLiveData
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.databinding.GameLayoutBinding
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.GlesRenderVersions
import com.mobilerpgpack.phone.main.ANGLE_SHADER_CACHE_NATLIVE_LIB_NAME
import com.mobilerpgpack.phone.main.C_PLUS_PLUS_SHARED_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.main.NG_GL4ES_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.ONE_FRAME_DELAY
import com.mobilerpgpack.phone.main.angleLibs
import com.mobilerpgpack.phone.main.gl4esLibraryName
import com.mobilerpgpack.phone.main.ngGL4ESFullLibraryName
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsProvider
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLKeyboard
import com.mobilerpgpack.phone.utils.GyroInput
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.ScreenResolution
import com.mobilerpgpack.phone.utils.displayInSafeArea
import com.mobilerpgpack.phone.utils.getComposableValue
import com.mobilerpgpack.phone.utils.getScreenResolution
import com.mobilerpgpack.phone.utils.hideSystemBarsAndWait
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import com.sun.jna.Native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
import org.libsdl3.app.SDLActivity
import java.io.File
import kotlin.system.exitProcess

abstract class EngineInfo(
    mainEngineLib: String,
    private val allLibs: Array<String>,
    activeEngineType: EngineTypes) : KoinComponent, IEngineInfo {

    private var layoutBinding : GameLayoutBinding? = null

    private val mainThreadScope : CoroutineScope by inject (
        named(KoinModulesProvider.MAIN_THREAD_COROUTINE_KEY))

    protected val controlsProvider : ControlsProvider = get (named(activeEngineType.name))
    protected open val preferencesStorage: PreferencesStorage by inject()
    protected open val blockTouchCameraEvents : Boolean get() = controlsProvider.run {
        blockTouchCameraEventsWhenOnScreenStickActive.value!! && activeControlsType.value!! == ControlsType.OnScreenStick }
    protected abstract val sdlKeyboard : SDLKeyboard
    protected open val allowedToEnableAngle = true
    protected open val enableAngleSupport get() = allowedToEnableAngle &&
            preferencesStorage.enableAngleSupport.value!!
    protected open val keyboardInputType : CustomKeyboardView.KeyboardType =
        SDLKeyboard.DEFAULT_KEYBOARD_INPUT_TYPE
    protected lateinit var resolution: ScreenResolution
        private set
    protected lateinit var activity: ComponentActivity
        private set
    protected val gl4esFullLibraryName get() = if (useLegacyGl4es) com.mobilerpgpack.phone.main.gl4esFullLibraryName else
        ngGL4ESFullLibraryName
    protected val pathToRootUserFolder: String get() = preferencesStorage.pathToRootUserFolder.value!!
    protected open val needToShowScreenControls : Boolean get() = needToShowScreenControls()
    protected open val commandLineParams : String = ""
    protected abstract val pathToResource : String
    protected open val loadGL4ES : Boolean = true
    protected open val targetGLESVersion : Int = GLES_300_VERSION
    protected open val enableGyroscope : Boolean get() = preferencesStorage.enableGyroscope.value!!
    protected open val callExitProcessOnDestroy : Boolean = true
    protected open val enableNGGL4ESSimpleShaderConv = false
    protected open val gl4esShaderCacheFolderName : String = "gl4es_cache"
    protected abstract val gyroInput : GyroInput

    private var wasInit = false
    private var hideScreenControls: Boolean = false
    private var showCustomMouseCursor: Boolean = false
    private val isCursorVisible = MutableLiveData(false)
    private var displayInSafeArea: Boolean = false
    private val hideOnScreenControlsMutableState = MutableLiveData(false)
    private val useLegacyGl4es get() = preferencesStorage.glesRenderVersion.value == GlesRenderVersions.OpenGLES_2_0
    private val gl4esLibraryName get() = if (useLegacyGl4es) com.mobilerpgpack.phone.main.gl4esLibraryName else
        NG_GL4ES_NATIVE_LIB_NAME
    private val gL4ESJnaLayer by lazy { GL4ESJnaLayer (gl4esLibraryName) }

    private external fun needToShowScreenControls() : Boolean
    protected external fun needToInvokeMouseButtonsEvents() : Boolean
    private external fun onNativePause()
    private external fun onNativeResume()
    private external fun needToReInitGameControllers() : Boolean
    private external fun setPathToSDLControllerDB (pathToSDLControllerDB : String)
    private external fun setUseGLES2_0State (useGLES2_0 : Boolean)

    override val engineReadyToStart = true

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

    override val useGyroscope: Boolean get() = !mouseButtonsEventsCanBeInvoked

    override val supportRenderChanges get() = loadGL4ES

    final override val pathToResourceExists : Boolean
        get() {
            val pathToResource = this.pathToResource
            return pathToResource.isNotEmpty() &&
                    File(pathToResource).exists()
        }

    override val pathToResourceIsCorrect: Boolean get() = pathToResourceExists &&
            (requiredResourceExtensions.isEmpty() || requiredResourceExtensions.any { pathToResource.endsWith(it) })

    override val requiredResourceExtensions = emptyList<String>()

    override val nativeLibraries: Array<String> get() =
        mutableListOf<String>().run {
            this += C_PLUS_PLUS_SHARED_NATIVE_LIB_NAME
            if (enableAngleSupport) {
                this += angleLibs
            }
            if (loadGL4ES){
                this += gl4esLibraryName
            }
            this += allLibs
            this.toTypedArray()
        }

    override val mouseButtonsEventsCanBeInvoked: Boolean get() = needToInvokeMouseButtonsEvents()

    override val touchFullScreenModeCanBeUsed: Boolean = true

    override val commandLineArgs: Array<String>
        get() {
            commandLineParams.apply {
                if (isEmpty() || contains("-")) {
                    return emptyArray()
                }

                return try {
                    mutableListOf<String>().run {
                        this@apply.split(" ".toRegex()).forEach {
                            it.trim().also { trimmedString ->
                                if (trimmedString.isNotBlank() && trimmedString.isNotEmpty()) {
                                    this += trimmedString
                                }
                            }
                        }

                        toTypedArray()
                    }
                } catch (_: Exception) {
                    emptyArray()
                }
            }
        }

    override fun onNativeLibrariesLoaded() {
        Native.register(EngineInfo::class.java, mainLibraryName)
        setUseGLES2_0State(preferencesStorage.glesRenderVersion.value!! == GlesRenderVersions.OpenGLES_2_0)
        setPathToSDLControllerDB("${pathToRootUserFolder}${File.separator}gamecontrollerdb.txt")
        if (enableAngleSupport){
            AngleShaderCacheJnaLayer.setAngleState(true)
        }
        if (loadGL4ES){
            gL4ESJnaLayer.apply {
                val gl4esShaderCacheFolder = File(activity.cacheDir, gl4esShaderCacheFolderName)
                gl4esShaderCacheFolder.mkdirs()
                initializeGL4ESData (enableNGGL4ESSimpleShaderConv,
                    enableAngleSupport, targetGLESVersion,
                    false,
                    gl4esShaderCacheFolder.absolutePath)
                initialize_gl4es()
            }
        }
    }

    override fun initialize(activity: ComponentActivity) {
        if (wasInit){
            return
        }

        wasInit = true
        this.activity = activity
        resolution = activity.getScreenResolution()
        hideScreenControls = preferencesStorage.hideScreenControls.value!!
        showCustomMouseCursor = preferencesStorage.showCustomMouseCursor.value!!
        displayInSafeArea = preferencesStorage.enableDisplayInSafeArea.value!!

        val customAspectRatio = preferencesStorage.customAspectRatio.value!!
        val customScreenResolution = preferencesStorage.customScreenResolution.value!!
        val customScreenResolutionWasSet = setScreenResolution(customScreenResolution)

        if (!customAspectRatio.isEmpty() && !customScreenResolutionWasSet) {
            preserveCustomScreenAspectRatio(customAspectRatio)
        }
    }

    override fun onPause() {
        onNativePause()
        if (enableGyroscope) {
            gyroInput.stop()
        }
    }

    override fun onResume() {
        onNativeResume()
        if (enableGyroscope) {
            gyroInput.start()
        }
    }

    override fun onNativeTrimMemory(aggressive : Boolean) {}

    override fun onDestroy() {
        mainThreadScope.coroutineContext.cancelChildren()
        if (enableAngleSupport){
            AngleShaderCacheJnaLayer.angle_blobcache_shutdown()
        }
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
                    !preferencesStorage.alwaysShowKeyboardButton.value!!)

            customKeyboard.alpha = preferencesStorage.customOnScreenKeyboardTransparency.value!!

            sdlContainer.post {
                sdlContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (showCustomMouseCursor) {
                            mouseOverlayUI.setContent {
                                val binding = remember { layoutBinding!! }
                                val isCursorVisible = isCursorVisible.getComposableValue()
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
                                    rememberSaveable { preferencesStorage.alwaysShowKeyboardButton.value!! }
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

                val enableControlsAutoHidingFeature = preferencesStorage.autoHideScreenControls.value!! && !hideScreenControls

                if (enableControlsAutoHidingFeature) {
                    hideOnScreenControlsMutableState.value = false
                    mainThreadScope.launch { changeScreenControlsVisibility() }
                }
            }
        }
    }

    private suspend fun changeScreenControlsVisibility() {
        while (currentCoroutineContext().isActive) {
            val needToHideOnScreenControls: Boolean = !this.needToShowScreenControls
            if (needToHideOnScreenControls != hideOnScreenControlsMutableState.value!!) {
                hideOnScreenControlsMutableState.value = needToHideOnScreenControls
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
                    isCursorVisible.value = isMouseShown
                    binding.mouseOverlayUI.visibility = if(isCursorVisible.value!!) View.VISIBLE else View.GONE
                    choreographer.postFrameCallback(this)
                }
            }
            choreographer.postFrameCallback(frameCallback)

            onDispose {
                choreographer.removeFrameCallback(frameCallback)
            }
        }
    }

    private class GL4ESJnaLayer (gl4esLibraryName : String) {
        external fun initialize_gl4es()
        external fun close_gl4es()
        external fun initializeGL4ESData(enableSimpleShaderConv : Boolean,
                                         enableAngle : Boolean,targetESVersion : Int,
                                         useMediumpShaderPrecision : Boolean, pathToShaderCache : String)

        init {
            Native.register(GL4ESJnaLayer::class.java, gl4esLibraryName)
        }
    }

    private object AngleShaderCacheJnaLayer{
        external fun setAngleState (enableAngle: Boolean)
        external fun angle_blobcache_shutdown()

        init {
            Native.register(AngleShaderCacheJnaLayer::class.java,
                ANGLE_SHADER_CACHE_NATLIVE_LIB_NAME
            )
        }
    }

    protected companion object {
        private const val RESOLUTION_DELIMITER = "x"
        const val GLES_320_VERSION = 320
        const val GLES_310_VERSION = 310
        const val GLES_300_VERSION = 300
    }
}