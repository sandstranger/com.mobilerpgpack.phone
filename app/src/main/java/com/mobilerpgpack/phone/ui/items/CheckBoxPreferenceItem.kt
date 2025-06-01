package com.mobilerpgpack.phone.ui.items

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow

@Composable
fun CheckBoxPreferenceItem(
    title: String,
    checkedFlow: Flow<Boolean?>,
    onCheckedChange : (Boolean) -> Unit
) {
    val checked by checkedFlow.collectAsState(initial = false)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, modifier = Modifier.weight(1f))
        Checkbox(
            checked = checked!!,
            onCheckedChange = onCheckedChange
        )
    }
}