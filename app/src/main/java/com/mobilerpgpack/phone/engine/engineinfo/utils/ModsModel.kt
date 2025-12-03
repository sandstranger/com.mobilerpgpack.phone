@file:OptIn(ExperimentalSerializationApi::class)

package com.mobilerpgpack.phone.engine.engineinfo.utils

import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.ComposeImmutableList
import com.mobilerpgpack.phone.utils.MutableValue
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import java.io.File

@Serializable
@JsonIgnoreUnknownKeys
sealed class ModsModel {

    protected abstract val jsonFileName : String

    private val modsCollection = ComposeImmutableList<Mod>()

    @Transient
    private val jsoFile = File(pathToRootUserFolder + File.separator + jsonFileName)

    val enableModsSupport = MutableValue<Boolean>()

    var modsCount : Int
        get() = modsCollection.count!!
        set(value) {
            modsCollection.count = value
            save()
        }

    val pathToMods get() = modsCollection.sourceList

    val pathToModsComposeCollection get() = modsCollection.composeList

    init {
        modsCollection.initialize(defaultValue = { Mod() })
        enableModsSupport.initialize(false){
            save()
        }
    }

    fun updateComposeModsList () = modsCollection.updateComposeList()

    fun save () = jsoFile.writeText(Json.encodeToString(this))

    companion object{
        val pathToRootUserFolder : String = get(String()::class.java,
            named(KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY))

        inline fun <reified T> load(jsonFileName : String ): T where T : ModsModel {
            val jsoFile = File(pathToRootUserFolder + File.separator + jsonFileName)

            return if (jsoFile.exists())
                Json.decodeFromString<T>(jsoFile.readText()) else
                T::class.java.getDeclaredConstructor().newInstance()
        }
    }
}