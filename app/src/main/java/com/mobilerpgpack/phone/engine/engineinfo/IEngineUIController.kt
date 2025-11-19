package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView

interface IEngineUIController {

    val screenViewsToDraw : Collection<IScreenControlsView>

    @Composable
    fun DrawSettings(navController: NavHostController)
}