package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.annotation.SuppressLint
import android.view.ViewTreeObserver
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.getButtonsColors
import com.mobilerpgpack.phone.ui.getOnPrimaryColor
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.ui.getOnSurfaceVariantColor
import com.mobilerpgpack.phone.ui.getPrimaryColor
import com.mobilerpgpack.phone.ui.getSurfaceContainerHighColor
import com.mobilerpgpack.phone.ui.getTextButtonsColors
import com.mobilerpgpack.phone.ui.items.CheckBox
import com.mobilerpgpack.phone.ui.items.EnumDropdown
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.keyCodeMap
import com.mobilerpgpack.phone.utils.sharesprefs.Key
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import kotlin.math.roundToInt

abstract class ScreenController : KoinComponent, IScreenController {

    private val _activeViewsToDraw: MutableList<IScreenControlsView> by mutableStateOf(mutableListOf())

    protected val preferencesStorage : PreferencesStorage = get ()

    final override val activeViewsToDraw: Collection<IScreenControlsView> get() = _activeViewsToDraw

    final override var showScreenControls by mutableStateOf(true)

    final override var showQuickPanelItems by mutableStateOf(false)

    @SuppressLint("ConfigurationScreenWidthHeight")
    @Composable
    override fun DrawScreenControls(
        activeEngine : EngineTypes,
        inGame: Boolean,
        blockTouchCameraEvents : Boolean,
        allowToEditControls: Boolean,
        drawInSafeArea : Boolean,
        onBack: () -> Unit) {

        val controlsProvider : ControlsProvider = koinInject(named(activeEngine.name))
        val customViews = remember { buildCustomViews(activeEngine) }
        val commonsViewsToDraw = remember { controlsProvider.controlsToDraw }
        val activeViewsToDraw = remember {
            _activeViewsToDraw.apply {
                clear()
                addAll(commonsViewsToDraw)
                addAll(customViews)
            }
        }

        val activity = LocalActivity.current!!
        val configuration = LocalConfiguration.current
        val density = remember (activity.resources.displayMetrics.density) {
            activity.resources.displayMetrics.density }
        val coroutineScope = rememberCoroutineScope()

        val clampButtonsPrefsKey = koinInject<Key<Boolean>> { parametersOf(activeEngine) }
        val viewsToDraw by remember { mutableStateOf(mutableMapOf<String, IScreenControlsView>()) }
        var selectedButtonId by remember { mutableStateOf<String?>(null) }
        var isEditMode by remember { mutableStateOf((!inGame)) }
        var backgroundColor by remember { mutableStateOf(Color.Transparent) }
        var readyToDrawControls by remember { mutableStateOf(false) }
        val clampButtons = preferencesStorage.getBooleanValue( clampButtonsPrefsKey, true)
        var allContentLoaded by remember { mutableStateOf(false) }

        var screenWidthPx by remember { mutableFloatStateOf(0f) }
        var screenHeightPx by remember { mutableFloatStateOf(0f) }

        fun clampView(state: ViewState, clampForced : Boolean = false) {
            if (clampButtons || clampForced) {
                state.apply {
                    offsetXPercent = offsetXPercent.coerceIn(0f, 1f - state.sizePercent)
                    val buttonHeightPx = sizePercent * screenWidthPx
                    val buttonHeightPercent = buttonHeightPx / screenHeightPx
                    offsetYPercent = offsetYPercent.coerceIn(0f, 1f - buttonHeightPercent)
                }
            }
        }

        fun preloadButtons() {
            val loadedMap = activeViewsToDraw.associateBy { it.viewState.id }
            loadedMap.values.forEach { view ->
                view.viewState.apply {
                    load()
                    if (!this.isDeleted){
                        clampView(this)
                        save()
                    }
                }
            }
            viewsToDraw.apply {
                clear()
                this.putAll(loadedMap)
            }
        }

        activeViewsToDraw.forEach { it.screenController = this }

        if (drawInSafeArea) {
            activity.window.decorView.rootView.apply {
                DisposableEffect(this) {
                    val listener = ViewTreeObserver.OnGlobalLayoutListener {
                        val insets = ViewCompat.getRootWindowInsets(this@apply)!!
                        val metrics = resources.displayMetrics
                        val systemBarsInsets = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
                        )

                        screenWidthPx =
                            (metrics.widthPixels - systemBarsInsets.left - systemBarsInsets.right).toFloat()
                        screenHeightPx =
                            (metrics.heightPixels - systemBarsInsets.top - systemBarsInsets.bottom).toFloat()

                        if (!allContentLoaded) {
                            allContentLoaded = true
                            preloadButtons()
                            readyToDrawControls = true
                        }
                    }
                    viewTreeObserver.addOnGlobalLayoutListener(listener)
                    onDispose {
                        viewTreeObserver.removeOnGlobalLayoutListener(listener)
                    }
                }
            }
        }
        else{
            screenWidthPx = configuration.screenWidthDp * density
            screenHeightPx = configuration.screenHeightDp * density
            preloadButtons()
            readyToDrawControls = true
        }

        backgroundColor = if (!inGame) {
            Color.DarkGray
        } else {
            if (isEditMode) transparentDarkColor else Color.Transparent
        }

        DrawTouchCamera(blockTouchCameraEvents,drawInSafeArea,isEditMode, inGame) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                if (isEditMode) {
                    EditControls(
                        selectedButtonId,
                        inGame,
                        onAlphaChange = { delta ->
                            selectedButtonId?.let { id ->
                                viewsToDraw[id]!!.viewState.apply {
                                    alpha = (alpha + delta).coerceIn(0.0f, 1f)
                                    save()
                                }
                            }
                        },
                        onSizeChange = { deltaPercent ->
                            selectedButtonId?.let { id ->
                                viewsToDraw[id]!!.viewState.apply {
                                    sizePercent = (sizePercent + deltaPercent).coerceIn(
                                        MIN_VIEW_SIZE,
                                        MAX_VIEW_SIZE
                                    )
                                    save()
                                }
                            }
                        },
                        onCustomViewSelected = { customView ->
                            customView.viewState.apply {
                                isDeleted = false
                                clampView(this, clampForced = true)
                                save()
                            }
                        },
                        onViewDeleted = { viewIdToDelete ->
                            viewsToDraw[viewIdToDelete]!!.viewState.apply {
                                resetToDefaults()
                                isDeleted = true
                                save()
                            }
                            selectedButtonId = null
                        },
                        onReset = {
                            selectedButtonId = null
                            coroutineScope.launch {
                                preferencesStorage.setBooleanValueAsync(clampButtonsPrefsKey, true)
                                viewsToDraw.values.forEach { view ->
                                    view.viewState.apply {
                                        val wasDeletedBeforeReset = isDeleted
                                        resetToDefaults()
                                        if (!isDeleted) {
                                            clampView(this)
                                        }
                                        if (!wasDeletedBeforeReset || !isDeleted) {
                                            save()
                                        }
                                    }
                                }
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
                    if (inGame && allowToEditControls) {
                        Image(
                            painter = painterResource(R.drawable.cog),
                            contentDescription = "settings_button",
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .size(60.dp)
                                .alpha(0.5f)
                                .minimumInteractiveComponentSize()
                                .padding(8.dp)
                                .clickable(indication = null,
                                    interactionSource = null
                                ){
                                    isEditMode = !isEditMode
                                }
                        )
                    }

                    viewsToDraw.forEach { (id, view) ->

                        val sizePx: Float = screenWidthPx * view.viewState.sizePercent
                        val sizeDp: Dp = (sizePx / density).dp

                        val renderOffsetX = view.viewState.offsetXPercent * screenWidthPx
                        val renderOffsetY = view.viewState.offsetYPercent * screenHeightPx

                        val renderView = !view.viewState.isDeleted && (isEditMode || view.renderView)

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
                                        preferencesStorage.setBooleanValue(
                                            clampButtonsPrefsKey,
                                            false
                                        )
                                    }
                                },
                                onDragEnd = { newX, newY ->
                                    view.viewState.apply {
                                        offsetXPercent = (newX / screenWidthPx)
                                        offsetYPercent = (newY / screenHeightPx)
                                        save()
                                    }
                                },
                                inGame = inGame,
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    protected abstract fun DrawTouchCamera(blockTouchCameraEvents: Boolean,
                                           inSafeArea : Boolean,
                                           isEditMode: Boolean, inGame: Boolean,
                                           content: @Composable () -> Unit)

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
        var position by remember(viewToDraw.viewState.id) { mutableStateOf(offset) }

        LaunchedEffect(offset) {
            position = offset
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
                .size(sizeDp)
                .minimumInteractiveComponentSize()
                .alpha(viewToDraw.viewState.alpha)
                .background(
                    if (isSelected && isEditMode) selectedViewBackgroundColor
                    else Color.Transparent,
                    RoundedCornerShape(8.dp)
                )
                .pointerInput(isEditMode, isSelected) {
                    if (!isEditMode) {
                        return@pointerInput
                    }
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
                    if (!isEditMode){
                        return@pointerInput
                    }
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
    private fun EditControls(
        selectedButtonId: String?,
        inGame: Boolean,
        onAlphaChange: (Float) -> Unit,
        onSizeChange: (Float) -> Unit,
        onCustomViewSelected : (selectedView : IScreenControlsView) -> Unit,
        onViewDeleted: (selectedButtonId : String) -> Unit,
        onReset: () -> Unit,
        onBack: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val onPrimaryColor = getOnPrimaryColor()
        val buttonColors = getButtonsColors()
        CompositionLocalProvider(LocalContentColor provides onPrimaryColor){
            var showCustomViewsEditor by remember { mutableStateOf(false) }
            var showViewEditor by remember { mutableStateOf(false) }

            Column(
                modifier = modifier
                    .background(Color.Gray.copy(alpha = 0.6f), RoundedCornerShape(5.dp))
                    .padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!selectedButtonId.isNullOrBlank()) {
                    Text(
                        text = selectedButtonId,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 18.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onAlphaChange(SCREEN_ITEMS_CHANGE_ALPHA_OFFSET) },
                        contentPadding = ButtonDefaults.TextButtonContentPadding, colors = buttonColors) {
                        Text(stringResource(R.string.increase_controls_alpha), color = onPrimaryColor,fontSize = 13.sp)
                    }
                    Button(onClick = { onAlphaChange(-SCREEN_ITEMS_CHANGE_ALPHA_OFFSET) },
                        contentPadding = ButtonDefaults.TextButtonContentPadding,colors = buttonColors) {
                        Text(stringResource(R.string.decrease_controls_alpha),color = onPrimaryColor,fontSize = 13.sp)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onSizeChange(SCREEN_ITEMS_CHANGE_SIZE_OFFSET) },
                        contentPadding = ButtonDefaults.TextButtonContentPadding,colors = buttonColors) {
                        Text(stringResource(R.string.increase_controls_size),color = onPrimaryColor,fontSize = 13.sp)
                    }
                    Button(onClick = { onSizeChange(-SCREEN_ITEMS_CHANGE_SIZE_OFFSET) },
                        contentPadding = ButtonDefaults.TextButtonContentPadding,colors = buttonColors) {
                        Text(stringResource(R.string.decrease_controls_size),color = onPrimaryColor,fontSize = 13.sp)
                    }
                }

                if (selectedButtonId!=null) {
                    Button(onClick = { showViewEditor = true },
                        contentPadding = ButtonDefaults.TextButtonContentPadding,
                        colors = buttonColors) {
                        Text(stringResource(R.string.view_editor, selectedButtonId),
                            color = onPrimaryColor,fontSize = 13.sp
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { showCustomViewsEditor = true },
                        contentPadding = ButtonDefaults.TextButtonContentPadding, colors = buttonColors) {
                        Text(stringResource(R.string.add_controls_items), color = onPrimaryColor,fontSize = 13.sp)
                    }

                    if (selectedButtonId != null) {
                        Button(onClick = { onViewDeleted(selectedButtonId) },
                            contentPadding = ButtonDefaults.TextButtonContentPadding,colors = buttonColors) {
                            Text(stringResource(R.string.delete),  color = onPrimaryColor,fontSize = 13.sp)
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onReset,contentPadding = ButtonDefaults.TextButtonContentPadding, colors = buttonColors) {
                        Text(stringResource(R.string.reset_controls_to_default), color = onPrimaryColor, fontSize = 13.sp)
                    }
                    if (!inGame) {
                        Button(onClick = onBack,contentPadding = ButtonDefaults.TextButtonContentPadding,colors = buttonColors) {
                            Text(stringResource(R.string.close_controls_configuration), color = onPrimaryColor,fontSize = 13.sp)
                        }
                    }
                }
            }

            if (showCustomViewsEditor) {
                DrawCustomViewsEditor { customView ->
                    showCustomViewsEditor = false
                    if (customView != null) {
                        onCustomViewSelected.invoke(customView)
                    }
                }
            }

            if (showViewEditor){
                val viewToDraw by remember (selectedButtonId)
                { mutableStateOf(_activeViewsToDraw.first { it.viewState.id == selectedButtonId }) }
                DrawViewEditor(viewToDraw) {
                    showViewEditor = false
                }
            }
        }
    }

    @Composable
    private fun DrawViewEditor (viewToEdit : IScreenControlsView, onViewEditorClosed : () -> Unit ){
        val onSurfaceVariantColor = getOnSurfaceVariantColor()
        val onSurfaceColor = getOnSurfaceColor()
        val primaryColor = getPrimaryColor()
        val surfaceContainerHighColor = getSurfaceContainerHighColor()
        val buttonColors = getButtonsColors()
        val onPrimaryColor = getOnPrimaryColor()
        var showKeyCodeDialog by remember { mutableStateOf(false) }
        val keyCodeMap = remember { keyCodeMap }
        val keyCodesToDraw = remember (keyCodeMap) { keyCodeMap.toList() }
        val scrollState = rememberScrollState()

        AlertDialog(containerColor = surfaceContainerHighColor,
            textContentColor = onSurfaceVariantColor,
            iconContentColor = onSurfaceVariantColor,
            titleContentColor = onSurfaceColor,
            onDismissRequest = { onViewEditorClosed() },
            confirmButton = {
                TextButton(onClick = { onViewEditorClosed() }, colors = getTextButtonsColors()) {
                    Text(stringResource(R.string.close_text), color = primaryColor)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally){

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(modifier = Modifier.size(50.dp)
                            .background(Color.Transparent,
                                RoundedCornerShape(8.dp)).graphicsLayer {
                                colorFilter = ColorFilter.tint(onSurfaceVariantColor)
                            }, contentAlignment = Alignment.Center){
                            viewToEdit.DrawView(isEditMode = false, false, 50.dp)
                        }
                        Text(modifier = Modifier.wrapContentHeight(),
                            text = viewToEdit.viewState.id,
                            color = onSurfaceVariantColor,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Right
                        )
                    }

                    Button(onClick = {
                        viewToEdit.viewState.apply {
                            resetToDefaultsFromViewEditor()
                            save()
                        } },contentPadding = ButtonDefaults.TextButtonContentPadding, colors = buttonColors) {
                        Text(stringResource(R.string.reset_controls_to_default), color = onPrimaryColor)
                    }

                    Column( modifier = Modifier.verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally){
                        viewToEdit.viewState.apply {
                            EnumDropdown(
                                stringResource(R.string.screen_controls_view_render_rule),
                                viewRenderRule
                            ){
                                viewRenderRule = it
                                save()
                            }

                            if (allowToUseViewAsToggle) {
                                CheckBox(stringResource(R.string.use_as_toggle), useViewAsToggle) {
                                    useViewAsToggle = it
                                    save()
                                }
                            }

                            if (!alwaysConsumeTouchEvents){
                                CheckBox(stringResource(R.string.consume_touch_events), consumeTouchEvents) {
                                    consumeTouchEvents = it
                                    save()
                                }
                            }

                            if (touchEventsCanIgnoreOutOfBounds){
                                CheckBox(stringResource(R.string.ignore_out_of_bounds_touch_events),
                                    ignoreOutOfBoundsTouchEvents) {
                                    ignoreOutOfBoundsTouchEvents = it
                                    save()
                                }
                            }

                            if (!viewToEdit.isUpdateQuickPanelStateButton){
                                CheckBox(stringResource(R.string.show_in_quick_panel),
                                    showInQuickPanel) {
                                    showInQuickPanel = it
                                    save()
                                }
                            }

                            if (this is MouseViewState){
                                CheckBox(stringResource(R.string.invoke_wheel_events),
                                    invokeWheelEventsWhilePressing) {
                                    invokeWheelEventsWhilePressing = it
                                    save()
                                }
                            }

                            if (sdlKeyCode!= Int.MIN_VALUE){
                                Row(horizontalArrangement = Arrangement.spacedBy(3.dp),
                                    verticalAlignment = Alignment.CenterVertically){
                                    Text( modifier = Modifier.wrapContentHeight(), text = stringResource(R.string.selected_key_code),
                                        color = onSurfaceVariantColor)

                                    Text(modifier = Modifier.widthIn(min = 100.dp).wrapContentHeight().clickable { showKeyCodeDialog = true }, color = onSurfaceVariantColor,
                                        text = keyCodeMap[sdlKeyCode]?.keyCodeName ?: stringResource(R.string.uknown), textAlign = TextAlign.Left)
                                }
                            }
                        }
                    }
                }

            })

        if (showKeyCodeDialog) {
            AlertDialog( modifier = Modifier.fillMaxSize(),
                onDismissRequest = { showKeyCodeDialog = false },
                confirmButton = {
                    TextButton(onClick = { showKeyCodeDialog = false }, colors = getTextButtonsColors()) {
                        Text(stringResource(R.string.close_text), color = primaryColor)
                    }
                },
                containerColor = surfaceContainerHighColor,
                textContentColor = onSurfaceVariantColor,
                iconContentColor = onSurfaceVariantColor,
                titleContentColor = onSurfaceColor,
                title = { Text(stringResource(R.string.select_key_code), color = onSurfaceColor) },
                text = {
                    LazyColumn(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp))
                    {
                        itemsIndexed(keyCodesToDraw, key = { _, pair -> pair.second.keyCode }) { _, pair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewToEdit.viewState.apply {
                                            sdlKeyCode = pair.first
                                            save()
                                        }
                                        showKeyCodeDialog = false
                                    }) {
                                Text(pair.second.keyCodeName, color = onSurfaceVariantColor)
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    private fun DrawCustomViewsEditor(onViewSelected: (selectedView: IScreenControlsView?) -> Unit) {
        val itemsToDraw = remember { _activeViewsToDraw.filter { it.viewState.isDeleted }.toList() }
        if (itemsToDraw.isEmpty()) {
            onViewSelected(null)
            return
        }
        val onSurfaceVariantColor = getOnSurfaceVariantColor()
        val onSurfaceColor = getOnSurfaceColor()
        val primaryColor = getPrimaryColor()
        val surfaceContainerHighColor = getSurfaceContainerHighColor()

        AlertDialog(
            modifier = Modifier.fillMaxSize(),
            containerColor = surfaceContainerHighColor,
            textContentColor = onSurfaceVariantColor,
            iconContentColor = onSurfaceVariantColor,
            titleContentColor = onSurfaceColor,
            onDismissRequest = { onViewSelected(null) },
            confirmButton = {
                TextButton(onClick = { onViewSelected(null) }, colors = getTextButtonsColors()) {
                    Text(stringResource(R.string.close_text), color = primaryColor)
                }
            },
            title = { Text(stringResource(R.string.select_view), color = onSurfaceColor) },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(itemsToDraw, key = { _, view ->
                        view.viewState.id
                    }) { _, view ->
                        Row(
                            modifier = Modifier.clickable { onViewSelected(view) },
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(40.dp)
                                    .background(
                                        Color.Transparent,
                                        RoundedCornerShape(8.dp)).graphicsLayer {
                                    colorFilter = ColorFilter.tint(onSurfaceVariantColor)
                                }, contentAlignment = Alignment.Center){
                                        view.DrawView(isEditMode = false, false, 40.dp)
                            }
                            Text(
                                modifier = Modifier.wrapContentHeight(),
                                text = view.viewState.id,
                                color = onSurfaceVariantColor,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Right
                            )
                        }
                    }
                }
            }
        )
    }

    private companion object {
        private const val SCREEN_ITEMS_CHANGE_SIZE_OFFSET : Float = 0.005f

        private const val SCREEN_ITEMS_CHANGE_ALPHA_OFFSET : Float = 0.05f

        private const val MIN_VIEW_SIZE : Float = 0.025f

        private const val MAX_VIEW_SIZE : Float = 1.0f

        private val selectedViewBackgroundColor = Color.Red.copy(0.5f)

        private val transparentDarkColor = Color.DarkGray.copy(alpha = 0.5f)
    }
}

