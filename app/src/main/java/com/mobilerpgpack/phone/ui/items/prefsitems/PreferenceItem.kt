package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.utils.getComposableValue

@Composable
fun PreferenceItem(title: String, initialValue: LiveData<String>, onClick: () -> Unit = {}){
    val liveDataState = initialValue.getComposableValue()
    PreferenceItem(title, liveDataState, onClick)
}

@Composable
fun PreferenceItem(title: String, initialValue: String = "", onClick: () -> Unit = {}) {
    val onBackgroundColor = getOnBackgroundColor()
    Column(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = title, overflow = TextOverflow.Ellipsis, color = onBackgroundColor)

        if (initialValue.isNotBlank()) {
            Text(
                text = initialValue,
                style = MaterialTheme.typography.bodySmall,
                color = onBackgroundColor,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
