package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.github.sproctor.composepreferences.Preference

@Composable
fun PreferenceItem(title: String, initialValue: String = "", onClick: () -> Unit = {}) {
    if (!initialValue.isEmpty()) {
        Preference(
            title = { Text(title, overflow = TextOverflow.Ellipsis) },
            summary = { Text(initialValue) },
            modifier = Modifier.clickable { onClick() })
    } else {
        Preference(
            title = { Text(title, overflow = TextOverflow.Ellipsis) },
            modifier = Modifier.clickable { onClick() })
    }
}