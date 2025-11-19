package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.min

abstract class SDLOnScreenGamepad(engineType: EngineTypes,
                                  private val stickId : Int = 0,
                                  private val offsetXPercent: Float = 0f,
                                  private val offsetYPercent: Float = 0f,
                                  private val sizePercent: Float = 0.13f,
                                  private val alpha: Float = 0.65f) : IScreenControlsView {

    private val axisX = stickId * 2
    private val axisY = stickId * 2 + 1

    override val buttonState: ButtonState = ButtonState(
        GAMEPAD_ID,
        engineType,
        offsetXPercent = offsetXPercent,
        offsetYPercent = offsetYPercent,
        sizePercent = sizePercent,
        alpha = alpha
    )

    override var show: Boolean by mutableStateOf(true)

    override val enabled: Boolean = true

    override val isQuickPanel: Boolean = false

    @Composable
    override fun DrawView(isEditMode: Boolean, inGame: Boolean, size: Dp) = DrawGamepad(isEditMode, inGame)

    @Composable
    private fun DrawGamepad(isEditMode: Boolean, inGame: Boolean) {
        var registered by remember(false) { mutableStateOf(false) }

        if (!registered && !isEditMode && inGame) {
            registered = true

            val result = nativeAddJoystick(
                DEVICE_ID, "Virtual", "Virtual",
                0x045E, 0x028E, false,
                0xFFFF, 4, 0b1111, 0, 0)
            Log.d("SDL_INIT", "Joystick registration result: $result")
            if (result < 0) {
                Log.e("SDL_INIT", "Failed to register joystick, result: $result")
            }
        }

        fun updateStick(x: Float, y: Float) {
            if (isEditMode || !inGame){
                return
            }

            val processedX = when {
                abs(x) < GAMEPAD_DEAD_ZONE -> 0f
                x > 0 -> (x * GAMEPAD_SCALE).coerceAtMost(1f)
                else -> (x * GAMEPAD_SCALE).coerceAtLeast(-1f)
            }
            val processedY = when {
                abs(y) < GAMEPAD_DEAD_ZONE -> 0f
                y > 0 -> (y * GAMEPAD_SCALE).coerceAtMost(1f)
                else -> (y * GAMEPAD_SCALE).coerceAtLeast(-1f)
            }

            try {
                onNativeJoy(DEVICE_ID, axisX, processedX)
                onNativeJoy(DEVICE_ID, axisY, processedY)
            } catch (e: Exception) {
                Log.e("SDL_INPUT", "Failed to send to axis", e)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Joystick(
                isEditMode, inGame,
                onUpdateStick = ::updateStick
            )
        }
    }

    @Composable
    private fun Joystick(
        isEditMode: Boolean,
        inGame: Boolean,
        onUpdateStick: (Float, Float) -> Unit
    ) {
        var currentX by remember { mutableFloatStateOf(-1f) }
        var currentY by remember { mutableFloatStateOf(-1f) }
        var down by remember { mutableStateOf(false) }

        var canvasW by remember { mutableIntStateOf(0) }
        var canvasH by remember { mutableIntStateOf(0) }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    clip = false
                    compositingStrategy = CompositingStrategy.ModulateAlpha
                }
                .minimumInteractiveComponentSize()
                .onSizeChanged { size ->
                    canvasW = size.width
                    canvasH = size.height
                }
                .pointerInput(!isEditMode && inGame) {
                    if (isEditMode || !inGame) {
                        return@pointerInput
                    }
                    detectDragGestures(
                        onDragStart = { offset ->
                            down = true
                            currentX = offset.x
                            currentY = offset.y
                        },
                        onDrag = { change, _ ->
                            currentX = change.position.x
                            currentY = change.position.y
                            val strokeWidthPx = 2.dp.toPx()
                            onDrag(
                                canvasW,
                                canvasH,
                                strokeWidthPx,
                                currentX,
                                currentY,
                                onUpdateStick
                            )
                        },
                        onDragEnd = {
                            down = false
                            currentX = -1f
                            currentY = -1f
                            onUpdateStick(0f, 0f)
                        },
                        onDragCancel = {
                            down = false
                            currentX = -1f
                            currentY = -1f
                            onUpdateStick( 0f, 0f)
                        }
                    )
                }
        ) {
            val w = canvasW.toFloat().takeIf { it > 0f } ?: size.width
            val h = canvasH.toFloat().takeIf { it > 0f } ?: size.height
            val minDim = min(w, h)
            val strokeWidthPx = 2.dp.toPx()
            val paint = Paint().apply {
                style = PaintingStyle.Stroke
                strokeWidth = strokeWidthPx
            }

            val outerRadius = minDim / 2f - strokeWidthPx
            val knobRadius = minDim / 5f
            val allowedRadius = outerRadius - knobRadius
            val overshoot = knobRadius * 0.3f
            val maxAllowed = allowedRadius + overshoot

            val centerX = w / 2f
            val centerY = h / 2f

            drawCircle(
                color = Color.Gray,
                radius = outerRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = paint.strokeWidth)
            )

            if (down) {
                var vx = currentX - centerX
                var vy = currentY - centerY
                val dist = hypot(vx, vy)

                if (dist > maxAllowed && dist > 0f) {
                    val s = maxAllowed / dist
                    vx *= s
                    vy *= s
                }

                var drawX = centerX + vx
                var drawY = centerY + vy

                drawX = drawX.coerceIn(knobRadius, w - knobRadius)
                drawY = drawY.coerceIn(knobRadius, h - knobRadius)

                drawCircle(
                    color = Color.Gray,
                    radius = knobRadius,
                    center = Offset(drawX, drawY),
                    style = Stroke(width = paint.strokeWidth)
                )
            }
        }
    }

    protected abstract fun nativeAddJoystick(device_id: Int, name: String?, desc: String?,
                                             vendor_id: Int, product_id: Int,
                                             is_accelerometer: Boolean, button_mask: Int,
                                             naxes: Int, axis_mask: Int, nhats: Int, nballs: Int) : Int

    protected abstract fun onNativeJoy(device_id: Int, axis: Int, value: Float)

    private fun onDrag(
        canvasW: Int,
        canvasH: Int,
        strokeWidthPx: Float,
        currentX: Float,
        currentY: Float,
        onUpdateStick: (Float, Float) -> Unit
    ) {
        val w = canvasW.toFloat().takeIf { it > 0f } ?: return
        val h = canvasH.toFloat().takeIf { it > 0f } ?: return
        val minDim = min(w, h)

        val outerRadius = minDim / 2f - strokeWidthPx
        val knobRadius = minDim / 5f
        val allowedRadius = outerRadius - knobRadius
        val overshoot = knobRadius * 0.3f
        val maxAllowed = allowedRadius + overshoot

        val centerX = w / 2f
        val centerY = h / 2f

        var vx = currentX - centerX
        var vy = currentY - centerY
        val dist = hypot(vx, vy)

        if (dist > maxAllowed && dist > 0f) {
            val s = maxAllowed / dist
            vx *= s
            vy *= s
        }

        val drawX = (centerX + vx).coerceIn(knobRadius, w - knobRadius)
        val drawY = (centerY + vy).coerceIn(knobRadius, h - knobRadius)

        val normX = ((drawX - centerX) / (allowedRadius.coerceAtLeast(1f))).coerceIn(-1f, 1f)
        val normY = ((drawY - centerY) / (allowedRadius.coerceAtLeast(1f))).coerceIn(-1f, 1f)

        onUpdateStick(normX, normY)
    }

    private companion object{
        private const val DEVICE_ID = 1384510555
        private const val GAMEPAD_ID = "onscreen_gamepad"
        private const val GAMEPAD_DEAD_ZONE = 0.05f
        private const val GAMEPAD_SCALE = 1.0f
    }
}