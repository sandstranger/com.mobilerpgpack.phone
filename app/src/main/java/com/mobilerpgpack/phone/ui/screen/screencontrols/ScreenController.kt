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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.getButtonsColors
import com.mobilerpgpack.phone.ui.getOnPrimaryColor
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.ui.getOnSurfaceVariantColor
import com.mobilerpgpack.phone.ui.getPrimaryColor
import com.mobilerpgpack.phone.ui.getSurfaceContainerHighColor
import com.mobilerpgpack.phone.ui.getTextButtonsColors
import com.mobilerpgpack.phone.ui.items.CheckBox
import com.mobilerpgpack.phone.ui.items.EnumDropdown
import com.mobilerpgpack.phone.ui.items.safeAlpha
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.KeyboardType
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.SDLKeyboard
import com.mobilerpgpack.phone.ui.screen.screencontrols.utils.onTouchDown
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getComposableValue
import com.mobilerpgpack.phone.utils.getNotNullValue
import com.mobilerpgpack.phone.utils.keyCodeMap
import com.mobilerpgpack.phone.utils.sharesprefs.booleanPreferencesKey
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import java.util.Locale
import kotlin.math.roundToInt

abstract class ScreenController : IScreenController {

    private val _activeViewsToDraw: MutableList<IScreenControlsView> = mutableListOf()

    private val _showScreenControls = MutableLiveData(true)

    private val _showQuickPanelItems = MutableLiveData(false)

    private val _isEditMode = MutableLiveData (false)

    final override val activeViewsToDraw: Collection<IScreenControlsView> get() = _activeViewsToDraw

    final override var showScreenControls : Boolean
        get() = _showScreenControls.value!!
        set(value) {
            _showScreenControls.value = value
        }

    final override var showQuickPanelItems : Boolean
        get() = _showQuickPanelItems.value!!
        set(value) {
            _showQuickPanelItems.value = value
        }

    final override var isEditMode
        get() = _isEditMode.value!!
        set(value) {
            _isEditMode.value = value
        }

    @SuppressLint("ConfigurationScreenWidthHeight")
    @Composable
    override fun DrawScreenControls(
        activeEngine: EngineTypes,
        inGame: Boolean,
        blockTouchCameraEvents: Boolean,
        drawInSafeArea: Boolean,
        hideOnScreenControls: LiveData<Boolean>?,
        keyboardInputType: CustomKeyboardView.KeyboardType,
        onBack: () -> Unit
    ) {
        LaunchedEffect(Unit) {
            isEditMode = !inGame
        }

        DisposableEffect(Unit){
            onDispose {
                isEditMode = false
            }
        }

        val hideOnScreenControls = hideOnScreenControls?.getComposableValue() ?: false
        val preferencesStorage : PreferencesStorage = koinInject()
        val onBackSaved = remember { onBack }
        val activeEngineSaved = remember(activeEngine) {activeEngine}
        val drawInSafeAreaSaved = remember(drawInSafeArea) { drawInSafeArea }
        val inGame = remember(inGame) { inGame }
        val engineInfo : IEngineInfo = koinInject(named(activeEngineSaved.name))
        val controlsProvider : ControlsProvider = koinInject(named(activeEngineSaved.name))
        val customViews = remember { buildCustomViews(activeEngineSaved) }
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
        val rootView = activity.window.decorView.rootView
        val controlsType = remember { controlsProvider.activeControlsType.value!! }
        val density = remember (activity.resources.displayMetrics.density) {
            activity.resources.displayMetrics.density }
        val clampButtonsPrefsKey = remember { booleanPreferencesKey("${activeEngineSaved.name.lowercase()}_${controlsType.name.lowercase()}") }
        val viewsToDraw = remember { mutableMapOf<String, IScreenControlsView>() }
        var selectedButtonId by remember { mutableStateOf<String?>(null) }
        var selectedButtonAlpha by remember { mutableStateOf<String?>(null) }
        val isEditMode = _isEditMode.getComposableValue(!inGame)
        val backgroundColor by remember (inGame, isEditMode) {
            mutableStateOf(if (!inGame) {
                Color.DarkGray
            } else {
                if (isEditMode) transparentDarkColor else Color.Transparent
            }) }
        var readyToDrawControls by remember { mutableStateOf(false) }
        var clampButtons by remember {
            mutableStateOf(preferencesStorage.getClampButtonsValue(clampButtonsPrefsKey).getNotNullValue())
        }
        var screenWidthPx by remember { mutableFloatStateOf(configuration.screenWidthDp * density) }
        var screenHeightPx by remember { mutableFloatStateOf(configuration.screenHeightDp * density) }
        val showQuickPanelItems = _showQuickPanelItems.getComposableValue()
        val showScreenControls = _showScreenControls.getComposableValue()

        fun clampView(state: ViewState, clampForced : Boolean = false) {
            if (clampButtons || clampForced) {
                state.apply {
                    offsetXPercent.value = offsetXPercent.value!!.coerceIn(0f, 1f - sizePercent.value!!)
                    val buttonHeightPx = sizePercent.value!! * screenWidthPx
                    val buttonHeightPercent = buttonHeightPx / screenHeightPx
                    offsetYPercent.value = offsetYPercent.value!!.coerceIn(0f, 1f - buttonHeightPercent)
                }
            }
        }

        fun preloadButtons() {
            val loadedMap = activeViewsToDraw.associateBy { it.viewState.id }
            loadedMap.values.forEach { view ->
                view.viewState.apply {
                    load()
                    if (!this.isDeleted.value!!){
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

        fun loadAllViews(){
            if (!readyToDrawControls) {
                activeViewsToDraw.forEach { it.screenController = this@ScreenController }
                preloadButtons()
                readyToDrawControls = true
            }
        }

        @Composable
        fun getViewSize (sizePercent : Float) : Dp{
            val sizePx: Float by remember (screenWidthPx, sizePercent) {
                mutableFloatStateOf(screenWidthPx * sizePercent)
            }
            val sizeDp : Dp by remember(sizePx, density) {
                mutableStateOf((sizePx / density).dp)
            }
            return sizeDp
        }

        if (drawInSafeAreaSaved) {
            rootView.apply {
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

                        loadAllViews()
                    }
                    viewTreeObserver.addOnGlobalLayoutListener(listener)
                    onDispose {
                        viewTreeObserver.removeOnGlobalLayoutListener(listener)
                    }
                }
            }
        }
        else{
            LaunchedEffect(Unit) {
                loadAllViews()
            }
        }

        DrawTouchScreen(activeEngineSaved,blockTouchCameraEvents,
            drawInSafeAreaSaved,isEditMode, inGame) {
            if (!hideOnScreenControls) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                ) {
                    if (isEditMode) {
                        val editControlsViewSize = getViewSize(0.3f)
                        EditControls(
                            selectedButtonId,
                            selectedButtonAlpha,
                            onAlphaChange = { delta ->
                                selectedButtonId?.let { id ->
                                    viewsToDraw[id]!!.viewState.apply {
                                        alpha.value = (alpha.value!! + delta).coerceIn(MIN_VIEW_ALPHA, MAX_VIEW_ALPHA)
                                        selectedButtonAlpha = alpha.value!!.roundToString()
                                        save()
                                    }
                                }
                            },
                            onSizeChange = { deltaPercent ->
                                selectedButtonId?.let { id ->
                                    viewsToDraw[id]!!.viewState.apply {
                                        sizePercent.value = (sizePercent.value!! + deltaPercent).coerceIn(
                                            MIN_VIEW_SIZE,
                                            MAX_VIEW_SIZE
                                        )
                                        save()
                                    }
                                }
                            },
                            onCustomViewSelected = { customView ->
                                customView.viewState.apply {
                                    isDeleted.value = false
                                    clampView(this, clampForced = true)
                                    save()
                                }
                            },
                            onViewDeleted = { viewIdToDelete ->
                                viewsToDraw[viewIdToDelete]!!.viewState.apply {
                                    resetToDefaults()
                                    isDeleted.value = true
                                    save()
                                }
                                selectedButtonId = null
                                selectedButtonAlpha = null
                            },
                            onReset = {
                                selectedButtonId = null
                                selectedButtonAlpha = null
                                preferencesStorage.setBooleanValue(clampButtonsPrefsKey, true)
                                clampButtons = true
                                viewsToDraw.values.forEach { view ->
                                    view.viewState.apply {
                                        val wasDeletedBeforeReset = isDeleted.getNotNullValue()
                                        resetToDefaults()
                                        val isDeletedNow = isDeleted.getNotNullValue()
                                        if (!isDeletedNow) {
                                            clampView(this, true)
                                        }
                                        if (!wasDeletedBeforeReset || !isDeletedNow) {
                                            save()
                                        }
                                    }
                                }
                            },
                            onBack = {
                                selectedButtonId = null
                                selectedButtonAlpha = null
                                if (!inGame) {
                                    onBackSaved()
                                }
                                else{
                                    this@ScreenController.isEditMode = false
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .sizeIn(
                                    minWidth = editControlsViewSize,
                                    minHeight = editControlsViewSize
                                )
                        )
                    }

                    if (readyToDrawControls) {

                        viewsToDraw.forEach { (id, view) ->
                            val id = remember (id) { id }
                            val view = remember(view) { view }
                            remember(view.viewState) { view.viewState }.apply {
                                val sizePercent = sizePercent.getComposableValue()
                                val offsetXPercent = offsetXPercent.getComposableValue()
                                val renderOffsetX by remember (offsetXPercent, screenWidthPx) {
                                    mutableFloatStateOf(offsetXPercent * screenWidthPx) }
                                val offsetYPercent = offsetYPercent.getComposableValue()
                                val isDeleted = isDeleted.getComposableValue()
                                val viewRenderRule = viewRenderRule.observeAsState().value
                                val showInQuickPanel = showInQuickPanel.getComposableValue()

                                val renderOffsetY by remember (offsetYPercent, screenHeightPx) {
                                    mutableFloatStateOf(offsetYPercent * screenHeightPx) }

                                val renderView by remember(
                                    isDeleted,
                                    viewRenderRule,
                                    showInQuickPanel,
                                    showScreenControls,
                                    showQuickPanelItems,
                                    isEditMode,
                                    inGame
                                ) {
                                    mutableStateOf(!isDeleted && (isEditMode || view.renderView))
                                }

                                if (renderView) {
                                    DrawView(
                                        viewToDraw = view,
                                        offset = Offset(renderOffsetX, renderOffsetY),
                                        sizeDp = getViewSize(sizePercent),
                                        isEditMode = isEditMode,
                                        isSelected = (selectedButtonId == id),
                                        onClick = {
                                            if (isEditMode) {
                                                selectedButtonId = id
                                                selectedButtonAlpha = alpha.value!!.roundToString()
                                                preferencesStorage.setBooleanValue(
                                                    clampButtonsPrefsKey,
                                                    false
                                                )
                                                clampButtons = false
                                            }
                                        },
                                        onDragEnd = { newX, newY ->
                                            this.offsetXPercent.value = (newX / screenWidthPx)
                                            this.offsetYPercent.value = (newY / screenHeightPx)
                                            save()
                                        },
                                        inGame = inGame,
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }

        if (inGame && hideOnScreenControls){
            this.isEditMode = false
            val showOnlyVirtualKeyboardButton = remember {
                preferencesStorage.hideScreenControls.getNotNullValue() && preferencesStorage.alwaysShowKeyboardButton.getNotNullValue() }
            val sdlKeyboard = koinInject<SDLKeyboard>(named(KeyboardType.SDL3Keyboard.name))
            val keyboardInputType = remember { keyboardInputType }

            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.keyboard),
                    contentDescription = "keyboard_button",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(getViewSize(0.075f))
                        .safeAlpha(0.5f)
                        .minimumInteractiveComponentSize()
                        .padding(start = 8.dp, top = 8.dp)
                        .onTouchDown(isEditMode) {
                            sdlKeyboard.showKeyboard(
                                useReturnButton = true,
                                keyboardInputType
                            )
                        })

                if (!showOnlyVirtualKeyboardButton) {
                    Image(
                        painter = painterResource(R.drawable.pause),
                        contentDescription = "escape_button",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(getViewSize(0.065f))
                            .safeAlpha(0.5f)
                            .minimumInteractiveComponentSize()
                            .padding(end = 8.dp, top = 8.dp)
                            .onTouchDown(isEditMode) {
                                engineInfo.onBackPressed()
                            }
                    )
                }
            }
        }
    }

    @Composable
    protected abstract fun DrawTouchScreen(activeEngine : EngineTypes,
                                           blockTouchCameraEvents: Boolean,
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
        val viewToDraw = remember (viewToDraw){ viewToDraw }
        val viewState = remember (viewToDraw.viewState){ viewToDraw.viewState }
        var position by remember(viewState.id) { mutableStateOf(offset) }
        val intOffset by remember (viewState.id, position){ mutableStateOf(IntOffset(position.x.roundToInt(),
            position.y.roundToInt())) }
        val alpha = viewState.alpha.getComposableValue()
        val color by remember (isSelected, isEditMode){ mutableStateOf(if (isSelected && isEditMode) selectedViewBackgroundColor
        else Color.Transparent) }
        val shape = remember { RoundedCornerShape(8.dp) }

        LaunchedEffect(offset) {
            position = offset
        }

        Box(
            modifier = Modifier
                .offset { intOffset }
                .size(sizeDp)
                .minimumInteractiveComponentSize()
                .safeAlpha(alpha)
                .background(color, shape)
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
                    if (!isEditMode) {
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
        currentAlpha : String?,
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
        val shape = remember { RoundedCornerShape(5.dp) }
        CompositionLocalProvider(LocalContentColor provides onPrimaryColor){
            var showCustomViewsEditor by remember { mutableStateOf(false) }
            var showViewEditor by remember { mutableStateOf(false) }

            Column(
                modifier = modifier
                    .background(Color.Gray.copy(alpha = 0.6f), shape)
                    .padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
            ) {
                if (!selectedButtonId.isNullOrBlank()) {
                    Text(
                        text = selectedButtonId,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 18.sp
                    )
                }

                if (!currentAlpha.isNullOrBlank()){
                    Text(
                        text = "${stringResource(R.string.alpha_value)} $currentAlpha",
                        color = Color.White,
                        fontSize = 16.sp
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
                    Button(onClick = onBack,contentPadding = ButtonDefaults.TextButtonContentPadding,colors = buttonColors) {
                        Text(stringResource(R.string.close_controls_configuration), color = onPrimaryColor,fontSize = 13.sp)
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
        val viewState = remember (viewToEdit.viewState) { viewToEdit.viewState }
        val color = remember { Color.Transparent }

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
                        Box(modifier = Modifier
                            .size(50.dp)
                            .background(color)
                            .graphicsLayer {
                                colorFilter = ColorFilter.tint(onSurfaceVariantColor)
                            }, contentAlignment = Alignment.Center){
                            viewToEdit.DrawView(isEditMode = true, false, 50.dp)
                        }
                        Text(modifier = Modifier.wrapContentHeight(),
                            text = viewState.id,
                            color = onSurfaceVariantColor,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Right
                        )
                    }

                    Button(onClick = {
                        viewState.apply {
                            resetToDefaultsFromViewEditor()
                            save()
                        } },contentPadding = ButtonDefaults.TextButtonContentPadding, colors = buttonColors) {
                        Text(stringResource(R.string.reset_controls_to_default), color = onPrimaryColor)
                    }

                    Column( modifier = Modifier.verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally){
                        val sdlKeyCode = viewState.sdlKeyCode.getComposableValue()
                        viewState.apply {
                            EnumDropdown(
                                stringResource(R.string.screen_controls_view_render_rule),
                                viewRenderRule.value
                            ) {
                                viewRenderRule.value = it
                                save()
                            }

                            if (allowToUseViewAsToggle) {
                                CheckBox(stringResource(R.string.use_as_toggle), useViewAsToggle) {
                                    useViewAsToggle.value = it
                                    save()
                                }
                            }

                            if (!alwaysConsumeTouchEvents){
                                CheckBox(stringResource(R.string.consume_touch_events), consumeTouchEvents) {
                                    consumeTouchEvents.value = it
                                    save()
                                }
                            }

                            if (touchEventsCanIgnoreOutOfBounds){
                                CheckBox(stringResource(R.string.ignore_out_of_bounds_touch_events),
                                    ignoreOutOfBoundsTouchEvents.getComposableValue()) {
                                    ignoreOutOfBoundsTouchEvents.value = it
                                    save()
                                }
                            }

                            if (!viewToEdit.isUpdateQuickPanelStateButton){
                                CheckBox(stringResource(R.string.show_in_quick_panel),
                                    showInQuickPanel.getComposableValue()) {
                                    showInQuickPanel.value = it
                                    save()
                                }
                            }

                            if (this is MouseViewState){
                                CheckBox(stringResource(R.string.invoke_wheel_events),
                                    invokeWheelEventsWhilePressing.getComposableValue()) {
                                    invokeWheelEventsWhilePressing.value = it
                                    save()
                                }
                            }

                            if (sdlKeyCode!= Int.MIN_VALUE && !viewToEdit.isMouseButton){
                                Row(horizontalArrangement = Arrangement.spacedBy(3.dp),
                                    verticalAlignment = Alignment.CenterVertically){
                                    Text( modifier = Modifier.wrapContentHeight(), text = stringResource(R.string.selected_key_code),
                                        color = onSurfaceVariantColor)

                                    Text(modifier = Modifier
                                        .widthIn(min = 100.dp)
                                        .wrapContentHeight()
                                        .clickable { showKeyCodeDialog = true }, color = onSurfaceVariantColor,
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
                            val pair = remember (pair) { pair }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewState.apply {
                                            sdlKeyCode.value = pair.first
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
        val itemsToDraw = remember { _activeViewsToDraw.filter { it.viewState.isDeleted.value!! }.toList() }
        if (itemsToDraw.isEmpty()) {
            onViewSelected(null)
            return
        }
        val onSurfaceVariantColor = getOnSurfaceVariantColor()
        val onSurfaceColor = getOnSurfaceColor()
        val primaryColor = getPrimaryColor()
        val surfaceContainerHighColor = getSurfaceContainerHighColor()
        val color = remember { Color.Transparent }

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
                        val view = remember (view) { view }
                        val viewState = remember (view.viewState)  { view.viewState }
                        Row(
                            modifier = Modifier.clickable { onViewSelected(view) },
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier
                                .size(40.dp)
                                .background(color)
                                .graphicsLayer {
                                    colorFilter = ColorFilter.tint(onSurfaceVariantColor)
                                }, contentAlignment = Alignment.Center){
                                        view.DrawView(isEditMode = true, false, 40.dp)
                            }
                            Text(
                                modifier = Modifier.wrapContentHeight(),
                                text = viewState.id,
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

        private const val MIN_VIEW_ALPHA : Float = 0f

        private const val MAX_VIEW_ALPHA : Float = 1.0f

        private val selectedViewBackgroundColor = Color.Red.copy(0.5f)

        private val transparentDarkColor = Color.DarkGray.copy(alpha = 0.5f)

        private fun Float.roundToString () = "%.2f".format(Locale.ROOT,this).trimEnd('0').trimEnd('.')
    }
}

