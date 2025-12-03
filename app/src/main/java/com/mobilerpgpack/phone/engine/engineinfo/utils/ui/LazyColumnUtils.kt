package com.mobilerpgpack.phone.engine.engineinfo.utils.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ernestoyaquello.dragdropswipelazycolumn.AllowedSwipeDirections
import com.ernestoyaquello.dragdropswipelazycolumn.DragDropSwipeLazyColumn
import com.ernestoyaquello.dragdropswipelazycolumn.DraggableSwipeableItem
import com.ernestoyaquello.dragdropswipelazycolumn.DraggableSwipeableItemScope
import com.ernestoyaquello.dragdropswipelazycolumn.OrderedItem
import com.ernestoyaquello.dragdropswipelazycolumn.config.SwipeableItemShapes
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomModsModel
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode

@Composable
fun DrawModsLazyColumn(uzDoomMods: UZDoomModsModel, onItemsReordered : (List<OrderedItem<Mod>>) -> Unit){
    DragDropSwipeLazyColumn(
        modifier = Modifier
            .height(300.dp)
            .padding(top = 2.dp, bottom = 2.dp),
        items = uzDoomMods.pathToModsComposeCollection,
        key = remember { { it.index } },
        verticalArrangement = Arrangement.spacedBy(5.dp),
        onIndicesChangedViaDragAndDrop = { onItemsReordered(it) }) {
            _, item ->  DrawModItem(uzDoomMods,item)
    }
}

@Composable
private fun DraggableSwipeableItemScope<Mod>.DrawModItem(uzDoomMods: UZDoomModsModel,mod: Mod){
    DraggableSwipeableItem(
        modifier = Modifier.animateDraggableSwipeableItem(),
        shapes = SwipeableItemShapes.createRemembered(MaterialTheme.shapes.medium),
        dragDropEnabled = true,
        allowedSwipeDirections = AllowedSwipeDirections.None,
        clickIndication = null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(0.88f)) {
                HorizontalDivider()
                RequestPath(stringResource(R.string.path_to_mod),
                    mod.pathToMod.value ?: "", requestMode = RequestPathMode.File
                ) {
                    mod.pathToMod.value = it
                    uzDoomMods.save()
                }
                HorizontalDivider()
            }

            Icon(modifier = Modifier
                .dragDropModifier()
                .weight(0.12f),
                imageVector = Icons.Default.DragHandle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}
