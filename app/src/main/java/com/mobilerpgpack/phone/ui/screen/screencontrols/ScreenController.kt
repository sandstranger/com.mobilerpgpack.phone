package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

open class ScreenController : KoinComponent, IScreenController {

    private val context : Context = get ()

    private val preferencesStorage : PreferencesStorage = get ()

    override var activeViewsToDraw: Collection<IScreenControlsView>? = null

    @SuppressLint("ConfigurationScreenWidthHeight")
    @Composable
    override fun DrawScreenControls(
        views: Collection<IScreenControlsView>,
        activeEngine : EngineTypes,
        inGame: Boolean,
        allowToEditControls: Boolean,
        drawInSafeArea : Boolean,
        onBack: () -> Unit) {

        this.activeViewsToDraw = views

        val configuration = LocalConfiguration.current
        val density = context.resources.displayMetrics.density
        val coroutineScope = rememberCoroutineScope()

        val clampButtonsPrefsKey = koinInject<Preferences.Key<Boolean>> { parametersOf(activeEngine) }

        var viewsToDraw by remember { mutableStateOf(mapOf<String, IScreenControlsView>()) }
        var selectedButtonId by remember { mutableStateOf<String?>(null) }
        var isEditMode by remember { mutableStateOf((!inGame)) }
        var backgroundColor by remember { mutableStateOf(Color.Transparent) }
        var readyToDrawControls by remember { mutableStateOf(false) }
        val clampButtonsFlow by preferencesStorage.getBooleanValue( clampButtonsPrefsKey, true).collectAsStateWithLifecycle(true)

        var screenWidthPx by remember { mutableFloatStateOf(0f) }
        var screenHeightPx by remember { mutableFloatStateOf(0f) }

        fun clampButton(state: ButtonState) {
            if (!clampButtonsFlow) {
                return
            }

            state.offsetXPercent = state.offsetXPercent.coerceIn(0f, 1f - state.sizePercent)
            val buttonHeightPx = state.sizePercent * screenWidthPx
            val buttonHeightPercent = buttonHeightPx / screenHeightPx
            state.offsetYPercent = state.offsetYPercent.coerceIn(0f, 1f - buttonHeightPercent)
        }

        suspend fun preloadButtons() {
            val loadedMap = views.associateBy { it.buttonState.id }
            loadedMap.values.forEach { view ->
                view.buttonState.loadButtonState()
            }
            loadedMap.values.forEach { view ->
                clampButton(view.buttonState)
                coroutineScope.launch { view.buttonState.saveButtonState() }
            }
            viewsToDraw = loadedMap
        }

        views.forEach {
            it.setScreenController(this)
        }

        if (drawInSafeArea) {
            val activity = LocalActivity.current!!
            var screenResolutionCalculated by remember { mutableStateOf(false) }
            var allContentLoaded by remember { mutableStateOf(false) }
            activity.window.decorView.post {
                val insets = ViewCompat.getRootWindowInsets(activity.window.decorView)!!
                val metrics = activity.window.decorView.resources.displayMetrics
                val systemBarsInsets = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
                )

                screenWidthPx = (metrics.widthPixels - systemBarsInsets.left - systemBarsInsets.right).toFloat()
                screenHeightPx = (metrics.heightPixels - systemBarsInsets.top - systemBarsInsets.bottom).toFloat()
                screenResolutionCalculated = true
                readyToDrawControls = allContentLoaded
            }
            LaunchedEffect(Unit) {
                preloadButtons()
                allContentLoaded = true
                readyToDrawControls = screenResolutionCalculated
            }
        } else {
            screenWidthPx = configuration.screenWidthDp * density
            screenHeightPx = configuration.screenHeightDp * density
            LaunchedEffect(Unit) {
                preloadButtons()
                readyToDrawControls = true
            }
        }

        backgroundColor = if (!inGame) {
            Color.DarkGray
        } else {
            if (isEditMode) Color.DarkGray.copy(alpha = 0.5f) else Color.Transparent
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            if (inGame) {
                DrawBlockAndroidViewsBox()
                if (!isEditMode) {
                    DrawTouchCamera()
                }
            }

            if (isEditMode) {
                EditControls(
                    selectedButtonId,
                    inGame,
                    onAlphaChange = { delta ->
                        selectedButtonId?.let { id ->
                            val state = viewsToDraw[id]!!.buttonState
                            state.alpha = (state.alpha + delta).coerceIn(0.0f, 1f)
                            coroutineScope.launch {
                                state.saveButtonState()
                            }
                        }
                    },
                    onSizeChange = { deltaPercent ->
                        selectedButtonId?.let { id ->
                            val state = viewsToDraw[id]!!.buttonState
                            state.sizePercent = (state.sizePercent + deltaPercent).coerceIn(0f, 1f)
                            coroutineScope.launch {
                                state.saveButtonState()
                            }
                        }
                    },
                    onReset = {
                        coroutineScope.launch {
                            viewsToDraw.values.forEach { view ->
                                view.buttonState.resetToDefaults()
                            }
                            preferencesStorage.setBooleanValue( clampButtonsPrefsKey, true)
                            viewsToDraw.values.forEach { view ->
                                clampButton(view.buttonState)
                                view.buttonState.saveButtonState()
                            }
                            selectedButtonId = null
                        }
                    },
                    onBack = {
                        selectedButtonId = null
                        onBack()
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (readyToDrawControls) {
                viewsToDraw.forEach { (id, view) ->

                    val sizePx: Float = screenWidthPx * view.buttonState.sizePercent
                    val sizeDp: Dp = (sizePx / density).dp

                    val renderOffsetX = view.buttonState.offsetXPercent * screenWidthPx
                    val renderOffsetY = view.buttonState.offsetYPercent * screenHeightPx

                    val renderButton = view.isHideControlsButton || ((view.show || isEditMode) && view.enabled)
                    if (renderButton) {
                        DrawView(
                            viewToDraw = view,
                            offset = Offset(renderOffsetX, renderOffsetY),
                            sizeDp = sizeDp,
                            isEditMode = isEditMode,
                            isSelected = (selectedButtonId == id),
                            onClick = {
                                if (isEditMode) {
                                    selectedButtonId = id
                                    coroutineScope.launch {
                                        preferencesStorage.setBooleanValue(
                                            clampButtonsPrefsKey,
                                            false)
                                    }
                                }
                            },
                            onDragEnd = { newX, newY ->
                                view.buttonState.offsetXPercent = (newX / screenWidthPx)
                                view.buttonState.offsetYPercent = (newY / screenHeightPx)
                                coroutineScope.launch {
                                    view.buttonState.saveButtonState()
                                }
                            },
                            inGame = inGame,
                        )
                    }
                }
            }

            if (inGame && allowToEditControls) {
                Image(
                    painter = painterResource(R.drawable.cog),
                    contentDescription = "settings_button",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(60.dp)
                        .alpha(0.75f)
                        .padding(8.dp)
                        .then(
                            Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                isEditMode = !isEditMode
                            }
                        )
                )
            }
        }
    }

    @Composable
    protected open fun DrawTouchCamera(){

    }

    @Composable
    private fun DrawView(
        viewToDraw: IScreenControlsView,
        offset: Offset,
        sizeDp: Dp,
        isEditMode: Boolean,
        inGame: Boolean,
        isSelected: Boolean,
        onClick: () -> Unit,
        onDragEnd: (x: Float, y: Float) -> Unit) {
        var position by remember(viewToDraw.buttonState.id) { mutableStateOf(offset) }

        LaunchedEffect(offset) {
            position = offset
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
                .size(sizeDp)
                .alpha(viewToDraw.buttonState.alpha)
                .background(
                    if (isSelected && isEditMode) Color.Red.copy(alpha = 0.5f)
                    else Color.Transparent,
                    RoundedCornerShape(8.dp)
                )
                .pointerInput(isEditMode, isSelected) {
                    detectDragGestures(
                        onDragStart = {
                            if (isEditMode && !isSelected) {
                                onClick()
                            }
                        },
                        onDrag = { _, dragAmount ->
                            if (isEditMode && isSelected) {
                                position = Offset(position.x + dragAmount.x, position.y + dragAmount.y)
                            }
                        },
                        onDragEnd = {
                            if (isEditMode && isSelected) {
                                onDragEnd(position.x, position.y)
                            }
                        }
                    )
                }
                .pointerInput(isEditMode, isSelected) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Press && isEditMode && !isSelected && event.changes.any { it.pressed }) {
                                onClick()
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            viewToDraw.DrawView(isEditMode,inGame,sizeDp)
        }
    }

    @Composable
    private fun DrawBlockAndroidViewsBox() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                }
        )
    }

    @Composable
    private fun EditControls(
        selectedButtonId: String?,
        inGame: Boolean,
        onAlphaChange: (Float) -> Unit,
        onSizeChange: (Float) -> Unit,
        onReset: () -> Unit,
        onBack: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .background(Color.Gray.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!selectedButtonId.isNullOrBlank()) {
                Text(
                    text = selectedButtonId,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(8.dp))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onAlphaChange(+0.1f) }) {
                    Text(context.getString(R.string.increase_controls_alpha))
                }
                Button(onClick = { onAlphaChange(-0.1f) }) {
                    Text(context.getString(R.string.decrease_controls_alpha))
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onSizeChange(+0.01f) }) {
                    Text(context.getString(R.string.increase_controls_size))
                }
                Button(onClick = { onSizeChange(-0.01f) }) {
                    Text(context.getString(R.string.decrease_controls_size))
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onReset) {
                Text(context.getString(R.string.reset_controls_to_default))
            }
            Spacer(Modifier.height(8.dp))
            if (!inGame) {
                Button(onClick = onBack) {
                    Text(context.getString(R.string.close_controls_configuration))
                }
            }
        }
    }

    companion object {
        const val COMMON_SCREEN_CONTROLLER_NAME = "COMMON_SCREEN_CONTROLLER"
    }
}

