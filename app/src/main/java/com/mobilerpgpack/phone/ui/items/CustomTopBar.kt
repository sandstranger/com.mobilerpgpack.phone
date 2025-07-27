import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopBar(title: String, useDarkTheme : Boolean = false ) {
    val topBarColor  = if (useDarkTheme) Color.DarkGray else Color(0xFF6200EE)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(topBarColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}