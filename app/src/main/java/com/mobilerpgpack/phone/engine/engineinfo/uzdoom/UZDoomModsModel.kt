package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.ComposeImmutableList
import com.mobilerpgpack.phone.utils.MutableValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import java.io.File

@Serializable
class UZDoomModsModel {

    private val modsCollection = ComposeImmutableList<Mod>()

    val enableModsSupport = MutableValue<Boolean>()

    val enableDemoPlayingSupport = MutableValue<Boolean>()

    val pathToDemoFile = MutableValue<String>()

    val enableXLatSupport = MutableValue<Boolean>()

    val pathToXLatFile = MutableValue<String>()

    val enableBehSupport = MutableValue<Boolean>()

    val pathToBehFile = MutableValue<String> ()

    val enableDehSupport = MutableValue<Boolean>()

    val pathToDehFile = MutableValue<String>()

    var modsCount : Int
        get() = modsCollection.count!!
        set(value) {
            modsCollection.count = value
            updateIndexes()
            save()
        }

    val pathToMods get() = modsCollection.sourceList

    val pathToModsComposeCollection get() = modsCollection.composeList

    init {
        modsCollection.initialize(defaultValue = { Mod(it)})
        enableModsSupport.initialize(false){
            save()
        }
        enableDemoPlayingSupport.initialize(false){
            save()
        }
        pathToDemoFile.initialize(""){
            save()
        }
        enableXLatSupport.initialize(false){
            save()
        }
        pathToXLatFile.initialize(""){
            save()
        }
        enableBehSupport.initialize(false){
            save()
        }
        pathToBehFile.initialize(""){
            save()
        }
        enableDehSupport.initialize(false){
            save()
        }
        pathToDehFile.initialize(""){
            save()
        }
    }

    fun updateIndexes(){
        for (i in 0 until modsCollection.sourceList.count()) {
            modsCollection.sourceList[i].index  = i
        }
        modsCollection.updateComposeList()
    }

    fun save () = uzDoomModsJsonFile.writeText(Json.encodeToString(this))

    companion object{
        private val pathToRootUserFolder : String = get(String()::class.java,
            named(KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY))

        private val uzDoomModsJsonFile = File(pathToRootUserFolder + File.separator + "UZDoomMods.json")

        fun load () : UZDoomModsModel = if (uzDoomModsJsonFile.exists())
            Json.decodeFromString<UZDoomModsModel>(uzDoomModsJsonFile.readText(),) else UZDoomModsModel()
    }
}

