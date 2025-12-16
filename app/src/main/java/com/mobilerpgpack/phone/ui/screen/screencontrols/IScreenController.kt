package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.EngineTypes

interface IScreenController{

    var showScreenControls : Boolean

    val activeViewsToDraw: Collection<IScreenControlsView>

    @Composable
    fun DrawScreenControls(
        activeEngine : EngineTypes,
        inGame: Boolean,
        blockTouchCameraEvents : Boolean = false,
        allowToEditControls: Boolean = true,
        drawInSafeArea : Boolean = false,
        onBack: () -> Unit = { })
}