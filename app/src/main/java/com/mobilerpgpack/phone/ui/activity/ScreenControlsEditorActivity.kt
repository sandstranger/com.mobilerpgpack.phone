package com.mobilerpgpack.phone.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.utils.displayInSafeArea
import com.mobilerpgpack.phone.utils.getValueFromIntent
import com.mobilerpgpack.phone.utils.hideSystemBarsAndWait
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

class ScreenControlsEditorActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val selectedEngineType = intent
            .getValueFromIntent(EXTRA_ENGINE_TYPE, EngineTypes::class.java)!!
        val activeEngine : IEngineInfo = get (named(selectedEngineType.name))
        val displayInSafeArea = intent.getBooleanExtra(DISPLAY_IN_SAFE_AREA_KEY, false)

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

        forceLandscapeOrientation()
    }

    override fun onResume() {
        super.onResume()
        forceLandscapeOrientation()
    }

    private fun forceLandscapeOrientation() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
        }
    }

    companion object{
        private const val EXTRA_ENGINE_TYPE = "extra_engine_type"
        private const val DISPLAY_IN_SAFE_AREA_KEY = "display_in_safe_area"

        fun editControls(context: Context, engineType: EngineTypes, displayInSafeArea : Boolean) {
            with (Intent(context, ScreenControlsEditorActivity::class.java)){
                putExtra(EXTRA_ENGINE_TYPE, engineType)
                putExtra(DISPLAY_IN_SAFE_AREA_KEY, displayInSafeArea)
                context.startActivity(this)
            }
        }
    }
}