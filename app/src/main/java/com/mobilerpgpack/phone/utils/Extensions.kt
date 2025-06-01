package com.mobilerpgpack.phone.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.InputType
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.preference.EditTextPreference
import com.mobilerpgpack.phone.BuildConfig
import androidx.core.net.toUri

inline fun <reified T> Context.startActivity(finishParentActivity : Boolean = true) where T : Activity {
    val i = Intent(this, T::class.java)

    if (this is Application) i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(Intent(this, T::class.java))

    if (finishParentActivity && this is Activity) this.finish();
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

fun Activity.displayInSafeArea (prefsManager: SharedPreferences){
    val displayInSafeArea = prefsManager.getBoolean(DISPLAY_IN_SAFE_AREA_PREFS_KEY, true)
    if (displayInSafeArea) {
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
}
