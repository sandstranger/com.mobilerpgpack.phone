package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import com.mobilerpgpack.phone.engine.engineinfo.utils.Mod
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.ComposeImmutableList
import com.mobilerpgpack.phone.utils.MutableValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import java.io.File

@Serializable
class UZDoomModsModel : ModsModel() {

    val enableDemoPlayingSupport = MutableValue<Boolean>()

    val pathToDemoFile = MutableValue<String>()

    val enableXLatSupport = MutableValue<Boolean>()

    val pathToXLatFile = MutableValue<String>()

    val enableBehSupport = MutableValue<Boolean>()

    val pathToBehFile = MutableValue<String> ()

    val enableDehSupport = MutableValue<Boolean>()

    val pathToDehFile = MutableValue<String>()

    override val jsonFileName = JSON_FILE_NAME

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

