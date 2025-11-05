package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawTitleText (title: String){
    Text(title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 2.dp))
}

