package com.mobilerpgpack.phone.utils

import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.sun.jna.Native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named

internal class VirtualControllerJnaLayer : KoinComponent {
    @Volatile
    private var jnaWasInit = false
    @Volatile
    private var joystickRegistered = false
    @Volatile
    private var joystickRegisteredInSDL = false

    private val scope : CoroutineScope by inject (
        named(KoinModulesProvider.BACKGROUND_THREAD_COROUTINE_KEY))
    private val preferencesStorage : PreferencesStorage by inject ()

    private external fun createVirtualController()
    private external fun destroyVirtualController()
    private external fun setVirtualAxis(axisX : Int, axisXValue : Float,
                                        axisY : Int, axisYValue : Float)

    fun initializeJna (virtualControllerLibraryName : String) {
        if (!jnaWasInit) {
            jnaWasInit = true
            Native.register(VirtualControllerJnaLayer::class.java, virtualControllerLibraryName)
        }
    }

    fun initializeVirtualControllerAsync (){
        if (!joystickRegistered) {
            joystickRegistered = true
            scope.launch {
                createVirtualController()
                joystickRegisteredInSDL = true
            }
        }
    }

    fun destroyVirtualControllerAsync (engineInfo: IEngineInfo){
        if (joystickRegisteredInSDL && engineInfo.needToReInitGameControllers){
            joystickRegisteredInSDL = false
            scope.launch {
                destroyVirtualController()
                joystickRegistered = false
            }
        }
    }

    fun setControllerAxis(axisX : Int, axisXValue : Float, axisY : Int, axisYValue : Float){
        if (joystickRegisteredInSDL){
            setVirtualAxis(axisX, axisXValue, axisY, axisYValue)
        }
    }
}