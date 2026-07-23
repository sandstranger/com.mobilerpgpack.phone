package com.mobilerpgpack.phone.ui.items

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

fun Modifier.safeAlpha(alpha: Float): Modifier = this.drawWithContent {
    drawIntoCanvas { canvas ->
        canvas.saveLayer(
            Rect(0f, 0f, size.width, size.height),
            Paint().apply { this.alpha = alpha }
        )
        drawContent()
        canvas.restore()
    }
}