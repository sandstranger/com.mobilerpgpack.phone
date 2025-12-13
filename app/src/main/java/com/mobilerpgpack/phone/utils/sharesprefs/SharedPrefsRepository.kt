package com.mobilerpgpack.phone.utils.sharesprefs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.java.KoinJavaComponent.get
import java.util.concurrent.ConcurrentHashMap

open class SharedPrefsRepository {

    init {
        loadAllEntries()
    }

    protected fun getStringFlow(key: String, defaultValue: String = "") =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.stringFlow!!.flow

    protected fun getStringFlow(key: Key<String>, defaultValue: String = "") =
        loadedEntries.getOrPut(key.name) { buildSharedPrefsValue(key.name, defaultValue) }.stringFlow!!.flow

    protected fun getIntFlow(key: String, defaultValue: Int = 0) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.intFlow!!.flow

    protected fun getIntFlow(key: Key<Int>, defaultValue: Int = 0) =
        loadedEntries.getOrPut(key.name) { buildSharedPrefsValue(key.name, defaultValue) }.intFlow!!.flow

    protected fun getBooleanFlow(key: String, defaultValue: Boolean = false) =
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }.booleanFlow!!.flow

    protected fun getBooleanFlow(key: Key<Boolean>, defaultValue: Boolean = false) =
        loadedEntries.getOrPut(key.name) { buildSharedPrefsValue(key.name, defaultValue) }.booleanFlow!!.flow

    protected fun getFloatFlow(key: Key<Float>, defaultValue: Float = 0.0f) =
        loadedEntries.getOrPut(key.name) { buildSharedPrefsValue(key.name, defaultValue) }.floatFlow!!.flow

    protected fun getDoubleFlow(key: Key<Double>, defaultValue: Double = 0.0) =
        loadedEntries.getOrPut(key.name) { buildSharedPrefsValue(key.name, defaultValue) }.doubleFlow!!.flow

    fun setString(key: Key<String>, value: String) = setString(key.name, value)

    fun setString(key: String, value: String) = scope.launch { setStringAsync(key, value) }

    suspend fun setStringAsync(key: Key<String>, value: String) = setStringAsync(key.name, value)

    suspend fun setStringAsync(key: String, value: String) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.stringValue = value
            stringFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setInt(key: Key<Int>, value: Int) = setInt(key.name, value)

    fun setInt(key: String, value: Int) = scope.launch { setIntAsync(key, value) }

    suspend fun setIntAsync(key: Key<Int>, value: Int) = setIntAsync(key.name, value)

    suspend fun setIntAsync(key: String, value: Int) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.intValue = value
            intFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setBoolean(key: Key<Boolean>, value: Boolean) = setBoolean(key.name, value)

    fun setBoolean(key: String, value: Boolean) = scope.launch { setBooleanAsync(key, value) }

    suspend fun setBooleanAsync(key: Key<Boolean>, value: Boolean) = setBooleanAsync(key.name, value)

    suspend fun setBooleanAsync(key: String, value: Boolean) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.booleanValue = value
            booleanFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setFloat(key: String, value: Float) = scope.launch { setFloatAsync(key, value) }

    fun setFloat(key: Key<Float>, value: Float) = setFloat(key.name, value)

    suspend fun setFloatAsync(key: Key<Float>, value: Float) = setFloatAsync(key.name, value)

    suspend fun setFloatAsync(key: String, value: Float) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.floatValue = value
            floatFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setDouble(key: Key<Double>, value: Double) = setDouble(key.name, value)

    fun setDouble(key: String, value: Double) = scope.launch { setDoubleAsync(key, value) }

    suspend fun setDoubleAsync(key: Key<Double>, value: Double) = setDoubleAsync(key.name, value)

    suspend fun setDoubleAsync(key: String, value: Double) {
        loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }.apply {
            prefsEntry.doubleValue = value
            doubleFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    private class MutableFlow<T>(initialValue: T) {
        private val mutableFlow = MutableStateFlow(initialValue)

        val flow: Flow<T> = mutableFlow

        var value get() = mutableFlow.value
            set(value) { mutableFlow.value = value }
    }

    private class SharedPrefsValue(
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

    private companion object {
        @Volatile
        private var entriesWasLoaded = false

        private val mutex = Mutex()

        private val scope: CoroutineScope = get(CoroutineScope::class.java)

        private val dao: SharedPrefsDao = get(SharedPrefsDao::class.java)

        private val loadedEntries = ConcurrentHashMap<String, SharedPrefsValue>()

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
                    if (!loadedEntries.contains(entry.key)) {
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
    }
}