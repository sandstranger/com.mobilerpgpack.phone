package com.mobilerpgpack.phone.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.mobilerpgpack.phone.R
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest

val Context.isTelevision get() = this.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)

inline fun <reified T> Context.startActivity(finishParentActivity : Boolean = true) where T : Activity {
    val i = Intent(this, T::class.java)

    if (this is Application) i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(Intent(this, T::class.java))

    if (finishParentActivity && this is Activity) this.finish();
}


fun copyAssetsFolderToInternalStorage(context: Context, assetsFolder: String, destFolder: File) {
    val assetManager = context.assets
    try {
        val files = assetManager.list(assetsFolder)
        if (files != null) {
            if (!destFolder.exists()) {
                destFolder.mkdirs()
            }

            for (filename in files) {
                val assetPath = if (assetsFolder.isEmpty()) filename else "$assetsFolder/$filename"
                val outFile = File(destFolder, filename)

                val subFiles = assetManager.list(assetPath)
                if (subFiles != null && subFiles.isNotEmpty()) {
                    copyAssetsFolderToInternalStorage(context, assetPath, outFile)
                } else {
                    val shouldCopy = !outFile.exists() || !compareAssetAndFileHash(assetManager, assetPath, outFile)
                    if (shouldCopy) {
                        assetManager.open(assetPath).use { inputStream ->
                            FileOutputStream(outFile).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                    }
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
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

    this.requestResourceFileByAlternateFilePicker ( dirOnly = false, onFileSelected)
}

suspend fun Context.requestDirectory (launcher : ManagedActivityResultLauncher<Intent, ActivityResult>, onDirectorySelected : (String) -> Unit ){
    val useAlternateFilePicker = PreferencesStorage.getUseCustomFilePickerValue(this).first()!!

    if (!useAlternateFilePicker){
        with(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)) {
            addCategory(Intent.CATEGORY_DEFAULT)
            launcher.launch(createChooser(this, this@requestDirectory.getString(R.string.choose_directory)))
        }

        return
    }

    this.requestResourceFileByAlternateFilePicker ( dirOnly = true, onDirectorySelected)
}

private fun Context.requestResourceFileByAlternateFilePicker ( dirOnly : Boolean, onFileSelected : (String) -> Unit ){
    ChooserDialog(this)
        .withFilter(dirOnly, false, "ipa", "zip")
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

private fun compareAssetAndFileHash(assetManager: AssetManager, assetPath: String, file: File): Boolean {
    return try {
        val assetHash = assetManager.open(assetPath).use { inputStream ->
            computeSHA256(inputStream)
        }
        val fileHash = FileInputStream(file).use { inputStream ->
            computeSHA256(inputStream)
        }
        assetHash.contentEquals(fileHash)
    } catch (e: IOException) {
        false
    }
}

private fun computeSHA256(inputStream: InputStream): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    val buffer = ByteArray(4096)
    var bytesRead: Int
    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        digest.update(buffer, 0, bytesRead)
    }
    return digest.digest()
}