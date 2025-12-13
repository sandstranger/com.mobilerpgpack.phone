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

    protected fun getStringFlow(key: String, defaultValue: String = ""): Flow<String> {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }
        return prefsValue.stringFlow!!.state
    }

    protected fun getIntFlow(key: String, defaultValue: Int = 0): Flow<Int> {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }
        return prefsValue.intFlow!!.state
    }

    protected fun getBooleanFlow(key: String, defaultValue: Boolean = false): Flow<Boolean> {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }
        return prefsValue.booleanFlow!!.state
    }

    protected fun getFloatFlow(key: String, defaultValue: Float = 0.0f): Flow<Float> {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }
        return prefsValue.floatFlow!!.state
    }

    protected fun getDoubleFlow(key: String, defaultValue: Double = 0.0): Flow<Double> {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) }
        return prefsValue.doubleFlow!!.state
    }

    fun setString(key: String, value: String) = scope.launch { setStringAsync(key, value) }

    suspend fun setStringAsync(key: String, value: String) {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }
        dao.upsert(prefsValue.prefsEntry)
    }

    fun setInt(key: String, value: Int) = scope.launch { setIntAsync(key, value) }

    suspend fun setIntAsync(key: String, value: Int) {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }
        dao.upsert(prefsValue.prefsEntry)
    }

    fun setBoolean(key: String, value: Boolean) = scope.launch { setBooleanAsync(key, value) }

    suspend fun setBooleanAsync(key: String, value: Boolean) {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }
        dao.upsert(prefsValue.prefsEntry)
    }

    fun setFloat(key: String, value: Float) = scope.launch { setFloatAsync(key, value) }

    suspend fun setFloatAsync(key: String, value: Float) {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }
        dao.upsert(prefsValue.prefsEntry)
    }

    fun setDouble(key: String, value: Double) = scope.launch { setDoubleAsync(key, value) }

    suspend fun setDoubleAsync(key: String, value: Double) {
        val prefsValue = loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) }
        dao.upsert(prefsValue.prefsEntry)
    }

    private class SimpleFlow<T>(initialValue: T) {
        private val _state = MutableStateFlow(initialValue)

        val state: Flow<T> = _state

        fun set(value: T) {
            _state.value = value
        }
    }

    private class SharedPrefsValue(
        var prefsEntry: SharedPrefsEntry,
        val floatFlow: SimpleFlow<Float>? = null,
        val stringFlow: SimpleFlow<String>? = null,
        val intFlow: SimpleFlow<Int>? = null,
        val booleanFlow: SimpleFlow<Boolean>? = null,
        val doubleFlow: SimpleFlow<Double>? = null
    ) {

        val key get() = prefsEntry.key

        fun updateEntry(newPrefsEntry: SharedPrefsEntry) {
            prefsEntry = newPrefsEntry
            newPrefsEntry.apply {
                when {
                    floatFlow != null && floatValue != null -> floatFlow.set(floatValue!!)
                    intFlow != null && intValue != null -> intFlow.set(intValue!!)
                    booleanFlow != null && booleanValue != null -> booleanFlow.set(booleanValue!!)
                    doubleFlow != null && doubleValue != null -> doubleFlow.set(doubleValue!!)
                    stringFlow != null && stringValue != null -> stringFlow.set(stringValue!!)
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
                        stringFlow = SimpleFlow(stringValue!!)
                    )

                    intValue != null -> SharedPrefsValue(entry, intFlow = SimpleFlow(intValue!!))
                    booleanValue != null -> SharedPrefsValue(
                        entry,
                        booleanFlow = SimpleFlow(booleanValue!!)
                    )

                    doubleValue != null -> SharedPrefsValue(
                        entry,
                        doubleFlow = SimpleFlow(doubleValue!!)
                    )

                    floatValue != null -> SharedPrefsValue(
                        entry,
                        floatFlow = SimpleFlow(floatValue!!)
                    )

                    else -> null
                }
            }
        }

        private fun buildSharedPrefsValue(key: String, value: String) =
            SharedPrefsValue(
                SharedPrefsEntry(key, stringValue = value),
                stringFlow = SimpleFlow(value)
            )

        private fun buildSharedPrefsValue(key: String, value: Int) =
            SharedPrefsValue(SharedPrefsEntry(key, intValue = value), intFlow = SimpleFlow(value))

        private fun buildSharedPrefsValue(key: String, value: Float) =
            SharedPrefsValue(
                SharedPrefsEntry(key, floatValue = value),
                floatFlow = SimpleFlow(value)
            )

        private fun buildSharedPrefsValue(key: String, value: Double) =
            SharedPrefsValue(
                SharedPrefsEntry(key, doubleValue = value),
                doubleFlow = SimpleFlow(value)
            )

        private fun buildSharedPrefsValue(key: String, value: Boolean) =
            SharedPrefsValue(
                SharedPrefsEntry(key, booleanValue = value),
                booleanFlow = SimpleFlow(value)
            )
    }
}