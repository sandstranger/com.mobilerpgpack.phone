@file:OptIn(ExperimentalMaterial3Api::class)

package com.mobilerpgpack.phone.ui.items

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.ui.getSurfaceColor
import com.mobilerpgpack.phone.ui.getTextFieldColors

@Composable
inline fun <reified T : Enum<T>> EnumDropdown(
    title: String,
    initialValue: T? = null,
    crossinline onValueChange: (T) -> Unit = {}) {
    var expanded by rememberSaveable { mutableStateOf(false) }
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