package com.mobilerpgpack.phone.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.window.layout.WindowMetricsCalculator
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.main.ONE_FRAME_DELAY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.Serializable
import kotlin.math.roundToInt

data class ScreenResolution (val screenWidth : Int, val screenHeight : Int)

val Context.isTelevision get() = this.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)

@Suppress("DEPRECATION")
val Activity.supportedRefreshRates: Collection<Int> get() =
   this.windowManager.defaultDisplay.supportedModes.map { it.refreshRate.roundToInt() }.distinct().sorted()

fun com.sun.jna.Function.invokeBool(inArgs : Array<Any?>? = null) = this.invokeAs(Boolean::class.java, inArgs)

@Suppress("UNCHECKED_CAST")
fun <T> com.sun.jna.Function.invokeAs(returnType: Class<T>, inArgs : Array<Any?>? = null): T = this.invoke(returnType, inArgs) as T

fun Activity.showErrorDialogBox (messageToShowResource: Int,onCloseDialogBox :(()-> Unit)? =null) =
    this.showMessageDialogBox(R.string.error, messageToShowResource, onCloseDialogBox)

inline fun <reified T> Context.startActivity(finishParentActivity : Boolean = true) where T : Activity  =
    this.startActivity(T::class.java, finishParentActivity)

fun Activity.forceLandscapeOrientation() {
    if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }
}

fun Context.startActivity(activityClazz : Class<*>, finishParentActivity : Boolean = true) {
    with(Intent(this, activityClazz)){
        if (this@startActivity is Application) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        this@startActivity.startActivity(this)

        if (finishParentActivity && this@startActivity is Activity) {
            this@startActivity.finish()
        }
    }
}

fun Activity.showMessageDialogBox (titleResource : Int? = null, messageToShowResource: Int,
                                   onCloseDialogBox :(()-> Unit)? =null){
    this.runOnUiThread {
        MaterialDialog(this).show {
            if (titleResource!= null) {
                title(titleResource)
            }
            message(messageToShowResource)

            positiveButton(R.string.ok_text){ onCloseDialogBox?.invoke() }
        }
    }
}

fun Activity.showErrorDialogBox (messageToShow : String, onCloseDialogBox :(()-> Unit)? =null) =
    this.showMessageDialogBox(this.getString(R.string.error), messageToShow, onCloseDialogBox)

fun Activity.showMessageDialogBox (title: String = "", messageToShow : String,
                                   onCloseDialogBox :(()-> Unit)? =null){
    this.runOnUiThread {
        MaterialDialog(this).show {
            if (title.isNotEmpty()) {
                title(text = title)
            }
            message(text = messageToShow)
            onDismiss { onCloseDialogBox?.invoke() }
            positiveButton(R.string.ok_text)
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Serializable> Intent.getValueFromIntent (name : String, clazz : Class<T>) : T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializableExtra(name,clazz)
    } else {
        this.getSerializableExtra(name) as? T
    }
}

fun Activity.getScreenResolution(drawInSafeArea : Boolean = false): ScreenResolution {
    val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)

    val bounds = windowMetrics.bounds

    val screenResolution = ScreenResolution(bounds.width(), bounds.height())

    if (!drawInSafeArea){
        return screenResolution
    }

    return ViewCompat.getRootWindowInsets(window.decorView)?.let { insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())

        ScreenResolution(bounds.width() - bars.left - bars.right,
            bounds.height() - bars.top - bars.bottom)
    } ?: run {
        screenResolution
    }
}

fun Activity.hideSystemBarsAndWait(callback: () -> Unit = {}) {
    window.decorView.apply {
        post{
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    postDelayed({
                        ViewCompat.getRootWindowInsets(this@apply)?.let {
                            if (!it.isVisible(WindowInsetsCompat.Type.systemBars())) {
                                callback()
                            }
                        } ?: run {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    }, ONE_FRAME_DELAY)
                }
            })
            hideSystemBars()
        }
    }
}

fun Context.isInternetAvailable(): Boolean {
    return with(this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager){
        val network = activeNetwork ?: return false
        val capabilities = getNetworkCapabilities(network) ?: return false
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

fun Context.isWifiConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    @Suppress("DEPRECATION")
    val networkInfo = cm.activeNetworkInfo
    @Suppress("DEPRECATION")
    return networkInfo != null &&
            networkInfo.isConnected &&
            networkInfo.type == ConnectivityManager.TYPE_WIFI
}

fun Context.isExternalStoragePermissionGranted () : Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        return Environment.isExternalStorageManager()
    }

    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

fun Activity.displayInSafeArea() {
    val v = window.decorView.rootView
    ViewCompat.getRootWindowInsets(v)?.let { insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )

        v.also {
            it.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom)
            it.setBackgroundColor(Color.BLACK)
        }
    }
}

private fun Activity.hideSystemBars() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.window.insetsController?.let {
            it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        @Suppress("DEPRECATION")
        this.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }
}