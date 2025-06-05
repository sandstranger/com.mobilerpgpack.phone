package com.mobilerpgpack.phone.ui.screen

import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun KeyEventEditDialog(
    buttonStates: Collection<ButtonState>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(buttonStates) {
        scope.launch {
            buttonStates.forEach { it.loadButtonState(context) }
        }
    }

    var selectedButton by rememberSaveable { mutableStateOf(buttonStates.first()) }
    var selectedKeyCode by rememberSaveable { mutableIntStateOf(selectedButton.sdlKeyEvent) }

    var shouldSave by rememberSaveable { mutableStateOf(false) }
    var shouldReset by rememberSaveable { mutableStateOf(false) }

    var showButtonSelectDialog by rememberSaveable { mutableStateOf(false) }
    var showKeyCodeDialog by rememberSaveable { mutableStateOf(false) }

    val currentButton = rememberUpdatedState(selectedButton)

    val keyCodeMap: Map<Int, String> = remember {
        KeyEvent::class.java.fields
            .filter { it.name.startsWith("KEYCODE_") }
            .sortedBy { it.name }
            .associate { field ->
                field.getInt(null) to field.name
            }
    }

    if (shouldSave) {
        LaunchedEffect(buttonStates) {
            scope.launch {
                buttonStates.forEach { it.saveButtonState(context) }
            }
            shouldSave = false
        }
    }

    if (shouldReset) {
        LaunchedEffect(buttonStates) {
            scope.launch {
                buttonStates.forEach { it.resetKeyEvent(context) }
                selectedKeyCode = currentButton.value.sdlKeyEvent
            }
            selectedKeyCode = currentButton.value.sdlKeyEvent
            shouldReset = false
        }
    }

    if (showButtonSelectDialog) {
        val scrollState = rememberScrollState()

        AlertDialog(
            onDismissRequest = { showButtonSelectDialog = false },
            confirmButton = {
                TextButton(onClick = { showButtonSelectDialog = false }) {
                    Text("Close")
                }
            },
            title = { Text("Select Button") },
            text = {
                Column (modifier = Modifier.verticalScroll(scrollState)){
                    buttonStates.forEach { button ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedButton = button
                                    selectedKeyCode = button.sdlKeyEvent
                                    showButtonSelectDialog = false
                                }
                                .padding(8.dp)
                        ) {
                            if (button.buttonResId != 0) {
                                Image(
                                    painter = painterResource(id = button.buttonResId),
                                    contentDescription = button.id,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(button.id)
                        }
                    }
                }
            }
        )
    }

    if (showKeyCodeDialog) {
        val scrollState = rememberScrollState()

        AlertDialog(
            onDismissRequest = { showKeyCodeDialog = false },
            confirmButton = {
                TextButton(onClick = { showKeyCodeDialog = false }) {
                    Text("Close")
                }
            },
            title = { Text("Select Key Code") },
            text = {
                Column(modifier = Modifier.heightIn(max = 400.dp).verticalScroll(scrollState)) {
                    keyCodeMap.forEach { (code, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedKeyCode = code
                                    currentButton.value.sdlKeyEvent = selectedKeyCode
                                    showKeyCodeDialog = false
                                }
                                .padding(8.dp)
                        ) {
                            Text(name)
                        }
                    }
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                shouldSave = true
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                shouldReset = true
            }) {
                Text("Reset to default")
            }
        },
        title = { Text("Edit SDL Key Event") },
        text = {
            Column(modifier = modifier.fillMaxWidth()) {

                Text("Selected Button", style = MaterialTheme.typography.labelMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { showButtonSelectDialog = true }
                        .padding(8.dp)
                ) {
                    if (selectedButton.buttonResId != 0) {
                        Image(
                            painter = painterResource(id = selectedButton.buttonResId),
                            contentDescription = selectedButton.id,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(selectedButton.id)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Selected Key Code", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier
                        .clickable { showKeyCodeDialog = true }
                        .padding(8.dp)
                ) {
                    Text(text = keyCodeMap[selectedKeyCode] ?: "Unknown")
                }
            }
        }
    )
}