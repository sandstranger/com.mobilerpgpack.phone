package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.activity.SDL2GameActivity
import com.mobilerpgpack.phone.ui.screen.screencontrols.MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL2MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL2ScreenController
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface

abstract class SDL2EngineInfo(
    private val mainEngineLib: String,
    private val allLibs: Array<String>,
    private val buttonsToDraw: Collection<IScreenControlsView>,
    private val activeEngineType: EngineTypes,
    private val pathToResourceFlow: Flow<String>) :
    EngineInfo(mainEngineLib, allLibs, buttonsToDraw, activeEngineType, pathToResourceFlow) {

    private val mouseIcon: SDL2MouseIcon by inject()

    override val gameActivityClazz: Class<*> = SDL2GameActivity::class.java

    override val screenController: IScreenController by inject(
        named(SDL2ScreenController.SDL2_SCREEN_CONTROLLER_NAME)
    )

    override fun isMouseShown(): Int = SDLActivity.isMouseShown()

    @Composable
    override fun DrawMouseIcon() {
        super.DrawMouseIcon()
        mouseIcon.DrawMouseIcon()
    }

    override fun setScreenResolution(screenWidth: Int, screenHeight: Int) {
        SDLSurface.fixedWidth = screenWidth
        SDLSurface.fixedHeight = screenHeight
    }
}