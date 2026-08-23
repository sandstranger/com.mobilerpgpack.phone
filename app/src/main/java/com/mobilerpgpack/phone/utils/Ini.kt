package com.mobilerpgpack.phone.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.io.File

class Ini(pathToFile: String, removeSpacesBetweenSeparator: Boolean = false) : KoinComponent {
    private val iniFile : File by inject { parametersOf(pathToFile) }
    private val iniValues = mutableMapOf<String, IniValue>()
    private val sections = linkedMapOf<String, LinkedHashMap<String, String>>()
    private var loaded = false

    private val outputSeparator: String = if (removeSpacesBetweenSeparator) "=" else " = "

    fun getBooleanValueFromInt(key: String): LiveData<Boolean> {
        ensureLoaded()

        return iniValues.getOrPut(key) {
            IniValue().apply {
                booleanValue.value = readString(key)?.toIntOrNull()?.let { it > 0 } ?: false
                iniValueType = IniValueType.Boolean
            }
        }.booleanValue
    }

    fun getFloatValue(key: String): LiveData<Float> {
        ensureLoaded()

        return iniValues.getOrPut(key) {
            IniValue().apply {
                floatValue.value = readString(key)?.toFloatOrNull() ?: 0f
                iniValueType = IniValueType.Float
            }
        }.floatValue
    }

    fun getBooleanValue(key: String): LiveData<Boolean> {
        ensureLoaded()

        return iniValues.getOrPut(key) {
            IniValue().apply {
                booleanValue.value = readBoolean(key) ?: false
                iniValueType = IniValueType.Boolean
            }
        }.booleanValue
    }

    fun getStringValue(key: String, defaultValue: String = ""): LiveData<String> {
        ensureLoaded()

        return iniValues.getOrPut(key) {
            IniValue().apply {
                stringValue.value = readString(key) ?: defaultValue
                iniValueType = IniValueType.String
            }
        }.stringValue.apply {
            if (this.value.isNullOrEmpty()) {
                this.value = defaultValue
            }
        }
    }

    fun getIntValue(key: String): LiveData<Int> {
        ensureLoaded()

        return iniValues.getOrPut(key) {
            IniValue().apply {
                intValue.value = readString(key)?.toIntOrNull() ?: 0
                iniValueType = IniValueType.Int
            }
        }.intValue
    }

    fun setValueAsInt(key: String, value: Boolean) {
        ensureLoaded()

        iniValues.getOrPut(key) { IniValue() }.apply {
            booleanValue.value = value
            iniValueType = IniValueType.Boolean
        }

        writeChanges(key, if (value) 1 else 0)
    }

    fun <T> setValue(key: String, value: T) {
        ensureLoaded()

        iniValues.getOrPut(key) { IniValue() }.apply {
            when (value) {
                is String -> {
                    stringValue.value = value
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

    fun clear() {
        loaded = false
        sections.clear()

        iniValues.values.forEach {
            it.apply {
                floatValue.value = 0f
                intValue.value = 0
                stringValue.value = ""
                booleanValue.value = false
            }
        }
    }

    fun load() {
        clear()

        if (iniFile.exists()) {
            parseFile()
            refreshCachedValues()
        }

        loaded = true
    }

    private fun ensureLoaded() {
        if (!loaded) {
            load()
        }
    }

    private fun parseFile() {
        var currentSection = ROOT_SECTION
        sections.getOrPut(currentSection) { linkedMapOf() }

        iniFile.bufferedReader(Charsets.UTF_8).useLines { lines ->
            lines.forEach { rawLine ->
                val line = rawLine.trim()

                if (line.isEmpty()) return@forEach
                if (line.startsWith(";") || line.startsWith("#")) return@forEach

                if (line.startsWith("[") && line.endsWith("]") && line.length >= 2) {
                    currentSection = line.substring(1, line.length - 1).trim()
                    sections.getOrPut(currentSection) { linkedMapOf() }
                    return@forEach
                }

                val sepIndex = findSeparatorIndex(line)
                if (sepIndex <= 0) return@forEach

                val key = line.substring(0, sepIndex).trim()
                val value = normalizeIniValue(line.substring(sepIndex + 1))

                if (key.isNotEmpty()) {
                    sections.getOrPut(currentSection) { linkedMapOf() }[key] = value
                }
            }
        }
    }

    private fun refreshCachedValues() {
        iniValues.forEach { (fullKey, iniValue) ->
            val stored = readString(fullKey)
            if (stored != null) {
                iniValue.apply {
                    when (iniValueType) {
                        IniValueType.Float -> floatValue.value = stored.toFloatOrNull() ?: 0f
                        IniValueType.String -> stringValue.value = stored
                        IniValueType.Int -> intValue.value = stored.toIntOrNull() ?: 0
                        IniValueType.Boolean -> booleanValue.value = parseBooleanLoose(stored)
                    }
                }
            }
        }
    }

    private fun readString(fullKey: String): String? {
        val (sectionName, entryKey) = splitFullKey(fullKey)

        if (sectionName != null) {
            sections[sectionName]?.get(entryKey)?.let { return normalizeIniValue(it) }
        }

        sections[ROOT_SECTION]?.get(fullKey)?.let { return normalizeIniValue(it) }

        if (sectionName == null) {
            sections[ROOT_SECTION]?.get(entryKey)?.let { return normalizeIniValue(it) }
        }

        return null
    }

    private fun readBoolean(fullKey: String): Boolean? {
        val value = readString(fullKey) ?: return null
        return parseBooleanLoose(value)
    }

    private fun <T> writeChanges(key: String, value: T) {
        val (sectionName, entryKey) = splitFullKey(key)
        val stringValue = value.toString()

        if (sectionName != null) {
            val section = sections.getOrPut(sectionName) { linkedMapOf() }
            section[entryKey] = stringValue
        } else {
            val root = sections.getOrPut(ROOT_SECTION) { linkedMapOf() }
            root[entryKey] = stringValue
        }

        iniFile.parentFile?.mkdirs()
        iniFile.bufferedWriter(Charsets.UTF_8).use { writer ->
            writeSections(writer)
        }
    }

    private fun writeSections(writer: java.io.Writer) {
        val root = sections[ROOT_SECTION].orEmpty()

        fun writeEntries(entries: Map<String, String>) {
            entries.forEach { (k, v) ->
                writer.write(k)
                writer.write(outputSeparator)
                writer.write(v)
                writer.write("\n")
            }
        }

        if (root.isNotEmpty()) {
            writeEntries(root)
        }

        sections.forEach { (sectionName, entries) ->
            if (sectionName == ROOT_SECTION || entries.isEmpty()) return@forEach

            if (root.isNotEmpty()) {
                writer.write("\n")
            }

            writer.write("[")
            writer.write(sectionName)
            writer.write("]\n")
            writeEntries(entries)
        }

        writer.flush()
    }

    private fun splitFullKey(fullKey: String): Pair<String?, String> {
        val dotIndex = fullKey.indexOf('.')

        return if (dotIndex > 0 && dotIndex < fullKey.length - 1) {
            val section = fullKey.substring(0, dotIndex).trim()
            val key = fullKey.substring(dotIndex + 1).trim()
            if (section.isNotEmpty() && key.isNotEmpty()) {
                section to key
            } else {
                null to fullKey
            }
        } else {
            null to fullKey
        }
    }

    private fun findSeparatorIndex(line: String): Int {
        val eq = line.indexOf('=')
        val colon = line.indexOf(':')

        return when {
            eq == -1 && colon == -1 -> -1
            eq == -1 -> colon
            colon == -1 -> eq
            else -> minOf(eq, colon)
        }
    }

    private fun normalizeIniValue(value: String): String {
        val v = value.trim()
        if (v.length >= 2) {
            val first = v.first()
            val last = v.last()
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return v.substring(1, v.length - 1)
            }
        }
        return v
    }

    private fun parseBooleanLoose(value: String): Boolean {
        return when (value.trim().lowercase()) {
            "1", "true", "yes", "on" -> true
            else -> false
        }
    }

    private class IniValue {
        val floatValue = MutableLiveData(0f)
        val stringValue = MutableLiveData("")
        val intValue = MutableLiveData(0)
        val booleanValue = MutableLiveData(false)
        lateinit var iniValueType: IniValueType
    }

    private enum class IniValueType {
        Float,
        String,
        Int,
        Boolean
    }

    private companion object {
        const val ROOT_SECTION = ""
    }
}