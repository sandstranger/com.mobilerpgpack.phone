package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface IEngineUIController {
    @Composable
    fun DrawSettings(navController: NavHostController)
}