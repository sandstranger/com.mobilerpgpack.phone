package com.mobilerpgpack.phone.ui.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.getBackgroundColor
import com.mobilerpgpack.phone.ui.getFabIconContainerColor
import com.mobilerpgpack.phone.ui.getIconButtonsColors
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import org.koin.core.component.KoinComponent

abstract class ComposeScreen(val route: String) : KoinComponent {

    protected open val drawFloatingActionButton = false

    protected open val drawBackButton = false

    protected var onFloatingActionButtonClickedDelegate: (() -> Unit)? = null

    protected var onBackPressedDelegate : (()->Unit)? = null

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DrawScreen(navController: NavHostController) {
        val activity = LocalActivity.current!!
        val onSurfaceColor = getOnSurfaceColor()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = getBackgroundColor(),
            contentColor = getOnBackgroundColor(),
            topBar = {
                if (drawBackButton){
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackPressedDelegate?.invoke()
                            navController.navigateUp()
                        }, colors = getIconButtonsColors()) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back",
                                tint = getOnBackgroundColor()
                            )
                        }
                    }
                )
                    }
            },
            floatingActionButton = {
                if (drawFloatingActionButton) {
                    FloatingActionButton(
                        onClick = { onFloatingActionButtonClickedDelegate?.invoke() },
                        contentColor = onSurfaceColor,
                        containerColor = getFabIconContainerColor()
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = activity.getString(R.string.start_game),
                            tint = onSurfaceColor
                        )
                    }
                }
            }
        ) { innerPadding ->
            DrawScreenContent(innerPadding, navController)
        }
    }

    @Composable
    protected abstract fun DrawScreenContent(
        innerPadding: PaddingValues,
        navController: NavHostController)
}