package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.modsCanBeUsed
import com.sun.jna.Function
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class PsyDoomEngineInfo(mainEngineLib: String,
                        allLibs: Array<String>,
                        commandLineParams: String ) :
    SDL2EngineInfo (mainEngineLib, allLibs, activeEngineType = EngineTypes.PsyDoom,
        commandLineParams = commandLineParams) {

    private val modsModel : ModsModel by inject (named(EngineTypes.PsyDoom.toString()))

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

    private val registerJoysticksNativeDelegate by lazy {
        Function.getFunction(mainEngineLib, "RegisterJoysticks")
    }

    override val preferencesStorage = psyDoomPreferencesStorage

    override val pathToResource get() = psyDoomPreferencesStorage.pathToPsyDoomCueFile

    override val requiredResourceExtensions = listOf(".cue", ".CUE")

    override val needToShowScreenControls = true

    override val mouseButtonsEventsCanBeInvoked = false

    override fun isMouseShown() = false

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            return mutableListOf<String>().let {
                it += baseCommandLineArgs

                val pathToCue = psyDoomPreferencesStorage.pathToPsyDoomCueFile

                if (pathToCue.isNotEmpty() && File(pathToCue).exists() &&
                    !baseCommandLineArgs.contains(CUE_COMMAND)
                ) {
                    it += CUE_COMMAND
                    it += pathToCue
                }

                if (!baseCommandLineArgs.contains(FILE_COMMAND) && modsModel.modsCanBeUsed) {
                    it += FILE_COMMAND

                    modsModel.mods.forEach { mod: Mod ->
                        if (!mod.pathToMod.value.isNullOrEmpty() && File(mod.pathToMod.value!!).exists()) {
                            it += mod.pathToMod.value!!
                        }
                    }
                }

                val enablePsyDoomMods = preferencesStorage.enablePsyDoomMods

                if (enablePsyDoomMods) {
                    val modsFolder = psyDoomPreferencesStorage.pathToPsyDoomModsFolder

                    if (modsFolder.isNotEmpty() && File(modsFolder).exists() &&
                        !baseCommandLineArgs.contains(DATA_DIR_COMMAND)
                    ) {
                        it += DATA_DIR_COMMAND
                        it += modsFolder
                    }
                }

                val usePistolStart = psyDoomPreferencesStorage.forcePistolStart

                if (usePistolStart && !baseCommandLineArgs.contains(PISTOL_START_COMMAND)) {
                    it += PISTOL_START_COMMAND
                }

                val recordDemos = psyDoomPreferencesStorage.recordDemos

                if (recordDemos && !baseCommandLineArgs.contains(RECORD_DEMOS_COMMAND)) {
                    it += RECORD_DEMOS_COMMAND
                }

                val turboMode = psyDoomPreferencesStorage.turboMode

                if (turboMode && !baseCommandLineArgs.contains(TURBO_COMMAND)) {
                    it += TURBO_COMMAND
                }

                val noMonsters = psyDoomPreferencesStorage.noMonsters

                if (noMonsters && !baseCommandLineArgs.contains(NO_MONSTERS_COMMAND)) {
                    it += NO_MONSTERS_COMMAND
                }

                val nmBossFixup = psyDoomPreferencesStorage.nmBossFixUp

                if (nmBossFixup && !baseCommandLineArgs.contains(BOSS_FIX_COMMAND)) {
                    it += BOSS_FIX_COMMAND
                }

                val host = psyDoomPreferencesStorage.host
                val addHost = host.isNotEmpty() && host.isNotBlank()

                val port = psyDoomPreferencesStorage.port
                val addPort = port > 0

                val peerType = enumValueOf<PeerType>(psyDoomPreferencesStorage.peerType)

                when (peerType) {
                    PeerType.Server -> {
                        if (addPort && !baseCommandLineArgs.contains(SERVER_COMMAND)) {
                            it += "-server"
                            it += port.toString()
                        }
                    }

                    PeerType.Client -> {
                        if ((addPort || addHost) && !baseCommandLineArgs.contains(CLIENT_COMMAND)) {
                            it += CLIENT_COMMAND

                            it += if (!addPort) {
                                host
                            } else {
                                if (!addHost) {
                                    "localhost:$port"
                                } else {
                                    "$host:$port"
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

    override fun registerJoysticks() = registerJoysticksNativeDelegate.invokeVoid(null)

    private companion object{
        private const val FILE_COMMAND = "-file"
        private const val CUE_COMMAND = "-cue"
        private const val DATA_DIR_COMMAND = "-datadir"
        private const val CLIENT_COMMAND = "-client"
        private const val NO_MONSTERS_COMMAND = "-nomonsters"
        private const val PISTOL_START_COMMAND = "-pistolstart"
        private const val RECORD_DEMOS_COMMAND = "-record"
        private const val TURBO_COMMAND = "-turbo"
        private const val SERVER_COMMAND = "-server"
        private const val BOSS_FIX_COMMAND = "-nmbossfixup"
    }
}