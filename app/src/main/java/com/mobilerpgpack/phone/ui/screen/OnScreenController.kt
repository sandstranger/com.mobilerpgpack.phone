package com.mobilerpgpack.phone.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface
import kotlin.math.roundToInt

private const val dpadId = "dpad"
private const val notExistingResId = Int.MIN_VALUE

private val clampButtonsPrefsKey = booleanPreferencesKey("clamp_buttons")

enum class ButtonType {
    Default,
    Dpad,
    DpadUp,
    DpadDown,
    DpadLeft,
    DpadRight,
    ControlsHider
}

class ButtonState(
    val id: String,
    val engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = 0,
    val buttonResId: Int = android.R.drawable.ic_menu_add,
    val buttonType: ButtonType = ButtonType.Default
) : Cloneable {
    private val defaultSdlKeyEvent = sdlKeyEvent
    private val defaultOffsetXPercent = offsetXPercent
    private val defaultOffsetYPercent = offsetYPercent
    private val defaultSizePercent = sizePercent
    private val defaultAlpha = alpha
    private val engineTypeString = engineType.toString().lowercase()

    private val keyX: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_x")
    private val keyY: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_y")
    private val keySize: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_size_percent")
    private val keyAlpha: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_alpha")
    private val sdlKeyEventPrefsKey: Preferences.Key<Int> = intPreferencesKey("${engineTypeString}_${id}_sdl_key")

    val allowToEditKeyEvent
        get() = buttonResId != notExistingResId && buttonType != ButtonType.Dpad && buttonType != ButtonType.ControlsHider

    var offsetXPercent by mutableFloatStateOf(offsetXPercent)
    var offsetYPercent by mutableFloatStateOf(offsetYPercent)
    var sizePercent by mutableFloatStateOf(sizePercent)
    var alpha by mutableFloatStateOf(alpha)
    var sdlKeyEvent by mutableIntStateOf(sdlKeyEvent)

    suspend fun loadButtonState(context: Context) {
        offsetXPercent = PreferencesStorage.getFloatValue(context, keyX, defaultOffsetXPercent).first()!!
        offsetYPercent = PreferencesStorage.getFloatValue(context, keyY, defaultOffsetYPercent).first()!!
        sizePercent = PreferencesStorage.getFloatValue(context, keySize, defaultSizePercent).first()!!
        alpha = PreferencesStorage.getFloatValue(context, keyAlpha, defaultAlpha).first()!!
        sdlKeyEvent = PreferencesStorage.getIntValue(context, sdlKeyEventPrefsKey, defaultSdlKeyEvent).first()!!
    }

    suspend fun saveButtonState(context: Context) {
        PreferencesStorage.setFloatValue(context, keyX, offsetXPercent)
        PreferencesStorage.setFloatValue(context, keyY, offsetYPercent)
        PreferencesStorage.setFloatValue(context, keySize, sizePercent)
        PreferencesStorage.setFloatValue(context, keyAlpha, alpha)
        PreferencesStorage.setIntValue(context, sdlKeyEventPrefsKey, sdlKeyEvent)
    }

    suspend fun resetToDefaults(context: Context) {
        offsetXPercent = defaultOffsetXPercent
        offsetYPercent = defaultOffsetYPercent
        sizePercent = defaultSizePercent
        alpha = defaultAlpha
        sdlKeyEvent = defaultSdlKeyEvent
        saveButtonState(context)
    }

    suspend fun resetKeyEvent(context: Context) {
        sdlKeyEvent = defaultSdlKeyEvent
        saveButtonState(context)
    }
}

val wolfensteinButtons = listOf(
    ButtonState(
        dpadId,
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.05f,
        offsetYPercent = 0.5f,
        sizePercent = 0.25f,
        buttonType = ButtonType.Dpad
    ),
    ButtonState(
        "attack",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.8f,
        offsetYPercent = 0.45f,
        sizePercent = 0.13f,
        buttonResId = R.drawable.attack_button,
        sdlKeyEvent = KeyEvent.KEYCODE_ENTER
    ),
    ButtonState(
        "next_weapon",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.1f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.next_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_Z
    ),
    ButtonState(
        "prev_weapon",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.85f,
        offsetYPercent = 0.26f,
        sizePercent = 0.075f,
        buttonResId = R.drawable.prev_weapon,
        sdlKeyEvent = KeyEvent.KEYCODE_X
    ),
    ButtonState(
        "pass_turn",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.97f,
        offsetYPercent = 0.7f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.pass_turn,
        sdlKeyEvent = KeyEvent.KEYCODE_C
    ),
    ButtonState(
        "automap",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.86f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.automap,
        sdlKeyEvent = KeyEvent.KEYCODE_TAB
    ),
    ButtonState(
        "journal",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.76f,
        offsetYPercent = 0.26f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.journal,
        sdlKeyEvent = KeyEvent.KEYCODE_P
    ),
    ButtonState(
        "items",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.72f,
        offsetYPercent = 0.9f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.items,
        sdlKeyEvent = KeyEvent.KEYCODE_I
    ),
    ButtonState(
        "syringes",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.7f,
        offsetYPercent = 0.45f,
        sizePercent = 0.085f,
        buttonResId = R.drawable.syringe,
        sdlKeyEvent = KeyEvent.KEYCODE_O
    ),
    ButtonState(
        "escape",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.99f,
        offsetYPercent = 0.05f,
        sizePercent = 0.07f,
        buttonResId = R.drawable.pause,
        sdlKeyEvent = KeyEvent.KEYCODE_ESCAPE
    ),
    ButtonState(
        "hide_controls",
        EngineTypes.WolfensteinRpg,
        offsetXPercent = 0.5f,
        offsetYPercent = 0.05f,
        sizePercent = 0.05f,
        buttonResId = R.drawable.toggles,
        buttonType = ButtonType.ControlsHider
    ),
    ButtonState(
        ButtonType.DpadDown.toString().lowercase(),
        EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_DOWN,
        buttonType = ButtonType.DpadDown,
        buttonResId = R.drawable.dpad_down,
    ),
    ButtonState(
        ButtonType.DpadUp.toString().lowercase(),
        EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_UP,
        buttonType = ButtonType.DpadUp,
        buttonResId = R.drawable.dpad_up,
    ),
    ButtonState(
        ButtonType.DpadLeft.toString().lowercase(),
        EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_LEFT,
        buttonType = ButtonType.DpadLeft,
        buttonResId = R.drawable.dpad_left,
    ),
    ButtonState(
        ButtonType.DpadRight.toString().lowercase(),
        EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_RIGHT,
        buttonType = ButtonType.DpadRight,
        buttonResId = R.drawable.dpad_right,
    )
)

@Composable
fun OnScreenController(
    buttonsToDraw: Collection<ButtonState>,
    inGame: Boolean,
    allowToEditControls: Boolean = true,
    onBack: () -> Unit = { }
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val density = LocalContext.current.resources.displayMetrics.density
    val screenWidthPx = configuration.screenWidthDp * density
    val screenHeightPx = configuration.screenHeightDp * density
    val coroutineScope = rememberCoroutineScope()

    var buttonStates by remember { mutableStateOf(mapOf<String, ButtonState>()) }
    var selectedButtonId by remember { mutableStateOf<String?>(null) }
    var isEditMode by remember { mutableStateOf((!inGame)) }
    var backgroundColor by remember { mutableStateOf(Color.Transparent) }
    var hideScreenControls by remember(false) { mutableStateOf(false) }
    val clampButtonsFlow by PreferencesStorage.getBooleanValue(context, clampButtonsPrefsKey, true).collectAsStateWithLifecycle (true)

    fun clampButton(state: ButtonState) {
        if (!clampButtonsFlow!!){
            return
        }

        val extraMarginPercent = 0.05f // 5% запас от ширины/высоты экрана

        // Горизонтальные границы с запасом
        val minX = -extraMarginPercent
        val maxX = 1f - state.sizePercent + extraMarginPercent
        state.offsetXPercent = state.offsetXPercent.coerceIn(minX, maxX)

        // Высота кнопки в пикселях и процентовка от экрана
        val buttonHeightPx = state.sizePercent * screenWidthPx
        val buttonHeightPercent = buttonHeightPx / screenHeightPx

        // Вертикальные границы с запасом
        val minY = -extraMarginPercent
        val maxY = 1f - buttonHeightPercent + extraMarginPercent
        state.offsetYPercent = state.offsetYPercent.coerceIn(minY, maxY)
    }

    LaunchedEffect(Unit) {
        val loadedMap = buttonsToDraw.associateBy { it.id }
        loadedMap.values.forEach { state ->
            state.loadButtonState(context)
        }
        loadedMap.values.forEach { state ->
            clampButton(state)
            coroutineScope.launch { state.saveButtonState(context) }
        }
        buttonStates = loadedMap
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
                context,
                selectedButtonId,
                inGame,
                onAlphaChange = { delta ->
                    selectedButtonId?.let { id ->
                        val state = buttonStates[id] ?: return@let
                        state.alpha = (state.alpha + delta).coerceIn(0.0f, 1f)
                        coroutineScope.launch {
                            state.saveButtonState(context)
                        }
                    }
                },
                onSizeChange = { deltaPercent ->
                    selectedButtonId?.let { id ->
                        val state = buttonStates[id] ?: return@let
                        state.sizePercent = (state.sizePercent + deltaPercent).coerceIn(0.02f, 1f)
                        coroutineScope.launch {
                            state.saveButtonState(context)
                        }
                    }
                },
                onReset = {
                    coroutineScope.launch {
                        buttonStates.values.forEach { state ->
                            state.resetToDefaults(context)
                        }
                        PreferencesStorage.setBooleanValue(context, clampButtonsPrefsKey, true)
                        buttonStates.values.forEach { state ->
                            clampButton(state)
                            state.saveButtonState(context)
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

        buttonStates.forEach { (id, state) ->
            if (state.buttonType.ordinal in ButtonType.DpadUp.ordinal..ButtonType.DpadRight.ordinal) {
                return@forEach
            }

            val sizePx: Float = screenWidthPx * state.sizePercent
            val sizeDp: Dp = (sizePx / density).dp

            val renderOffsetX = state.offsetXPercent * screenWidthPx
            val renderOffsetY = state.offsetYPercent * screenHeightPx

            val renderButton = state.buttonType == ButtonType.ControlsHider || !hideScreenControls || isEditMode
            if (renderButton) {
                DraggableImageButton(
                    id = id,
                    state = state,
                    offset = Offset(renderOffsetX, renderOffsetY),
                    sizeDp = sizeDp,
                    isEditMode = isEditMode,
                    isSelected = (selectedButtonId == id),
                    onClick = {
                        if (isEditMode) {
                            selectedButtonId = id
                            coroutineScope.launch {
                                PreferencesStorage.setBooleanValue(context, clampButtonsPrefsKey, false)
                            }
                        }

                        if (state.buttonType == ButtonType.ControlsHider && inGame && !isEditMode) {
                            hideScreenControls = !hideScreenControls
                        }
                    },
                    onDragEnd = { newX, newY ->
                        state.offsetXPercent = (newX / screenWidthPx)
                        state.offsetYPercent = (newY / screenHeightPx)
                        coroutineScope.launch {
                            state.saveButtonState(context)
                        }
                    },
                    inGame = inGame,
                    buttonsToDraw = buttonsToDraw
                )
            }
        }
    }
}

@Composable
private fun DraggableImageButton(
    id: String,
    state: ButtonState,
    offset: Offset,
    sizeDp: Dp,
    isEditMode: Boolean,
    inGame: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDragEnd: (x: Float, y: Float) -> Unit,
    buttonsToDraw: Collection<ButtonState>
) {
    var position by remember(id) { mutableStateOf(offset) }

    LaunchedEffect(offset) {
        position = offset
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
            .size(sizeDp)
            .alpha(state.alpha)
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
        when (state.buttonType) {
            ButtonType.Default -> {
                Image(
                    painter = painterResource(id = state.buttonResId),
                    contentDescription = id,
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(!isEditMode, inGame) {
                            detectTapGestures(
                                onPress = {
                                    if (isEditMode || !inGame) return@detectTapGestures
                                    onTouchDown(state.sdlKeyEvent)
                                    try {
                                        awaitRelease()
                                        onTouchUp(state.sdlKeyEvent)
                                    } catch (_: Exception) {
                                        onTouchUp(state.sdlKeyEvent)
                                    }
                                }
                            )
                        }
                )
            }
            ButtonType.Dpad -> {
                DPad(
                    modifier = Modifier.fillMaxSize(),
                    isEditMode = isEditMode,
                    inGame = inGame,
                    buttonsToDraw = buttonsToDraw,
                    dpadSize = sizeDp
                )
            }
            ButtonType.ControlsHider -> {
                Image(
                    painter = painterResource(id = state.buttonResId),
                    contentDescription = id,
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (!isEditMode && inGame) {
                                Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onClick()
                                }
                            } else {
                                Modifier
                            }
                        )
                )
            }
            else -> {
            }
        }
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
private fun DrawTouchCamera() {
    var mWidth by remember { mutableFloatStateOf(0.0f) }
    var mHeight by remember { mutableFloatStateOf(0.0f) }
    var isActionDownActive by remember { mutableStateOf(false) }
    var widthSize = 0
    var heightSize = 0

    fun onTouchEvent(event: MotionEvent): Boolean {
        var touchDevId = event.deviceId
        val pointerCount = event.pointerCount
        var action = event.actionMasked
        var pointerFingerId: Int
        var i = -1
        var x: Float
        var y: Float
        var p: Float

        if (touchDevId < 0) {
            touchDevId -= 1
        }

        when (action) {
            MotionEvent.ACTION_MOVE -> {
                i = 0
                while (i < pointerCount) {
                    pointerFingerId = if (isActionDownActive) event.getPointerId(i) else (event.getPointerId(i) - 1)
                    if (pointerFingerId < 0) pointerFingerId = 0
                    x = event.getX(i) / mWidth
                    y = event.getY(i) / mHeight
                    p = event.getPressure(i)
                    if (p > 1.0f) p = 1.0f
                    SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action, x, y, p)
                    i++
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_DOWN -> {
                isActionDownActive = event.actionMasked == MotionEvent.ACTION_DOWN
                i = 0
                if (i == -1) i = event.actionIndex
                pointerFingerId = event.getPointerId(i)
                x = event.getX(i) / mWidth
                y = event.getY(i) / mHeight
                p = event.getPressure(i)
                if (p > 1.0f) p = 1.0f
                SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action, x, y, p)
            }

            MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_POINTER_DOWN -> {
                if (i == -1) i = event.actionIndex
                pointerFingerId = if (isActionDownActive) event.getPointerId(i) else (event.getPointerId(i) - 1)
                if (pointerFingerId < 0) pointerFingerId = 0
                if (!isActionDownActive && pointerFingerId == 0) {
                    action = if (action == MotionEvent.ACTION_POINTER_DOWN) MotionEvent.ACTION_DOWN else MotionEvent.ACTION_UP
                }
                x = event.getX(i) / mWidth
                y = event.getY(i) / mHeight
                p = event.getPressure(i)
                if (p > 1.0f) p = 1.0f
                SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action, x, y, p)
            }

            MotionEvent.ACTION_CANCEL -> {
                isActionDownActive = false
                i = 0
                while (i < pointerCount) {
                    pointerFingerId = event.getPointerId(i)
                    x = event.getX(i) / mWidth
                    y = event.getY(i) / mHeight
                    p = event.getPressure(i)
                    if (p > 1.0f) p = 1.0f
                    SDLActivity.onNativeTouch(
                        touchDevId,
                        pointerFingerId,
                        MotionEvent.ACTION_UP,
                        x,
                        y,
                        p
                    )
                    i++
                }
            }

            else -> {}
        }

        return true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .layout { measurable, constraints ->
                widthSize = constraints.maxWidth
                heightSize = constraints.maxHeight

                if (SDLSurface.fixedWidth > 0) {
                    val myAspect = 1.0f * SDLSurface.fixedWidth / SDLSurface.fixedHeight
                    var resultWidth = widthSize.toFloat()
                    var resultHeight = resultWidth / myAspect
                    if (resultHeight > heightSize) {
                        resultHeight = heightSize.toFloat()
                        resultWidth = resultHeight * myAspect
                    }
                    mWidth = resultWidth
                    mHeight = resultHeight
                } else {
                    mWidth = widthSize.toFloat()
                    mHeight = heightSize.toFloat()
                }

                val placeable = measurable.measure(
                    Constraints.fixed(mWidth.roundToInt(), mHeight.roundToInt())
                )

                layout(mWidth.roundToInt(), mHeight.roundToInt()) {
                    placeable.place(0, 0)
                }
            }
            .alpha(0f)
            .pointerInteropFilter { motionEvent ->
                onTouchEvent(motionEvent)
                true
            }
    )
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun DPad(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    inGame: Boolean,
    buttonsToDraw: Collection<ButtonState>,
    dpadSize: Dp
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val buttonSize = dpadSize * 0.4f
        val offsetAmount = dpadSize * 0.33f

        val offsetYStorage = hashMapOf<ButtonType, Dp>(
            ButtonType.DpadUp to -offsetAmount,
            ButtonType.DpadDown to offsetAmount
        )
        val offsetXStorage = hashMapOf<ButtonType, Dp>(
            ButtonType.DpadLeft to -offsetAmount,
            ButtonType.DpadRight to offsetAmount
        )

        @Composable
        fun dpadButton(
            painterId: Int,
            desc: String,
            sdlKeyEvent: Int = 0,
            offsetX: Dp = 0.dp,
            offsetY: Dp = 0.dp
        ) {
            Image(
                painter = painterResource(painterId),
                contentDescription = desc,
                modifier = Modifier
                    .size(buttonSize)
                    .offset(x = offsetX, y = offsetY)
                    .pointerInput(!isEditMode, inGame) {
                        detectTapGestures(
                            onPress = {
                                if (isEditMode || !inGame) return@detectTapGestures
                                onTouchDown(sdlKeyEvent)
                                try {
                                    awaitRelease()
                                    onTouchUp(sdlKeyEvent)
                                } catch (_: Exception) {
                                    onTouchUp(sdlKeyEvent)
                                }
                            }
                        )
                    }
            )
        }

        for (button in buttonsToDraw) {
            if (button.buttonType in listOf(ButtonType.DpadUp, ButtonType.DpadDown)) {
                dpadButton(
                    button.buttonResId,
                    button.id,
                    button.sdlKeyEvent,
                    offsetY = offsetYStorage[button.buttonType]!!
                )
            } else if (button.buttonType in listOf(ButtonType.DpadLeft, ButtonType.DpadRight)) {
                dpadButton(
                    button.buttonResId,
                    button.id,
                    button.sdlKeyEvent,
                    offsetX = offsetXStorage[button.buttonType]!!
                )
            }
        }
    }
}

@Composable
private fun EditControls(
    context: Context,
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
            Button(onClick = { onSizeChange(+0.02f) }) {
                Text(context.getString(R.string.increase_controls_size))
            }
            Button(onClick = { onSizeChange(-0.02f) }) {
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

private fun onTouchDown(keyCode: Int) = SDLActivity.onNativeKeyDown(keyCode)
private fun onTouchUp(keyCode: Int) = SDLActivity.onNativeKeyUp(keyCode)
