package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.ui.getOnBackgroundColor

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
