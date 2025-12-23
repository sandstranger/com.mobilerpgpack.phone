package com.mobilerpgpack.phone.engine.engineinfo.utils.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.ui.getButtonsColors
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.ui.getOnPrimaryColor
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.ui.items.SwitchItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun DrawModsSupport(mods: ModsModel) {
    val buttonsColors = getButtonsColors()
    val onPrimaryColor = getOnPrimaryColor()
    mods.apply {
        DrawHorizontalDivider()

        val enableModsSupport by rememberSaveable(this.enableModsSupport.value) {
            mutableStateOf(enableModsSupport.value!!) }

        SwitchItem(
            stringResource(R.string.enable_separate_mods_support),
            enableModsSupport
        ) {
            this@apply.enableModsSupport.value = it
            save()
        }
        DrawHorizontalDivider()

        if (enableModsSupport) {
            Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Row (modifier = Modifier.weight(0.8f)) {
                    RequestPath(
                        stringResource(R.string.path_to_mods_folder),
                        pathToModsFolder.value!!
                    ) {
                        pathToModsFolder.value = it
                    }
                }

                val pathToModsFolderSaved by remember (pathToModsFolder.value) {
                    mutableStateOf(pathToModsFolder.value) }

                if (!pathToModsFolderSaved.isNullOrEmpty()) {
                    Button( modifier = Modifier.padding(end = 4.dp), onClick = {
                        pathToModsFolder.value = ""
                        save()
                    }, colors = buttonsColors
                    ) {
                        Text(
                            text = stringResource(R.string.clear),
                            textAlign = TextAlign.Center,
                            color = onPrimaryColor
                        )
                    }
                }
            }

            DrawHorizontalDivider()

            SwitchItem(
                stringResource(R.string.enable_mods_autoupdate),
                enableModsAutoUpdateInFolder
            ) {
                enableModsAutoUpdateInFolder = it
            }

            DrawHorizontalDivider()

            EditTextItem(
                stringResource(R.string.uzdoom_mods_count),
                modsCount
            ) {
                modsCount = it.coerceAtLeast(0)
            }

            DrawHorizontalDivider()

            val modsCount by remember (modsCount) { mutableStateOf(modsCount) }

            if (modsCount > 0) {
                DrawModsLazyColumn(mods)
                DrawHorizontalDivider()
            }
        }
    }
}

@Composable
private fun DrawModsLazyColumn(mods: ModsModel){
    val lazyListState = rememberLazyListState()
    val buttonsColors = getButtonsColors()
    val onPrimaryColor = getOnPrimaryColor()
    val onBackgroundColor = getOnBackgroundColor()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        mods.apply {
            this.mods.apply {
                add(to.index, removeAt(from.index))
            }
            updateComposeModsList()
            save()
        }
    }

    mods.apply {
        val modsCollection by remember(modsComposeCollection) {
            mutableStateOf(modsComposeCollection)
        }

        LazyColumn(
            modifier = Modifier
                .heightIn(max = 300.dp)
                .padding(top = 2.dp, bottom = 2.dp),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            itemsIndexed(modsCollection, key = { _, mod -> mod.key }) { _, mod ->
                ReorderableItem(reorderableLazyListState, key = mod.key) {
                    val mod by remember (mod) { mutableStateOf(mod) }
                    Column {
                        DrawHorizontalDivider()
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.weight(0.88f)) {
                                RequestPath(
                                    stringResource(R.string.path_to_mod),
                                    mod.pathToMod.value ?: "",
                                    requestMode = RequestPathMode.File,
                                    requiredFileExtensions = allowedModsExtensions
                                ) {
                                    mod.pathToMod.value = it
                                    save()
                                }
                            }
                            val pathToMod by remember (mod.pathToMod.value) {
                                mutableStateOf(mod.pathToMod.value) }

                            Column {
                                if (!pathToMod.isNullOrEmpty()) {
                                    Button(onClick = {
                                        mod.pathToMod.value = ""
                                        save()
                                    }, colors = buttonsColors) {
                                        Text(
                                            text = stringResource(R.string.clear),
                                            textAlign = TextAlign.Center,
                                            color = onPrimaryColor
                                        )
                                    }
                                }

                                Button(onClick = {
                                    this@apply.mods -= mod
                                    --modsCount
                                    updateComposeModsList()
                                    save()
                                }, colors = buttonsColors) {
                                    Text(
                                        text = stringResource(R.string.delete),
                                        textAlign = TextAlign.Center,
                                        color = onPrimaryColor
                                    )
                                }
                            }

                            Icon(
                                modifier = Modifier
                                    .draggableHandle()
                                    .weight(0.12f),
                                imageVector = Icons.Default.DragHandle,
                                contentDescription = null,
                                tint = onBackgroundColor,
                            )
                        }
                        DrawHorizontalDivider()
                    }
                }
            }
        }
    }
}