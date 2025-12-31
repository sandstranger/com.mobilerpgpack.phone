@file:OptIn(ExperimentalSerializationApi::class)

package com.mobilerpgpack.phone.engine.engineinfo.utils

import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.ComposeImmutableList
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.MutableValue
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.util.Locale.getDefault

@Serializable
@JsonIgnoreUnknownKeys
abstract sealed class ModsModel : KoinComponent {

    protected abstract val jsonFileName: String

    private val modsCollection = ComposeImmutableList<Mod>()

    private val _enableModsAutoUpdateInFolder = MutableValue<Boolean>()

    @Transient
    open val allowedModsExtensions: Collection<String> =
        listOf("wad", "pk3", "iwad", "ipk3", "ipk7").let {
            val result = mutableListOf<String>()
            result += it
            it.forEach { fileExtension ->
                result += fileExtension.uppercase(getDefault())
            }
            result
        }

    val enableModsSupport = MutableValue<Boolean>()

    val pathToModsFolder = MutableValue<String>()

    var enableModsAutoUpdateInFolder
        get() = _enableModsAutoUpdateInFolder.value!!
        set(value) {
            _enableModsAutoUpdateInFolder.value = value
        }

    var modsCount: Int
        get() = modsCollection.count!!
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
        _enableModsAutoUpdateInFolder.initialize(true) {
            save()
        }

        val assetsProvider : IAssetExtractor = get()
        assetsProvider.assetsFinishCopyListeners += {
            save()
        }
    }

    fun updateComposeModsList() = modsCollection.updateComposeList()

    fun save() {
        File(preferencesStorage.pathToRootUserFolder + File.separator + jsonFileName)
            .writeText(Json.encodeToString(this))
    }

    protected companion object {
        val preferencesStorage : PreferencesStorage = get(PreferencesStorage::class.java)

        inline fun <reified T> load(jsonFileName: String): T where T : ModsModel {
            val jsoFile = File(preferencesStorage.pathToRootUserFolder + File.separator + jsonFileName)
            val model = if (jsoFile.exists())
                Json.decodeFromString<T>(jsoFile.readText()) else
                T::class.java.getDeclaredConstructor().newInstance()
            return model.apply {
                initialize()
            }
        }
    }
}

val ModsModel.modsCanBeUsed
    get() = enableModsSupport.value!! && modsCount > 0 &&
            this.mods.any { mod -> !mod.pathToMod.value.isNullOrEmpty() && File(mod.pathToMod.value!!).exists() }