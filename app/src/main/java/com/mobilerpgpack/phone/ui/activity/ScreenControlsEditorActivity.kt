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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.ScreenController
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.displayInSafeArea
import com.mobilerpgpack.phone.utils.getValueFromIntent
import com.mobilerpgpack.phone.utils.hideSystemBarsAndWait
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import java.io.Serializable

class ScreenControlsEditorActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val selectedEngineType = intent
            .getValueFromIntent(EXTRA_ENGINE_TYPE, EngineTypes::class.java)!!
        val activeEngine : IEngineInfo = get (named(selectedEngineType.name))
        val displayInSafeArea = intent.getValueFromIntent(DISPLAY_IN_SAFE_AREA_KEY,
            Boolean::class.java)!!

        hideSystemBarsAndWait {
            if (displayInSafeArea) {
                displayInSafeArea()
            }
        }

        setContent {
            Theme {
                activeEngine.screenController.DrawScreenControls(
                    inGame = false,
                    activeEngine = selectedEngineType,
                    drawInSafeArea = displayInSafeArea, onBack = {
                        this@ScreenControlsEditorActivity.finish()
                    })
            }
        }
    }

    companion object{
        private const val EXTRA_ENGINE_TYPE = "extra_engine_type"
        private const val DISPLAY_IN_SAFE_AREA_KEY = "display_in_safe_area"

        fun editControls(context: Context, engineType: EngineTypes, displayInSafeArea : Boolean) {
            with (Intent(context, ScreenControlsEditorActivity::class.java)){
                this.putExtra(EXTRA_ENGINE_TYPE, engineType)
                this.putExtra(DISPLAY_IN_SAFE_AREA_KEY, displayInSafeArea)
                context.startActivity(this)
            }
        }
    }
}