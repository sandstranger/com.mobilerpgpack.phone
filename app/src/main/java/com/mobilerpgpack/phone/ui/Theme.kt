package com.mobilerpgpack.phone.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.compose.koinInject

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
fun getBackgroundColor () = getColor { if (it) md_dark_background else md_light_background }

@Composable
fun getSurfaceContainerHighColor () = getColor { if (it) md_dark_surfaceContainerHigh else md_light_surfaceContainerHigh }

@Composable
fun getSurfaceColor () = getColor { if (it) md_dark_surface else md_light_surface }

@Composable
fun getSurfaceVariantColor () = getColor { if (it) md_dark_surfaceVariant else md_light_surfaceVariant }

@Composable
fun getOnSurfaceVariantColor () = getColor { if (it) md_dark_onSurfaceVariant else md_light_onSurfaceVariant }

@Composable
fun getOnSurfaceColor () = getColor { if (it) md_dark_onSurface else md_light_onSurface }

@Composable
fun getOnBackgroundColor () = getColor { if (it) md_dark_onBackground else md_light_onBackground }

@Composable
fun getOnPrimaryColor () = getColor { if (it) md_dark_onPrimary else md_light_onPrimary }

@Composable
fun getPrimaryColor () = getColor { if (it) md_dark_primary else md_light_primary }

@Composable
fun getTextButtonsColors() :  ButtonColors{
    val primaryColor = getPrimaryColor()
    val transparentPrimaryColor by remember (primaryColor) { mutableStateOf(primaryColor.copy(0.4f)) }
    val transparentColor = remember { Color.Transparent }
    return ButtonDefaults.textButtonColors(
        containerColor = transparentColor,
        contentColor = primaryColor,
        disabledContainerColor = transparentPrimaryColor,
        disabledContentColor = transparentPrimaryColor
    )
}

@Composable
fun getButtonsColors() : ButtonColors{
    val primaryColor = getPrimaryColor()
    val onPrimaryColor = getOnPrimaryColor()
    val disabledContainerColor by remember (primaryColor) { mutableStateOf(primaryColor.copy(0.4f)) }
    val disabledContentColor by remember (onPrimaryColor) { mutableStateOf(onPrimaryColor.copy(0.4f)) }
    return ButtonDefaults.buttonColors(
        containerColor = primaryColor,
        contentColor = onPrimaryColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )
}

@Composable
fun getIconButtonsColors() : IconButtonColors{
    val primaryColor = getPrimaryColor()
    val disabledColor by remember (primaryColor) { mutableStateOf(primaryColor.copy(0.4f)) }
    val transparentColor = remember { Color.Transparent }
    return IconButtonDefaults.iconButtonColors(
        containerColor = transparentColor,
        contentColor = primaryColor,
        disabledContainerColor = disabledColor,
        disabledContentColor = disabledColor
    )
}

@Composable
fun getDividerColor () = getColor { if (it) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.2f) }

@Composable
fun getFabIconContainerColor() = getColor { if (it) md_light_primary else md_dark_primary }

@Composable
fun getTextFieldColors() :  TextFieldColors{
    val surfaceColor = getOnSurfaceVariantColor()
    val onSurfaceColor = getSurfaceColor()
    val disabledTextColor by remember (onSurfaceColor) { mutableStateOf(onSurfaceColor.copy(0.6f)) }
    val backgroundColor by remember (onSurfaceColor){ mutableStateOf(onSurfaceColor.copy(0.3f)) }
    return TextFieldDefaults.colors(
        focusedTextColor = onSurfaceColor,
        unfocusedTextColor = onSurfaceColor,
        disabledTextColor = disabledTextColor,
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
            backgroundColor = backgroundColor
        ),
        focusedContainerColor = surfaceColor,
        unfocusedContainerColor = surfaceColor,
        focusedIndicatorColor = onSurfaceColor,
        unfocusedIndicatorColor = onSurfaceColor)
}

@Composable
fun getRadioButtonsColors () : RadioButtonColors {
    val onSurfaceVariantColor = getOnSurfaceVariantColor()
    val disabledSelectedColor by remember (onSurfaceVariantColor)
    { mutableStateOf(onSurfaceVariantColor.copy(alpha = 0.4f)) }
    val disabledUnselectedColor by remember (onSurfaceVariantColor) { mutableStateOf(onSurfaceVariantColor.copy(alpha = 0.2f)) }
    return RadioButtonDefaults.colors(
        selectedColor = getPrimaryColor(),
        unselectedColor = onSurfaceVariantColor,
        disabledSelectedColor = disabledSelectedColor,
        disabledUnselectedColor = disabledUnselectedColor
    )
}

@Composable
fun getSwitchItemColors () : SwitchColors {
    val primaryColor = getPrimaryColor()
    val grayColor = remember { Color.Gray }
    val uncheckedTrackColor = remember { grayColor.copy(alpha = 0.5f) }
    val disabledCheckedThumbColor = remember { grayColor.copy(alpha = 0.4f) }
    val disabledColor = remember { grayColor.copy(alpha = 0.2f) }
    val disabledUncheckedTrackColor = remember { grayColor.copy(alpha = 0.1f) }
    return SwitchDefaults.colors(
        checkedThumbColor = getBackgroundColor(),
        uncheckedThumbColor = Color.Gray,
        checkedTrackColor = primaryColor,
        uncheckedTrackColor = uncheckedTrackColor,
        disabledCheckedThumbColor = disabledCheckedThumbColor,
        disabledUncheckedThumbColor = disabledColor,
        disabledCheckedTrackColor = disabledColor,
        disabledUncheckedTrackColor = disabledUncheckedTrackColor
    )
}

@Composable
fun getEditTextFieldColors() :  TextFieldColors{
    val useDarkTheme = useDarkTheme()
    val primaryColor = getPrimaryColor()
    val onSurfaceColor = getOnSurfaceColor()
    val disabledColor by remember (useDarkTheme) { mutableStateOf(if (useDarkTheme) Color.White.copy(0.4f) else Color.Black.copy(alpha = 0.4f))}
    val backgroundSelectionColor by remember (useDarkTheme) { mutableStateOf(if (useDarkTheme) Color.White.copy(0.2f)
    else Color.Black.copy(alpha = 0.2f)) }
    val grayColor = remember { Color.Gray }
    val transparentColor = remember { Color.Transparent }
    val redColor = remember { Color.Red }
    return TextFieldDefaults.colors(
        focusedTextColor = onSurfaceColor,
        unfocusedTextColor = onSurfaceColor,
        disabledTextColor = disabledColor,
        errorTextColor = redColor,
        cursorColor = onSurfaceColor,
        selectionColors = TextSelectionColors(
            handleColor = onSurfaceColor,
            backgroundColor = backgroundSelectionColor
        ),
        focusedContainerColor = transparentColor,
        unfocusedContainerColor = transparentColor ,
        focusedIndicatorColor = primaryColor,
        unfocusedIndicatorColor = grayColor)
}

@Composable
fun getMenuItemColors() : MenuItemColors{
    val onBackgroundColor = getOnBackgroundColor()
    val grayColor = remember { Color.Gray }
    return MenuItemColors(
        textColor = onBackgroundColor,
        leadingIconColor = onBackgroundColor,
        trailingIconColor = onBackgroundColor,
        disabledTextColor = grayColor,
        disabledLeadingIconColor = grayColor,
        disabledTrailingIconColor = grayColor
    )
}

@Composable
fun getCheckBoxColors() : CheckboxColors{
    val surfaceColor = getOnSurfaceColor()
    val uncheckedColor by remember (surfaceColor) { mutableStateOf(surfaceColor.copy(alpha = 0.6f)) }
    return CheckboxDefaults.colors(
        checkedColor = getPrimaryColor(),
        checkmarkColor = getOnPrimaryColor(),
        uncheckedColor = uncheckedColor)
}

@Composable
fun Theme(content: @Composable () -> Unit) {
    val useDarkTheme = useDarkTheme()
    val colorScheme by remember (useDarkTheme) { mutableStateOf(if(useDarkTheme)
        darkColorScheme else lightColorScheme) }
    MaterialTheme(colorScheme = colorScheme, content = content)
}

@Composable
fun useDarkTheme () : Boolean {
    val preferencesStorage : PreferencesStorage = koinInject()
    return preferencesStorage.getUseDarkThemeValue()
 }

@Composable
private fun getColor (colorGetter: (Boolean) -> Color) : Color{
    val useDarkTheme = useDarkTheme()
    val color by remember (useDarkTheme) { mutableStateOf(colorGetter(useDarkTheme)) }
    return color
}