package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.activity.SDL3GameActivity
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL3MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL3MouseIconHelper
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL3ScreenController
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.libsdl3.app.SDLSurface

abstract class SDL3EngineInfo (private val mainEngineLib: String,
                      private val allLibs : Array<String>,
                      private val buttonsToDraw : Collection<IScreenControlsView>) :
    EngineInfo(mainEngineLib, allLibs, buttonsToDraw ) {

    private val mouseIcon : SDL3MouseIcon by inject ()

    override val gameActivityClazz: Class<*> = SDL3GameActivity::class.java

    override val screenController: IScreenController by inject (
        named(SDL3ScreenController.SDL3_SCREEN_CONTROLLER_NAME))

    override fun isMouseShown(): Int = if(SDL3MouseIconHelper.isMouseShown()) 1 else 0

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