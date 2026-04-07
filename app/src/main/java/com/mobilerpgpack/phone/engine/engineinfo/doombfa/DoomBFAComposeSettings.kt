package com.mobilerpgpack.phone.engine.engineinfo.doombfa

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.SettingScreen
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class DoomBFAComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val prefsStorage : DoomBFAPreferencesStorage = koinInject(
            named(EngineTypes.Classic_RBDOOM_3_BFG.name))
        prefsStorage.apply {
            DrawCommandLinePreferences(commandLineArgs,
                commandLineArgsPrefsKey.name)
            DrawHorizontalDivider()
            RequestPath(stringResource(R.string.path_to_doom3_resources),
                pathToDoom3Resources,
                pathToDoom3ResourcesPreferenceKey)
            DrawHorizontalDivider()
            PreferenceItem(stringResource(R.string.graphics_settings)) {
                navController.navigate(GRAPHICS_SETTINGS_SCREEN)
            }
        }
    }

    @Composable
    private fun DrawGraphicsSettings(){
        val prefsStorage : DoomBFAPreferencesStorage = koinInject(
            named(EngineTypes.Classic_RBDOOM_3_BFG.name))
        prefsStorage.apply {
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.disable_postprocess_effects),
                disablePostProcessEffects,disablePostProcessEffectsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.skip_prelight_shadows),
                disablePrelightShadows,disablePrelightShadowsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.disable_shadows),
                disableShadows,disableShadowsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.disable_particles),
                disableParticles,disableParticlesPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.disable_bfg_new_ambient_effect),
                disableNewAmbients,disableNewAmbientsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.disable_blend_lights),
                disableBlendLights,disableBlendLightsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.disable_dynamic_textures),
                disableDynamicTextures,disableDynamicTexturesPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.disable_copy_textures),
                disableCopyTextures,disableCopyTexturesPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.skip_deforms),
                skipDeformsPrefs,skipDeformsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.use_shadow_mapping),
                useShadowMapping,useShadowMappingPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.disable_overlays),
                disableOverlays,disableOverlaysPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.use_light_depth_bounds),
                useLightDepthBounds,useLightDepthBoundsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.disable_intel_workarounds),
                disableIntelWorkarounds,disableIntelWorkaroundsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.use_shadow_depth_bounds),
                useLightDepthBounds,useLightDepthBoundsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.enable_dxt_hardware_support),
                enableDXTHardwareSupport,enableDXTHardwareSupportPrefsKey.name)
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