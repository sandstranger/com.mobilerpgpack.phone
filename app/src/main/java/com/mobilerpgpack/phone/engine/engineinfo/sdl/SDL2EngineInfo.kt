package com.mobilerpgpack.phone.engine.engineinfo.sdl

import android.view.KeyEvent
import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.activity.SDL2GameActivity
import com.mobilerpgpack.phone.engine.engineinfo.EngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl2.SDL2ScreenController
import com.mobilerpgpack.phone.utils.ScreenResolution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface
import org.libsdl.app.onKeyDown

abstract class SDL2EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    activeEngineType: EngineTypes,
    pathToResourceFlow: Flow<String> = emptyFlow(),
    commandLineParamsFlow : Flow<String> = emptyFlow()) :
    EngineInfo(mainEngineLib, allLibs, activeEngineType,
        pathToResourceFlow, commandLineParamsFlow) {

    private val mouseIcon: SDL2MouseIcon by inject()

    final override val gameActivityClazz: Class<*> = SDL2GameActivity::class.java

    final override val screenController: IScreenController by inject(
        named(SDL2ScreenController.SDL2_SCREEN_CONTROLLER_NAME)
    )

    override fun isMouseShown() = SDLActivity.isMouseShown()

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