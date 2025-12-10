package com.mobilerpgpack.phone.engine.engineinfo.sdl

import android.view.KeyEvent
import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.activity.SDL3GameActivity
import com.mobilerpgpack.phone.engine.engineinfo.EngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3MouseIconHelper
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3.SDL3ScreenController
import com.mobilerpgpack.phone.utils.ScreenResolution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.libsdl3.app.onKeyDown
import org.libsdl3.app.SDLActivity
import org.libsdl3.app.SDLSurface

abstract class SDL3EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    buttonsToDraw: Collection<IScreenControlsView>,
    activeEngineType: EngineTypes,
    pathToResourceFlow: Flow<String> = emptyFlow(),
    commandLineParamsFlow : Flow<String> = emptyFlow()) :
    EngineInfo(mainEngineLib, allLibs, buttonsToDraw,
        activeEngineType, pathToResourceFlow, commandLineParamsFlow) {

    private val mouseIcon: SDL3MouseIcon by inject()

    final override val gameActivityClazz: Class<*> = SDL3GameActivity::class.java

    final override val screenController: IScreenController by inject(
        named(SDL3ScreenController.SDL3_SCREEN_CONTROLLER_NAME)
    )

    override fun isMouseShown() = SDL3MouseIconHelper.isMouseShown()

    @Composable
    override fun DrawMouseIcon() {
        super.DrawMouseIcon()
        mouseIcon.DrawMouseIcon()
    }

    final override fun onUseSdlStandardTextInputValueChanged(useSdlTextStandardInput: Boolean) {
        SDLActivity.useStandardSDLInput = useSdlTextStandardInput
    }

    final override fun onBackPressed(): Boolean {
        if(!super.onBackPressed()){
            onKeyDown(KeyEvent.KEYCODE_ESCAPE, delayBeforeKeyRelease = 50L)
        }
        return true
    }

    override fun setScreenResolution(screenResolution: ScreenResolution) {
        SDLSurface.fixedWidth = screenResolution.screenWidth
        SDLSurface.fixedHeight = screenResolution.screenHeight
    }
}