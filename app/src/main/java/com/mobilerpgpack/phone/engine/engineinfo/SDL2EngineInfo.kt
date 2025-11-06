package com.mobilerpgpack.phone.engine.engineinfo

import android.annotation.SuppressLint
import android.app.Activity
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
import com.mobilerpgpack.phone.databinding.GameLayoutBinding
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.activity.SDL2GameActivity
import com.mobilerpgpack.phone.ui.screen.screencontrols.DrawDoomRpgSeriesKeyboard
import com.mobilerpgpack.phone.ui.items.MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL2ScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.ScreenController
import com.mobilerpgpack.phone.utils.displayInSafeArea
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.libsdl.app.SDLActivity.isMouseShown
import org.libsdl.app.SDLSurface

abstract class SDL2EngineInfo(private val mainEngineLib: String,
                     private val allLibs : Array<String>,
                     private val buttonsToDraw : Collection<IScreenControlsView> ) :
    EngineInfo(mainEngineLib, allLibs, buttonsToDraw ) {

    private val screenControls : SDL2ScreenController by inject ()

    private var hideScreenControls: Boolean = false
    private var showCustomMouseCursor: Boolean = false
    private var allowToEditScreenControlsInGame = false
    private var isCursorVisible by mutableIntStateOf(0)
    private var enableControlsAutoHidingFeature = false
    private var displayInSafeArea : Boolean = false

    override val gameActivityClazz: Class<*> = SDL2GameActivity::class.java

    override suspend fun initialize(activity: Activity) {
        super.initialize(activity)

        hideScreenControls = preferencesStorage.hideScreenControls.first()
        enableControlsAutoHidingFeature = preferencesStorage.autoHideScreenControls.first()
                && engineType!= EngineTypes.DoomRpg && !hideScreenControls

        allowToEditScreenControlsInGame = preferencesStorage.editCustomScreenControlsInGame.first()
        showCustomMouseCursor = preferencesStorage.showCustomMouseCursor.first()
        displayInSafeArea = preferencesStorage.enableDisplayInSafeArea.first()
        val customAspectRatio = preferencesStorage.customAspectRatio.first()
        val customScreenResolution = preferencesStorage.customScreenResolution.first()
        val customScreenResolutionWasSet = setScreenResolution(customScreenResolution)

        if (!customAspectRatio.isEmpty() && !customScreenResolutionWasSet) {
            preserveCustomScreenAspectRatio(customAspectRatio)
        }

        if (displayInSafeArea) {
            activity.displayInSafeArea()
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun loadControlsLayout() {
        if (showCustomMouseCursor || !hideScreenControls) {
            val binding = GameLayoutBinding.inflate(activity.layoutInflater)

            activity.window.addContentView(
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
                                screenControls.DrawScreenControls(
                                    buttonsToDraw,
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

    private companion object{
        private const val RESOLUTION_DELIMITER = "x"
    }
}