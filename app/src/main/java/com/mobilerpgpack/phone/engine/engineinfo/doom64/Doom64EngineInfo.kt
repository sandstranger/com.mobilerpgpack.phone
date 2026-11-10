package com.mobilerpgpack.phone.engine.engineinfo.doom64

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.modsCanBeUsed
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File
import kotlin.collections.contains

open class Doom64EngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, EngineTypes.Doom64ExPlus) {
    private val modsModel : ModsModel by inject (named(EngineTypes.Doom64ExPlus.toString()))

    override val gl4esShaderCacheFolderName = "doom64_gl4es_cache"
    final override val commandLineParams: String get() = preferencesStorage.doom64CommandLineArgsString.value!!
    final override val pathToResource get() = preferencesStorage.pathToDoom64MainWadsFolder.value!!
    final override val touchFullScreenModeCanBeUsed = false
    final override val enableNGGL4ESSimpleShaderConv = false
    final override val loadGL4ES = true
    final override val supportRenderChanges = true

    private external fun MouseCursorCanBeDrawn() : Boolean
    private external fun setPathsToResources (pathToWadsFolder : String, pathToUserFolder : String)
    private external fun updateMaxAnisotropyValue (targetAnisotropyValue : Int)
    private external fun updateOnScreenControlsState (onScreenControlsActive : Boolean)

    final override fun onNativeLibrariesLoadedBeforeStart() {
        super.onNativeLibrariesLoadedBeforeStart()
        Native.register(Doom64EngineInfo::class.java, mainLibraryName)
        setPathsToResources(pathToResource, getPathToDoom64UserFolder())
        preferencesStorage.apply {
            updateMaxAnisotropyValue(doom64AnisotropyTexturesValue.value!!)
            updateOnScreenControlsState(!hideScreenControls.value!!)
        }
    }

    final override fun isMouseShown() = MouseCursorCanBeDrawn()

    final override fun buildCustomCommandLineArgs () : Collection<String>{
        return mutableListOf<String>().also {
            it += buildNetworkCommandLineArgs()

            if (modsModel.modsCanBeUsed){
                it += FILE_COMMAND
                modsModel.mods.forEach { mod : Mod ->
                    val pathToMode = mod.pathToMod.liveData.value
                    if (!pathToMode.isNullOrEmpty() && File(pathToMode).exists()){
                        it+=pathToMode
                    }
                }
            }

            val pathToDoom64ModsFolder = getPathToDoom64ModsFolder()

            if (pathToDoom64ModsFolder.isNotEmpty()){
                it +=MODS_COMMAND
                it +=pathToDoom64ModsFolder
            }
        }
    }


    private fun buildNetworkCommandLineArgs(): Collection<String> {
        val settings = preferencesStorage
        val networkMode = settings.doom64NetworkMode.value ?: Doom64NetworkMode.Disabled
        if (networkMode == Doom64NetworkMode.Disabled) {
            return emptyList()
        }

        val args = mutableListOf<String>()
        val port = settings.doom64CoopPort.value?.coerceIn(1, 65535) ?: DEFAULT_COOP_PORT
        args += listOf(PORT_COMMAND, port.toString())

        settings.doom64CoopPlayerName.value?.trim()?.takeIf { it.isNotEmpty() }?.let { playerName ->
            args += listOf(PLAYER_NAME_COMMAND, playerName)
        }

        when (networkMode) {
            Doom64NetworkMode.Disabled -> Unit
            Doom64NetworkMode.Host -> {
                args += SERVER_COMMAND
                args += listOf(
                    NODES_COMMAND,
                    (settings.doom64CoopPlayersToStart.value ?: DEFAULT_COOP_PLAYERS)
                        .coerceIn(1, MAX_COOP_PLAYERS).toString(),
                    WARP_COMMAND,
                    (settings.doom64CoopMap.value ?: DEFAULT_COOP_MAP).coerceIn(1, MAX_COOP_MAP)
                        .toString(),
                    SKILL_COMMAND,
                    (settings.doom64CoopSkill.value ?: DEFAULT_COOP_SKILL).coerceIn(1, MAX_COOP_SKILL)
                        .toString()
                )

                if (settings.doom64CoopDeathmatch.value == true) args += DEATHMATCH_COMMAND
                if (settings.doom64CoopRespawnMonsters.value == true) args += RESPAWN_COMMAND
                if (settings.doom64CoopRespawnItems.value == true) args += RESPAWN_ITEMS_COMMAND
                if (settings.doom64CoopFastMonsters.value == true) args += FAST_MONSTERS_COMMAND
                if (settings.doom64CoopNoMonsters.value == true) args += NO_MONSTERS_COMMAND

                args += listOf(
                    SET_VARS_COMMAND,
                    FRIENDLY_FIRE_CVAR,
                    if (settings.doom64CoopFriendlyFire.value == true) "1" else "0",
                    KEEP_ITEMS_CVAR,
                    if (settings.doom64CoopKeepItems.value == true) "1" else "0"
                )
            }

            Doom64NetworkMode.Join -> {
                val address = settings.doom64CoopServerAddress.value?.trim()
                if (address.isNullOrEmpty()) {
                    return emptyList()
                }
                args += listOf(CONNECT_COMMAND, address)
            }

            Doom64NetworkMode.AutoJoin -> args += AUTOJOIN_COMMAND
        }

        return args
    }

    protected open fun getPathToDoom64UserFolder() =
        pathToRootUserFolder + File.separator + "doom64ex-plus" + File.separator

    private fun getPathToDoom64ModsFolder(): String {
        val enableDoom64Mods = preferencesStorage.enableDoom64Mods.value!!

        if (!enableDoom64Mods) {
            return ""
        }

        var pathToDoom64ModsFolder = preferencesStorage.pathToDoom64ModsFolder.value!!

        if (pathToDoom64ModsFolder.isEmpty()){
            return ""
        }

        val pathToDoom64ModsFolderExists = File(pathToDoom64ModsFolder).exists()

        if (!pathToDoom64ModsFolderExists) {
            pathToDoom64ModsFolder = ""
        }

        return pathToDoom64ModsFolder
    }

    private companion object {
        private const val FILE_COMMAND = "-file"
        private const val MODS_COMMAND = "-mod"

        private const val DEFAULT_COOP_PORT = 2342
        private const val DEFAULT_COOP_PLAYERS = 2
        private const val DEFAULT_COOP_MAP = 1
        private const val DEFAULT_COOP_SKILL = 3
        private const val MAX_COOP_PLAYERS = 4
        private const val MAX_COOP_MAP = 32
        private const val MAX_COOP_SKILL = 5

        private const val SERVER_COMMAND = "-server"
        private const val CONNECT_COMMAND = "-connect"
        private const val AUTOJOIN_COMMAND = "-autojoin"
        private const val PORT_COMMAND = "-port"
        private const val PLAYER_NAME_COMMAND = "-playername"
        private const val NODES_COMMAND = "-nodes"
        private const val WARP_COMMAND = "-warp"
        private const val SKILL_COMMAND = "-skill"
        private const val DEATHMATCH_COMMAND = "-deathmatch"
        private const val RESPAWN_COMMAND = "-respawn"
        private const val RESPAWN_ITEMS_COMMAND = "-respawnitem"
        private const val FAST_MONSTERS_COMMAND = "-fast"
        private const val NO_MONSTERS_COMMAND = "-nomonsters"
        private const val SET_VARS_COMMAND = "-setvars"
        private const val FRIENDLY_FIRE_CVAR = "sv_friendlyfire"
        private const val KEEP_ITEMS_CVAR = "sv_keepitems"
    }
}

