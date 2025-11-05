package com.mobilerpgpack.phone.ui.items

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.github.sproctor.composepreferences.SwitchPreference
import kotlinx.coroutines.flow.Flow

@Composable
fun SwitchPreferenceItem(
    title: String,
    initialValue: Boolean,
    key: String,
    enabled: Boolean = true) {
    val titleColor = if (enabled) LocalContentColor.current else LocalContentColor.current.copy(alpha = 0.38f)
    SwitchPreference(
        title = { Text(title, color = titleColor) },
        initialValue = initialValue,
        key = key,
        enabled = enabled)
}

@Composable
fun SwitchPreferenceItem(
    title: String,
    initialValueFlow: Flow<Boolean>,
    key: String,
    enabled: Boolean = true) {
    val initialValue by initialValueFlow.collectAsState(initial = false)
    SwitchPreferenceItem(title, initialValue, key, enabled)
}
