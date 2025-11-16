package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.screen.screencontrols.wolfensteinButtons

class PsyDoomComposeSettings : IEngineUIController {

    override val screenViewsToDraw = wolfensteinButtons

    @Composable
    override fun DrawSettings() {}
}