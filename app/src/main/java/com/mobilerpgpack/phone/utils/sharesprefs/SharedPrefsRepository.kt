package com.mobilerpgpack.phone.utils.sharesprefs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilerpgpack.phone.main.KoinModulesProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get

open class SharedPrefsRepository {
    val prefsWasLoaded get() = _prefsWasLoaded

    suspend fun loadAllEntriesAsync () = SharedPrefsRepository.loadAllEntriesAsync()

    fun <T : Enum<T>> getEnumValue(key: String, enumClass: Class<T>, defaultValue: T): MutableLiveData<T> {
        val result = MutableLiveData<T>()
        loadedEntries
            .getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }
            .stringValue
            .observeForever { stringValue ->

                result.value = try {
                    if (stringValue.isNotEmpty()) {
                        java.lang.Enum.valueOf(enumClass, stringValue)
                    } else {
                        defaultValue
                    }
                } catch (_: Exception) {
                    defaultValue
                }
            }

        return result
    }

    fun <T : Enum<T>> getEnumValue(key: Key<T>, enumClass: Class<T>, defaultValue: T): LiveData<T> =
        getEnumValue(key.name, enumClass, defaultValue)

    fun getStringValue(key: String, defaultValue: String = "") : LiveData<String> =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.stringValue

    fun getStringValue(key: Key<String>, defaultValue: String = "") : LiveData<String> =
        getStringValue(key.name, defaultValue)

    fun getIntValue(key: String, defaultValue: Int = 0) : LiveData<Int> =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.intValue

    fun getIntValue(key: Key<Int>, defaultValue: Int = 0) : LiveData<Int> = getIntValue(key.name, defaultValue)

    fun getLongValue(key: String, defaultValue: Long = 0L) : LiveData<Long> =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.longValue

    fun getLongValue(key: Key<Long>, defaultValue: Long = 0L) : LiveData<Long> = getLongValue(key.name, defaultValue)

    fun getBooleanValue(key: String, defaultValue: Boolean = false) : LiveData<Boolean> =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.booleanValue

    fun getBooleanValue(key: Key<Boolean>, defaultValue: Boolean = false) : LiveData<Boolean> =
        getBooleanValue(key.name, defaultValue)

    fun getFloatValue(key: Key<Float>, defaultValue: Float = 0.0f) : LiveData<Float> =
        getFloatValue(key.name, defaultValue)

    fun getFloatValue(key: String, defaultValue: Float = 0.0f) : LiveData<Float> =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.floatValue

    fun getDoubleValue(key: Key<Double>, defaultValue: Double = 0.0) : LiveData<Double> =
        getDoubleValue(key.name, defaultValue)

    fun getDoubleValue(key: String, defaultValue: Double = 0.0) : LiveData<Double> =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.doubleValue

    fun setStringValue(key: Key<String>, value: String) = setStringValue(key.name, value)

    fun setStringValue(key: String, value: String) = scope.launch { setStringValueAsync(key, value) }

    suspend fun setStringValueAsync(key: Key<String>, value: String) = setStringValueAsync(key.name, value)

    suspend fun setStringValueAsync(key: String, value: String) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.stringValue = value
            stringValue.value = value
            withContext(Dispatchers.IO) {
                dao.upsert(prefsEntry)
            }
        }
    }

    fun setIntValue(key: Key<Int>, value: Int) = setIntValue(key.name, value)

    fun setIntValue(key: String, value: Int) = scope.launch { setIntValueAsync(key, value) }

    suspend fun setIntValueAsync(key: Key<Int>, value: Int) = setIntValueAsync(key.name, value)

    suspend fun setIntValueAsync(key: String, value: Int) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.intValue = value
            intValue.value = value
            withContext(Dispatchers.IO) {
                dao.upsert(prefsEntry)
            }
        }
    }

    fun setLongValue(key: Key<Long>, value: Long) = setLongValue(key.name, value)

    fun setLongValue(key: String, value: Long) = scope.launch { setLongValueAsync(key, value) }

    suspend fun setLongValueAsync(key: Key<Long>, value: Long) = setLongValueAsync(key.name, value)

    suspend fun setLongValueAsync(key: String, value: Long) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.longValue = value
            longValue.value = value
            withContext(Dispatchers.IO) {
                dao.upsert(prefsEntry)
            }
        }
    }

    fun setBooleanValue(key: Key<Boolean>, value: Boolean) = setBooleanValue(key.name, value)

    fun setBooleanValue(key: String, value: Boolean) = scope.launch { setBooleanValueAsync(key, value) }

    suspend fun setBooleanValueAsync(key: Key<Boolean>, value: Boolean) = setBooleanValueAsync(key.name, value)

    suspend fun setBooleanValueAsync(key: String, value: Boolean) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.booleanValue = value
            booleanValue.value = value
            withContext(Dispatchers.IO) {
                dao.upsert(prefsEntry)
            }
        }
    }

    fun setFloatValue(key: String, value: Float) = scope.launch { setFloatValueAsync(key, value) }

    fun setFloatValue(key: Key<Float>, value: Float) = setFloatValue(key.name, value)

    suspend fun setFloatValueAsync(key: Key<Float>, value: Float) = setFloatValueAsync(key.name, value)

    suspend fun setFloatValueAsync(key: String, value: Float) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.floatValue = value
            floatValue.value = value
            withContext(Dispatchers.IO) {
                dao.upsert(prefsEntry)
            }
        }
    }

    fun setDoubleValue(key: Key<Double>, value: Double) = setDoubleValue(key.name, value)

    fun setDoubleValue(key: String, value: Double) = scope.launch { setDoubleValueAsync(key, value) }

    suspend fun setDoubleValueAsync(key: Key<Double>, value: Double) = setDoubleValueAsync(key.name, value)

    suspend fun setDoubleValueAsync(key: String, value: Double) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.doubleValue = value
            doubleValue.value = value
            withContext(Dispatchers.IO) {
                dao.upsert(prefsEntry)
            }
        }
    }

    fun <T : Enum<T>> setEnumValue(key: String, value: T) = scope.launch { setEnumValueAsync(key, value) }

    fun <T : Enum<T>> setEnumValue(key: Key<T>, value: T) = setEnumValue(key.name, value)

    suspend fun <T : Enum<T>> setEnumValueAsync(key: Key<T>, value: T) = setEnumValueAsync(key.name, value)

    suspend fun <T : Enum<T>> setEnumValueAsync(key: String, value: T) =
        setStringValueAsync(key, value.name)

    private class SharedPrefsValue(var prefsEntry: SharedPrefsEntry) {
        val floatValue = MutableLiveData(0f)
        val stringValue = MutableLiveData("")
        val intValue = MutableLiveData(0)
        val booleanValue = MutableLiveData(false)
        val longValue = MutableLiveData(0L)
        val doubleValue = MutableLiveData(0.0)

        fun updateEntry(newPrefsEntry: SharedPrefsEntry) {
            prefsEntry = newPrefsEntry
            newPrefsEntry.also {
                floatValue.value = it.floatValue
                stringValue.value = it.stringValue
                doubleValue.value = it.doubleValue
                booleanValue.value = it.booleanValue
                intValue.value = it.intValue
                longValue.value = it.longValue
            }
        }
    }

    private companion object {
        private val scope : CoroutineScope = get(CoroutineScope::class.java,
            named(KoinModulesProvider.BACKGROUND_THREAD_COROUTINE_KEY))
        private val dao: SharedPrefsDao = get(SharedPrefsDao::class.java)
        private val loadedEntries = mutableMapOf<String, SharedPrefsValue>()
        @Volatile
        private var loadAllEntriesWasCalled = false
        @Volatile
        private var _prefsWasLoaded = false

        suspend fun loadAllEntriesAsync() {
            if (!loadAllEntriesWasCalled) {
                loadAllEntriesWasCalled = true
                dao.getAllEntries().apply {
                    withContext(Dispatchers.Main) {
                        this@apply.forEach { entry ->
                            if (!loadedEntries.containsKey(entry.key)) {
                                loadedEntries[entry.key] = buildSharedPrefsValue(entry)
                            } else {
                                loadedEntries[entry.key]!!.updateEntry(entry)
                            }
                        }
                        _prefsWasLoaded = true
                    }
                }
            }
        }

        private fun buildSharedPrefsValue(entry: SharedPrefsEntry): SharedPrefsValue {
            entry.let {
                return SharedPrefsValue(entry).apply {
                    stringValue.value = it.stringValue
                    floatValue.value = it.floatValue
                    booleanValue.value = it.booleanValue
                    doubleValue.value = it.doubleValue
                    intValue.value = it.intValue
                    longValue.value = it.longValue
                }
            }
        }

        private fun buildSharedPrefsValue(key: String, value: String) =
            SharedPrefsValue(SharedPrefsEntry(key, stringValue = value)).apply {
                stringValue.value = value
            }

        private fun buildSharedPrefsValue(key: String, value: Int) =
            SharedPrefsValue(SharedPrefsEntry(key, intValue = value)).apply {
                intValue.value = value
            }

        private fun buildSharedPrefsValue(key: String, value: Float) =
            SharedPrefsValue(SharedPrefsEntry(key, floatValue = value)).apply {
                floatValue.value = value
            }

        private fun buildSharedPrefsValue(key: String, value: Double) =
            SharedPrefsValue(SharedPrefsEntry(key, doubleValue = value)).apply {
                doubleValue.value = value
            }

        private fun buildSharedPrefsValue(key: String, value: Long) =
            SharedPrefsValue(SharedPrefsEntry(key, longValue = value)).apply {
                longValue.value = value
            }

        private fun buildSharedPrefsValue(key: String, value: Boolean) =
            SharedPrefsValue(SharedPrefsEntry(key, booleanValue = value)).apply {
                booleanValue.value = value
            }

        private fun <T : Enum<T>> buildSharedPrefsValue(key: String, value: T) =
            SharedPrefsValue(SharedPrefsEntry(key, stringValue = value.name)).apply {
                stringValue.value = value.name
            }
    }
}