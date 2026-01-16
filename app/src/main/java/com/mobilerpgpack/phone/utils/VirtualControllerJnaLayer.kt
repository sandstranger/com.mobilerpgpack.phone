package com.mobilerpgpack.phone.utils

import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.sun.jna.Native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal object VirtualControllerJnaLayer {
    @Volatile
    private var jnaWasInit = false
    @Volatile
    private var joystickRegistered = false
    @Volatile
    private var joystickRegisteredInSDL = false

    private val scope : CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private external fun createVirtualController()

    private external fun destroyVirtualController()

    private external fun setVirtualAxis(axisX : Int, axisXValue : Float,
                                        axisY : Int, axisYValue : Float)

    fun initializeVirtualControllerAsync (virtualControllerLibraryName : String){
        if (!joystickRegistered) {
            joystickRegistered = true
            scope.launch {
                initializeJna(virtualControllerLibraryName)
                createVirtualController()
                withContext(Dispatchers.Main) {
                    joystickRegisteredInSDL = true
                }
            }
        }
    }

    fun destroyVirtualControllerAsync (engineInfo: IEngineInfo){
        if (joystickRegisteredInSDL && engineInfo.needToReInitGameControllers){
            joystickRegisteredInSDL = false
            scope.launch {
                destroyVirtualController()
                withContext(Dispatchers.Main) {
                    joystickRegistered = false
                }
            }
        }
    }

    fun setControllerAxis(axisX : Int, axisXValue : Float, axisY : Int, axisYValue : Float){
        if (joystickRegisteredInSDL){
            setVirtualAxis(axisX, axisXValue, axisY, axisYValue)
        }
    }

    private fun initializeJna (virtualControllerLibraryName : String){
        if (!jnaWasInit) {
            Native.register(VirtualControllerJnaLayer::class.java, virtualControllerLibraryName)
            jnaWasInit = true
        }
    }
}