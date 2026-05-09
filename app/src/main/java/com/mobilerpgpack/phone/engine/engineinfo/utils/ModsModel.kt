@file:OptIn(ExperimentalSerializationApi::class)

package com.mobilerpgpack.phone.engine.engineinfo.utils

import com.mobilerpgpack.phone.utils.ComposeImmutableList
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.MutableValue
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.writeTextSafely
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parameterSetOf
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.util.Locale.getDefault

@Serializable
@JsonIgnoreUnknownKeys
abstract sealed class ModsModel : KoinComponent {

    protected abstract val jsonFileName: String

    private val modsCollection = ComposeImmutableList<Mod>()

    private val preferencesStorage : PreferencesStorage by inject ()

    private val jsonFile : File by inject { parametersOf(jsonFileName) }

    val enableModsAutoUpdateInFolder = MutableValue<Boolean>()

    @Transient
    open val allowedModsExtensions: Collection<String> =
        listOf("wad", "pk3","pk7", "iwad", "ipk3", "ipk7").let {
            val result = mutableListOf<String>()
            result += it
            it.forEach { fileExtension ->
                result += fileExtension.uppercase(getDefault())
            }
            result
        }

    val enableModsSupport = MutableValue<Boolean>()

    val pathToModsFolder = MutableValue<String>()

    val modsCountAsLiveData get() = modsCollection.countAsLiveData

    var modsCount: Int
        get() = modsCollection.count
        set(value) {
            modsCollection.count = value
            save()
        }

    val mods get() = modsCollection.sourceList

    val modsComposeCollection get() = modsCollection.composeList

    protected open fun initialize() {
        modsCollection.initialize(defaultValue = { Mod() })
        enableModsSupport.initialize(false) {
            save()
        }
        enableModsAutoUpdateInFolder.initialize(true) {
            save()
        }

        val assetsProvider : IAssetExtractor = get()
        assetsProvider.assetsFinishCopyListeners += {
            save()
        }
    }

    fun updateComposeModsList() = modsCollection.updateComposeList()

    fun save() = jsonFile.writeTextSafely(Json.encodeToString(this))

    protected companion object {
        inline fun <reified T> load(jsonFileName: String): T where T : ModsModel {
            val jsonFile : File = get(File::class.java, parameters = { parametersOf(jsonFileName) })
            val model = if (jsonFile.exists())
                Json.decodeFromString<T>(jsonFile.readText()) else
                T::class.java.getDeclaredConstructor().newInstance()
            return model.apply {
                initialize()
            }
        }
    }
}

val ModsModel.modsCanBeUsed
    get() = enableModsSupport.liveData.value!! && modsCount > 0 &&
            this.mods.any { mod -> !mod.pathToMod.liveData.value.isNullOrEmpty() && File(mod.pathToMod.liveData.value!!).exists() }