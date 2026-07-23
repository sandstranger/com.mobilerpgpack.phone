package com.mobilerpgpack.phone.engine.engineinfo.doombfa

import android.content.Context
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.utils.supportedRefreshRates
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class DoomBFAEngineInfo(mainEngineLib: String, allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, EngineTypes.Classic_RBDOOM_3_BFG) {
    private val context : Context by inject ()
    private val homeDirectoryFolder : File by inject { parametersOf(HOME_DIRECTORY_NAME) }
    private val doomBFAPreferenceStorage by inject<DoomBFAPreferencesStorage>(
        named(EngineTypes.Classic_RBDOOM_3_BFG.name))
    private val baseGameDirName by lazy {
        return@lazy preferencesStorage.run {
            val pathToModsDirectory = pathDoom3ModsDir.value!!
            val modsDir = File(pathToModsDirectory)
            return@run if (enableDoom3Mods.value!! && modsDir.exists() && pathToModsDirectory.isNotEmpty() &&
                pathToModsDirectory.startsWith(
                    pathToResource
                )
            ) {
                modsDir.name
            } else {
                BASE_GAME
            }
        }
    }

    val textureCacheDir by lazy { File(context.cacheDir,"doom3_texture_cache") }
    @Volatile
    var isTexturesResourcesDeleting = false

    override val engineReadyToStart: Boolean get() = !isTexturesResourcesDeleting
    override val pathToResource: String get() = doomBFAPreferenceStorage.pathToDoom3Resources.value!!
    override val preferencesStorage get() = doomBFAPreferenceStorage
    override val commandLineParams: String get() = doomBFAPreferenceStorage.commandLineArgs.value!!
    override val loadGL4ES = false
    override val callExitProcessOnDestroy = true
    override val touchFullScreenModeCanBeUsed = false
    override val targetGLESVersion get() = preferencesStorage.targetGLESVersion.value!!.glesIntVersion
    override val mouseButtonsEventsCanBeInvoked: Boolean get() = needToInvokeMouseButtonsEvents()

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs
            return with(mutableListOf<String>()) {
                this += baseCommandLineArgs
                preferencesStorage.apply {
                    this@with += buildCommand("r_skipInteractionFastPath", "1")
                    this@with += buildCommand("r_useLightStencilSelect", "0")
                    this@with += buildCommand("r_skipPostProcess",
                        disablePostProcessEffects.value!!)
                    this@with += buildCommand("r_skipShadows", disableShadows.value!!)
                    this@with += buildCommand("r_skipParticles", disableParticles.value!!)
                    this@with += buildCommand("r_skipNewAmbient", disableNewAmbients.value!!)
                    this@with += buildCommand("r_skipBlendLights", disableBlendLights.value!!)
                    this@with += buildCommand("r_skipDynamicTextures", disableDynamicTextures.value!!)
                    this@with += buildCommand("r_skipCopyTexture", disableCopyTextures.value!!)
                    this@with += buildCommand("r_skipDeforms", skipDeforms.value!!)
                    this@with += buildCommand("r_skipBlendLights", disableBlendLights.value!!)
                    this@with += buildCommand("r_skipOverlays", disableOverlays.value!!)
                    this@with += buildCommand("r_useLightDepthBounds", useLightDepthBounds.value!!)
                    this@with += buildCommand("r_skipIntelWorkarounds", disableIntelWorkarounds.value!!)
                    this@with += buildCommand("r_useShadowDepthBounds", useShadowDepthBounds.value!!)
                    this@with += buildCommand("r_skipPrelightShadows", disablePrelightShadows.value!!)
                    this@with += buildCommand("r_skipTranslucent", disableTranslucent.value!!)
                    this@with += buildCommand("r_skipFogLights", disableFogLights.value!!)
                    this@with += buildCommand("r_skipSpecular", disableSpecular.value!!)
                    this@with += buildCommand("r_skipInteractions", disableLightInteractions.value!!)
                    val cullingValue = if (simplifyCulling.value!!) "1" else "2"
                    this@with += buildCommand("r_useLightPortalCulling", cullingValue)
                    this@with += buildCommand("r_useLightAreaCulling", cullingValue)
                    this@with += buildCommand("r_shadowMapImageSize", shadowMapImageSize.value!!)
                    this@with += buildCommand("r_useStateCaching", true)
                    this@with += buildCommand("r_skipStaticShadows", disableStaticShadows.value!!)
                    this@with += buildCommand("r_skipDynamicShadows",
                        disableDynamicShadows.value!!)
                    this@with += buildCommand("r_useShadowPreciseInsideTest",
                        useShadowPreciseInsideTest.value!!)
                    this@with += buildCommand("r_lodMaterialDistance",
                        lodDistance.value!!.toString())
                    this@with += buildCommand("r_displayRefresh",framePacingTargetFPS.value!!.toString())
                    this@with += buildCommand("r_maxAnisotropicFiltering",
                        anisotropyLevel.value!!.toString())
                    this@with += buildCommand(GAME_COMMAND,baseGameDirName )
                }
                toTypedArray()
            }
        }

    private external fun setPathsToResources (pathToHomeFolder : String, pathToResourcesFolder : String)
    private external fun setHardwareDXTSupport(enableHardwareDXTSupport : Boolean)
    private external fun setGLESVersion (targetGLESVersion: Int)
    private external fun setRefreshRates(targetRefreshRates : IntArray, arraySize: Int)
    private external fun enableTexturesShrinking (enableTexturesShrinking : Boolean)
    private external fun setTextureCacheData(enableTextureCache : Boolean, pathToTextureCacheDir : String)
    private external fun ClearRamCache()
    private external fun clearBlobShaderCache()
    private external fun updateGLSynchronizationState(enableGLSynchronization : Boolean)
    private external fun setBaseGameDir (baseGameDirName : String)
    private external fun updateOnScreenControlsActiveState (onScreenControlsActive : Boolean)

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        textureCacheDir.mkdirs()
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(DoomBFAEngineInfo::class.java, mainLibraryName)
        setPathsToResources(homeDirectoryFolder.absolutePath,
            pathToResource)
        preferencesStorage.apply {
            setHardwareDXTSupport(enableDXTHardwareSupport.value!!)
            enableTexturesShrinking(enableTexturesShrinking.value!!)
            setTextureCacheData(enableETC2TextureCache.value!!,
                textureCacheDir.absolutePath)
            updateGLSynchronizationState(enableGLSynchronization.value!!)
            updateOnScreenControlsActiveState(!hideScreenControls.value!!)
        }
        setGLESVersion(GLES_320_VERSION)
        activity.supportedRefreshRates.toIntArray().apply {
            setRefreshRates(this, size)
        }
        setBaseGameDir(baseGameDirName)
    }

    override fun onNativeTrimMemory(aggressive: Boolean) {
        super.onNativeTrimMemory(aggressive)
        ClearRamCache()
        clearBlobShaderCache()
    }

    override fun onPause() {
        super.onPause()
        ClearRamCache()
    }

    companion object{
        private const val GAME_COMMAND = "fs_game"
        private const val BASE_GAME = "base"
        const val DEFAULT_SHADOW_IMAGE_MAP_SIZE = "256"
        const val HOME_DIRECTORY_NAME = "doombfa"
        val shadowMapImageSizes = listOf(DEFAULT_SHADOW_IMAGE_MAP_SIZE, "512", "1024")

        private fun buildCommand(commandKey : String, commandValue : String) =
            arrayOf("+set",commandKey, commandValue)

        private fun buildCommand(commandKey : String, commandValue : Boolean) =
            buildCommand(commandKey, if(commandValue) "1" else "0")
    }
}