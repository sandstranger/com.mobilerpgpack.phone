package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobilerpgpack.phone.ui.getCheckBoxColors
import com.mobilerpgpack.phone.ui.getOnSurfaceVariantColor

@Composable
fun CheckBox (title : String,
              initialValue : Boolean,
              onValueChanged : ((newValue : Boolean) -> Unit)? = null){
    Row(horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, modifier = Modifier.wrapContentHeight(),
            color = getOnSurfaceVariantColor(), textAlign = TextAlign.Left)
        Checkbox(
            checked = initialValue,
            colors = getCheckBoxColors(),
            onCheckedChange = { onValueChanged?.invoke(it)}
        )
    }
}