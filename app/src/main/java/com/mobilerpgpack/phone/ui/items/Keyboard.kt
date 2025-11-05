package com.mobilerpgpack.phone.ui.items

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

private data class Key(val label: String, val keyCode: Int)

// Define the keys and their corresponding key codes
private val keyMap = mapOf(
    "1" to Key("1", KeyEvent.KEYCODE_1),
    "2" to Key("2", KeyEvent.KEYCODE_2),
    "3" to Key("3", KeyEvent.KEYCODE_3),
    "4" to Key("4", KeyEvent.KEYCODE_4),
    "5" to Key("5", KeyEvent.KEYCODE_5),
    "6" to Key("6", KeyEvent.KEYCODE_6),
    "7" to Key("7", KeyEvent.KEYCODE_7),
    "8" to Key("8", KeyEvent.KEYCODE_8),
    "9" to Key("9", KeyEvent.KEYCODE_9),
    "0" to Key("0", KeyEvent.KEYCODE_0),
    "-" to Key("-", KeyEvent.KEYCODE_MINUS),
    "=" to Key("=", KeyEvent.KEYCODE_EQUALS),
    "Backspace" to Key("Backspace", KeyEvent.KEYCODE_DEL),
    "q" to Key("q", KeyEvent.KEYCODE_Q),
    "w" to Key("w", KeyEvent.KEYCODE_W),
    "e" to Key("e", KeyEvent.KEYCODE_E),
    "r" to Key("r", KeyEvent.KEYCODE_R),
    "t" to Key("t", KeyEvent.KEYCODE_T),
    "y" to Key("y", KeyEvent.KEYCODE_Y),
    "u" to Key("u", KeyEvent.KEYCODE_U),
    "i" to Key("i", KeyEvent.KEYCODE_I),
    "o" to Key("o", KeyEvent.KEYCODE_O),
    "p" to Key("p", KeyEvent.KEYCODE_P),
    "[" to Key("[", KeyEvent.KEYCODE_LEFT_BRACKET),
    "]" to Key("]", KeyEvent.KEYCODE_RIGHT_BRACKET),
    "\\" to Key("\\", KeyEvent.KEYCODE_BACKSLASH),
    "a" to Key("a", KeyEvent.KEYCODE_A),
    "s" to Key("s", KeyEvent.KEYCODE_S),
    "d" to Key("d", KeyEvent.KEYCODE_D),
    "f" to Key("f", KeyEvent.KEYCODE_F),
    "g" to Key("g", KeyEvent.KEYCODE_G),
    "h" to Key("h", KeyEvent.KEYCODE_H),
    "j" to Key("j", KeyEvent.KEYCODE_J),
    "k" to Key("k", KeyEvent.KEYCODE_K),
    "l" to Key("l", KeyEvent.KEYCODE_L),
    ";" to Key(";", KeyEvent.KEYCODE_SEMICOLON),
    "'" to Key("'", KeyEvent.KEYCODE_APOSTROPHE),
    "z" to Key("z", KeyEvent.KEYCODE_Z),
    "x" to Key("x", KeyEvent.KEYCODE_X),
    "c" to Key("c", KeyEvent.KEYCODE_C),
    "v" to Key("v", KeyEvent.KEYCODE_V),
    "b" to Key("b", KeyEvent.KEYCODE_B),
    "n" to Key("n", KeyEvent.KEYCODE_N),
    "m" to Key("m", KeyEvent.KEYCODE_M),
    "," to Key(",", KeyEvent.KEYCODE_COMMA),
    "." to Key(".", KeyEvent.KEYCODE_PERIOD),
    "/" to Key("/", KeyEvent.KEYCODE_SLASH),
    "L-CTRL" to Key("L-CTRL", KeyEvent.KEYCODE_CTRL_LEFT),
    "SPACE" to Key("SPACE", KeyEvent.KEYCODE_SPACE),
    "LEFT" to Key("LEFT", KeyEvent.KEYCODE_DPAD_LEFT),
    "DOWN" to Key("DOWN", KeyEvent.KEYCODE_DPAD_DOWN),
    "RIGHT" to Key("RIGHT", KeyEvent.KEYCODE_DPAD_RIGHT)
)

private fun pressed(keyCode: Int) {
    SDLActivity.onNativeKeyDown(keyCode)
}

private fun released(keyCode: Int) {
    SDLActivity.onNativeKeyUp(keyCode)
}

@Composable
fun BoxGrid2() {
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

