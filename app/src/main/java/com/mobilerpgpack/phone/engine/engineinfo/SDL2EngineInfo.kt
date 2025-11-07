package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.activity.SDL2GameActivity
import com.mobilerpgpack.phone.ui.items.MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL2ScreenController
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.libsdl.app.SDLSurface

abstract class SDL2EngineInfo(private val mainEngineLib: String,
                     private val allLibs : Array<String>,
                     private val buttonsToDraw : Collection<IScreenControlsView> ) :
    EngineInfo(mainEngineLib, allLibs, buttonsToDraw ) {

    override val gameActivityClazz: Class<*> = SDL2GameActivity::class.java

    override val screenController: IScreenController by inject (
        named(SDL2ScreenController.SDL2_SCREEN_CONTROLLER_NAME))

    @Composable
    override fun DrawMouseIcon() {
        super.DrawMouseIcon()
        MouseIcon()
    }

    override fun setScreenResolution(screenWidth: Int, screenHeight: Int) {
        SDLSurface.fixedWidth = screenWidth
        SDLSurface.fixedHeight = screenHeight
    }
}