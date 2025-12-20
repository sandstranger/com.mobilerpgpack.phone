package com.mobilerpgpack.phone.ui.items

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.mobilerpgpack.phone.ui.getSurfaceColor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.compose.koinInject

@Suppress("DEPRECATION")
@Composable
fun SetupNavigationBar() {
    val preferencesStorage : PreferencesStorage = koinInject()
    val useDarkTheme by preferencesStorage.getUseDarkThemeValue().collectAsState(initial = false)
    val activity = LocalActivity.current!!
    val colorCodeToUse = getSurfaceColor().toArgb()

    SideEffect {
        val window = activity.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        window.navigationBarColor = colorCodeToUse
        insetsController.isAppearanceLightNavigationBars = !useDarkTheme
    }
}