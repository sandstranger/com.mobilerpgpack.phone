package com.mobilerpgpack.phone.utils.sharesprefs

data class Key<T>(val name: String)

@JvmName("intKey")
fun intPreferencesKey(name: String): Key<Int> = Key(name)

@JvmName("doubleKey")
fun doublePreferencesKey(name: String): Key<Double> = Key(name)

@JvmName("stringKey")
fun stringPreferencesKey(name: String): Key<String> = Key(name)

@JvmName("booleanKey")
fun booleanPreferencesKey(name: String): Key<Boolean> = Key(name)

@JvmName("floatKey")
fun floatPreferencesKey(name: String): Key<Float> = Key(name)