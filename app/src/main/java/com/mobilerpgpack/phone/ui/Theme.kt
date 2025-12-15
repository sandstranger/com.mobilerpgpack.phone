package com.mobilerpgpack.phone.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val md_light_primary = Color(0xFF6750A4)
private val md_light_onPrimary = Color(0xFFFFFFFF)
private val md_light_primaryContainer = Color(0xFFEADDFF)
private val md_light_onPrimaryContainer = Color(0xFF21005D)

private val md_light_secondary = Color(0xFF625B71)
private val md_light_onSecondary = Color(0xFFFFFFFF)
private val md_light_secondaryContainer = Color(0xFFE8DEF8)
private val md_light_onSecondaryContainer = Color(0xFF1E192B)

private val md_light_tertiary = Color(0xFF7D5260)
private val md_light_onTertiary = Color(0xFFFFFFFF)
private val md_light_tertiaryContainer = Color(0xFFFFD8E4)
private val md_light_onTertiaryContainer = Color(0xFF31111D)

private val md_light_background = Color.White
private val md_light_onBackground = Color.Black

private val md_light_surface = Color.White
private val md_light_onSurface = Color.Black

private val md_light_surfaceVariant = Color(0xFFE6E1E5)
private val md_light_onSurfaceVariant = Color(0xFF1C1B1F)

private val md_light_error = Color(0xFFB3261E)
private val md_light_onError = Color(0xFFFFFFFF)

private val md_dark_primary = Color(0xFFB39DDB)
private val md_dark_onPrimary = Color(0xFF2A1847)
private val md_dark_primaryContainer = Color(0xFF42325E)
private val md_dark_onPrimaryContainer = Color(0xFFE6DEFF)

private val md_dark_secondary = Color(0xFFCCC2DC)
private val md_dark_onSecondary = Color(0xFF332D41)
private val md_dark_secondaryContainer = Color(0xFF4A4458)
private val md_dark_onSecondaryContainer = Color(0xFFE8DEF8)

private val md_dark_tertiary = Color(0xFFEFB8C8)
private val md_dark_onTertiary = Color(0xFF492532)
private val md_dark_tertiaryContainer = Color(0xFF633B48)
private val md_dark_onTertiaryContainer = Color(0xFFFFD8E4)

private val md_dark_background = Color(0xFF1C1B1F)
private val md_dark_onBackground = Color(0xFFEAE6E6)

private val md_dark_surface = Color(0xFF1C1B1F)
private val md_dark_onSurface = Color(0xFFE6E1E5)

private val md_dark_surfaceVariant = Color(0xFF49454F)
private val md_dark_onSurfaceVariant = Color(0xFFD0C8D6)

private val md_dark_error = Color(0xFFF2B8B5)
private val md_dark_onError = Color(0xFF601410)

private val LightColorScheme = lightColorScheme(
    primary = md_light_primary,
    onPrimary = md_light_onPrimary,
    primaryContainer = md_light_primaryContainer,
    onPrimaryContainer = md_light_onPrimaryContainer,

    secondary = md_light_secondary,
    onSecondary = md_light_onSecondary,
    secondaryContainer = md_light_secondaryContainer,
    onSecondaryContainer = md_light_onSecondaryContainer,

    tertiary = md_light_tertiary,
    onTertiary = md_light_onTertiary,
    tertiaryContainer = md_light_tertiaryContainer,
    onTertiaryContainer = md_light_onTertiaryContainer,

    background = md_light_background,
    onBackground = md_light_onBackground,

    surface = md_light_surface,
    onSurface = md_light_onSurface,
    surfaceVariant = md_light_surfaceVariant,
    onSurfaceVariant = md_light_onSurfaceVariant,

    error = md_light_error,
    onError = md_light_onError
)

private val DarkColorScheme = darkColorScheme(
    primary = md_dark_primary,
    onPrimary = md_dark_onPrimary,
    primaryContainer = md_dark_primaryContainer,
    onPrimaryContainer = md_dark_onPrimaryContainer,

    secondary = md_dark_secondary,
    onSecondary = md_dark_onSecondary,
    secondaryContainer = md_dark_secondaryContainer,
    onSecondaryContainer = md_dark_onSecondaryContainer,

    tertiary = md_dark_tertiary,
    onTertiary = md_dark_onTertiary,
    tertiaryContainer = md_dark_tertiaryContainer,
    onTertiaryContainer = md_dark_onTertiaryContainer,

    background = md_dark_background,
    onBackground = md_dark_onBackground,

    surface = md_dark_surface,
    onSurface = md_dark_onSurface,
    surfaceVariant = md_dark_surfaceVariant,
    onSurfaceVariant = md_dark_onSurfaceVariant,

    error = md_dark_error,
    onError = md_dark_onError
)

@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
