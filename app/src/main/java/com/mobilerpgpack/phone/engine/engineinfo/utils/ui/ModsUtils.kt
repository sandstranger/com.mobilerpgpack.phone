package com.mobilerpgpack.phone.engine.engineinfo.utils.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.ui.items.SwitchItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun DrawModsSupport(mods: ModsModel) {
    HorizontalDivider()

    SwitchItem(stringResource(R.string.enable_separate_mods_support),
        mods.enableModsSupport.value!!) {
        mods.enableModsSupport.value = it
        mods.save()
    }

    HorizontalDivider()

    if (mods.enableModsSupport.value!!) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(0.65f)) {
                RequestPath(
                    stringResource(R.string.path_to_mods_folder),
                    mods.pathToModsFolder.value!!) {
                    mods.pathToModsFolder.value = it
                }
            }

            if (!mods.pathToModsFolder.value.isNullOrEmpty()) {
                Button(modifier = Modifier.weight(0.35f).height(40.dp), onClick = {
                    mods.pathToModsFolder.value = ""
                    mods.save()
                }) {
                    Text(
                        text = stringResource(R.string.clear),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        HorizontalDivider()

        SwitchItem(stringResource(R.string.enable_mods_autoupdate),mods.enableModsAutoUpdateInFolder){
            mods.enableModsAutoUpdateInFolder = it
        }

        HorizontalDivider()

        EditTextItem(
            stringResource(R.string.uzdoom_mods_count),
            mods.modsCount
        ) {
            mods.modsCount = it
        }

        HorizontalDivider()

        if (mods.modsCount > 0) {
            DrawModsLazyColumn(mods)
            HorizontalDivider()
        }
    }
}

@Composable
private fun DrawModsLazyColumn(mods: ModsModel){
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        mods.mods.apply {
            add(to.index, removeAt(from.index))
        }
        mods.updateComposeModsList()
        mods.save()
    }

    LazyColumn(modifier = Modifier.heightIn(max = 300.dp)
            .padding(top = 2.dp, bottom = 2.dp),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        itemsIndexed(mods.modsComposeCollection, key = { _, mod -> mod.key }) { _, mod ->
            ReorderableItem(reorderableLazyListState, key = mod.key) {
                Column {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.weight(0.88f)) {
                            RequestPath(stringResource(R.string.path_to_mod),
                                mod.pathToMod.value ?: "",
                                requestMode = RequestPathMode.File,
                                requiredFileExtensions = mods.allowedModsExtensions
                            ) {
                                mod.pathToMod.value = it
                                mods.save()
                            }
                        }

                        Column {
                            if (!mod.pathToMod.value.isNullOrEmpty()) {
                                Button(onClick = {
                                    mod.pathToMod.value = ""
                                    mods.save() }) {
                                    Text(
                                        text = stringResource(R.string.clear),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }

                            Button(onClick = {
                                mods.mods -=mod
                                --mods.modsCount
                                mods.updateComposeModsList()
                                mods.save() }) {
                                Text(
                                    text = stringResource(R.string.delete),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }

                        Icon(modifier = Modifier
                            .draggableHandle()
                            .weight(0.12f),
                            imageVector = Icons.Default.DragHandle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}