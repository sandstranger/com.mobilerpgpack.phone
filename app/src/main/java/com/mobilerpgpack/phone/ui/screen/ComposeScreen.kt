package com.mobilerpgpack.phone.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.getBackgroundColor
import com.mobilerpgpack.phone.ui.getFabIconContainerColor
import com.mobilerpgpack.phone.ui.getIconButtonsColors
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.core.component.KoinComponent

abstract class ComposeScreen(val route: String) : KoinComponent {
    protected open val drawFloatingActionButton = MutableLiveData(false)
    protected open val drawBackButton = false
    protected var onFloatingActionButtonClickedDelegate: (() -> Unit)? = null
    protected var onBackPressedDelegate : (()->Unit)? = null

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DrawScreen(navController: NavHostController) {
        val onSurfaceColor = getOnSurfaceColor()
        val onBackgroundColor = getOnBackgroundColor()
        val backgroundColor = getBackgroundColor()
        val fabIconContainerColor = getFabIconContainerColor()
        val iconButtonsColor = getIconButtonsColors()
        val drawFloatingActionButton = drawFloatingActionButton.getComposableValue()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = backgroundColor,
            contentColor = onBackgroundColor,
            topBar = {
                if (drawBackButton){
                TopAppBar( modifier = Modifier.fillMaxWidth().height(30.dp),
                    title = { },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = backgroundColor
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackPressedDelegate?.invoke()
                            navController.navigateUp()
                        }, colors = iconButtonsColor) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back",
                                tint = onBackgroundColor
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
                        containerColor = fabIconContainerColor
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.start_game),
                            tint = onSurfaceColor
                        )
                    }
                }
            }
        ) { innerPadding ->
            DrawScreenContent(innerPadding, navController)
        }
    }

    open fun onMainActivityFinish (){

    }

    @Composable
    protected abstract fun DrawScreenContent(
        innerPadding: PaddingValues,
        navController: NavHostController)
}