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
import com.mobilerpgpack.phone.R
import kotlinx.coroutines.launch

@Composable
fun KeysEditor(
    buttonStates: Collection<ButtonState>,
    onDismiss: () -> Unit,
) {
    val modifier = Modifier
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val buttonsToEdit = buttonStates.filter { it.allowToEditKeyEvent }

    LaunchedEffect(buttonsToEdit) {
        scope.launch {
            buttonsToEdit.forEach { it.loadButtonState(context) }
        }
    }

    var selectedButton by remember { mutableStateOf(buttonsToEdit.first()) }
    var selectedButtonId by rememberSaveable { mutableStateOf(selectedButton.id) }
    var selectedKeyCode by rememberSaveable { mutableIntStateOf(selectedButton.sdlKeyCode) }
    selectedButton = buttonsToEdit.first { it.id == selectedButtonId }

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

    if (shouldReset) {
        LaunchedEffect(buttonsToEdit) {
            scope.launch {
                buttonsToEdit.forEach { it.resetKeyEvent(context) }
                selectedKeyCode = currentButton.value.sdlKeyCode
            }
            selectedKeyCode = currentButton.value.sdlKeyCode
            shouldReset = false
        }
    }

    if (showButtonSelectDialog) {
        val scrollState = rememberScrollState()

        AlertDialog(
            onDismissRequest = { showButtonSelectDialog = false },
            confirmButton = {
                TextButton(onClick = { showButtonSelectDialog = false }) {
                    Text(context.getString(R.string.close_text))
                }
            },
            title = { Text(context.getString(R.string.select_button)) },
            text = {
                Column (modifier = Modifier.verticalScroll(scrollState)){
                    buttonsToEdit.forEach { button ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedButton = button
                                    selectedButtonId = button.id
                                    selectedKeyCode = button.sdlKeyCode
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
                                    currentButton.value.sdlKeyCode = selectedKeyCode
                                    scope.launch {
                                        currentButton.value.saveButtonState(context)
                                    }
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
                onDismiss()
            }) {
                Text(context.getString(R.string.close_text))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                shouldReset = true
            }) {
                Text(context.getString(R.string.reset_to_default))
            }
        },
        title = { Text(context.getString(R.string.keys_editor)) },
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

                Text(context.getString(R.string.selected_key_code), style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier
                        .clickable { showKeyCodeDialog = true }
                        .padding(8.dp)
                ) {
                    Text(text = keyCodeMap[selectedKeyCode] ?: context.getString(R.string.uknown))
                }
            }
        }
    )
}