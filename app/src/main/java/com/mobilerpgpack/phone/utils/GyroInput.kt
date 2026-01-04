package com.mobilerpgpack.phone.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.view.Display
import android.view.Surface
import android.view.WindowManager
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.abs

abstract class GyroInput(private val ctx: Context,private val engineInfo: IEngineInfo) : SensorEventListener, KoinComponent {
    private var sm: SensorManager? = null
    private var gyro: Sensor? = null
    private var display: Display? = null

    private var initialized = false

    private val preferencesStorage : PreferencesStorage by inject ()

    private var sensX : Float = DEFAULT_SENS_X
    private var sensY : Float = DEFAULT_SENS_Y
    private var dead : Float = DEFAULT_DEAD_ZONE
    private var invertXAxis = false
    private var invertYAxis = false

    protected abstract fun onNativeGyroMouse(dx: Float, dy: Float)

    private fun initialize() {
        if (initialized){
            return
        }
        initialized = true

        with(preferencesStorage){
            sensX = gyroscopeXSensitivity
            sensY = gyroscopeYSensitivity
            dead = gyroscopeDeadZone
            invertXAxis = invertGyroscopeXAxis
            invertYAxis = invertGyroscopeYAxis
        }

        sm = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyro = sm!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ctx.display else {
            @Suppress("DEPRECATION")
            (ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }
    }

    fun start() {
        initialize()
        if (gyro != null) {
            sm?.registerListener(this, gyro, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() = sm?.unregisterListener(this)

    final override fun onSensorChanged(e: SensorEvent) {
        if (e.sensor.type != Sensor.TYPE_GYROSCOPE || engineInfo.mouseButtonsEventsCanBeInvoked) {
            return
        }

        var x = -1f * (e.values[0] * (if (invertXAxis) -1f else 1f))
        var y = e.values[1] * (if (invertYAxis) -1f else 1f)

        when (display!!.rotation) {
            Surface.ROTATION_270 -> {
                x = -x
                y = -y
            }
            else -> {}
        }

        if (abs(x) < dead) x = 0f
        if (abs(y) < dead) y = 0f

        x *= sensX
        y *= sensY

        onNativeGyroMouse(x, y)
    }

    final override fun onAccuracyChanged(s: Sensor?, a: Int) {}

    companion object{
        const val DEFAULT_SENS_X : Float = 15.0f
        const val DEFAULT_SENS_Y : Float = 15.0f
        const val DEFAULT_DEAD_ZONE : Float = 0.02f
    }
}