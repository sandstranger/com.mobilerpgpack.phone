package com.mobilerpgpack.phone.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.apache.commons.configuration2.INIConfiguration
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Ini (pathToFile : String, removeSpacesBetweenSeparator : Boolean = false ) : KoinComponent{

    private val iniValues = mutableMapOf<String, IniValue>()

    private val iniFile : File by inject { parametersOf(pathToFile) }

    private val iniConfig = INIConfiguration()

    private var loaded = false

    init {
        if (removeSpacesBetweenSeparator) {
            iniConfig.separatorUsedInOutput = "="
        }
    }

    fun getBooleanValueFromInt (key: String) : LiveData<Boolean> {
        if (!loaded){
            load()
        }
        return iniValues.getOrPut(key){
            iniConfig.run {
                IniValue().apply {
                    booleanValue.value = if (containsKey(key)) getInt(key) > 0 else false
                    iniValueType = IniValueType.Boolean
                }
            }
        }.booleanValue
    }

    fun getFloatValue (key: String) : LiveData<Float> {
        if (!loaded){
            load()
        }
        return iniValues.getOrPut(key){
            iniConfig.run {
                IniValue().apply {
                    floatValue.value = if (containsKey(key)) getFloat(key) else 0f
                    iniValueType = IniValueType.Float
                }
            }
        }.floatValue
    }

    fun getBooleanValue (key: String) : LiveData<Boolean> {
        if (!loaded){
            load()
        }
        return iniValues.getOrPut(key){
            iniConfig.run {
                IniValue().apply {
                    booleanValue.value = if (containsKey(key)) getBoolean(key) else false
                    iniValueType = IniValueType.Boolean
                }
            }
        }.booleanValue
    }

    fun getStringValue (key: String, defaultValue : String = "") : LiveData<String> {
        if (!loaded){
            load()
        }

        return iniValues.getOrPut(key){
            iniConfig.run {
                IniValue().apply {
                    stringValue.value = if (containsKey(key)) getString(key) else defaultValue
                    iniValueType = IniValueType.String
                }
            }
        }.stringValue.run {
            if (this.value.isNullOrEmpty()){
                this.value = defaultValue
            }
            this
        }
    }

    fun getIntValue (key: String) : LiveData<Int>{
        if (!loaded){
            load()
        }

        return iniValues.getOrPut(key){
            iniConfig.run {
                IniValue().apply {
                    intValue.value = if (containsKey(key)) getInt(key) else 0
                    iniValueType = IniValueType.Int
                }
            }
        }.intValue
    }

    fun setValueAsInt (key: String, value: Boolean) {
        if (!loaded){
            load()
        }

        if (loaded) {
            iniValues.getOrPut(key){ IniValue() }.apply {
                booleanValue.value = value
                iniValueType = IniValueType.Boolean
            }
            writeChanges(key,if(value) 1 else 0)
        }
    }

    fun <T> setValue (key: String, value: T){
        if (!loaded){
            load()
        }

        if (loaded) {
            iniValues.getOrPut(key){ IniValue() }.apply {
                when (value) {
                    is String -> {
                        stringValue.value= value
                        iniValueType = IniValueType.String
                    }

                    is Float -> {
                        floatValue.value = value
                        iniValueType = IniValueType.Float
                    }

                    is Int -> {
                        intValue.value = value
                        iniValueType = IniValueType.Int
                    }

                    is Boolean -> {
                        booleanValue.value = value
                        iniValueType = IniValueType.Boolean
                    }
                }
            }
            writeChanges(key, value)
        }
    }

    fun clear(){
        loaded = false
        iniConfig.clear()
        iniValues.values.forEach {
            it.apply {
                floatValue.value = 0f
                intValue.value = 0
                stringValue.value = ""
                booleanValue.value = false
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
                                    IniValueType.Float -> floatValue.value = getFloat(it.key)
                                    IniValueType.String -> stringValue.value = getString(it.key)
                                    IniValueType.Int -> intValue.value = getInt(it.key)
                                    IniValueType.Boolean -> booleanValue.value = getBoolean(it.key)
                                }
                            }
                        }
                    }
                }
                loaded = true
            }
        }
    }

    private fun <T> writeChanges(key: String, value: T){
        iniFile.parentFile?.mkdirs()
        FileWriter(iniFile).use {
            iniConfig.apply {
                setProperty(key, value)
                write(it)
            }
        }
    }

    private class IniValue {
        val floatValue = MutableLiveData(0f)
        val stringValue = MutableLiveData("")
        val intValue = MutableLiveData(0)
        val booleanValue = MutableLiveData(false)
        lateinit var iniValueType : IniValueType
    }

    private enum class IniValueType {
        Float,
        String,
        Int,
        Boolean
    }
}