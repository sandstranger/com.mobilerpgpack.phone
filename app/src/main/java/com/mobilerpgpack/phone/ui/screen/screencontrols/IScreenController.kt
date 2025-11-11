package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.EngineTypes

interface IScreenController{

    var activeViewsToDraw: Collection<IScreenControlsView>?

    @Composable
    fun DrawScreenControls(
        views: Collection<IScreenControlsView>,
        activeEngine : EngineTypes,
        inGame: Boolean,
        allowToEditControls: Boolean = true,
        drawInSafeArea : Boolean = false,
        onBack: () -> Unit = { })
}