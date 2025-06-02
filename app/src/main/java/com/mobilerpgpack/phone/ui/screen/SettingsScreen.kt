package com.mobilerpgpack.phone.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.startEngine
import com.mobilerpgpack.phone.ui.items.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.PreferenceItem
import com.mobilerpgpack.phone.ui.items.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.isTelevision
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val context = LocalContext.current;
    val scope = rememberCoroutineScope()

    if (context.isTelevision){
        DrawTelevisionSettings(context, scope)
        return
    }

    DrawPhoneSettings(context, scope)
}

@Composable
private fun DrawTelevisionSettings (context: Context,scope: CoroutineScope){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Button(
            onClick = { scope.launch { startEngine(context) } },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(context.getString(R.string.start_game), fontSize = 25.sp)
        }

        DrawAllSettings(context, scope)
    }
}

@Composable
private fun DrawPhoneSettings (context: Context,scope: CoroutineScope){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { scope.launch { startEngine(context) }}
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = context.getString(R.string.start_game))
            }
        }
    ) { innerPadding ->
        DrawAllSettings(context, innerPadding, scope)
    }
}

@Composable
private fun DrawAllSettings (context: Context, scope: CoroutineScope) {
    DrawAllSettings(context, PaddingValues(),scope)
}

@Composable
private fun DrawAllSettings (context: Context, innerPadding: PaddingValues, scope: CoroutineScope){
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        DrawCommonSettings(context, scope)
        DrawWolfensteinRpgSettings(context, scope)
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

    SwitchPreferenceItem(context.getString(R.string.use_custom_file_picker),
        checkedFlow = PreferencesStorage.getUseCustomFilePickerValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setUseCustomFilePickerValue(context, newValue)
        }
    }

    HorizontalDivider()
}

@Composable
private fun DrawWolfensteinRpgSettings (context: Context,scope: CoroutineScope){
    Text(context.getString(R.string.wolfenstein_rpg_settings), style = MaterialTheme.typography.titleLarge)

    PreferenceItem(context.getString(R.string.wolfenstein_rpg_ipa_file))

    HorizontalDivider()

    PreferenceItem(context.getString(R.string.configure_screen_controls))

    HorizontalDivider()

    SwitchPreferenceItem(context.getString(R.string.hide_custom_screen_controls),
        checkedFlow = PreferencesStorage.getHideWolfensteinRpgScreenControlsValue(context),
    ) { newValue ->
        scope.launch {
            PreferencesStorage.setHideWolfensteinRpgScreenControlsValue(context, newValue)
        }
    }
}

