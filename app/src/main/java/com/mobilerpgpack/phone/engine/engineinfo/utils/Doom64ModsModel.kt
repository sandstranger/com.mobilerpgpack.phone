@file:OptIn(ExperimentalSerializationApi::class)

package com.mobilerpgpack.phone.engine.engineinfo.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
class Doom64ModsModel : ModsModel()  {

    override val jsonFileName get() = JSON_FILE_NAME

    @Transient
    override val allowedModsExtensions: Collection<String> = listOf("wad", "WAD")

    companion object{
        private const val JSON_FILE_NAME = "Doom64Mods.json"

        fun load () : Doom64ModsModel = load(JSON_FILE_NAME)
    }
}