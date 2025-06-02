package com.mobilerpgpack.phone.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.items.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.items.ListPreferenceItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
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
        DrawCommonSettings(context, scope)

    }
}
@Composable
private fun DrawCommonSettings (context: Context, scope: CoroutineScope){

    val engineState by produceState(
        initialValue = EngineTypes.DefaultActiveEngine,
        key1 = context
    ) {
        value = PreferencesStorage.getActiveEngineValue(context)
    }

    Text(context.getString(R.string.common_settings), style = MaterialTheme.typography.titleLarge)

    ListPreferenceItem(context.getString(R.string.active_engine),engineState,EngineTypes.entries) {
        newValue ->
        scope.launch {
            PreferencesStorage.setActiveEngineValue(context, enumValueOf<EngineTypes>(newValue))
        }
    }

    HorizontalDivider()

    SwitchPreferenceItem(context.getString(R.string.display_in_safe_area),
        checkedFlow = PreferencesStorage.getDisplayInSafeAreaValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setDisplayInSafeAreaValue(context, newValue)
        }
    }

    HorizontalDivider()

    SwitchPreferenceItem(context.getString(R.string.preserve_aspect_ratio),
        checkedFlow = PreferencesStorage.getPreserveAspectRatioValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setPreserveAspectRationValue(context, newValue)
        }
    }

    HorizontalDivider()

    SwitchPreferenceItem(context.getString(R.string.show_custom_mouse_cursor),
        checkedFlow = PreferencesStorage.getShowCustomMouseCursorValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setShowCustomMouseCursorValue(context, newValue)
        }
    }

    HorizontalDivider()
}

@Composable
private fun DrawSettingsDivider () = HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))


