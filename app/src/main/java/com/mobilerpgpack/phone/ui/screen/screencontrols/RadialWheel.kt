package com.mobilerpgpack.phone.ui.screen.screencontrols

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobilerpgpack.phone.ui.screen.screencontrols.utils.onTouchDown
import com.mobilerpgpack.phone.utils.IKeyCodesProvider
import org.koin.compose.koinInject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val ITEMS_COUNT = 10

@Composable
fun RadialWheel(modifier: Modifier = Modifier,
                getViewSize : @Composable (Float) -> Dp,
                onItemSelected: (Int) -> Unit) {
    val count = rememberSaveable { ITEMS_COUNT }
    val items = rememberSaveable { (0..count).map { it.toString() }.toList() }
    val anglePerItem = rememberSaveable { 360f / count }
    val textSizePx = with(LocalDensity.current) { 30.sp.toPx() }
    val backgroundColor = remember { Color.Gray.copy(alpha = 0.4f) }
    val selectedColor = remember { Color.LightGray.copy(0.8f) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var showRadialMenu by remember { mutableStateOf(false) }
    val keyCodesProvider = koinInject<IKeyCodesProvider>()

    Box(modifier = modifier
            .aspectRatio(1f)
            .minimumInteractiveComponentSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val down = awaitFirstDown()
                        if (showRadialMenu) {
                            val center = Offset(size.width / 2f, size.height / 2f)

                            fun angle(pos: Offset): Float {
                                val dx = pos.x - center.x
                                val dy = pos.y - center.y
                                var a =
                                    Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                                a += 90f
                                if (a < 0f) a += 360f
                                return a
                            }

                            fun indexFromAngle(a: Float): Int =
                                (a / anglePerItem).toInt().coerceIn(0, count - 1)

                            selectedIndex = indexFromAngle(angle(down.position))

                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.first()

                                if (!change.pressed) {
                                    if (selectedIndex >= 0) {
                                        onItemSelected(keyCodesProvider.getKeyCode(
                                            items[selectedIndex].first()))
                                        showRadialMenu = false
                                    }
                                    break
                                }

                                val idx = indexFromAngle(angle(change.position))
                                if (idx != selectedIndex) {
                                    selectedIndex = idx
                                }

                                change.consume()
                            }
                        }
                    }
                }
            }
    ) {
        if (showRadialMenu) {
            Canvas(Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2f
                val center = Offset(size.width / 2f, size.height / 2f)

                for (i in 0 until count) {
                    val start = -90f + i * anglePerItem

                    drawArc(
                        color = if (i == selectedIndex) selectedColor else backgroundColor,
                        startAngle = start,
                        sweepAngle = anglePerItem,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                    )

                    val mid = Math.toRadians((start + anglePerItem / 2).toDouble())
                    val rText = radius * 0.65f
                    val tx = center.x + cos(mid).toFloat() * rText
                    val ty = center.y + sin(mid).toFloat() * rText

                    drawIntoCanvas {
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = textSizePx
                            isAntiAlias = true
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                        it.nativeCanvas.drawText(
                            items[i],
                            tx,
                            ty + textSizePx / 3f,
                            paint
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier
                    .align(Alignment.Center)
                    .size(getViewSize(0.09f))
                    .minimumInteractiveComponentSize()
                    .onTouchDown(false, ignoreConsuming = true) {
                        showRadialMenu = true
                    })
        }
    }
}