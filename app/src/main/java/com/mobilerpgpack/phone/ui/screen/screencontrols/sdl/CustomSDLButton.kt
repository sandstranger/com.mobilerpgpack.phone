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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewRenderRule

abstract class CustomSDLButton(
    private val id: String,
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    sdlKeyEvent: Int = 0,
    useToggle: Boolean = false,
    defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default
) : SDLImageButton(
        id, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha, sdlKeyEvent,
        NOT_EXISTING_RES, useToggle, defaultViewRenderRule) {

    final override val buttonState: ButtonState = ButtonState(
        id,
        engineType,
        offsetXPercent = offsetXPercent,
        offsetYPercent = offsetYPercent,
        sizePercent = sizePercent,
        buttonResId = NOT_EXISTING_RES,
        sdlKeyEvent = sdlKeyEvent,
        alpha = alpha,
        defaultViewRenderRule = defaultViewRenderRule,
        isCustomButton = true)

    @Composable
    final override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) =
        DrawView(modifier = BuildModifier(isEditMode, inGame), id = id)

    companion object {
        @Composable
        fun DrawView(modifier: Modifier, color: Color = Color.White, id: String) {
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