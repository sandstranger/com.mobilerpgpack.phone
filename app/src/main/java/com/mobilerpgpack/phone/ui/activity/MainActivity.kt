package com.mobilerpgpack.phone.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.SettingScreen
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomComposeSettings.UZDoomMoreSettingsScreen
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.getOnPrimaryColor
import com.mobilerpgpack.phone.ui.getPrimaryColor
import com.mobilerpgpack.phone.ui.items.SetupNavigationBar
import com.mobilerpgpack.phone.ui.screen.ComposeScreen
import com.mobilerpgpack.phone.ui.screen.PermissionScreen
import com.mobilerpgpack.phone.ui.screen.SettingsScreen
import com.mobilerpgpack.phone.utils.isExternalStoragePermissionGranted
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        buildScreens()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun buildScreens() {
        val settingsScreen: SettingsScreen by inject()
        val permissionScreen: PermissionScreen by inject()
        val psyDoomSettingsScreens by inject<Collection<SettingScreen>>()
        val moreUZDoomSettingsScreen by inject<UZDoomMoreSettingsScreen>()

        val startScreen: String = if (this@MainActivity.isExternalStoragePermissionGranted())
            settingsScreen.route else permissionScreen.route

        setContent {
            val navController = rememberNavController()

            Theme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .systemBarsPadding()
                ) {
                    TopAppBar( modifier = Modifier.fillMaxWidth().height(40.dp),
                        title = {
                            Text(
                                text = stringResource(R.string.app_name),
                                color = getOnPrimaryColor()
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = getPrimaryColor()
                        )
                    )
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        NavHost(
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
                    SetupNavigationBar()
                }
            }
        }
    }
}
