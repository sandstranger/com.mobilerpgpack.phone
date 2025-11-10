package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.activity.SDL3GameActivity
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL3MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL3MouseIconHelper
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL3ScreenController
import com.mobilerpgpack.phone.utils.ScreenResolution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.libsdl3.app.SDLSurface

abstract class SDL3EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    buttonsToDraw: Collection<IScreenControlsView>,
    activeEngineType: EngineTypes,
    pathToResourceFlow: Flow<String>,
    commandLineParamsFlow : Flow<String> = emptyFlow()) :
    EngineInfo(mainEngineLib, allLibs, buttonsToDraw,
        activeEngineType, pathToResourceFlow, commandLineParamsFlow) {

    private val mouseIcon: SDL3MouseIcon by inject()

    override val gameActivityClazz: Class<*> = SDL3GameActivity::class.java

    override val screenController: IScreenController by inject(
        named(SDL3ScreenController.SDL3_SCREEN_CONTROLLER_NAME)
    )

    override fun isMouseShown(): Int = if (SDL3MouseIconHelper.isMouseShown()) 1 else 0

    @Composable
    override fun DrawMouseIcon() {
        super.DrawMouseIcon()
        mouseIcon.DrawMouseIcon()
    }

    override fun setScreenResolution(screenResolution: ScreenResolution) {
        SDLSurface.fixedWidth = screenResolution.screenWidth
        SDLSurface.fixedHeight = screenResolution.screenHeight
    }
}