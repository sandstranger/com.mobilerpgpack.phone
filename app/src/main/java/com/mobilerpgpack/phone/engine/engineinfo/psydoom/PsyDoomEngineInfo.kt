package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.sun.jna.Function
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class PsyDoomEngineInfo(mainEngineLib: String,
                        allLibs: Array<String>,
                        buttonsToDraw: Collection<IScreenControlsView>,
                        commandLineParamsFlow : Flow<String>) :
    SDL2EngineInfo (mainEngineLib, allLibs, buttonsToDraw,activeEngineType = EngineTypes.PsyDoom,
        commandLineParamsFlow = commandLineParamsFlow) {

    private val psyDoomPreferencesStorage by inject <PsyDoomPreferencesStorage>(named(
        EngineTypes.PsyDoom.toString()))

    private val destroyVulkanSwapChainNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "destroyVulkanSwapChain")
    }

    private val recreateVulkanSwapChainNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "recreateVulkanSwapChain")
    }

    override val preferencesStorage = psyDoomPreferencesStorage

    override val pathToResource get() = psyDoomPreferencesStorage.pathToPsyDoomCueFile

    override val needToShowScreenControls = true

    override val mouseButtonsEventsCanBeInvoked = false

    override fun isMouseShown() = false

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            if (baseCommandLineArgs.contains(CUE_COMMAND)){
                return baseCommandLineArgs
            }

            return mutableListOf<String>().let {
                it.addAll(baseCommandLineArgs)

                runBlocking {
                    it += CUE_COMMAND
                    it += psyDoomPreferencesStorage.pathToPsyDoomCueFile.first()

                    val modsFolder = psyDoomPreferencesStorage.pathToPsyDoomModsFolder.first()

                    if (modsFolder.isNotEmpty()){
                        it += DATA_DIR_COMMAND
                        it += modsFolder
                    }

                    val usePistolStart = psyDoomPreferencesStorage.forcePistolStart.first()

                    if (usePistolStart){
                        it += "-pistolstart"
                    }

                    val recordDemos = psyDoomPreferencesStorage.recordDemos.first()

                    if (recordDemos){
                        it += "-record"
                    }

                    val turboMode = psyDoomPreferencesStorage.turboMode.first()

                    if (turboMode){
                        it += "-turbo"
                    }

                    val noMonsters = psyDoomPreferencesStorage.noMonsters.first()

                    if (noMonsters){
                        it += "-nomonsters"
                    }

                    val nmBossFixup = psyDoomPreferencesStorage.nmBossFixUp.first()

                    if (nmBossFixup){
                        it += "-nmbossfixup"
                    }

                    val host = psyDoomPreferencesStorage.host.first()
                    val addHost = host.isNotEmpty() && host.isNotBlank()

                    val port = psyDoomPreferencesStorage.port.first()
                    val addPort = port > 0

                    val peerType = enumValueOf<PeerType>(psyDoomPreferencesStorage.peerType.first())

                    when (peerType) {
                        PeerType.Server -> {
                            it +="-server"
                            if (addPort){
                                it += port.toString()
                            }
                        }
                        PeerType.Client -> {
                            if (addPort || addHost){
                                it += CLIENT_COMMAND

                                it += if (!addPort){
                                    host
                                } else{
                                    if (!addHost){
                                        "localhost:$port"
                                    } else{
                                        "$host:$port"
                                    }
                                }
                            }
                        }
                    }
                }

                it.toTypedArray()
            }
        }

    override fun onResume() = recreateVulkanSwapChainNativeDelegate.invokeVoid(null)

    override fun onPause() = destroyVulkanSwapChainNativeDelegate.invokeVoid(null)

    private companion object{
        private const val CUE_COMMAND = "-cue"
        private const val DATA_DIR_COMMAND = "-datadir"
        private const val CLIENT_COMMAND = "-client"
    }
}