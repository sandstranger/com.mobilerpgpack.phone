package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.ui.screen.screencontrols.utils.touchListenerModifier
import org.koin.core.component.KoinComponent

abstract class ImageButton(
    id: String,
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
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    showInQuickPanel : Boolean = false) : IScreenControlsView, KoinComponent {

    final override var screenController: IScreenController? = null

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
        ignoreOutOfBoundsTouchEventsInitialState = ignoreOutOfBoundsTouchEvents,
        showInQuickPanelInitialState = showInQuickPanel
    )

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        val viewState = remember { viewState }
        val inGame = remember { inGame }
        Image(painter = painterResource(id = viewState.buttonResId),
            contentDescription = viewState.id,
            modifier = Modifier.interactiveControlModifier(isEditMode, inGame)
        )
    }

    protected abstract fun onClick(context: Context)

    @Composable
    protected fun Modifier.interactiveControlModifier(isEditMode: Boolean, inGame: Boolean): Modifier {
        val modifier = this
            .fillMaxSize()
            .minimumInteractiveComponentSize()

        if (!inGame){
            return modifier
        }

        val context = LocalContext.current
        return modifier.touchListenerModifier(isEditMode, viewState, onTouchDown = {
            onClick(context)
        })
    }
}