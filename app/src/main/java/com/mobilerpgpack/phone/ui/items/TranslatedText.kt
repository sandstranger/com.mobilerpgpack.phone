package com.mobilerpgpack.phone.ui.items

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.map

@Composable
fun TranslatedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null
    ) {
    val context = LocalContext.current

    val isModelDownloaded by TranslationManager.isTranslationSupportedAsFlow().collectAsState(initial = false)
    val activeTranslationType by
    PreferencesStorage.getTranslationModelTypeValue(context).collectAsState(initial = TranslationType.DefaultTranslationType.toString())

    val allowTranslate by PreferencesStorage
        .getEnableLauncherTextTranslationValue(context)
        .map { it }
        .collectAsState(initial = false)

    var displayText by remember { mutableStateOf(text) }

    LaunchedEffect(allowTranslate, text, isModelDownloaded, activeTranslationType) {
        displayText = text

        if (!allowTranslate || !isModelDownloaded || text.isBlank()) {
            return@LaunchedEffect
        }

        displayText = try {
                TranslationManager.translateAsync(text)
        } catch (_: Exception) {
            text
        }
    }

    Text(text = displayText, modifier = modifier,
        style = style, fontSize = fontSize, maxLines = maxLines,
        overflow = overflow, color = color, textAlign = textAlign)
}