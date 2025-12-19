@file:OptIn(ExperimentalMaterial3Api::class)

package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.ui.getMenuItemColors
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.ui.getSurfaceColor
import com.mobilerpgpack.phone.ui.getSurfaceContainerHighColor
import com.mobilerpgpack.phone.ui.getTextFieldColors

@Composable
inline fun <reified T : Enum<T>> EnumDropdown(
    title: String,
    initialValue: T? = null,
    crossinline onValueChange: (T) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    var selectedValue by remember (initialValue) { mutableStateOf(initialValue) }
    val enumValues = remember { enumValues<T>() }
    val surfaceColor = getSurfaceColor()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = title + if (selectedValue == null) "" else ": ${selectedValue!!.name}",
            onValueChange = {},
            readOnly = true,
            colors = getTextFieldColors(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded, modifier = Modifier.graphicsLayer {
                colorFilter = ColorFilter.tint(surfaceColor)
            }) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = getOnSurfaceColor()
        ) {
            enumValues.forEach { value ->
                DropdownMenuItem(
                    colors = MenuItemColors(
                        textColor = surfaceColor,
                        leadingIconColor = surfaceColor,
                        trailingIconColor = surfaceColor,
                        disabledTextColor = Color.Gray,
                        disabledLeadingIconColor = Color.Gray,
                        disabledTrailingIconColor = Color.Gray
                    ),
                    text = { Text(value.name, color = surfaceColor) },
                    onClick = {
                        selectedValue = value
                        onValueChange.invoke(value)
                        expanded = false
                    }
                )
            }
        }
    }
}