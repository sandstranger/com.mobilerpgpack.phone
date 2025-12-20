package com.mobilerpgpack.phone.ui.items

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.mobilerpgpack.phone.ui.getSurfaceColor
import com.mobilerpgpack.phone.ui.useDarkTheme

@Suppress("DEPRECATION")
@Composable
fun SetupSystemBars() {
    val useDarkTheme = useDarkTheme()
    val activity = LocalActivity.current!!
    val navigationColor = getSurfaceColor().toArgb()

    SideEffect {
        activity.window.apply {
            navigationBarColor = navigationColor
            statusBarColor = navigationColor
            WindowCompat.getInsetsController(this, decorView).apply {
                isAppearanceLightNavigationBars = !useDarkTheme
                isAppearanceLightStatusBars = !useDarkTheme
            }
        }
    }
}