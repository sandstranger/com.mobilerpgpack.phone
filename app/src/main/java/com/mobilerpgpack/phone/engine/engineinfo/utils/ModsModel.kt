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
import java.util.Locale.getDefault

@Serializable
@JsonIgnoreUnknownKeys
abstract sealed class ModsModel {

    protected abstract val jsonFileName : String

    private val modsCollection = ComposeImmutableList<Mod>()

    private val _enableModsAutoUpdateInFolder = MutableValue<Boolean>()

    @Transient
    private val jsoFile = File(pathToRootUserFolder + File.separator + jsonFileName)

    @Transient
    open val allowedModsExtensions : Collection<String> = listOf("wad", "pk3", "iwad", "ipk3", "ipk7").let {
        val result = mutableListOf<String>()
        result += it
        it.forEach { fileExtension ->
            result += fileExtension.uppercase(getDefault())
        }
        result
    }

    open val enableModsSupport = MutableValue<Boolean>()

    open val pathToModsFolder = MutableValue<String>()

    open var enableModsAutoUpdateInFolder
        get() = _enableModsAutoUpdateInFolder.value!!
        set(value) {
            _enableModsAutoUpdateInFolder.value = value
        }

    open var modsCount : Int
        get() = modsCollection.count!!
        set(value) {
            modsCollection.count = value
            save()
        }

    open val mods get() = modsCollection.sourceList

    open val modsComposeCollection get() = modsCollection.composeList

    protected open fun initialize(){
        modsCollection.initialize(defaultValue = { Mod() })
        enableModsSupport.initialize(false){
            save()
        }
        _enableModsAutoUpdateInFolder.initialize(true){
            save()
        }
    }

    open fun updateComposeModsList () = modsCollection.updateComposeList()

    open fun save () = jsoFile.writeText(Json.encodeToString(this))

    protected companion object{

        val pathToRootUserFolder : String = get(String()::class.java,
            named(KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY))

        inline fun <reified T> load(jsonFileName : String ): T where T : ModsModel {
            val jsoFile = File(pathToRootUserFolder + File.separator + jsonFileName)
            val model = if (jsoFile.exists())
                Json.decodeFromString<T>(jsoFile.readText()) else
                T::class.java.getDeclaredConstructor().newInstance()
            return model.apply {
                initialize()
            }
        }
    }
}

val ModsModel.modsCanBeUsed get() = enableModsSupport.value!! && modsCount > 0 &&
        this.mods.any { mod -> !mod.pathToMod.value.isNullOrEmpty() && File(mod.pathToMod.value!!).exists() }