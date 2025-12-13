package com.mobilerpgpack.phone.utils.sharesprefs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.util.concurrent.ConcurrentHashMap

open class SharedPrefsRepository {

    init {
        loadAllEntries()
    }

    protected fun getStringFlow(key: String, defaultValue: String = ""): Flow<String> {
        val prefsValue = synchronized(lockObject) { loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) } }
        return prefsValue.stringFlow!!.flow
    }

    protected fun getIntFlow(key: String, defaultValue: Int = 0): Flow<Int> {
        val prefsValue = synchronized(lockObject) {loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) } }
        return prefsValue.intFlow!!.flow
    }

    protected fun getBooleanFlow(key: String, defaultValue: Boolean = false): Flow<Boolean> {
        val prefsValue = synchronized(lockObject) {loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) } }
        return prefsValue.booleanFlow!!.flow
    }

    protected fun getFloatFlow(key: String, defaultValue: Float = 0.0f): Flow<Float> {
        val prefsValue = synchronized(lockObject) {loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) } }
        return prefsValue.floatFlow!!.flow
    }

    protected fun getDoubleFlow(key: String, defaultValue: Double = 0.0): Flow<Double> {
        val prefsValue = synchronized(lockObject) { loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, defaultValue) } }
        return prefsValue.doubleFlow!!.flow
    }

    fun setString(key: String, value: String) = scope.launch { setStringAsync(key, value) }

    suspend fun setStringAsync(key: String, value: String) {
        synchronized(lockObject)  { loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) } }.apply {
            prefsEntry.stringValue = value
            stringFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setInt(key: String, value: Int) = scope.launch { setIntAsync(key, value) }

    suspend fun setIntAsync(key: String, value: Int) {
        synchronized(lockObject) {loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) } }.apply {
            prefsEntry.intValue = value
            intFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setBoolean(key: String, value: Boolean) = scope.launch { setBooleanAsync(key, value) }

    suspend fun setBooleanAsync(key: String, value: Boolean) {
        synchronized(lockObject)  { loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) } }.apply {
            prefsEntry.booleanValue = value
            booleanFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setFloat(key: String, value: Float) = scope.launch { setFloatAsync(key, value) }

    suspend fun setFloatAsync(key: String, value: Float) {
        synchronized(lockObject)  { loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) } }.apply {
            prefsEntry.floatValue = value
            floatFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    fun setDouble(key: String, value: Double) = scope.launch { setDoubleAsync(key, value) }

    suspend fun setDoubleAsync(key: String, value: Double) {
        synchronized(lockObject) {  loadedEntries.getOrPut(key) { buildSharedPrefsValue(key, value) } }.apply {
            prefsEntry.doubleValue = value
            doubleFlow!!.value = value
            dao.upsert(prefsEntry)
        }
    }

    private class SimpleFlow<T>(initialValue: T) {
        private val _state = MutableStateFlow(initialValue)

        val flow: Flow<T> = _state

        var value get() = _state.value
            set(value) { _state.value = value }
    }

    private class SharedPrefsValue(
        var prefsEntry: SharedPrefsEntry,
        val floatFlow: SimpleFlow<Float>? = null,
        val stringFlow: SimpleFlow<String>? = null,
        val intFlow: SimpleFlow<Int>? = null,
        val booleanFlow: SimpleFlow<Boolean>? = null,
        val doubleFlow: SimpleFlow<Double>? = null
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

        private val lockObject = Any()

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

            synchronized(lockObject){
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