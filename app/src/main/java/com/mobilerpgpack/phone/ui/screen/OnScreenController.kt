package com.mobilerpgpack.phone.ui.screen

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes

private const val dpadId = "dpad"

private enum class DpadDirection {
    UP, DOWN, LEFT, RIGHT
}

// Теперь каждый ButtonState один раз создаёт свои ключи
class ButtonState(
    val id: String,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    size: Float = 64f,
    alpha: Float = 0.75f,
    val buttonResId : Int = android.R.drawable.ic_menu_add,
    sdlKeyEvent : Int = 0,
) {
    // Ключи создаются единожды при инициализации инстанса
    private val keyX: Preferences.Key<Float> = floatPreferencesKey("${id}_x")
    private val keyY: Preferences.Key<Float> = floatPreferencesKey("${id}_y")
    private val keySize: Preferences.Key<Float> = floatPreferencesKey("${id}_size")
    private val keyAlpha: Preferences.Key<Float> = floatPreferencesKey("${id}_alpha")
    private val sdlKeyEventPrefsKey: Preferences.Key<Int> = intPreferencesKey("${id}_sdl_key")

    private val defaultOffsetXPercent = offsetXPercent
    private val defaultOffsetYPercent = offsetYPercent
    private val defaultSize = size
    private val defaultAlpha = alpha
    private val defaultSdlKeyEvent = sdlKeyEvent

    var offsetXPercent by mutableFloatStateOf(offsetXPercent)
    var offsetYPercent by mutableFloatStateOf(offsetYPercent)
    var size by mutableFloatStateOf(size)
    var alpha by mutableFloatStateOf(alpha)
    var sdlKeyEvent by mutableIntStateOf(sdlKeyEvent)


    suspend fun loadButtonState (context: Context){
        offsetXPercent = PreferencesStorage.getFloatValue(context, keyX, offsetXPercent).first()!!
        offsetYPercent = PreferencesStorage.getFloatValue(context, keyY, offsetXPercent).first()!!
        size = PreferencesStorage.getFloatValue(context, keySize, size).first()!!
        alpha = PreferencesStorage.getFloatValue(context, keyAlpha, alpha).first()!!
        sdlKeyEvent = PreferencesStorage.getIntValue(context, sdlKeyEventPrefsKey, sdlKeyEvent).first()!!
    }

    suspend fun saveButtonState(context: Context) {
        PreferencesStorage.setFloatValue(context,keyX, offsetXPercent)
        PreferencesStorage.setFloatValue(context, keyY, offsetYPercent)
        PreferencesStorage.setFloatValue(context, keySize, size)
        PreferencesStorage.setFloatValue(context, keyAlpha, alpha)
        PreferencesStorage.setIntValue(context, sdlKeyEventPrefsKey, sdlKeyEvent)
    }

    suspend fun resetToDefaults (context: Context){
        offsetXPercent = defaultOffsetXPercent
        offsetYPercent = defaultOffsetYPercent
        size = defaultSize
        alpha = defaultAlpha
        saveButtonState(context)
    }

    suspend fun resetKeyEvent (context: Context){
        sdlKeyEvent = defaultSdlKeyEvent
        saveButtonState(context)
    }
}

// Дефолтные экземпляры для сброса
private val defaultButtons = listOf(
    ButtonState("btn1", 0.1f, 0.1f, sdlKeyEvent = KeyEvent.KEYCODE_0),
    ButtonState("btn2", 0.6f, 0.2f),
    ButtonState("btn3", 0.3f, 0.4f),
    ButtonState("btn4", 0.8f, 0.5f),
    ButtonState("btn5", 0.5f, 0.6f),
    ButtonState(dpadId, 0.1f, 0.8f, size = 150f)
)

@Composable
fun OnScreenController() {
    KeyEventEditDialog(defaultButtons, onDismiss = {})
   // ButtonStateEditorDialog(defaultButtons, LocalContext.current)
//    OnScreenController(EngineTypes.DefaultActiveEngine, false)
}

@Composable
fun OnScreenController(activeEngineType : EngineTypes, inGame : Boolean) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val density = LocalContext.current.resources.displayMetrics.density
    val screenWidth = configuration.screenWidthDp * density
    val screenHeight = configuration.screenHeightDp * density
    val coroutineScope = rememberCoroutineScope()

    var buttonStates by remember { mutableStateOf(mapOf<String, ButtonState>()) }
    var selectedButtonId by remember { mutableStateOf<String?>(null) }
    var isEditMode by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val loadedMap = defaultButtons.associate { def ->
            // Создаём новый инстанс, но сразу переиспользуем дефолтные ключи из шаблона
            val instance = ButtonState(def.id, def.offsetXPercent, def.offsetYPercent, def.size, def.alpha)
            instance.loadButtonState(context)
            def.id to instance
        }
        buttonStates = loadedMap
    }

    Box(modifier = Modifier.fillMaxSize()) {
        EditControls(
            onAlphaChange = { delta ->
                selectedButtonId?.let { id ->
                    val state = buttonStates[id] ?: return@let
                    state.alpha = (state.alpha + delta).coerceIn(0.1f, 1f)
                    coroutineScope.launch {
                        state.saveButtonState(context)
                    }
                }
            },
            onSizeChange = { delta ->
                selectedButtonId?.let { id ->
                    val state = buttonStates[id] ?: return@let
                    state.size = (state.size + delta).coerceIn(24f, 200f)
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
            onBack = { selectedButtonId = null },
            modifier = Modifier.align(Alignment.Center)
        )

        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            onClick = { isEditMode = !isEditMode }
        ) {
            Text(if (isEditMode) "Exit Edit Mode" else "Edit Mode")
        }

        buttonStates.forEach { (id, state) ->
            val offsetX = state.offsetXPercent * screenWidth
            val offsetY = state.offsetYPercent * screenHeight

            DraggableImageButton(
                id = id,
                state = state,
                offset = Offset(offsetX, offsetY),
                isEditMode = isEditMode,
                isSelected = (selectedButtonId == id),
                onClick = {
                    if (isEditMode) selectedButtonId = id
                    else println("Action: $id")
                },
                onDragEnd = { newX, newY ->
                    state.offsetXPercent = (newX / screenWidth).coerceIn(0f, 1f)
                    state.offsetYPercent = (newY / screenHeight).coerceIn(0f, 1f)
                    coroutineScope.launch {
                        state.saveButtonState(context)
                    }
                }
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
    onDragEnd: (x: Float, y: Float) -> Unit
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
        if (id.contains(dpadId)) {
            DPad(
                modifier = Modifier.fillMaxSize(),
                isEditMode = isEditMode,
            )
        } else {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_add),
                contentDescription = id,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (!isEditMode) {
                            Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                // Здесь можно вызвать onClick() или любую другую логику "обычного" клика
                                onClick()
                            }
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun DPad(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val buttonSize = maxWidth * 0.4f
        val offsetAmount = maxWidth * 0.35f

        @Composable
        fun dpadButton(
            painterId: Int,
            desc: String,
            offsetX: Dp = 0.dp,
            offsetY: Dp = 0.dp,
            direction: DpadDirection
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

        dpadButton(R.drawable.dpad_up, "Up", offsetY = -offsetAmount, direction = DpadDirection.UP)
        dpadButton(R.drawable.dpad_down, "Down", offsetY = offsetAmount, direction = DpadDirection.DOWN)
        dpadButton(R.drawable.dpad_left, "Left", offsetX = -offsetAmount, direction = DpadDirection.LEFT)
        dpadButton(R.drawable.dpad_right, "Right", offsetX = offsetAmount, direction = DpadDirection.RIGHT)
    }
}


@Composable
private fun EditControls(
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
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onAlphaChange(+0.1f) }) { Text("ALPHA+") }
            Button(onClick = { onAlphaChange(-0.1f) }) { Text("ALPHA-") }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onSizeChange(+8f) }) { Text("SIZE+") }
            Button(onClick = { onSizeChange(-8f) }) { Text("SIZE-") }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onReset) { Text("RESET TO DEFAULTS") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onBack) { Text("BACK") }
    }
}