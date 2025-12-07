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

    private val _enableModsAutoUpdateInFolder = MutableValue<Boolean>()

    @Transient
    private val jsoFile = File(pathToRootUserFolder + File.separator + jsonFileName)

    open val allowedModsExtensions : Collection<String> = listOf("wad", "pk3", "iwad", "ipk3", "ipk7")

    val enableModsSupport = MutableValue<Boolean>()

    val pathToModsFolder = MutableValue<String>()

    var enableModsAutoUpdateInFolder
        get() = _enableModsAutoUpdateInFolder.value!!
        set(value) {
            _enableModsAutoUpdateInFolder.value = value
        }

    var modsCount : Int
        get() = modsCollection.count!!
        set(value) {
            modsCollection.count = value
            save()
        }

    val mods get() = modsCollection.sourceList

    val modsComposeCollection get() = modsCollection.composeList

    protected open fun initialize() {
        modsCollection.initialize(defaultValue = { Mod() })
        enableModsSupport.initialize(false){
            save()
        }
        pathToModsFolder.initialize(""){
            findFilesInModsFolder()
            save()
        }
        _enableModsAutoUpdateInFolder.initialize(true){
            save()
        }
        removeNotExistingMods()
        updateFilesInModsFolder()
    }

    fun updateComposeModsList () = modsCollection.updateComposeList()

    fun save () = jsoFile.writeText(Json.encodeToString(this))

    private fun findFilesInModsFolder(){
        if (pathToModsFolder.value.isNullOrEmpty()){
            return
        }

        getModsFromModsFolder()?.let {
            mods.clear()

            it.forEach { file ->
                val mod = Mod()
                mod.pathToMod.value = file.absolutePath
                mods +=mod
            }
            modsCount = it.count()
            updateComposeModsList()
        }
    }

    private fun removeNotExistingMods(){
        if (pathToModsFolder.value.isNullOrEmpty()){
            return
        }

        val modsToRemove = mutableSetOf<Mod>()

        mods.forEach {
            if (!it.pathToMod.value.isNullOrEmpty() && !File(it.pathToMod.value!!).exists()){
                modsToRemove += it
            }
        }

        mods.removeAll(modsToRemove)
        modsCount -= modsToRemove.count()

        if (modsToRemove.count() >0){
            save()
            updateComposeModsList()
        }
    }

    private fun updateFilesInModsFolder(){
        if (pathToModsFolder.value.isNullOrEmpty() || !enableModsAutoUpdateInFolder){
            return
        }

        var newFoundedModsCount = 0

        getModsFromModsFolder()?.let {
            it.forEach { file ->
                if (!mods.any{mod -> mod.pathToMod.value == file.absolutePath}){
                    val mod = Mod()
                    mod.pathToMod.value = file.absolutePath
                    mods +=mod
                    ++newFoundedModsCount
                }
            }

            if (newFoundedModsCount >0){
                modsCount +=newFoundedModsCount
                save()
                updateComposeModsList()
            }
        }

        File(pathToModsFolder.value!!).listFiles()?.filter { file -> file.isMod }?.toList()?.let {
            it.forEach { file ->
                val mod = Mod()
                mod.pathToMod.value = file.absolutePath
                mods +=mod
            }
            updateComposeModsList()
        }
    }

    private fun getModsFromModsFolder () = if (pathToModsFolder.value.isNullOrEmpty()) null else
        File(pathToModsFolder.value!!).listFiles()?.filter { file -> file.isMod }?.toList()

    protected companion object{

        val pathToRootUserFolder : String = get(String()::class.java,
            named(KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY))

        inline fun <reified T> load(jsonFileName : String ): T where T : ModsModel {
            val jsoFile = File(pathToRootUserFolder + File.separator + jsonFileName)

            val model = if (jsoFile.exists())
                Json.decodeFromString<T>(jsoFile.readText()) else
                T::class.java.getDeclaredConstructor().newInstance()
            return model.also {
                it.initialize()
            }
        }
    }

    private val File.isMod get() = this.isFile && allowedModsExtensions.contains(this.extension)
}

val ModsModel.modsCanBeUsed get() = enableModsSupport.value!! && modsCount > 0 &&
        this.mods.any { mod -> !mod.pathToMod.value.isNullOrEmpty() && File(mod.pathToMod.value!!).exists() }