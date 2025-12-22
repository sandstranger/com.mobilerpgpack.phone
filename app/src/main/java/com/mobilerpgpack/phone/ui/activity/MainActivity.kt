package com.mobilerpgpack.phone.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.SettingScreen
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomComposeSettings.UZDoomMoreSettingsScreen
import com.mobilerpgpack.phone.main.ONE_FRAME_DELAY
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.getBackgroundColor
import com.mobilerpgpack.phone.ui.items.SetupSystemBars
import com.mobilerpgpack.phone.ui.screen.ComposeScreen
import com.mobilerpgpack.phone.ui.screen.PermissionScreen
import com.mobilerpgpack.phone.ui.screen.SettingsScreen
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.isExternalStoragePermissionGranted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        scope.launch { buildScreensAsync() }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.coroutineContext.cancelChildren()
    }

    private suspend fun buildScreensAsync(){
        val preferencesStorage : PreferencesStorage = get()
        while (!preferencesStorage.prefsWasLoaded){
            delay(ONE_FRAME_DELAY)
        }
        buildScreens()
    }

    private fun buildScreens() {
        val settingsScreen: SettingsScreen by inject()
        val permissionScreen: PermissionScreen by inject()
        val psyDoomSettingsScreens by inject<Collection<SettingScreen>>()
        val moreUZDoomSettingsScreen by inject<UZDoomMoreSettingsScreen>()

        val startScreen: String = if (this@MainActivity.isExternalStoragePermissionGranted())
            settingsScreen.route else permissionScreen.route

        setContent {
            SetupSystemBars()
            val navController = rememberNavController()
            val backgroundColor = getBackgroundColor()

            Theme {
                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .systemBarsPadding(),
                    navController = navController,
                    startDestination = startScreen
                ) {
                    composable(permissionScreen.route)
                    {
                        permissionScreen.DrawScreen(navController) {
                            navController.navigate(settingsScreen.route) {
                                popUpTo(0) {
                                    inclusive = true
                                    saveState = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }

                    composable(settingsScreen.route) {
                        settingsScreen.DrawScreen(navController)
                    }

                    psyDoomSettingsScreens.forEach {
                        val screen: ComposeScreen = it
                        composable(screen.route) {
                            screen.DrawScreen(navController)
                        }
                    }

                    composable(moreUZDoomSettingsScreen.route) {
                        moreUZDoomSettingsScreen.DrawScreen(navController)
                    }
                }
            }
        }
    }
}
