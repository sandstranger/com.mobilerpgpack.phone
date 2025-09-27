package com.mobilerpgpack.phone.ui.screen

import CustomTopBar
import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.Theme
import com.mobilerpgpack.phone.ui.items.SetupNavigationBar
import com.mobilerpgpack.phone.utils.isExternalStoragePermissionGranted

@Composable
fun PermissionScreen( onPermissionGranted: () -> Unit ) {
    val activity = LocalActivity.current!!
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isSystemInDarkTheme) Color.Black else Color.White
    val topBarColor = if (isSystemInDarkTheme) Color.Gray else Color.Blue
    val textColor = if (isSystemInDarkTheme) Color.White else Color.Black

    Theme (darkTheme = isSystemInDarkTheme ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(topBarColor)
                .systemBarsPadding()
        ) {
            CustomTopBar(title = activity.getString(R.string.app_name),isSystemInDarkTheme)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val launcher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        if (activity.isExternalStoragePermissionGranted()) {
                            onPermissionGranted()
                        }
                    }

                val legacyPermissionsLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        onPermissionGranted()
                    }
                }

                Text(text = activity.getString(R.string.access_to_all_files), textAlign = TextAlign.Center,
                    color = textColor, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (!activity.isExternalStoragePermissionGranted()) {
                                val uri = "package:${BuildConfig.APPLICATION_ID}".toUri()
                                launcher.launch(
                                    Intent(
                                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                                        uri
                                    )
                                )
                            }
                        } else {
                            legacyPermissionsLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                    },
                ) {
                    Text(
                        text = activity.getString(R.string.grant_permission),
                        textAlign = TextAlign.Center, fontSize = 21.sp,
                    )
                }
            }
        }
    }
    SetupNavigationBar(isSystemInDarkTheme)
}