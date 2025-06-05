package com.mobilerpgpack.phone.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val dpadId = "dpad"

private const val notExistingResId = Int.MIN_VALUE

enum class ButtonType{
    Default,
    Dpad,
    DpadUp,
    DpadDown,
    DpadLeft,
    DpadRight,
    ControlsHider
}

// Теперь каждый ButtonState один раз создаёт свои ключи
class ButtonState(
    val id: String,
    val engineType : EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    size: Float = 64f,
    alpha: Float = 0.75f,
    sdlKeyEvent: Int = 0,
    val buttonResId: Int = android.R.drawable.ic_menu_add,
    val buttonType: ButtonType = ButtonType.Default,
) : Cloneable {
    private val defaultSdlKeyEvent = sdlKeyEvent
    private val defaultOffsetXPercent = offsetXPercent
    private val defaultOffsetYPercent = offsetYPercent
    private val defaultSize           = size
    private val defaultAlpha          = alpha
    private val engineTypeString = engineType.toString().lowercase()

    private val keyX: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_x")
    private val keyY: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_y") // Corrected key name
    private val keySize: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_size")
    private val keyAlpha: Preferences.Key<Float> = floatPreferencesKey("${engineTypeString}_${id}_alpha")
    private val sdlKeyEventPrefsKey: Preferences.Key<Int> = intPreferencesKey("${engineTypeString}_${id}_sdl_key")

    val allowToEditKeyEvent
        get() = buttonResId!=notExistingResId && buttonType != ButtonType.Dpad && buttonType!= ButtonType.ControlsHider

    var offsetXPercent by mutableFloatStateOf(offsetXPercent)
    var offsetYPercent by mutableFloatStateOf(offsetYPercent)
    var size by mutableFloatStateOf(size)
    var alpha by mutableFloatStateOf(alpha)
    var sdlKeyEvent by mutableIntStateOf(sdlKeyEvent)

    suspend fun loadButtonState(context: Context) {
        offsetXPercent = PreferencesStorage.getFloatValue(context, keyX, defaultOffsetXPercent).first()!! // Use default if not found
        offsetYPercent = PreferencesStorage.getFloatValue(context, keyY, defaultOffsetYPercent).first()!! // Use default if not found
        size = PreferencesStorage.getFloatValue(context, keySize, defaultSize).first()!! // Use default if not found
        alpha = PreferencesStorage.getFloatValue(context, keyAlpha, defaultAlpha).first()!! // Use default if not found
        sdlKeyEvent = PreferencesStorage.getIntValue(context, sdlKeyEventPrefsKey, defaultSdlKeyEvent).first()!! // Use default if not found
    }

    suspend fun saveButtonState(context: Context) {
        PreferencesStorage.setFloatValue(context, keyX, offsetXPercent)
        PreferencesStorage.setFloatValue(context, keyY, offsetYPercent)
        PreferencesStorage.setFloatValue(context, keySize, size)
        PreferencesStorage.setFloatValue(context, keyAlpha, alpha)
        PreferencesStorage.setIntValue(context, sdlKeyEventPrefsKey, sdlKeyEvent)
    }

    suspend fun resetToDefaults(context: Context) {
        offsetXPercent = defaultOffsetXPercent
        offsetYPercent = defaultOffsetYPercent
        size           = defaultSize
        alpha          = defaultAlpha
        sdlKeyEvent = defaultSdlKeyEvent // Reset SDL key event too
        saveButtonState(context)
    }

    suspend fun resetKeyEvent(context: Context) {
        sdlKeyEvent = defaultSdlKeyEvent
        saveButtonState(context)
    }
}

val defaultButtons = listOf(
    ButtonState("btn1", EngineTypes.WolfensteinRpg, 0.1f, 0.1f, sdlKeyEvent = KeyEvent.KEYCODE_0),
    ButtonState("btn2",EngineTypes.WolfensteinRpg, 0.6f, 0.2f),
    ButtonState("btn3",EngineTypes.WolfensteinRpg, 0.3f, 0.4f),
    ButtonState("btn4",EngineTypes.WolfensteinRpg, 0.8f, 0.5f),
    ButtonState("btn5",EngineTypes.WolfensteinRpg, 0.5f, 0.6f),
    ButtonState(dpadId,EngineTypes.WolfensteinRpg, 0.1f, 0.8f, size = 150f, buttonType = ButtonType.Dpad),
    ButtonState(ButtonType.DpadDown.toString().lowercase(),EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_DOWN, buttonType = ButtonType.DpadDown, buttonResId = R.drawable.dpad_down, size = 64f),
    ButtonState(ButtonType.DpadUp.toString().lowercase(),EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_UP, buttonType = ButtonType.DpadUp, buttonResId = R.drawable.dpad_up, size = 64f),
    ButtonState(ButtonType.DpadLeft.toString().lowercase(),EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_LEFT, buttonType = ButtonType.DpadLeft, buttonResId = R.drawable.dpad_left, size = 64f),
    ButtonState(ButtonType.DpadRight.toString().lowercase(),EngineTypes.WolfensteinRpg,
        sdlKeyEvent = KeyEvent.KEYCODE_DPAD_RIGHT, buttonType = ButtonType.DpadRight, buttonResId = R.drawable.dpad_right, size = 64f),
)

@Composable
fun OnScreenController(
    buttonsToDraw: Collection<ButtonState>, inGame: Boolean,
    allowToEditControls: Boolean = true, onBack: () -> Unit = { }
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val density = LocalContext.current.resources.displayMetrics.density
    val screenWidth = configuration.screenWidthDp * density
    val screenHeight = configuration.screenHeightDp * density
    val coroutineScope = rememberCoroutineScope()

    var buttonStates by remember { mutableStateOf(mapOf<String, ButtonState>()) }
    var selectedButtonId by remember { mutableStateOf<String?>(null) }
    var isEditMode by remember(!inGame) { mutableStateOf((!inGame)) }
    var backgroundColor by remember { mutableStateOf(Color.Transparent) }

    LaunchedEffect(Unit) {
        val loadedMap = buttonsToDraw.associateBy { it.id } // { id -> тот же ButtonState }
        loadedMap.values.forEach { state ->
            state.loadButtonState(context)
        }
        buttonStates = loadedMap
    }

    if (!inGame){
        backgroundColor = Color.DarkGray
    }
    else if (isEditMode){
        backgroundColor = Color.DarkGray.copy(alpha = 0.5f)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {
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
                onSizeChange = { delta ->
                    selectedButtonId?.let { id ->
                        val state = buttonStates[id] ?: return@let
                        state.size = (state.size + delta).coerceIn(24f, Float.MAX_VALUE)
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
                        selectedButtonId = null
                    }
                },
                onBack =
                    {
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
                    ))
        }

        buttonStates.forEach { (id, state) ->
            // Skip D-pad directional buttons here, they will be drawn within the DPad composable
            if (state.buttonType.ordinal >= ButtonType.DpadUp.ordinal && state.buttonType.ordinal <= ButtonType.DpadRight.ordinal) {
                return@forEach // Skip to the next iteration
            }

            val sizePx = state.size * density
            val availableWidth = screenWidth - sizePx
            val offsetX = state.offsetXPercent * availableWidth
            val availableHeight = screenHeight - sizePx
            val offsetY = state.offsetYPercent * availableHeight

            DraggableImageButton(
                id = id,
                state = state,
                offset = Offset(offsetX, offsetY),
                isEditMode = isEditMode,
                isSelected = (selectedButtonId == id),
                onClick = {
                    if (isEditMode) selectedButtonId = id
                },
                onDragEnd = { newX, newY ->
                    state.offsetXPercent = (newX / availableWidth).coerceIn(0f, 1f)
                    state.offsetYPercent = (newY / availableHeight).coerceIn(0f, 1f)
                    coroutineScope.launch {
                        state.saveButtonState(context)
                    }
                },
                buttonsToDraw = buttonsToDraw
            )
        }
    }
}

@Composable
private fun DraggableImageButton(
    id: String,
    state: ButtonState,
    offset: Offset,
    isEditMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDragEnd: (x: Float, y: Float) -> Unit,
    buttonsToDraw: Collection<ButtonState> // This is passed for DPad specifically
) {
    var position by remember(id) { mutableStateOf(offset) }

    LaunchedEffect(offset) {
        position = offset
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
            .size(state.size.dp)
            .alpha(state.alpha)
            .background(
                if (isSelected && isEditMode) Color.Red.copy(alpha = 0.5f)
                else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .pointerInput(isEditMode) {
                detectDragGestures(
                    onDragStart = {
                        if (isEditMode && !isSelected) {
                            onClick()
                        }
                    },
                    onDrag = { _, dragAmount ->
                        if (isEditMode) {
                            position += dragAmount
                        }
                    },
                    onDragEnd = {
                        if (isEditMode) {
                            onDragEnd(position.x, position.y)
                        }
                    }
                )
            }
            .pointerInput(isEditMode) {
                detectTapGestures(
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {

        when (state.buttonType) {
            ButtonType.Default ->{
                Image(
                    painter = painterResource(id = state.buttonResId), // Use state.buttonResId
                    contentDescription = id,
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (!isEditMode) {
                                Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    // Here you'd likely want to send the SDL key event
                                    Log.d("Button Click", "Button ${state.id} clicked. SDL KeyEvent: ${state.sdlKeyEvent}")
                                }
                            } else {
                                Modifier
                            }
                        )
                )
            }
            ButtonType.Dpad -> {
                DPad(
                    modifier = Modifier.fillMaxSize(),
                    isEditMode = isEditMode,
                    buttonsToDraw = buttonsToDraw
                )
            }
            else -> {}
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun DPad(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    buttonsToDraw: Collection<ButtonState>
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val buttonSize = maxWidth * 0.4f
        val offsetAmount = maxWidth * 0.35f

        val offsetYStorage = hashMapOf<ButtonType, Dp>(ButtonType.DpadUp to -offsetAmount,
            ButtonType.DpadDown to offsetAmount)

        val offsetXStorage = hashMapOf<ButtonType, Dp>(ButtonType.DpadLeft to -offsetAmount,
            ButtonType.DpadRight to offsetAmount)

        @Composable
        fun dpadButton(
            painterId: Int,
            desc: String,
            sdlKeyEvent: Int = 0,
            offsetX: Dp = 0.dp,
            offsetY: Dp = 0.dp,
        ) {
            Image(
                painter = painterResource(painterId),
                contentDescription = desc,
                modifier = Modifier
                    .size(buttonSize)
                    .offset(x = offsetX, y = offsetY)
                    .then(
                        if (!isEditMode) {
                            Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                // Обычное действие D-pad (например, перемещение)
                            }
                        } else {
                            Modifier // в режиме редактирования — никакой кликабельности
                        }
                    )
            )
        }

        for (button in buttonsToDraw) {
            if (offsetYStorage.containsKey(button.buttonType)){
                dpadButton(button.buttonResId, button.id,  button.sdlKeyEvent, offsetY = offsetYStorage[button.buttonType]!!)
                continue
            }

            if (offsetXStorage.containsKey(button.buttonType)){
                dpadButton(button.buttonResId, button.id,  button.sdlKeyEvent, offsetX = offsetXStorage[button.buttonType]!!)
            }
        }
    }
}


@Composable
private fun EditControls(
    context: Context,
    selectedButtonId : String?,
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
        if (selectedButtonId !=null && selectedButtonId.isNotBlank()){
            Text(
                text = selectedButtonId,
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(8.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onAlphaChange(+0.1f) }) { Text(context.getString(R.string.increase_controls_alpha)) }
            Button(onClick = { onAlphaChange(-0.1f) }) { Text(context.getString(R.string.decrease_controls_alpha)) }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onSizeChange(+8f) }) { Text(context.getString(R.string.increase_controls_size)) }
            Button(onClick = { onSizeChange(-8f) }) { Text(context.getString(R.string.decrease_controls_size)) }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onReset) { Text(context.getString(R.string.reset_controls_to_default)) }
        Spacer(Modifier.height(8.dp))
        if (!inGame) {
            Button(onClick = onBack) { Text(context.getString(R.string.close_controls_configuration)) }
        }
    }
}