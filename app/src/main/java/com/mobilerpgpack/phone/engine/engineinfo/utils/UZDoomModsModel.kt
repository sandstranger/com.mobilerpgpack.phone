@file:OptIn(ExperimentalSerializationApi::class)

package com.mobilerpgpack.phone.engine.engineinfo.utils

import com.mobilerpgpack.phone.utils.MutableValue
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import java.io.File

@Serializable
@JsonIgnoreUnknownKeys
class UZDoomModsModel : ModsModel() {

    val enableDemoPlayingSupport = MutableValue<Boolean>()

    val pathToDemoFile = MutableValue<String>()

    val enableXLatSupport = MutableValue<Boolean>()

    val pathToXLatFile = MutableValue<String>()

    val enableBehSupport = MutableValue<Boolean>()

    val pathToBehFile = MutableValue<String>()

    val enableDehSupport = MutableValue<Boolean>()

    val pathToDehFile = MutableValue<String>()

    override val jsonFileName : String get() = JSON_FILE_NAME

    init {
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

    companion object{
        private const val JSON_FILE_NAME = "UZDoomMods.json"

        fun load () : UZDoomModsModel = load(JSON_FILE_NAME)
    }
}

val UZDoomModsModel.playingRecordsFileCanBeUsed get() =
    enableDemoPlayingSupport.value!! && !this.pathToDemoFile.value.isNullOrEmpty() && File(pathToDemoFile.value!!).exists()

val UZDoomModsModel.xlatFileCanBeUsed get() =
    this.enableXLatSupport.value!! && !this.pathToXLatFile.value.isNullOrEmpty() && File(pathToXLatFile.value!!).exists()

val UZDoomModsModel.behFileCanBeUsed get() =
    this.enableBehSupport.value!! && !this.pathToBehFile.value.isNullOrEmpty() && File(pathToBehFile.value!!).exists()

val UZDoomModsModel.dehFileCanBeUsed get() =
    this.enableDehSupport.value!! && !this.pathToDehFile.value.isNullOrEmpty() && File(pathToDehFile.value!!).exists()