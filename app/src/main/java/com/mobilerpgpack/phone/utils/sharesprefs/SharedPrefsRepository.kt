package com.mobilerpgpack.phone.utils.sharesprefs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.java.KoinJavaComponent.get
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap

open class SharedPrefsRepository {

    init {
        loadAllEntries()
    }

    inline fun <reified T : Enum<T>> getEnumValue(key: String, defaultValue: T): Flow<T> =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.stringFlow!!.flow
            .map { stringValue ->
                if (stringValue.isNotEmpty()) {
                    try {
                        enumValueOf<T>(stringValue)
                    } catch (_: Exception) {
                        defaultValue
                    }
                } else {
                    defaultValue
                }
            }

    inline fun <reified T : Enum<T>> getEnumValue(key: Key<T>, defaultValue: T): Flow<T> =
        getEnumValue(key.name, defaultValue)

    fun getStringValue(key: String, defaultValue: String = "") =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.stringFlow!!.flow

    fun getStringValue(key: Key<String>, defaultValue: String = "") = getStringValue(key.name, defaultValue)

    fun getIntValue(key: String, defaultValue: Int = 0) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.intFlow!!.flow

    fun getIntValue(key: Key<Int>, defaultValue: Int = 0) = getIntValue(key.name, defaultValue)

    fun getBooleanValue(key: String, defaultValue: Boolean = false) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.booleanFlow!!.flow

    fun getBooleanValue(key: Key<Boolean>, defaultValue: Boolean = false) = getBooleanValue(key.name, defaultValue)

    fun getFloatValue(key: Key<Float>, defaultValue: Float = 0.0f) = getFloatValue(key.name, defaultValue)

    fun getFloatValue(key: String, defaultValue: Float = 0.0f) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.floatFlow!!.flow

    fun getDoubleValue(key: Key<Double>, defaultValue: Double = 0.0) = getDoubleValue(key.name, defaultValue)

    fun getDoubleValue(key: String, defaultValue: Double = 0.0) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.doubleFlow!!.flow

    fun setStringValue(key: Key<String>, value: String) = setStringValue(key.name, value)

    fun setStringValue(key: String, value: String) = scope.launch { setStringValueAsync(key, value) }

    suspend fun setStringValueAsync(key: Key<String>, value: String) = setStringValueAsync(key.name, value)

    suspend fun setStringValueAsync(key: String, value: String) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.stringValue = value
            stringFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setIntValue(key: Key<Int>, value: Int) = setIntValue(key.name, value)

    fun setIntValue(key: String, value: Int) = scope.launch { setIntValueAsync(key, value) }

    suspend fun setIntValueAsync(key: Key<Int>, value: Int) = setIntValueAsync(key.name, value)

    suspend fun setIntValueAsync(key: String, value: Int) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.intValue = value
            intFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setBooleanValue(key: Key<Boolean>, value: Boolean) = setBooleanValue(key.name, value)

    fun setBooleanValue(key: String, value: Boolean) = scope.launch { setBooleanValueAsync(key, value) }

    suspend fun setBooleanValueAsync(key: Key<Boolean>, value: Boolean) = setBooleanValueAsync(key.name, value)

    suspend fun setBooleanValueAsync(key: String, value: Boolean) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.booleanValue = value
            booleanFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setFloatValue(key: String, value: Float) = scope.launch { setFloatValueAsync(key, value) }

    fun setFloatValue(key: Key<Float>, value: Float) = setFloatValue(key.name, value)

    suspend fun setFloatValueAsync(key: Key<Float>, value: Float) = setFloatValueAsync(key.name, value)

    suspend fun setFloatValueAsync(key: String, value: Float) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.floatValue = value
            floatFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setDoubleValue(key: Key<Double>, value: Double) = setDoubleValue(key.name, value)

    fun setDoubleValue(key: String, value: Double) = scope.launch { setDoubleValueAsync(key, value) }

    suspend fun setDoubleValueAsync(key: Key<Double>, value: Double) = setDoubleValueAsync(key.name, value)

    suspend fun setDoubleValueAsync(key: String, value: Double) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.doubleValue = value
            doubleFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    inline fun <reified T : Enum<T>> setEnumValue(key: String, value: T) = scope.launch { setEnumValueAsync(key, value) }

    inline fun <reified T : Enum<T>> setEnumValue(key: Key<T>, value: T) = setEnumValue(key.name, value)

    suspend inline fun <reified T : Enum<T>> setEnumValueAsync(key: Key<T>, value: T) =
        setEnumValueAsync(key.name, value)

    suspend inline fun <reified T : Enum<T>> setEnumValueAsync(key: String, value: T) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.stringValue = value.name
            stringFlow!!.value = value.name
            dao.upsert(prefsEntry)
        }
    }

    class MutableFlow<T>(initialValue: T) {
        private val mutableFlow = MutableStateFlow(initialValue)

        val flow: Flow<T> = mutableFlow

        var value get() = mutableFlow.value
            set(value) { mutableFlow.value = value }
    }

    class SharedPrefsValue(
        var prefsEntry: SharedPrefsEntry,
        val floatFlow: MutableFlow<Float>? = null,
        val stringFlow: MutableFlow<String>? = null,
        val intFlow: MutableFlow<Int>? = null,
        val booleanFlow: MutableFlow<Boolean>? = null,
        val doubleFlow: MutableFlow<Double>? = null
    ) {

        fun updateEntry(newPrefsEntry: SharedPrefsEntry) {
            prefsEntry = newPrefsEntry
            newPrefsEntry.apply {
                when {
                    floatFlow != null && floatValue != null -> floatFlow.value = floatValue!!
                    intFlow != null && intValue != null -> intFlow.value = intValue!!
                    booleanFlow != null && booleanValue != null -> booleanFlow.value = booleanValue!!
                    doubleFlow != null && doubleValue != null -> doubleFlow.value = doubleValue!!
                    stringFlow != null && stringValue != null -> stringFlow.value = stringValue!!
                }
            }
        }
    }

    companion object {
        @Volatile
        private var entriesWasLoaded = false

        private val mutex = Mutex()

        val scope: CoroutineScope = get(CoroutineScope::class.java)

        val dao: SharedPrefsDao = get(SharedPrefsDao::class.java)

        val loadedEntries = ConcurrentHashMap<String, SharedPrefsValue>()

        private fun loadAllEntries() {
            if (!entriesWasLoaded) {
                entriesWasLoaded = true
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
            }
        }

        private fun buildSharedPrefsValue(entry: SharedPrefsEntry): SharedPrefsValue? {
            entry.run {
                return when {
                    stringValue != null -> SharedPrefsValue(
                        entry,
                        stringFlow = MutableFlow(stringValue!!)
                    )

                    intValue != null -> SharedPrefsValue(entry, intFlow = MutableFlow(intValue!!))
                    booleanValue != null -> SharedPrefsValue(
                        entry,
                        booleanFlow = MutableFlow(booleanValue!!)
                    )

                    doubleValue != null -> SharedPrefsValue(
                        entry,
                        doubleFlow = MutableFlow(doubleValue!!)
                    )

                    floatValue != null -> SharedPrefsValue(
                        entry,
                        floatFlow = MutableFlow(floatValue!!)
                    )

                    else -> null
                }
            }
        }

        private fun buildSharedPrefsValue(key: String, value: String) =
            SharedPrefsValue(
                SharedPrefsEntry(key, stringValue = value),
                stringFlow = MutableFlow(value)
            )

        private fun buildSharedPrefsValue(key: String, value: Int) =
            SharedPrefsValue(SharedPrefsEntry(key, intValue = value), intFlow = MutableFlow(value))

        private fun buildSharedPrefsValue(key: String, value: Float) =
            SharedPrefsValue(
                SharedPrefsEntry(key, floatValue = value),
                floatFlow = MutableFlow(value)
            )

        private fun buildSharedPrefsValue(key: String, value: Double) =
            SharedPrefsValue(
                SharedPrefsEntry(key, doubleValue = value),
                doubleFlow = MutableFlow(value)
            )

        private fun buildSharedPrefsValue(key: String, value: Boolean) =
            SharedPrefsValue(
                SharedPrefsEntry(key, booleanValue = value),
                booleanFlow = MutableFlow(value)
            )

        fun <T : Enum<T>> buildSharedPrefsValue(key: String, value: T) =
            SharedPrefsValue(
                SharedPrefsEntry(key, stringValue = value.name),
                stringFlow = MutableFlow(value.name)
            )
    }
}