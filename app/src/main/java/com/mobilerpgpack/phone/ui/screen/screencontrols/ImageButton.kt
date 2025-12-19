package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getBlockingValue
import com.mobilerpgpack.phone.utils.waitForUpOrCancellation
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

abstract class ImageButton(
    val id: String,
    val engineType: EngineTypes,
    private val offsetXPercent: Float = 0f,
    private val offsetYPercent: Float = 0f,
    private val sizePercent: Float = 0.13f,
    private val alpha: Float = 0.65f,
    private val buttonResId: Int = NOT_EXISTING_RES,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,
    isDeleted : Boolean = false,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false) : IScreenControlsView, KoinComponent {

    protected var screenController: IScreenController? = null
        private set

    override val isQuickPanel: Boolean = false

    override var show: Boolean by mutableStateOf(true)

    final override val viewState: ViewState = ViewState(
        id,
        engineType,
        offsetXPercent = offsetXPercent,
        offsetYPercent = offsetYPercent,
        sizePercent = sizePercent,
        buttonResId = buttonResId,
        alpha = alpha,
        defaultViewRenderRule = defaultViewRenderRule,
        controlsType = controlsType,
        isDeletedInitialState = isDeleted,
        alwaysConsumeTouchEvents = false,
        consumeTouchEventsInitialState = consumeTouchEventsByDefault,
        touchEventsCanIgnoreOutOfBounds = true,
        ignoreOutOfBoundsTouchEventsInitialState = ignoreOutOfBoundsTouchEvents
    )

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        Image(painter = painterResource(id = viewState.buttonResId),
            contentDescription = id,
            modifier = Modifier.interactiveControlModifier(isEditMode, inGame)
        )
    }

    override fun setScreenController(screenController: IScreenController) {
        this.screenController = screenController
    }

    protected abstract fun onClick(context: Context)

    @Composable
    private fun Modifier.interactiveControlModifier(isEditMode: Boolean, inGame: Boolean): Modifier {
        val modifier = this.fillMaxSize().minimumInteractiveComponentSize()

        if (!inGame){
            return modifier
        }

        val context = LocalContext.current
        val preferencesStorage : PreferencesStorage = koinInject()
        val activeEngineString by preferencesStorage.activeEngineAsFlowString.collectAsState("")

        if (activeEngineString.isEmpty()){
            return modifier
        }

        val engineInfo : IEngineInfo = koinInject(named(activeEngineString))
        val mouseButtonsEventsCanBeInvoked by engineInfo.mouseButtonsEventsCanBeInvokedAsFlow.collectAsState(initial = false)

        return modifier.pointerInput(!isEditMode, mouseButtonsEventsCanBeInvoked,
            viewState.consumeTouchEvents,viewState.ignoreOutOfBoundsTouchEvents) {
            if (isEditMode) {
                return@pointerInput
            }
            awaitEachGesture {
                viewState.apply {
                    val consumeEvents = consumeTouchEvents || mouseButtonsEventsCanBeInvoked
                    val pointerPassToUse = if (consumeEvents) PointerEventPass.Initial else PointerEventPass.Main
                    val down = awaitFirstDown(pass = pointerPassToUse)
                    if (consumeEvents) {
                        down.consume()
                    }
                    onClick(context)
                    val up = waitForUpOrCancellation(pointerPassToUse, ignoreOutOfBoundsTouchEvents
                    )
                    if (consumeEvents) {
                        up?.consume()
                    }
                }
            }
        }
    }
}