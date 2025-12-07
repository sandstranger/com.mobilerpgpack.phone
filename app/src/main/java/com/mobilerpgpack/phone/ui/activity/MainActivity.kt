package com.mobilerpgpack.phone.ui.activity

import CustomTopBar
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.sproctor.composepreferences.LocalPreferenceHandler
import com.github.sproctor.composepreferences.PreferenceHandler
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.SettingScreen
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomComposeSettings.UZDoomMoreSettingsScreen
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.getBackgroundColor
import com.mobilerpgpack.phone.ui.getTopBarColor
import com.mobilerpgpack.phone.ui.items.SetupNavigationBar
import com.mobilerpgpack.phone.ui.screen.ComposeScreen
import com.mobilerpgpack.phone.ui.screen.PermissionScreen
import com.mobilerpgpack.phone.ui.screen.SettingsScreen
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.isExternalStoragePermissionGranted
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.datastore.DataStoreSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parameterSetOf

class MainActivity : ComponentActivity(), KoinComponent {

    private val preferencesStorage = get<PreferencesStorage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        buildScreens()
    }

    @OptIn(ExperimentalSettingsApi::class, ExperimentalSettingsImplementation::class)
    private fun buildScreens() {
        val settingsScreen: SettingsScreen by inject()
        val permissionScreen: PermissionScreen by inject()
        val psyDoomSettingsScreens by inject<Collection<SettingScreen>> ()
        val moreUZDoomSettingsScreen by inject<UZDoomMoreSettingsScreen>()

        val startScreen: String = if (this@MainActivity.isExternalStoragePermissionGranted())
            settingsScreen.route else permissionScreen.route

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val settings = DataStoreSettings(preferencesStorage.dataStore)
                val prerefencesHandler: PreferenceHandler = get { parameterSetOf(settings) }
                val isSystemInDarkTheme = isSystemInDarkTheme()
                val useDarkTheme by preferencesStorage.getUseDarkThemeValue(isSystemInDarkTheme)
                    .collectAsState(initial = isSystemInDarkTheme)
                val topBarColor = getTopBarColor(useDarkTheme)
                val backgroundColor = getBackgroundColor(useDarkTheme)

                Theme(darkTheme = useDarkTheme) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(topBarColor)
                            .systemBarsPadding()
                    ) {
                        CustomTopBar(title = stringResource(R.string.app_name), useDarkTheme)
                        Column(modifier = Modifier.background(backgroundColor).fillMaxSize()
                        ) {
                            CompositionLocalProvider(LocalPreferenceHandler provides prerefencesHandler) {
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
                                        val screen : ComposeScreen = it
                                        composable (screen.route) {
                                            screen.DrawScreen(navController)
                                        }
                                    }

                                    composable (moreUZDoomSettingsScreen.route) {
                                        moreUZDoomSettingsScreen.DrawScreen(navController)
                                    }
                                }
                            }
                        }
                    }
                    SetupNavigationBar(useDarkTheme)
                }
            }
        }
    }

    private fun updateTheme() {
        var useDarkTheme = false
        runBlocking {
            useDarkTheme = preferencesStorage.getUseDarkThemeValue().first()
        }
        if (useDarkTheme) {
            setTheme(R.style.AppThemeDark)
        }
    }
}
