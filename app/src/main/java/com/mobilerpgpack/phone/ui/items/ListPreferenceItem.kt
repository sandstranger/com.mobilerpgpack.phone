package com.mobilerpgpack.phone.ui.items

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*
// DataStore delegate
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Ключи
val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
val LANGUAGE_KEY = stringPreferencesKey("language")

@Composable
fun SettingsScreen(dataStore: DataStore<Preferences>) {
    val scope = rememberCoroutineScope()

    val prefs by dataStore.data.collectAsState(initial = emptyPreferences())

    val darkMode = prefs[DARK_MODE_KEY] ?: false
    val language = prefs[LANGUAGE_KEY] ?: "ru"

    val languageOptions = listOf("ru" to "Русский", "en" to "English")

    var showLanguageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Настройки", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(24.dp))

        SwitchPreferenceItem(
            title = "Тёмная тема",
            checked = darkMode,
            onCheckedChange = {
                scope.launch {
                    dataStore.edit { prefs ->
                        prefs[DARK_MODE_KEY] = it
                    }
                }
            }
        )

        Divider(modifier = Modifier.padding(vertical = 12.dp))

        ListPreferenceItem(
            title = "Язык интерфейса",
            value = languageOptions.find { it.first == language }?.second ?: "Не выбрано",
            onClick = { showLanguageDialog = true }
        )

        if (showLanguageDialog) {
            AlertDialog(
                onDismissRequest = { showLanguageDialog = false },
                title = { Text("Выберите язык") },
                text = {
                    Column {
                        languageOptions.forEach { (code, label) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        scope.launch {
                                            dataStore.edit { prefs ->
                                                prefs[LANGUAGE_KEY] = code
                                            }
                                        }
                                        showLanguageDialog = false
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = code == language,
                                    onClick = null
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(label)
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }
}

@Composable
fun SwitchPreferenceItem(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun ListPreferenceItem(title: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}*/