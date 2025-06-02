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
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.preference.EditTextPreference
import com.mobilerpgpack.phone.BuildConfig
import androidx.core.net.toUri
import com.mobilerpgpack.phone.R
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

val Context.isTelevision get() = this.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)

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

suspend fun Context.requestResourceFile (launcher : ManagedActivityResultLauncher<Intent, ActivityResult>, onFileSelected : (String) -> Unit ){
    val useAlternateFilePicker = PreferencesStorage.getUseCustomFilePickerValue(this).first()!!

    if (!useAlternateFilePicker){
        launcher.launch(Intent.createChooser(buildRequestResourceFileIntent(), this.getString(R.string.choose_file_request)))
        return
    }

    this.requestResourceFileByAlternateFilePicker (onFileSelected)
}

private fun Context.requestResourceFileByAlternateFilePicker (onFileSelected : (String) -> Unit ){
    ChooserDialog(this)
        .withFilter(false, false, "ipa", "zip")
        .withStartFile(Environment.getExternalStorageDirectory().absolutePath)
        .withChosenListener { path, _ ->
            onFileSelected(path)
        }
        .build()
        .show()
}

private fun buildRequestResourceFileIntent () : Intent {
    return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "*/*"
        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/zip", "application/octet-stream"))
    }
}