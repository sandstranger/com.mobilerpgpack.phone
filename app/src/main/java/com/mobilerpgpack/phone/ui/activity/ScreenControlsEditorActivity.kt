package com.mobilerpgpack.phone.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.ui.screen.OnScreenController
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.displayInSafeArea
import com.mobilerpgpack.phone.utils.hideSystemBars
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class ScreenControlsEditorActivity : ComponentActivity(), KoinComponent {

    private val preferencesStorage : PreferencesStorage by inject ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBars()

        var displayInSafeArea = false
        runBlocking {
            displayInSafeArea = preferencesStorage.enableDisplayInSafeArea.first()
        }

        if (displayInSafeArea){
            this.displayInSafeArea()
        }

        val selectedEngine = getSelectedEngineType()

        setContent {
            MaterialTheme {
                OnScreenController(enginesInfo[selectedEngine]!!.buttonsToDraw,
                    inGame = false,
                    activeEngine = selectedEngine,
                    drawInSafeArea = displayInSafeArea, onBack = {
                        this@ScreenControlsEditorActivity.finish()
                    }){
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