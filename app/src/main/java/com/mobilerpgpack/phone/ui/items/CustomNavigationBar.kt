package com.mobilerpgpack.phone.ui.items

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat

@Suppress("DEPRECATION")
@Composable
fun SetupNavigationBar(useDarkTheme: Boolean) {
    val activity = LocalActivity.current!!
    val colorCodeToUse = MaterialTheme.colorScheme.surface.toArgb()

    SideEffect {
        val window = activity.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        window.navigationBarColor = colorCodeToUse
        insetsController.isAppearanceLightNavigationBars = !useDarkTheme
    }
}