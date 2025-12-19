package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.ui.getSwitchItemColors
import kotlinx.coroutines.flow.Flow

@Composable
fun SwitchItem(
    title: String,
    initialValue : Boolean,
    enabled: Boolean = true,
    onCheckedChange : ((Boolean) -> Unit)? = null) {
    var checkedState by rememberSaveable (initialValue) { mutableStateOf(initialValue) }
    val color = getOnBackgroundColor()
    val disabledColor = color.copy(alpha = 0.38f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (enabled) {
                    checkedState = !checkedState
                    onCheckedChange?.invoke(checkedState)
                }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(title,
            modifier = Modifier.weight(1f),
            color = if (enabled) color else disabledColor)
        Switch(
            checked = checkedState,
            onCheckedChange = null,
            colors = getSwitchItemColors(),
            enabled = enabled
        )
    }
}

@Composable
fun SwitchItem(
    title: String,
    valueFlow : Flow<Boolean>,
    enabled: Boolean = true,
    onCheckedChange : ((Boolean) -> Unit)? = null) {
    val initialValue by valueFlow.collectAsState(initial = false)
    SwitchItem(title, initialValue, enabled, onCheckedChange)
}