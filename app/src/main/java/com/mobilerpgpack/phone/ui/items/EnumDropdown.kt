@file:OptIn(ExperimentalMaterial3Api::class)

package com.mobilerpgpack.phone.ui.items

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
inline fun <reified T : Enum<T>> EnumDropdown(
    title: String,
    initialValue: T? = null,
    crossinline onValueChange: (T) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedValue by remember (initialValue) { mutableStateOf(initialValue) }
    val enumValues = remember { enumValues<T>() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = title + if (selectedValue == null) "" else ": ${selectedValue!!.name}",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            enumValues.forEach { value ->
                DropdownMenuItem(
                    text = { Text(value.name) },
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