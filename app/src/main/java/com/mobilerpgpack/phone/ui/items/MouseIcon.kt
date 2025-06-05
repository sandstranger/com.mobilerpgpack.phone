package com.mobilerpgpack.phone.ui.items

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.delay
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface
import kotlin.math.roundToInt

@Composable
fun MouseIcon() {
    val context = LocalContext.current
    var iconOffset by remember { mutableStateOf(IntOffset.Zero) }
    val offsetXMouse by PreferencesStorage.getOffsetXMouse(context).collectAsState(initial = 0f)
    val offsetYMouse by PreferencesStorage.getOffsetYMouse(context).collectAsState(initial = 0f)
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels
    val sdlWidth = SDLSurface.fixedWidth
    val sdlHeight = SDLSurface.fixedHeight

    LaunchedEffect(Unit) {
        while (true) {
            val x = (SDLActivity.getMouseX().toFloat() + (offsetXMouse ?: 0f)) * (screenWidth / sdlWidth)
            val y = (SDLActivity.getMouseY().toFloat() + (offsetYMouse ?: 0f)) * (screenHeight / sdlHeight)
            iconOffset = IntOffset(x.roundToInt(), y.roundToInt())
            delay(16L)
        }
    }

    Box {
        Image(
            painter = painterResource(id = R.drawable.pointer_arrow),
            contentDescription = "Pointer Icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .offset { iconOffset }
                .size(32.dp)
        )
    }
}

