package com.mobilerpgpack.phone.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.ui.DoomBFAComposeSettings
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.SettingScreen
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomComposeSettings.UZDoomMoreSettingsScreen
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.getBackgroundColor
import com.mobilerpgpack.phone.ui.items.SetupSystemBars
import com.mobilerpgpack.phone.ui.screen.ComposeScreen
import com.mobilerpgpack.phone.ui.screen.PermissionScreen
import com.mobilerpgpack.phone.ui.screen.SettingsScreen
import com.mobilerpgpack.phone.ui.viewmodel.MainActivityViewModel
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.isExternalStoragePermissionGranted
import com.mobilerpgpack.phone.utils.waitUntil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class MainActivity : ComponentActivity(), KoinComponent {
    private var wasInitialized = false
    private val scope = CoroutineScope(Dispatchers.Main)
    private val viewModel : MainActivityViewModel by inject ()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if (gameActivityStarted){
            finish()
            return
        }
        viewModel.copyAllAssetsFromApk()
        scope.launch { buildScreensAsync() }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.coroutineContext.cancelChildren()
    }

    private suspend fun buildScreensAsync(){
        val preferencesStorage : PreferencesStorage = get()
        waitUntil { !preferencesStorage.prefsWasLoaded }
        buildScreens()
        wasInitialized = true
    }

    private fun buildScreens() {
        setContent {
            val settingsScreen: SettingsScreen = koinInject()
            val permissionScreen: PermissionScreen = koinInject()
            val psyDoomSettingsScreens = koinInject<Collection<SettingScreen>>()
            val moreUZDoomSettingsScreen = koinInject<UZDoomMoreSettingsScreen>()
            val doomBFAGraphicsSettingsScreen = koinInject<DoomBFAComposeSettings.DoomBFAGraphicsScreen>()
            val startScreen: String = remember { if (this@MainActivity.isExternalStoragePermissionGranted())
                settingsScreen.route else permissionScreen.route }
            val navController = rememberNavController()
            val backgroundColor = getBackgroundColor()

            Theme {
                SetupSystemBars()
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

                    doomBFAGraphicsSettingsScreen.apply {
                        composable(this.route) {
                            DrawScreen((navController))
                        }
                    }
                }
            }
        }
    }

    override fun finish() {
        if (wasInitialized){
            val composeScreens = get <Collection<ComposeScreen>> (
                named(KoinModulesProvider.ALL_COMPOSE_SCREENS))
            composeScreens.forEach { it.onMainActivityFinish() }
            get <IAssetExtractor> ().clearSubscribers()
        }
        super.finish()
    }

    companion object{
        var gameActivityStarted = false
    }
}
