package com.mobilerpgpack.phone.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.apache.commons.configuration2.INIConfiguration
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.concurrent.ConcurrentHashMap

class Ini (pathToFile : String, removeSpacesBetweenSeparator : Boolean = false ){

    private val iniValues = HashMap<String, IniValue>()

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
        return iniValues.getOrPut(key){
            iniConfig.run {
                IniValue().apply {
                    floatValue = if (containsKey(key)) getFloat(key) else 0f
                    iniValueType = IniValueType.Float
                }
            }
        }.floatValue
    }

    fun getBooleanValue (key: String) : Boolean {
        if (!loaded){
            load()
        }
        return iniValues.getOrPut(key){
            iniConfig.run {
                IniValue().apply {
                    booleanValue = if (containsKey(key)) getBoolean(key) else false
                    iniValueType = IniValueType.Boolean
                }
            }
        }.booleanValue
    }

    fun getStringValue (key: String, defaultValue : String = "") : String {
        if (!loaded){
            load()
        }

        return iniValues.getOrPut(key){
            iniConfig.run {
                IniValue().apply {
                    stringValue = if (containsKey(key)) getString(key) else defaultValue
                    iniValueType = IniValueType.String
                }
            }
        }.stringValue.ifEmpty { defaultValue }
    }

    fun getIntValue (key: String) : Int{
        if (!loaded){
            load()
        }

        return iniValues.getOrPut(key){
            iniConfig.run {
                IniValue().apply {
                    intValue = if (containsKey(key)) getInt(key) else 0
                    iniValueType = IniValueType.Int
                }
            }
        }.intValue
    }

    fun setValueAsInt (key: String, value: Boolean) = setValue(key, if (value) 1 else 0)

    fun <T> setValue (key: String, value: T){
        if (!loaded){
            load()
        }

        if (loaded) {
            iniValues.getOrPut(key){ IniValue() }.apply {
                when (value) {
                    is String -> {
                        stringValue = value
                        iniValueType = IniValueType.String
                    }

                    is Float -> {
                        floatValue = value
                        iniValueType = IniValueType.Float
                    }

                    is Int -> {
                        intValue = value
                        iniValueType = IniValueType.Int
                    }

                    is Boolean -> {
                        booleanValue = value
                        iniValueType = IniValueType.Boolean
                    }
                }
            }
            FileWriter(iniFile).use {
                iniConfig.apply {
                    setProperty(key, value)
                    write(it)
                }
            }
        }
    }

    fun clear(){
        loaded = false
        iniConfig.clear()
        iniValues.values.forEach {
            it.apply {
                floatValue = 0f
                intValue = 0
                stringValue = ""
                booleanValue = false
            }
        }
    }

    fun load (){
        clear()
        iniFile.apply {
            if (exists()) {
                FileReader(this).use {
                    iniConfig.read(it)
                }
                with(iniConfig) {
                    iniValues.forEach {
                        if (containsKey(it.key)) {
                            it.value.apply {
                                when (iniValueType) {
                                    IniValueType.Float -> floatValue = getFloat(it.key)
                                    IniValueType.String -> stringValue = getString(it.key)
                                    IniValueType.Int -> intValue = getInt(it.key)
                                    IniValueType.Boolean -> booleanValue = getBoolean(it.key)
                                }
                            }
                        }
                    }
                }
                loaded = true
            }
        }
    }

    private class IniValue {
        var floatValue by mutableFloatStateOf(0f)
        var stringValue by mutableStateOf("")
        var intValue by mutableIntStateOf(0)
        var booleanValue by mutableStateOf(false)
        lateinit var iniValueType : IniValueType
    }

    private enum class IniValueType {
        Float,
        String,
        Int,
        Boolean
    }
}