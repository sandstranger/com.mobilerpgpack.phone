package com.mobilerpgpack.phone.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.items.CheckBoxPreferenceItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current;

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(context.getString(R.string.common_settings), style = MaterialTheme.typography.titleLarge)

        CheckBoxPreferenceItem(context.getString(R.string.display_in_safe_area),
            checkedFlow = PreferencesStorage.getDisplayInSafeAreaValue(context),
            ) { newValue ->
            scope.launch {
                PreferencesStorage.setDisplayInSafeAreaValue(context, newValue)
            }
        }

        DrawDivider()

        CheckBoxPreferenceItem(context.getString(R.string.preserve_aspect_ratio),
            checkedFlow = PreferencesStorage.getPreserveAspectRatioValue(context),
            ) { newValue ->
            scope.launch {
                PreferencesStorage.setPreserveAspectRationValue(context, newValue)
            }
        }

        DrawDivider()

        CheckBoxPreferenceItem(context.getString(R.string.show_custom_mouse_cursor),
            checkedFlow = PreferencesStorage.getShowCustomMouseCursorValue(context),
            ) { newValue ->
            scope.launch {
                PreferencesStorage.setShowCustomMouseCursorValue(context, newValue)
            }
        }

        DrawDivider()
    }
}

@Composable
private fun DrawDivider () = HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))


