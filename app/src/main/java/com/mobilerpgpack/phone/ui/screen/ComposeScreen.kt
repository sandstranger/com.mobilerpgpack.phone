package com.mobilerpgpack.phone.ui.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.getBackgroundColor
import com.mobilerpgpack.phone.ui.getTextColor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

abstract class ComposeScreen(val route: String) : KoinComponent {

    protected open val drawFloatingActionButton = false

    protected var onFloatingActionButtonClickedDelegate: (() -> Unit)? = null

    @Composable
    fun DrawScreen(navController: NavHostController) {
        val activity = LocalActivity.current!!
        val preferencesStorage: PreferencesStorage = get()
        val isSystemInDarkTheme = isSystemInDarkTheme()
        val useDarkTheme by preferencesStorage.getUseDarkThemeValue(isSystemInDarkTheme)
            .collectAsState(initial = isSystemInDarkTheme)
        val backgroundColor = getBackgroundColor(useDarkTheme)
        val textColor = getTextColor(useDarkTheme)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                if (drawFloatingActionButton) {
                    FloatingActionButton(
                        onClick = { onFloatingActionButtonClickedDelegate?.invoke() }
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = activity.getString(R.string.start_game)
                        )
                    }
                }
            }
        ) { innerPadding ->
            DrawScreenContent(
                innerPadding,
                navController, backgroundColor, textColor, useDarkTheme
            )
        }
    }

    @Composable
    protected abstract fun DrawScreenContent(
        innerPadding: PaddingValues,
        navController: NavHostController, backgroundColor: Color,
        textColor: Color,
        isSystemInDarkTheme: Boolean
    )
}