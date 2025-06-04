package com.mobilerpgpack.phone.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.setFullscreen
import com.mobilerpgpack.phone.ui.screen.OnScreenController

class ConfigureControlsActivity : ComponentActivity() {
    private lateinit var selectedEngineType : EngineTypes

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        selectedEngineType = getSelectedEngineType()

        setContent {
            MaterialTheme {
                OnScreenController()
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
            with (Intent(context, ConfigureControlsActivity::class.java)){
                this.putExtra(EXTRA_ENGINE_TYPE, engineType)
                context.startActivity(this)
            }
        }
    }
}