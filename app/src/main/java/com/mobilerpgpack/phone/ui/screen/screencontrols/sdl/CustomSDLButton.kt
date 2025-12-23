package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ControlsType
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES
import org.koin.core.component.KoinComponent

abstract class CustomSDLButton(
    id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = 0,
    useToggle: Boolean = false,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
    controlsType: ControlsType = ControlsType.Default,
    consumeTouchEventsByDefault : Boolean = true,
    ignoreOutOfBoundsTouchEvents : Boolean = false,
    showInQuickPanel : Boolean = false) : SDLImageButton(
        id, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha, sdlKeyEvent,
        NOT_EXISTING_RES, useToggle, defaultViewRenderRule, controlsType,
    consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents,showInQuickPanel), KoinComponent {

    final override val viewState: ViewState = ViewState(
        id,
        engineType,
        offsetXPercent = offsetXPercent,
        offsetYPercent = offsetYPercent,
        sizePercent = sizePercent,
        buttonResId = NOT_EXISTING_RES,
        sdlKeyEvent = sdlKeyEvent,
        alpha = alpha,
        defaultViewRenderRule = defaultViewRenderRule,
        isDeletedInitialState = true,
        controlsType = controlsType,
        allowToUseViewAsToggle = true,
        useViewAsToggleInitialState = useToggle,
        ignoreOutOfBoundsTouchEventsInitialState = ignoreOutOfBoundsTouchEvents,
        touchEventsCanIgnoreOutOfBounds = true,
        alwaysConsumeTouchEvents = false,
        consumeTouchEventsInitialState = consumeTouchEventsByDefault,
        showInQuickPanelInitialState = showInQuickPanel)

    @Composable
    final override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        val viewState = remember { viewState }
        DrawView(modifier = Modifier.interactiveControlModifier(isEditMode, inGame), id = viewState.id)
    }

    private companion object {
        @Composable
        private fun DrawView(modifier: Modifier, color: Color = Color.White, id: String) {
            Box(modifier = modifier
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = color,
                        shape = CircleShape
                    )
                    .padding(7.dp)
            ) {
                BasicText(
                    text = id, style = TextStyle(
                        color = color,
                        textAlign = TextAlign.Center
                    ),
                    autoSize = TextAutoSize.StepBased(minFontSize = 2.sp),
                    modifier = Modifier.fillMaxSize().wrapContentHeight()
                )
            }
        }
    }
}