package com.mobilerpgpack.phone.engine.engineinfo.doombfa.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.DoomBFAEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.DoomBFAPreferencesStorage
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.GLESVersions
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.SettingScreen
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class DoomBFAComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val prefsStorage : DoomBFAPreferencesStorage = koinInject(
            named(EngineTypes.Classic_RBDOOM_3_BFG.name)
        )
        prefsStorage.apply {
            val enableModsSupport = enableDoom3Mods.getComposableValue()
            DrawCommandLinePreferences(
                commandLineArgs,
                commandLineArgsPrefsKey.name
            )
            DrawHorizontalDivider()
            RequestPath(
                stringResource(R.string.path_to_doom3_resources),
                pathToDoom3Resources,
                pathToDoom3ResourcesPreferenceKey
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.enable_mods_support),
                enableModsSupport, enableDoom3ModsPrefsKey.name
            )
            DrawHorizontalDivider()
            if (enableModsSupport) {
                RequestPath(
                    stringResource(R.string.path_to_common_mods_folder),
                    pathDoom3ModsDir,
                    pathDoom3ModsDirPrefsKey, requestMode = RequestPathMode.Directory
                )
                DrawHorizontalDivider()
            }

            PreferenceItem(stringResource(R.string.graphics_settings)) {
                navController.navigate(GRAPHICS_SETTINGS_SCREEN)
            }
        }
    }

    @Composable
    private fun DrawGraphicsSettings(){
        val prefsStorage : DoomBFAPreferencesStorage = koinInject(
            named(EngineTypes.Classic_RBDOOM_3_BFG.name)
        )
        prefsStorage.apply {
            DrawTitleText(stringResource(R.string.graphics_settings))
            SwitchPreferenceItem(
                stringResource(R.string.enable_gl_synchronization),
                enableGLSynchronization, enableGLSynchronizationPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.enable_textures_shrinking_support),
                enableTexturesShrinking, enableTexturesShrinkingPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.enable_etc_textures_cache),
                enableETC2TextureCache, enableETC2TextureCachePrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_postprocess_effects),
                disablePostProcessEffects, disablePostProcessEffectsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.skip_prelight_shadows),
                disablePrelightShadows, disablePrelightShadowsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_shadows),
                disableShadows, disableShadowsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.skip_static_shadows),
                disableStaticShadows, disableStaticShadowsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.skip_dynamic_shadows),
                disableDynamicShadows, disableDynamicShadowsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.use_shadow_precise_inside_test),
                useShadowPreciseInsideTest, useShadowPreciseInsideTestPrefsKey.name
            )
            DrawHorizontalDivider()
            EditTextPreferenceItem(stringResource(R.string.lod_distance), lodDistance){
                this.setIntValue(lodDistancePrefsKey, it.coerceAtLeast(50))
            }
            DrawHorizontalDivider()
            EditTextPreferenceItem(stringResource(R.string.anisotropy_level), anisotropyLevel){
                this.setIntValue(anisotropyLevelPrefsKey, it.coerceIn(1,16))
            }
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_particles),
                disableParticles, disableParticlesPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_bfg_new_ambient_effect),
                disableNewAmbients, disableNewAmbientsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_blend_lights),
                disableBlendLights, disableBlendLightsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_dynamic_textures),
                disableDynamicTextures, disableDynamicTexturesPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_copy_textures),
                disableCopyTextures, disableCopyTexturesPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.skip_deforms),
                skipDeforms, skipDeformsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_overlays),
                disableOverlays, disableOverlaysPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.use_light_depth_bounds),
                useLightDepthBounds, useLightDepthBoundsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_intel_workarounds),
                disableIntelWorkarounds, disableIntelWorkaroundsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.use_shadow_depth_bounds),
                useShadowDepthBounds, useShadowDepthBoundsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_translucent),
                disableTranslucent, disableTranslucentPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_fog_lights),
                disableFogLights, disableFogLightsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_specular),
                disableSpecular, disableSpecularPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.disable_light_interactions),
                disableLightInteractions, disableLightInteractionsPrefsKey.name
            )
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.simplify_culling),
                simplifyCulling, simplifyCullingPrefsKey.name
            )
            DrawHorizontalDivider()
            ListPreferenceItem(
                stringResource(R.string.shadow_map_image_size),
                shadowMapImageSize.getComposableValue(DoomBFAEngineInfo.DEFAULT_SHADOW_IMAGE_MAP_SIZE),
                DoomBFAEngineInfo.shadowMapImageSizes
            ) {
                setStringValue(shadowMapImageSizePrefsKey, it)
            }
            DrawHorizontalDivider()
            SwitchPreferenceItem(
                stringResource(R.string.enable_dxt_hardware_support),
                enableDXTHardwareSupport, enableDXTHardwareSupportPrefsKey.name
            )
            DrawHorizontalDivider()
        }
    }

    data class DoomBFAGraphicsScreen(private val composeSettings: DoomBFAComposeSettings) :
        SettingScreen(GRAPHICS_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            composeSettings.DrawGraphicsSettings()
    }

    private companion object{
        private const val GRAPHICS_SETTINGS_SCREEN = "doom3_bfg_edition_graphics_settings_screen"
    }
}