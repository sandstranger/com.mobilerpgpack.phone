package com.mobilerpgpack.phone.ui.screen

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.getButtonsColors
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.ui.getPrimaryColor
import com.mobilerpgpack.phone.utils.isExternalStoragePermissionGranted

class PermissionScreen : ComposeScreen (SCREEN_NAME) {

    private lateinit var onPermissionGranted: () -> Unit

    @Composable
    fun DrawScreen(navController: NavHostController, onPermissionGranted: () -> Unit){
        this.onPermissionGranted = onPermissionGranted
        super.DrawScreen(navController)
    }

    @Composable
    override fun DrawScreenContent(innerPadding: PaddingValues, navController: NavHostController) {
        val activity = LocalActivity.current!!
        val onBackgroundColor = getOnBackgroundColor()
        val buttonsColor = getButtonsColors()
        val primaryColor = getPrimaryColor()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally){

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

            Text(text = stringResource(R.string.access_to_all_files),
                textAlign = TextAlign.Center, fontSize = 24.sp, color = onBackgroundColor)
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
                }, colors = buttonsColor
            ) {
                Text(
                    text = stringResource(R.string.grant_permission),
                    textAlign = TextAlign.Center, fontSize = 21.sp,
                    color = primaryColor
                )
            }
        }
    }

    companion object {
        const val SCREEN_NAME = "permission_screem"
    }
}