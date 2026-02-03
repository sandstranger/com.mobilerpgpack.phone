package com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer

import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.enumPreferencesKey
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey

class VanillaConquerPreferencesStorage : PreferencesStorage () {
    val pathToRedAlertResourcesPrefsKey = stringPreferencesKey("path_to_red_alert_resources")
    val pathToTiberianDawnResourcesPrefsKey = stringPreferencesKey("path_to_tiberian_dawn_resources")
    val activeVanillaConquerGamePrefsKey = enumPreferencesKey<VanillaConquerGames>("active_vanilla_conquer_game")

    val pathToRedAlertResources = getStringValue(pathToRedAlertResourcesPrefsKey)
    val pathToTiberianDawnResources = getStringValue(pathToTiberianDawnResourcesPrefsKey)
    val activeVanillaConquerGame = getEnumValue(activeVanillaConquerGamePrefsKey,
        VanillaConquerGames::class.java, VanillaConquerGames.DefaultGame)
}