@file:OptIn(ExperimentalMaterial3Api::class)

package com.mobilerpgpack.phone.ui.items

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mobilerpgpack.phone.ui.getMenuItemColors
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
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
    val onBackgroundColor = getOnBackgroundColor()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = title + if (selectedValue == null) "" else ": ${selectedValue!!.name}",
            onValueChange = {},
            readOnly = true,
            colors = getTextFieldColors(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = getSurfaceContainerHighColor()
        ) {
            enumValues.forEach { value ->
                DropdownMenuItem(
                    colors = getMenuItemColors(),
                    text = { Text(value.name, color = onBackgroundColor) },
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