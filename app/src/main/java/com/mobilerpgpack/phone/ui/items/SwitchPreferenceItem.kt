package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import kotlinx.coroutines.flow.Flow

@Composable
fun SwitchPreferenceItem(
    title: String,
    checkedFlow: Flow<Boolean?>,
    enabled: Boolean = true,
    onCheckedChange : (Boolean) -> Unit
) {
    val checked by checkedFlow.collectAsState(initial = false)
    var checkedState by rememberSaveable (checked!!) { mutableStateOf(checked!!) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (enabled) {
                    checkedState = !checkedState
                    onCheckedChange(checkedState)
                }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TranslatedText(title,
            modifier = Modifier.weight(1f),
            color = if (enabled) LocalContentColor.current else  LocalContentColor.current.copy(alpha = 0.38f))
        Spacer(Modifier.width(5.dp))
        Switch(
            checked = checkedState,
            onCheckedChange = null,
            enabled = enabled
        )
    }
}