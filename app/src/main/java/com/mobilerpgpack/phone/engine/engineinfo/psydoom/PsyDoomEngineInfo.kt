package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.modsCanBeUsed
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class PsyDoomEngineInfo(mainEngineLib: String,
                        allLibs: Array<String>
) :
    SDL3EngineInfo (mainEngineLib, allLibs, activeEngineType = EngineTypes.PsyDoom) {

    private val modsModel : ModsModel by inject (named(EngineTypes.PsyDoom.toString()))

    private val psyDoomPreferencesStorage by inject <PsyDoomPreferencesStorage>(named(
        EngineTypes.PsyDoom.toString()))

    override val commandLineParams: String get() = psyDoomPreferencesStorage.psyDoomCommandLineArgsString.value!!
    override val preferencesStorage = psyDoomPreferencesStorage
    override val pathToResource get() = psyDoomPreferencesStorage.pathToPsyDoomCueFile.value!!
    override val requiredResourceExtensions = listOf(".cue", ".CUE")
    override val needToShowScreenControls = true
    override val allowedToEnableAngle = false
    override val mouseButtonsEventsCanBeInvoked = false
    override fun isMouseShown() = false
    override val loadGL4ES: Boolean = false
    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            return mutableListOf<String>().let {
                it += baseCommandLineArgs

                val pathToCue = psyDoomPreferencesStorage.pathToPsyDoomCueFile.value!!

                if (pathToCue.isNotEmpty() && File(pathToCue).exists() &&
                    !baseCommandLineArgs.contains(CUE_COMMAND)
                ) {
                    it += CUE_COMMAND
                    it += pathToCue
                }

                if (!baseCommandLineArgs.contains(FILE_COMMAND) && modsModel.modsCanBeUsed) {
                    it += FILE_COMMAND

                    modsModel.mods.forEach { mod: Mod ->
                        val pathToMod = mod.pathToMod.liveData.value
                        if (!pathToMod.isNullOrEmpty() && File(pathToMod).exists()) {
                            it += pathToMod
                        }
                    }
                }

                val enablePsyDoomMods = preferencesStorage.enablePsyDoomMods.value!!

                if (enablePsyDoomMods) {
                    val modsFolder = psyDoomPreferencesStorage.pathToPsyDoomModsFolder.value!!

                    if (modsFolder.isNotEmpty() && File(modsFolder).exists() &&
                        !baseCommandLineArgs.contains(DATA_DIR_COMMAND)
                    ) {
                        it += DATA_DIR_COMMAND
                        it += modsFolder
                    }
                }

                val usePistolStart = psyDoomPreferencesStorage.forcePistolStart.value!!

                if (usePistolStart && !baseCommandLineArgs.contains(PISTOL_START_COMMAND)) {
                    it += PISTOL_START_COMMAND
                }

                val recordDemos = psyDoomPreferencesStorage.recordDemos.value!!

                if (recordDemos && !baseCommandLineArgs.contains(RECORD_DEMOS_COMMAND)) {
                    it += RECORD_DEMOS_COMMAND
                }

                val turboMode = psyDoomPreferencesStorage.turboMode.value!!

                if (turboMode && !baseCommandLineArgs.contains(TURBO_COMMAND)) {
                    it += TURBO_COMMAND
                }

                val noMonsters = psyDoomPreferencesStorage.noMonsters.value!!

                if (noMonsters && !baseCommandLineArgs.contains(NO_MONSTERS_COMMAND)) {
                    it += NO_MONSTERS_COMMAND
                }

                val nmBossFixup = psyDoomPreferencesStorage.nmBossFixUp.value!!

                if (nmBossFixup && !baseCommandLineArgs.contains(BOSS_FIX_COMMAND)) {
                    it += BOSS_FIX_COMMAND
                }

                val host = psyDoomPreferencesStorage.host.value!!
                val addHost = host.isNotEmpty() && host.isNotBlank()

                val port = psyDoomPreferencesStorage.port.value!!
                val addPort = port > 0

                val peerType = enumValueOf<PeerType>(psyDoomPreferencesStorage.peerType.value!!)

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

    private external fun setPathToUserFolder (pathToUserFolder : String)

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(PsyDoomEngineInfo::class.java, mainLibraryName)
        setPathToUserFolder(pathToRootUserFolder)
    }

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