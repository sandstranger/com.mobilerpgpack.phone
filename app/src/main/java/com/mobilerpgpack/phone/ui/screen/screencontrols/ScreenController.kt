package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.mobilerpgpack.phone.ui.items.EnumDropdown
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.CustomSDLButton
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

abstract class ScreenController : KoinComponent, IScreenController {

    private val customViews : MutableList<IScreenControlsView> = mutableListOf()

    private val _activeViewsToDraw: MutableList<IScreenControlsView> = mutableListOf()

    protected val preferencesStorage : PreferencesStorage = get ()

    final override val activeViewsToDraw: Collection<IScreenControlsView> get() = _activeViewsToDraw

    @SuppressLint("ConfigurationScreenWidthHeight")
    @Composable
    override fun DrawScreenControls(
        views: Collection<IScreenControlsView>,
        activeEngine : EngineTypes,
        inGame: Boolean,
        allowToEditControls: Boolean,
        drawInSafeArea : Boolean,
        onBack: () -> Unit) {

        customViews.apply {
            clear()
            addAll(buildCustomViews(activeEngine))
        }

        _activeViewsToDraw.apply {
            clear()
            addAll(views)
            addAll(customViews)
        }

        val context = LocalContext.current
        val configuration = LocalConfiguration.current
        val density = context.resources.displayMetrics.density
        val coroutineScope = rememberCoroutineScope()

        val clampButtonsPrefsKey = koinInject<Preferences.Key<Boolean>> { parametersOf(activeEngine) }

        var viewsToDraw by remember { mutableStateOf(mapOf<String, IScreenControlsView>()) }
        var selectedButtonId by remember { mutableStateOf<String?>(null) }
        var selectedViewRenderRule by remember { mutableStateOf<ViewRenderRule?>(null) }
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
            val loadedMap = _activeViewsToDraw.associateBy { it.buttonState.id }
            loadedMap.values.forEach { view ->
                view.buttonState.load()
            }
            loadedMap.values.forEach { view ->
                clampButton(view.buttonState)
                coroutineScope.launch { view.buttonState.save() }
            }
            viewsToDraw = loadedMap
        }

        _activeViewsToDraw.forEach {
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
                    selectedViewRenderRule,
                    inGame,
                    onAlphaChange = { delta ->
                        selectedButtonId?.let { id ->
                            val state = viewsToDraw[id]!!.buttonState
                            state.alpha = (state.alpha + delta).coerceIn(0.0f, 1f)
                            coroutineScope.launch {
                                state.save()
                            }
                        }
                    },
                    onSizeChange = { deltaPercent ->
                        selectedButtonId?.let { id ->
                            val state = viewsToDraw[id]!!.buttonState
                            state.sizePercent = (state.sizePercent + deltaPercent).coerceIn(0.025f, 1f)
                            coroutineScope.launch {
                                state.save()
                            }
                        }
                    },
                    onRenderRuleChange = { newRenderRule ->
                        selectedButtonId?.let { id ->
                            val state = viewsToDraw[id]!!.buttonState
                            state.viewRenderRule = newRenderRule
                            coroutineScope.launch {
                                state.save()
                            }
                        }
                    },
                    onCustomViewSelected = { customView ->
                        customView.buttonState.isDeleted = false
                        coroutineScope.launch { customView.buttonState.save() }
                    },
                    onReset = {
                        coroutineScope.launch {
                            viewsToDraw.values.forEach { view ->
                                view.buttonState.resetToDefaults()
                            }
                            preferencesStorage.setBooleanValueAsync( clampButtonsPrefsKey, true)
                            viewsToDraw.values.forEach { view ->
                                clampButton(view.buttonState)
                                view.buttonState.save()
                            }
                            selectedButtonId = null
                            selectedViewRenderRule = null
                        }
                    },
                    onBack = {
                        selectedButtonId = null
                        selectedViewRenderRule = null
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

                    val renderView = (view.isHideControlsButton || view.renderView || isEditMode) && !view.buttonState.isDeleted

                    if (renderView) {
                        DrawView(
                            viewToDraw = view,
                            offset = Offset(renderOffsetX, renderOffsetY),
                            sizeDp = sizeDp,
                            isEditMode = isEditMode,
                            isSelected = (selectedButtonId == id),
                            onClick = {
                                if (isEditMode) {
                                    selectedButtonId = id
                                    selectedViewRenderRule = view.buttonState.viewRenderRule
                                    coroutineScope.launch {
                                        preferencesStorage.setBooleanValueAsync(
                                            clampButtonsPrefsKey,
                                            false)
                                    }
                                }
                            },
                            onDragEnd = { newX, newY ->
                                view.buttonState.offsetXPercent = (newX / screenWidthPx)
                                view.buttonState.offsetYPercent = (newY / screenHeightPx)
                                coroutineScope.launch {
                                    view.buttonState.save()
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
                        .minimumInteractiveComponentSize()
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
    protected abstract fun DrawTouchCamera()

    protected abstract fun buildCustomViews (engineTypes: EngineTypes) : Collection<IScreenControlsView>

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
                .minimumInteractiveComponentSize()
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
                                position =
                                    Offset(position.x + dragAmount.x, position.y + dragAmount.y)
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
        viewRenderRule: ViewRenderRule?,
        inGame: Boolean,
        onAlphaChange: (Float) -> Unit,
        onSizeChange: (Float) -> Unit,
        onRenderRuleChange : (ViewRenderRule) -> Unit,
        onCustomViewSelected : (selectedView : IScreenControlsView) -> Unit,
        onReset: () -> Unit,
        onBack: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        var showCustomViewsEditor by remember { mutableStateOf(false) }

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
                Button(onClick = { onAlphaChange(SCREEN_ITEMS_CHANGE_ALPHA_OFFSET) }) {
                    Text(stringResource(R.string.increase_controls_alpha))
                }
                Button(onClick = { onAlphaChange(-SCREEN_ITEMS_CHANGE_ALPHA_OFFSET) }) {
                    Text(stringResource(R.string.decrease_controls_alpha))
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onSizeChange(SCREEN_ITEMS_CHANGE_SIZE_OFFSET) }) {
                    Text(stringResource(R.string.increase_controls_size))
                }
                Button(onClick = { onSizeChange(-SCREEN_ITEMS_CHANGE_SIZE_OFFSET) }) {
                    Text(stringResource(R.string.decrease_controls_size))
                }
            }
            Spacer(Modifier.height(8.dp))
            if (viewRenderRule!=null){
                EnumDropdown(stringResource(R.string.screen_controls_view_render_rule), viewRenderRule,
                    onRenderRuleChange)
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { showCustomViewsEditor = true }) {
                Text(stringResource(R.string.add_custom_buttons))
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onReset) {
                    Text(stringResource(R.string.reset_controls_to_default))
                }
                if (!inGame) {
                    Button(onClick = onBack) {
                        Text(stringResource(R.string.close_controls_configuration))
                    }
                }
            }
        }

        if (showCustomViewsEditor){
            DrawCustomButtonsEditor { customView ->
                showCustomViewsEditor = false
                if (customView!=null){
                    onCustomViewSelected.invoke(customView)
                }
            }
        }
    }

    @Composable
    private fun DrawCustomButtonsEditor(onViewSelected: (selectedView: IScreenControlsView?) -> Unit) {
        if (customViews.isEmpty()) {
            onViewSelected(null)
            return
        }
        val itemsToDraw by mutableStateOf(mutableListOf<IScreenControlsView>())
        itemsToDraw.addAll(customViews.filter { it.buttonState.isDeleted }.toList())

        AlertDialog(
            onDismissRequest = { onViewSelected(null) },
            confirmButton = {
                TextButton(onClick = { onViewSelected(null) }) {
                    Text(stringResource(R.string.close_text))
                }
            },
            title = { Text(stringResource(R.string.select_button)) },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(itemsToDraw, key = { _, view ->
                        view.buttonState.id
                    }) { _, view ->
                        Row(
                            modifier = Modifier.clickable { onViewSelected(view) },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomSDLButton.DrawView(
                                Modifier
                                    .width(50.dp)
                                    .height(50.dp), Color.Black,
                                view.buttonState.id
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                modifier = Modifier.wrapContentHeight(),
                                text = view.buttonState.id,
                                color = Color.Black,
                                fontSize = 21.sp,
                                textAlign = TextAlign.Right
                            )
                        }
                    }
                }
            }
        )
    }

    companion object {
        private const val SCREEN_ITEMS_CHANGE_SIZE_OFFSET : Float = 0.005f

        private const val SCREEN_ITEMS_CHANGE_ALPHA_OFFSET : Float = 0.05f
    }
}

