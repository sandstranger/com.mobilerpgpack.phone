package com.mobilerpgpack.phone.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.ScreenController
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.displayInSafeArea
import com.mobilerpgpack.phone.utils.hideSystemBarsAndWait
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

class ScreenControlsEditorActivity : ComponentActivity(), KoinComponent {

    private val preferencesStorage : PreferencesStorage by inject ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedEngine = getSelectedEngineType()

        var displayInSafeArea = false
        var activeEngine : IEngineInfo

        runBlocking {
            displayInSafeArea = preferencesStorage.enableDisplayInSafeArea.first()
            activeEngine = get (named(selectedEngine.toString()))
        }

        enableEdgeToEdge()

        window.decorView.post {
            hideSystemBarsAndWait {
                if (displayInSafeArea) {
                    displayInSafeArea()
                }
            }
        }

        setContent {
            MaterialTheme {
                val isSystemInDarkTheme = isSystemInDarkTheme()
                val useDarkTheme by preferencesStorage.getUseDarkThemeValue(isSystemInDarkTheme)
                    .collectAsState(initial = isSystemInDarkTheme)

                Theme(darkTheme = useDarkTheme) {
                    activeEngine.screenController.DrawScreenControls(
                        inGame = false,
                        activeEngine = selectedEngine,
                        drawInSafeArea = displayInSafeArea, onBack = {
                            this@ScreenControlsEditorActivity.finish()
                        })
                }
            }
        }
    }

    private fun getSelectedEngineType () : EngineTypes {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getSerializableExtra(EXTRA_ENGINE_TYPE,EngineTypes::class.java) ?: EngineTypes.DefaultActiveEngine
        } else {
            intent?.getSerializableExtra(EXTRA_ENGINE_TYPE) as? EngineTypes
                ?: EngineTypes.DefaultActiveEngine
        }
    }

    companion object{
        private const val EXTRA_ENGINE_TYPE = "extra_engine_type"

        fun editControls(context: Context, engineType: EngineTypes) {
            with (Intent(context, ScreenControlsEditorActivity::class.java)){
                this.putExtra(EXTRA_ENGINE_TYPE, engineType)
                context.startActivity(this)
            }
        }
    }
}