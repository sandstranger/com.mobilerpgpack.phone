package com.mobilerpgpack.phone.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

val Context.isTelevision get() = this.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)

@Suppress("UNCHECKED_CAST")
fun <T> com.sun.jna.Function.callAs(returnType: Class<T>): T  = this.invoke(returnType, null) as T

inline fun <reified T> Context.startActivity(finishParentActivity : Boolean = true) where T : Activity {
    val i = Intent(this, T::class.java)

    if (this is Application) i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(Intent(this, T::class.java))

    if (finishParentActivity && this is Activity) this.finish();
}

fun Activity.hideSystemBars() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.post {
            this.window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    } else {
        window.decorView.post {
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
}

fun Context.isInternetAvailable(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    @Suppress("DEPRECATION")
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networkInfo = cm.activeNetworkInfo
        networkInfo != null && networkInfo.isConnected
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
    ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars()
                    or WindowInsetsCompat.Type.displayCutout()
        )
        v.updatePadding(
            left = bars.left,
            top = bars.top,
            right = bars.right,
            bottom = bars.bottom,
        )

        val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
        if (cutout.top > 0 || cutout.left > 0 || cutout.right > 0) {
            v.setBackgroundColor(Color.BLACK)
        }

        WindowInsetsCompat.CONSUMED
    }
}