package com.mobilerpgpack.phone.utils.sharesprefs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.java.KoinJavaComponent.get
import java.util.concurrent.ConcurrentHashMap

open class SharedPrefsRepository {

    val prefsWasLoaded get() = _prefsWasLoaded

    fun loadAllEntries () = SharedPrefsRepository.loadAllEntries()

    fun <T : Enum<T>> getEnumValue(key: String,enumClass: Class<T>, defaultValue: T) : T =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.stringValue
            .let { stringValue ->
                if (stringValue.isNotEmpty()) {
                    try {
                        java.lang.Enum.valueOf(enumClass, stringValue)
                    } catch (_: Exception) {
                        defaultValue
                    }
                } else {
                    defaultValue
                }
            }

    fun <T : Enum<T>> getEnumValue(key: Key<T>,enumClass: Class<T>, defaultValue: T) =
        getEnumValue(key.name, enumClass, defaultValue)

    fun getStringValue(key: String, defaultValue: String = "") =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.stringValue

    fun getStringValue(key: Key<String>, defaultValue: String = "") = getStringValue(key.name, defaultValue)

    fun getIntValue(key: String, defaultValue: Int = 0) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.intValue

    fun getIntValue(key: Key<Int>, defaultValue: Int = 0) = getIntValue(key.name, defaultValue)

    fun getLongValue(key: String, defaultValue: Long = 0L) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.longValue

    fun getLongValue(key: Key<Long>, defaultValue: Long = 0L) = getLongValue(key.name, defaultValue)

    fun getBooleanValue(key: String, defaultValue: Boolean = false) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.booleanValue

    fun getBooleanValue(key: Key<Boolean>, defaultValue: Boolean = false) = getBooleanValue(key.name, defaultValue)

    fun getFloatValue(key: Key<Float>, defaultValue: Float = 0.0f) = getFloatValue(key.name, defaultValue)

    fun getFloatValue(key: String, defaultValue: Float = 0.0f) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.floatValue

    fun getDoubleValue(key: Key<Double>, defaultValue: Double = 0.0) = getDoubleValue(key.name, defaultValue)

    fun getDoubleValue(key: String, defaultValue: Double = 0.0) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.doubleValue

    fun setStringValue(key: Key<String>, value: String) = setStringValue(key.name, value)

    fun setStringValue(key: String, value: String) = scope.launch { setStringValueAsync(key, value) }

    suspend fun setStringValueAsync(key: Key<String>, value: String) = setStringValueAsync(key.name, value)

    suspend fun setStringValueAsync(key: String, value: String) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.stringValue = value
            stringValue = value
            dao.upsert(prefsEntry)
        }
    }

    fun setIntValue(key: Key<Int>, value: Int) = setIntValue(key.name, value)

    fun setIntValue(key: String, value: Int) = scope.launch { setIntValueAsync(key, value) }

    suspend fun setIntValueAsync(key: Key<Int>, value: Int) = setIntValueAsync(key.name, value)

    suspend fun setIntValueAsync(key: String, value: Int) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.intValue = value
            intValue = value
            dao.upsert(prefsEntry)
        }
    }

    fun setLongValue(key: Key<Long>, value: Long) = setLongValue(key.name, value)

    fun setLongValue(key: String, value: Long) = scope.launch { setLongValueAsync(key, value) }

    suspend fun setLongValueAsync(key: Key<Long>, value: Long) = setLongValueAsync(key.name, value)

    suspend fun setLongValueAsync(key: String, value: Long) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.longValue = value
            longValue = value
            dao.upsert(prefsEntry)
        }
    }

    fun setBooleanValue(key: Key<Boolean>, value: Boolean) = setBooleanValue(key.name, value)

    fun setBooleanValue(key: String, value: Boolean) = scope.launch { setBooleanValueAsync(key, value) }

    suspend fun setBooleanValueAsync(key: Key<Boolean>, value: Boolean) = setBooleanValueAsync(key.name, value)

    suspend fun setBooleanValueAsync(key: String, value: Boolean) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.booleanValue = value
            booleanValue = value
            dao.upsert(prefsEntry)
        }
    }

    fun setFloatValue(key: String, value: Float) = scope.launch { setFloatValueAsync(key, value) }

    fun setFloatValue(key: Key<Float>, value: Float) = setFloatValue(key.name, value)

    suspend fun setFloatValueAsync(key: Key<Float>, value: Float) = setFloatValueAsync(key.name, value)

    suspend fun setFloatValueAsync(key: String, value: Float) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.floatValue = value
            floatValue = value
            dao.upsert(prefsEntry)
        }
    }

    fun setDoubleValue(key: Key<Double>, value: Double) = setDoubleValue(key.name, value)

    fun setDoubleValue(key: String, value: Double) = scope.launch { setDoubleValueAsync(key, value) }

    suspend fun setDoubleValueAsync(key: Key<Double>, value: Double) = setDoubleValueAsync(key.name, value)

    suspend fun setDoubleValueAsync(key: String, value: Double) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.doubleValue = value
            doubleValue = value
            dao.upsert(prefsEntry)
        }
    }

    fun <T : Enum<T>> setEnumValue(key: String, value: T) = scope.launch { setEnumValueAsync(key, value) }

    fun <T : Enum<T>> setEnumValue(key: Key<T>, value: T) = setEnumValue(key.name, value)

    suspend fun <T : Enum<T>> setEnumValueAsync(key: Key<T>, value: T) = setEnumValueAsync(key.name, value)

    suspend fun <T : Enum<T>> setEnumValueAsync(key: String, value: T) =
        setStringValueAsync(key, value.name)

    private class SharedPrefsValue(var prefsEntry: SharedPrefsEntry) {
        var floatValue by mutableFloatStateOf(0f)
        var stringValue by mutableStateOf("")
        var intValue by mutableIntStateOf(0)
        var booleanValue by mutableStateOf(false)
        var longValue by mutableLongStateOf(0L)
        var doubleValue by mutableDoubleStateOf(0.0)

        fun updateEntry(newPrefsEntry: SharedPrefsEntry) {
            prefsEntry = newPrefsEntry
            newPrefsEntry.also {
                floatValue = it.floatValue
                stringValue = it.stringValue
                doubleValue = it.doubleValue
                booleanValue = it.booleanValue
                intValue = it.intValue
                longValue = it.longValue
            }
        }
    }

    private companion object {
        @Volatile
        private var loadAllEntriesWasCalled = false
        @Volatile
        private var _prefsWasLoaded = false
        private val mutex = Mutex()
        private val scope: CoroutineScope = get(CoroutineScope::class.java)
        private val dao: SharedPrefsDao = get(SharedPrefsDao::class.java)
        private val loadedEntries = ConcurrentHashMap<String, SharedPrefsValue>()

        private fun loadAllEntries() {
            if (!loadAllEntriesWasCalled) {
                loadAllEntriesWasCalled = true
                scope.launch { loadAllEntriesAsync() }
            }
        }

        private suspend fun loadAllEntriesAsync() {
            val entries = dao.getAllEntries()
            mutex.withLock {
                entries.forEach { entry ->
                    if (!loadedEntries.containsKey(entry.key)) {
                        loadedEntries[entry.key] = buildSharedPrefsValue(entry)!!
                    } else {
                        loadedEntries[entry.key]!!.updateEntry(entry)
                    }
                }
                _prefsWasLoaded = true
            }
        }

        private fun buildSharedPrefsValue(entry: SharedPrefsEntry): SharedPrefsValue? {
            entry.let {
                return SharedPrefsValue(entry).apply {
                    stringValue = it.stringValue
                    floatValue = it.floatValue
                    booleanValue = it.booleanValue
                    doubleValue = it.doubleValue
                    intValue = it.intValue
                    longValue = it.longValue
                }
            }
        }

        private fun buildSharedPrefsValue(key: String, value: String) =
            SharedPrefsValue(SharedPrefsEntry(key, stringValue = value)).apply {
                stringValue = value
            }

        private fun buildSharedPrefsValue(key: String, value: Int) =
            SharedPrefsValue(SharedPrefsEntry(key, intValue = value)).apply {
                intValue = value
            }

        private fun buildSharedPrefsValue(key: String, value: Float) =
            SharedPrefsValue(SharedPrefsEntry(key, floatValue = value)).apply {
                floatValue = value
            }

        private fun buildSharedPrefsValue(key: String, value: Double) =
            SharedPrefsValue(SharedPrefsEntry(key, doubleValue = value)).apply {
                doubleValue = value
            }

        private fun buildSharedPrefsValue(key: String, value: Long) =
            SharedPrefsValue(SharedPrefsEntry(key, longValue = value)).apply {
                longValue = value
            }

        private fun buildSharedPrefsValue(key: String, value: Boolean) =
            SharedPrefsValue(SharedPrefsEntry(key, booleanValue = value)).apply {
                booleanValue = value
            }

        private fun <T : Enum<T>> buildSharedPrefsValue(key: String, value: T) =
            SharedPrefsValue(SharedPrefsEntry(key, stringValue = value.name)).apply {
                stringValue = value.name
            }
    }
}