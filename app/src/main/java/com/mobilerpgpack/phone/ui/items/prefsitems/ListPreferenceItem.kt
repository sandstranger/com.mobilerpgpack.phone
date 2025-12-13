package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow

@Composable
fun ListPreferenceItem(title: String,
                       initialValue: String,
                       entries : Collection<String>,
                       onValueChange : ((String) -> Unit)? = null){
    var showValuesDialog by rememberSaveable  { mutableStateOf(false) }
    var activeValue by rememberSaveable (initialValue) { mutableStateOf(initialValue) }
    val entriesToDraw by rememberSaveable { mutableStateOf(entries.toList()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showValuesDialog = true }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, Modifier.weight(0.4f, true))
        Text(activeValue, modifier = Modifier.weight(0.6f).fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Right)
    }

    if (showValuesDialog){
        AlertDialog(
            onDismissRequest = { showValuesDialog = false },
            title = { Text(title) },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp))
                {
                    itemsIndexed(entriesToDraw, key = { _, stringValue -> stringValue }) { _, stringValue ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    activeValue = stringValue
                                    onValueChange?.invoke(stringValue)
                                    showValuesDialog = false
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(selected = activeValue == stringValue, onClick = null)
                            Text(stringValue)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun ListPreferenceItem(title: String,
                       initialValueFlow: Flow<String>,
                       entries : Collection<String>,
                       onValueChange : ((String) -> Unit)? = null){
    val initialValue by initialValueFlow.collectAsState(initial = "")
    ListPreferenceItem(title, initialValue, entries, onValueChange)
}

@Composable
inline fun <reified T : Enum<T>> ListPreferenceItem(
    title: String,
    initialValue: T? = null,
    crossinline onValueChange: (T) -> Unit = {}) {
    var selectedValue by remember (initialValue) { mutableStateOf(initialValue) }
    val enumValues = remember { enumValues<T>().map { it.toString() }.toList() }
    ListPreferenceItem(title, selectedValue.toString(),enumValues){
        val newValue = enumValueOf<T>(it)
        selectedValue = newValue
        onValueChange.invoke(newValue)
    }
}
