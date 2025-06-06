package com.mobilerpgpack.phone.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.activity.EngineActivity
import com.mobilerpgpack.phone.engine.enginesInfo
import com.mobilerpgpack.phone.engine.setFullscreen
import com.mobilerpgpack.phone.ui.screen.OnScreenController
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.displayInSafeArea
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

class ScreenControlsEditorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)

        var displayInSafeArea = false

        runBlocking {
            displayInSafeArea = PreferencesStorage.getDisplayInSafeAreaValue(this@ScreenControlsEditorActivity).first()!!
        }

        if (displayInSafeArea){
            this.displayInSafeArea()
        }

        val selectedEngine = getSelectedEngineType()

        setContent {
            MaterialTheme {
                OnScreenController(enginesInfo[selectedEngine]!!.buttonsToDraw,
                    inGame = false, activeEngine = selectedEngine){
                    this@ScreenControlsEditorActivity.finish()
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

        fun startActivity(context: Context, engineType: EngineTypes) {
            with (Intent(context, ScreenControlsEditorActivity::class.java)){
                this.putExtra(EXTRA_ENGINE_TYPE, engineType)
                context.startActivity(this)
            }
        }
    }
}