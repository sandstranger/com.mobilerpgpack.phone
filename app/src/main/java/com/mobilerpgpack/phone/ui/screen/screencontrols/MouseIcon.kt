package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import kotlin.math.roundToInt

abstract class MouseIcon {

    protected abstract val fixedWidth: Int

    protected abstract val fixedHeight: Int

    @SuppressLint("NotConstructor")
    @Composable
    fun DrawMouseIcon() {
        val preferencesStorage = koinInject<PreferencesStorage>()
        var iconOffset by remember { mutableStateOf(IntOffset.Zero) }
        val offsetXMouse by preferencesStorage.offsetXMouse.collectAsState(initial = 0f)
        val offsetYMouse by preferencesStorage.offsetYMouse.collectAsState(initial = 0f)
        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val sdlWidth = fixedWidth
        val sdlHeight = fixedHeight

        LaunchedEffect(Unit) {
            while (true) {
                if (sdlWidth > 0 && sdlHeight > 0) {
                    val x = (getMouseX() + offsetXMouse) * (screenWidth / sdlWidth)
                    val y = (getMouseY() + offsetYMouse) * (screenHeight / sdlHeight)
                    iconOffset = IntOffset(x.roundToInt(), y.roundToInt())
                } else {
                    val x = (getMouseX() + offsetXMouse)
                    val y = (getMouseY() + offsetYMouse)
                    iconOffset = IntOffset(x.roundToInt(), y.roundToInt())
                }

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

    protected abstract fun getMouseX(): Float

    protected abstract fun getMouseY(): Float
}