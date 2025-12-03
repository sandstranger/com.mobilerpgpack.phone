package com.mobilerpgpack.phone.utils

import org.apache.commons.configuration2.INIConfiguration
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Ini (pathToFile : String, removeSpacesBetweenSeparator : Boolean = false ){

    private val iniFile = File(pathToFile)

    private val iniConfig = INIConfiguration()

    private var loaded = false

    init {
        if (removeSpacesBetweenSeparator) {
            iniConfig.separatorUsedInOutput = "="
        }
    }

    fun getBooleanValueFromInt (key: String) = getIntValue(key) > 0

    fun getFloatValue (key: String) : Float {
        if (!loaded){
            load()
        }
        return if (iniConfig.containsKey(key)) iniConfig.getFloat(key) else 0.0f
    }

    fun getBooleanValue (key: String) : Boolean {
        if (!loaded){
            load()
        }
        return if (iniConfig.containsKey(key)) iniConfig.getBoolean(key) else false
    }

    fun getStringValue (key: String) : String {
        if (!loaded){
            load()
        }
        return if (iniConfig.containsKey(key)) iniConfig.getString(key) else ""
    }

    fun getIntValue (key: String) : Int{
        if (!loaded){
            load()
        }
        return if (iniConfig.containsKey(key)) iniConfig.getInt(key) else 0
    }

    fun setValueAsInt (key: String, value: Boolean) = setValue(key, if (value) 1 else 0)

    fun <T> setValue (key: String, value: T){
        if (!loaded){
            load()
        }

        if (loaded) {
            FileWriter(iniFile).use {
                iniConfig.setProperty(key, value)
                iniConfig.write(it)
            }
        }
    }

    fun clear(){
        loaded = false
        iniConfig.clear()
    }

    fun load (){
        clear()
        if (iniFile.exists()) {
            FileReader(iniFile).use {
                iniConfig.read(it)
            }
            loaded = true
        }
    }
}