package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState

interface IEngineUIController {

    val screenButtonsToDraw : Collection<ButtonState>

    @Composable
    fun DrawSettings()
}