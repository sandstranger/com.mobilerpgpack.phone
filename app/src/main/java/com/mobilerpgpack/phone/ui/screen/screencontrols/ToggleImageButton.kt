package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ViewState.Companion.NOT_EXISTING_RES

abstract class ToggleImageButton(id: String,
                                 engineType: EngineTypes,
                                 offsetXPercent: Float = 0f,
                                 offsetYPercent: Float = 0f,
                                 sizePercent: Float = 0.13f,
                                 alpha: Float = 0.65f,
                                 buttonResId: Int = NOT_EXISTING_RES,
                                 defaultViewRenderRule: ViewRenderRule = ViewRenderRule.Default,
                                 controlsType: ControlsType = ControlsType.Default,
                                 isDeleted : Boolean = false,
                                 consumeTouchEventsByDefault : Boolean = true,
                                 ignoreOutOfBoundsTouchEvents : Boolean = false,
                                 showInQuickPanel : Boolean = false):
    ImageButton (id,engineType, offsetXPercent, offsetYPercent, sizePercent, alpha,
        buttonResId,defaultViewRenderRule, controlsType, isDeleted,
        consumeTouchEventsByDefault, ignoreOutOfBoundsTouchEvents, showInQuickPanel) {

    private var isActive by mutableStateOf(false)

    @Composable
    final override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) {
        Image(painter = painterResource(id = viewState.buttonResId),
            contentDescription = id,
            modifier = Modifier.interactiveControlModifier(isEditMode, inGame).let{
                if (isActive && !isEditMode && inGame)
                    it.graphicsLayer { colorFilter = ColorFilter.tint(color = Color.Yellow) } else it
            } )
    }

    final override fun onClick(context: Context) {
        isActive=!isActive
        onToggleStateChanged(isActive)
    }

    protected abstract fun  onToggleStateChanged (isActive : Boolean)
}