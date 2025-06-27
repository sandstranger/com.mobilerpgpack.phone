package com.mobilerpgpack.phone.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobilerpgpack.phone.ui.screen.PermissionScreen
import com.mobilerpgpack.phone.ui.screen.SettingsScreen
import com.mobilerpgpack.phone.utils.copyAssetsFolderToInternalStorage
import com.mobilerpgpack.phone.utils.isExternalStoragePermissionGranted

internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        buildScreens()
    }

    private fun buildScreens() {
        val startScreen: String = if (this@MainActivity.isExternalStoragePermissionGranted())
            Screen.Settings.route else Screen.Permission.route
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = startScreen) {
                    composable(Screen.Permission.route)
                    {
                        PermissionScreen {
                            navController.navigate(Screen.Settings.route) {
                                popUpTo(Screen.Permission.route) { inclusive = true }
                            }
                        }
                    }
                    composable(Screen.Settings.route) { SettingsScreen() }
                }
            }
        }
    }

    private sealed class Screen(val route: String) {
        data object Permission : Screen("permission")
        data object Settings : Screen("settings")
    }
}
