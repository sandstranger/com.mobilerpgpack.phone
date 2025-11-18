package com.mobilerpgpack.phone.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.sproctor.composepreferences.LocalPreferenceHandler
import com.github.sproctor.composepreferences.PreferenceHandler
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.psydoom.PsyDoomComposeSettings
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
import kotlin.getValue

class MainActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        buildScreens()
    }

    @OptIn(ExperimentalSettingsApi::class, ExperimentalSettingsImplementation::class)
    private fun buildScreens() {
        val settingsScreen : SettingsScreen by inject ()
        val permissionScreen : PermissionScreen by inject ()
        val psyDoomLauncherSettings : PsyDoomComposeSettings.PsyDoomLauncherSettingsScreen by inject ()
        val psyDoomMoreSettingsScreen : PsyDoomComposeSettings.PsyDoomMoreSettingsScreen by inject ()

        val startScreen: String = if (this@MainActivity.isExternalStoragePermissionGranted())
            settingsScreen.route else permissionScreen.route

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val preferencesStorage : PreferencesStorage = get ()
                val settings = DataStoreSettings(preferencesStorage.dataStore)
                val prerefencesHandler : PreferenceHandler = get {parameterSetOf(settings) }
                CompositionLocalProvider(LocalPreferenceHandler provides prerefencesHandler) {
                    NavHost(navController = navController, startDestination = startScreen) {
                        composable(permissionScreen.route)
                        {
                            permissionScreen.DrawScreen (navController) {
                                navController.navigate(settingsScreen.route) {
                                    popUpTo(settingsScreen.route) { inclusive = true }
                                }
                            }
                        }

                        composable(settingsScreen.route) {
                            settingsScreen.DrawScreen(navController)
                        }

                        composable(psyDoomLauncherSettings.route) {
                            psyDoomLauncherSettings.DrawScreen(navController)
                        }

                        composable (psyDoomMoreSettingsScreen.route) {
                            psyDoomMoreSettingsScreen.DrawScreen(navController)
                        }
                    }
                }
            }
        }
    }

    private fun updateTheme(){
        var useDarkTheme = false
        val preferencesStorage = get<PreferencesStorage>()
        runBlocking {
            useDarkTheme = preferencesStorage.getUseDarkThemeValue().first()
        }
        if (useDarkTheme) {
            setTheme(R.style.AppThemeDark)
        }
    }
}
