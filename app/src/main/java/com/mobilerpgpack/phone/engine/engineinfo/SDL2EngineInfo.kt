package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.activity.SDL2GameActivity
import com.mobilerpgpack.phone.ui.screen.screencontrols.MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenController
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL2MouseIcon
import com.mobilerpgpack.phone.ui.screen.screencontrols.SDL2ScreenController
import com.mobilerpgpack.phone.utils.ScreenResolution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface

abstract class SDL2EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    buttonsToDraw: Collection<IScreenControlsView>,
    activeEngineType: EngineTypes,
    pathToResourceFlow: Flow<String>,
    commandLineParamsFlow : Flow<String> = emptyFlow()) :
    EngineInfo(mainEngineLib, allLibs, buttonsToDraw, activeEngineType,
        pathToResourceFlow, commandLineParamsFlow) {

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

    final override fun onUseSdlStandardTextInputValueChanged(useSdlTextStandardInput: Boolean) {
        SDLActivity.useStandardSDLInput = useSdlTextStandardInput
    }

    override fun setScreenResolution(screenResolution: ScreenResolution) {
        SDLSurface.fixedWidth = screenResolution.screenWidth
        SDLSurface.fixedHeight = screenResolution.screenHeight
    }
}