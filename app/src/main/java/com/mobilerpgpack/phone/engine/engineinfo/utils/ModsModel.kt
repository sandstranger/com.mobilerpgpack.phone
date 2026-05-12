@file:OptIn(ExperimentalSerializationApi::class)

package com.mobilerpgpack.phone.engine.engineinfo.utils

import com.mobilerpgpack.phone.utils.ComposeImmutableList
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.MutableValue
import com.mobilerpgpack.phone.utils.writeTextSafely
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.util.Locale.getDefault

@Serializable
@JsonIgnoreUnknownKeys
abstract sealed class ModsModel : KoinComponent {
    abstract val jsonFileName: String

    private val modsCollection = ComposeImmutableList<Mod>()

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

    override fun toString() = Json.encodeToString(this)

    fun updateComposeModsList() = modsCollection.updateComposeList()

    fun load(inputString : String) {
        if (inputString.isNotEmpty()) {
            try {
                Json.decodeFromString<ModsModel>(inputString).let {
                    this.enableModsSupport.value = it.enableModsSupport.value
                    this.enableModsAutoUpdateInFolder.value = it.enableModsAutoUpdateInFolder.value
                    this.pathToModsFolder.value = it.pathToModsFolder.value
                    this.modsCount = it.modsCollection.sourceList.size
                    for (i in 0 until this.modsCount) {
                        this.modsCollection.sourceList[i].pathToMod.value =
                            it.modsCollection.sourceList[i].pathToMod.value
                    }
                    this.modsCollection.updateComposeList()
                    save()
                }
            } catch (_: Exception) { }
        }
    }

    fun save() = jsonFile.writeTextSafely(toString())

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