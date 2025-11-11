package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.view.KeyEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.libsdl.app.SDLActivity
import kotlin.random.Random


private fun pressed(keyCode: Int) {
    SDLActivity.onNativeKeyDown(keyCode)
}

private fun released(keyCode: Int) {
    SDLActivity.onNativeKeyUp(keyCode)
}

@Composable
fun DrawDoomRpgSeriesKeyboard() {
    Column(
        modifier = Modifier
            .width(700.dp)
    ) {
        // Define the colors for the RGB effect
        val colors = listOf(
            Color.Red, Color.Green, Color.Blue
        )

        // Animation state
        var colorIndex by remember { mutableIntStateOf(0) }
        var nextColorIndex by remember { mutableIntStateOf(1) }
        var fraction by remember { mutableFloatStateOf(0f) }

        // Launch a coroutine to gradually change the color fraction
        LaunchedEffect(Unit) {
            while (true) {
                delay(20)  // Adjust delay for smoother transition
                fraction += 0.01f
                if (fraction >= 1f) {
                    fraction = 0f
                    colorIndex = nextColorIndex
                    nextColorIndex = (nextColorIndex + 1) % colors.size
                }
            }
        }

        // Custom lerp function for Color
        fun customLerp(start: Color, end: Color, fraction: Float): Color {
            return lerp(start, end, fraction.coerceIn(0f, 1f))
        }

        // Function to get the current color based on fraction
        fun getCurrentColor(offset: Float): Color {
            return customLerp(colors[colorIndex], colors[nextColorIndex], fraction + offset)
        }

        // Function to create rows of boxes with sizes
        @Composable
        fun createRow(chars: List<String>, sizes: List<Modifier>) {
            Row {
                chars.zip(sizes).forEach { (char, sizeModifier) ->
                    val offset = Random.nextFloat() * 0.5f  // Random offset for wave effect
                    val animatedColor by animateColorAsState(
                        targetValue = getCurrentColor(offset)
                    )
                    Box(
                        modifier = sizeModifier.offset(y = 68.dp)
                            .background(Color.Black)  // Set the background to black
                            .border(2.dp, animatedColor)  // Set the border to the animated RGB color
                            .minimumInteractiveComponentSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        keyMap[char]?.let { key ->
                                            pressed(key.keyCode)
                                            try {
                                                awaitRelease()
                                                released(key.keyCode)
                                            } catch (_: Exception) {
                                                released(key.keyCode)
                                            }
                                        }
                                    }
                                )
                            }
                    ) {
                        Text(
                            text = char,
                            color = animatedColor,  // Set the text color to the animated RGB color
                            fontSize = 18.sp,  // Set the text size
                            fontWeight = FontWeight.Bold,  // Set the text style to bold
                            modifier = Modifier.align(Alignment.Center)  // Center the text in the box
                        )
                    }
                }
            }
        }



        val topRowChars = listOf( "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
        val topRowSizes = List(10) {
            Modifier
                .padding(all = 2.dp)
                .size(60.dp)
        } + listOf(
            Modifier
                .padding(all = 2.dp)
                .height(60.dp)
                .weight(1f)
        )
        createRow(topRowChars, topRowSizes)
    }
}

