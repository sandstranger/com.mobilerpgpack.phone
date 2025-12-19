package com.mobilerpgpack.phone.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.java.KoinJavaComponent.get

private val preferencesStorage : PreferencesStorage = get(PreferencesStorage::class.java)
private val useDarkThemeFlow = preferencesStorage.getUseDarkThemeValue(false)

private val md_light_primary = Color(0xFF6750A4)
private val md_light_background = Color.White
private val md_light_onBackground = Color.Black
private val md_light_onPrimary = Color.White
private val md_light_surface = Color.White
private val md_light_onSurface = Color.Black
private val md_light_surfaceVariant = Color.White
private val md_light_onSurfaceVariant = Color.Black
private val md_light_surfaceContainerHigh =Color(0xFFE6E1E5)

private val md_dark_background = Color.Black
private val md_dark_onBackground = Color.White
private val md_dark_surface = Color.Black
private val md_dark_onSurface = Color.White
private val md_dark_onPrimary = Color.Black
private val md_dark_surfaceVariant = Color(0xFF49454F)
private val md_dark_onSurfaceVariant = Color.White
private val md_dark_primary = Color(0xFFD0BCFF)
private val md_dark_surfaceContainerHigh = Color(0xFF292D32)

private val lightColorScheme = lightColorScheme(
    background = md_light_background,
    primary = md_light_primary,
    onBackground = md_light_onBackground,
    surface = md_light_surface,
    onSurface = md_light_onSurface,
    surfaceContainerHigh = md_light_surfaceContainerHigh,
    surfaceVariant = md_light_surfaceVariant,
    onSurfaceVariant = md_light_onSurfaceVariant,
    onPrimary = md_light_onPrimary)

private val darkColorScheme = darkColorScheme(
    onPrimary = md_dark_onPrimary,
    primary = md_dark_primary,
    surfaceContainerHigh = md_dark_surfaceContainerHigh,
    background = md_dark_background,
    onBackground = md_dark_onBackground,
    surface = md_dark_surface,
    onSurface = md_dark_onSurface,
    surfaceVariant = md_dark_surfaceVariant,
    onSurfaceVariant = md_dark_onSurfaceVariant)

@Composable
fun getBackgroundColor () : Color{
    return if (useDarkTheme()) md_dark_background else md_light_background
}

@Composable
fun getSurfaceContainerHighColor () : Color{
    return if (useDarkTheme()) md_dark_surfaceContainerHigh else md_light_surfaceContainerHigh
}

@Composable
fun getSurfaceColor () : Color{
    return if (useDarkTheme()) md_dark_surface else md_light_surface
}

@Composable
fun getSurfaceVariantColor () : Color{
    return if (useDarkTheme()) md_dark_surfaceVariant else md_light_surfaceVariant
}

@Composable
fun getOnSurfaceVariantColor () : Color{
    return if (useDarkTheme()) md_dark_onSurfaceVariant else md_light_onSurfaceVariant
}

@Composable
fun getOnSurfaceColor () : Color{
    return if (useDarkTheme()) md_dark_onSurface else md_light_onSurface
}

@Composable
fun getOnBackgroundColor () : Color{
    return if (useDarkTheme()) md_dark_onBackground else md_light_onBackground
}

@Composable
fun getOnPrimaryColor () : Color{
    return if (useDarkTheme()) md_dark_onPrimary else md_light_onPrimary
}

@Composable
fun getPrimaryColor () : Color{
    return if (useDarkTheme()) md_dark_primary else md_light_primary
}

@Composable
fun getTextButtonsColors() :  ButtonColors{
    val primaryColor = getPrimaryColor()
    val transparentPrimaryColor = primaryColor.copy(0.4f)
    return ButtonDefaults.textButtonColors(
        containerColor = Color.Transparent,
        contentColor = primaryColor,
        disabledContainerColor = transparentPrimaryColor,
        disabledContentColor = transparentPrimaryColor
    )
}

@Composable
fun getButtonsColors() : ButtonColors{
    val primaryColor = getPrimaryColor()
    val onPrimaryColor = getOnPrimaryColor()
    return ButtonDefaults.buttonColors(
        containerColor = primaryColor,
        contentColor = onPrimaryColor,
        disabledContainerColor = primaryColor.copy(0.4f),
        disabledContentColor = onPrimaryColor.copy(0.4f)
    )
}

@Composable
fun getIconButtonsColors() : IconButtonColors{
    val primaryColor = getPrimaryColor()
    return IconButtonDefaults.iconButtonColors(
        containerColor = Color.Transparent,
        contentColor = primaryColor,
        disabledContainerColor = primaryColor.copy(0.4f),
        disabledContentColor = primaryColor.copy(0.4f)
    )
}

@Composable
fun getDividerColor () = if (useDarkTheme()) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.2f)

@Composable
fun getFabIconContainerColor() = if (useDarkTheme()) md_light_primary else md_dark_primary

@Composable
fun getTextFieldColors() :  TextFieldColors{
    val surfaceColor = getOnSurfaceVariantColor()
    val onSurfaceColor = getSurfaceColor()
    return TextFieldDefaults.colors(
        focusedTextColor = onSurfaceColor,
        unfocusedTextColor = onSurfaceColor,
        disabledTextColor = onSurfaceColor.copy(0.6f),
        errorTextColor = Color.Red,
        cursorColor = onSurfaceColor,
        errorLeadingIconColor = surfaceColor,
        focusedTrailingIconColor = surfaceColor,
        errorTrailingIconColor = surfaceColor,
        focusedLeadingIconColor = surfaceColor,
        unfocusedLeadingIconColor = surfaceColor,
        unfocusedTrailingIconColor = surfaceColor,
        selectionColors = TextSelectionColors(
            handleColor = onSurfaceColor,
            backgroundColor = onSurfaceColor.copy(0.3f)
        ),
        focusedContainerColor = surfaceColor,
        unfocusedContainerColor = surfaceColor,
        focusedIndicatorColor = onSurfaceColor,
        unfocusedIndicatorColor = onSurfaceColor)
}

@Composable
fun getRadioButtonsColors () : RadioButtonColors {
    val onSurfaceVariantColor = getOnSurfaceVariantColor()
    return RadioButtonDefaults.colors(
        selectedColor = getPrimaryColor(),
        unselectedColor = onSurfaceVariantColor,
        disabledSelectedColor = onSurfaceVariantColor.copy(alpha = 0.4f),
        disabledUnselectedColor = onSurfaceVariantColor.copy(alpha = 0.2f)
    )
}

@Composable
fun getSwitchItemColors () : SwitchColors {
    val primaryColor = getPrimaryColor()
    return SwitchDefaults.colors(
        checkedThumbColor = getBackgroundColor(),
        uncheckedThumbColor = Color.Gray,
        checkedTrackColor = primaryColor,
        uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f),
        disabledCheckedThumbColor = Color.Gray.copy(alpha = 0.4f),
        disabledUncheckedThumbColor = Color.Gray.copy(alpha = 0.2f),
        disabledCheckedTrackColor = Color.Gray.copy(alpha = 0.2f),
        disabledUncheckedTrackColor = Color.Gray.copy(alpha = 0.1f)
    )
}

@Composable
fun getEditTextFieldColors() :  TextFieldColors{
    val useDarkTheme = useDarkTheme()
    val primaryColor = getPrimaryColor()
    return TextFieldDefaults.colors(
        focusedTextColor = if (useDarkTheme) Color.White else Color.Black,
        unfocusedTextColor = if (useDarkTheme) Color.White else Color.Black,
        disabledTextColor = if (useDarkTheme) Color.White else Color.Black.copy(alpha = 0.4f),
        errorTextColor = Color.Red,
        cursorColor = if (useDarkTheme) Color.White else Color.Black,
        selectionColors = TextSelectionColors(
            handleColor = if (useDarkTheme) Color.White else Color.Black,
            backgroundColor = if (useDarkTheme) Color.White.copy(alpha = 0.3f) else Color.Black.copy(
                alpha = 0.2f
            )
        ),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent ,
        focusedIndicatorColor = primaryColor,
        unfocusedIndicatorColor = Color.Gray)
}

@Composable
fun getMenuItemColors() : MenuItemColors{
    val onBackgroundColor = getOnBackgroundColor()
    return MenuItemColors(
        textColor = onBackgroundColor,
        leadingIconColor = onBackgroundColor,
        trailingIconColor = onBackgroundColor,
        disabledTextColor = Color.Gray,
        disabledLeadingIconColor = Color.Gray,
        disabledTrailingIconColor = Color.Gray
    )
}

@Composable
fun getCheckBoxColors() : CheckboxColors{
    val surfaceColor = getOnSurfaceColor()
    return CheckboxDefaults.colors(
        checkedColor = getPrimaryColor(),
        checkmarkColor = getOnPrimaryColor(),
        uncheckedColor = surfaceColor.copy(alpha = 0.6f))
}

@Composable
fun Theme(content: @Composable () -> Unit) {
    val colorScheme = if(useDarkTheme()) darkColorScheme else lightColorScheme
    MaterialTheme(colorScheme = colorScheme, content = content)
}

@Composable
private fun useDarkTheme () : Boolean {
    val useDarkTheme by useDarkThemeFlow.collectAsState(initial = false)
    return useDarkTheme
}