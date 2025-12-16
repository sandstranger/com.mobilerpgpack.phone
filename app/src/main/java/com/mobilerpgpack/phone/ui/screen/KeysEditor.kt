package com.mobilerpgpack.phone.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.ui.getOnSurfaceVariantColor
import com.mobilerpgpack.phone.ui.getPrimaryColor
import com.mobilerpgpack.phone.ui.getSurfaceContainerHighColor
import com.mobilerpgpack.phone.ui.getTextButtonsColors
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.allowToEditKeyEvent
import com.mobilerpgpack.phone.utils.keyCodeMap
import kotlinx.coroutines.launch

@Composable
fun KeysEditor(
    buttonStates: Collection<IScreenControlsView>,
    onDismiss: () -> Unit,
) {
    val modifier = Modifier
    val scope = rememberCoroutineScope()
    val buttonsToEdit = buttonStates.filter { it.allowToEditKeyEvent }
    val onSurfaceVariantColor = getOnSurfaceVariantColor()
    val onSurfaceColor = getOnSurfaceColor()
    val primaryColor = getPrimaryColor()
    val surfaceContainerHighColor = getSurfaceContainerHighColor()

    LaunchedEffect(buttonsToEdit) {
        scope.launch {
            buttonsToEdit.forEach { it.viewState.load() }
        }
    }

    var selectedButton by remember { mutableStateOf(buttonsToEdit.first()) }
    var selectedButtonId by rememberSaveable { mutableStateOf(selectedButton.viewState.id) }
    var selectedKeyCode by rememberSaveable { mutableIntStateOf(selectedButton.viewState.sdlKeyCode) }
    selectedButton = buttonsToEdit.first { it.viewState.id == selectedButtonId }

    var shouldReset by rememberSaveable { mutableStateOf(false) }
    var showButtonSelectDialog by rememberSaveable { mutableStateOf(false) }
    var showKeyCodeDialog by rememberSaveable { mutableStateOf(false) }

    val currentButton = rememberUpdatedState(selectedButton)
    val keyCodeMap = remember { keyCodeMap }
    val keyCodesToDraw by rememberSaveable { mutableStateOf(keyCodeMap.toList()) }

    if (shouldReset) {
        LaunchedEffect(buttonsToEdit) {
            buttonsToEdit.forEach { it.viewState.resetKeyEvent() }
            selectedKeyCode = currentButton.value.viewState.sdlKeyCode
            shouldReset = false
        }
    }

    if (showButtonSelectDialog) {
        AlertDialog(
            onDismissRequest = { showButtonSelectDialog = false },
            confirmButton = {
                TextButton(onClick = { showButtonSelectDialog = false }) {
                    Text(stringResource(R.string.close_text))
                }
            },
            containerColor = surfaceContainerHighColor,
            textContentColor = onSurfaceVariantColor,
            iconContentColor = onSurfaceVariantColor,
            titleContentColor = onSurfaceColor,
            title = { Text(stringResource(R.string.select_button), color = onSurfaceColor) },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp))
                {
                    itemsIndexed(
                        buttonsToEdit,
                        key = { _, button -> button.viewState.id }) { _, button ->
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedButton = button
                                    selectedButtonId = button.viewState.id
                                    selectedKeyCode = button.viewState.sdlKeyCode
                                    showButtonSelectDialog = false
                                }, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (button.viewState.buttonResId != 0) {
                                Image(
                                    painter = painterResource(id = button.viewState.buttonResId),
                                    contentDescription = button.viewState.id,
                                    modifier = Modifier.size(32.dp),
                                    colorFilter = ColorFilter.tint(onSurfaceVariantColor)
                                )
                            }
                            Text(button.viewState.id, color = onSurfaceVariantColor)
                        }
                    }
                }
            }
        )
    }

    if (showKeyCodeDialog) {
        AlertDialog(
            onDismissRequest = { showKeyCodeDialog = false },
            confirmButton = {
                TextButton(onClick = { showKeyCodeDialog = false }, colors = getTextButtonsColors()) {
                    Text(stringResource(R.string.close_text), color = primaryColor)
                }
            },
            containerColor = surfaceContainerHighColor,
            textContentColor = onSurfaceVariantColor,
            iconContentColor = onSurfaceVariantColor,
            titleContentColor = onSurfaceColor,
            title = { Text(stringResource(R.string.select_key_code), color = onSurfaceColor) },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp))
                {
                    itemsIndexed(keyCodesToDraw, key = { _, pair -> pair.second.keyCode }) { _, pair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedKeyCode = pair.first
                                    currentButton.value.viewState.apply {
                                        sdlKeyCode = selectedKeyCode
                                        save()
                                    }
                                    showKeyCodeDialog = false
                                }) {
                            Text(pair.second.keyCodeName, color = onSurfaceVariantColor)
                        }
                     }
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onDismiss() }, colors = getTextButtonsColors()) {
                Text(stringResource(R.string.close_text), color = primaryColor)
            }
        },
        containerColor = surfaceContainerHighColor,
        textContentColor = onSurfaceVariantColor,
        iconContentColor = onSurfaceVariantColor,
        titleContentColor = onSurfaceColor,
        dismissButton = {
            TextButton(onClick = { shouldReset = true }, colors = getTextButtonsColors() ) {
                Text(stringResource(R.string.reset_to_default), color = primaryColor)
            }
        },
        title = { Text(stringResource(R.string.keys_editor), color = onSurfaceColor) },
        text = {
            Column(modifier = modifier.heightIn(max = 400.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(3.dp,Alignment.CenterVertically)) {
                Text(stringResource(R.string.select_button), style = MaterialTheme.typography.labelMedium, color = onSurfaceVariantColor)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showButtonSelectDialog = true },
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (selectedButton.viewState.buttonResId != 0) {
                        Image(
                            painter = painterResource(id = selectedButton.viewState.buttonResId),
                            contentDescription = selectedButton.viewState.id,
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(onSurfaceVariantColor)
                        )
                    }
                    Text(selectedButton.viewState.id, color = onSurfaceVariantColor)
                }

                Text(stringResource(R.string.selected_key_code), style = MaterialTheme.typography.labelMedium,
                    color = onSurfaceVariantColor)

                Text(modifier = Modifier.fillMaxWidth().clickable { showKeyCodeDialog = true }, color = onSurfaceVariantColor,
                    text = keyCodeMap[selectedKeyCode]?.keyCodeName ?: stringResource(R.string.uknown), textAlign = TextAlign.Left)
            }
        }
    )
}