package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView

interface IEngineUIController {

    val screenViewsToDraw : Collection<IScreenControlsView>

    @Composable
    fun DrawSettings()
}