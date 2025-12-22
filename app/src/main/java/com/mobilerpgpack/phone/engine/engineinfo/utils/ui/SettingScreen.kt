package com.mobilerpgpack.phone.engine.engineinfo.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.ui.screen.ComposeScreen

abstract class SettingScreen (screenName : String) : ComposeScreen(screenName){

    override val drawBackButton = true

    @Composable
    override fun DrawScreenContent(innerPadding: PaddingValues, navController: NavHostController) {
        val scrollState = rememberScrollState()
        val transparentColor = remember { Color.Transparent }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(transparentColor)
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            DrawSettingsScreen(navController)
        }
    }

    @Composable
    protected abstract fun DrawSettingsScreen(navController: NavHostController)
}