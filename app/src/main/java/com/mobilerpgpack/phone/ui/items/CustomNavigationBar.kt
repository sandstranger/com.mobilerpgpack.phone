package com.mobilerpgpack.phone.ui.items

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat

private val darkNavigationBarColor = Color(0xFF121212).toArgb()

private val whiteNavigationBarColor = Color.Gray.toArgb()

@Suppress("DEPRECATION")
@Composable
fun SetupNavigationBar(useDarkTheme: Boolean) {
    val activity = LocalActivity.current!!

    SideEffect {
        val window = activity.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        window.navigationBarColor = if (useDarkTheme) darkNavigationBarColor else
            whiteNavigationBarColor
        insetsController.isAppearanceLightNavigationBars = !useDarkTheme
    }
}