package com.mobilerpgpack.phone.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData

fun <T> LiveData<T>.getNotNullValue() = this.value!!

@Composable
fun LiveData<String>.getComposableValue(defaultValue: String = "") = observeAsState(defaultValue).value

@Composable
fun LiveData<Float>.getComposableValue(defaultValue: Float = 0f) = observeAsState(defaultValue).value

@Composable
fun LiveData<Double>.getComposableValue(defaultValue: Double = 0.0) = observeAsState(defaultValue).value

@Composable
fun LiveData<Int>.getComposableValue(defaultValue: Int = 0) = observeAsState(defaultValue).value

@Composable
fun LiveData<Long>.getComposableValue(defaultValue: Long = 0) = observeAsState(defaultValue).value

@Composable
fun LiveData<Boolean>.getComposableValue(defaultValue: Boolean = false) = observeAsState(defaultValue).value

@Composable
inline fun <reified T : Enum<T>> LiveData<T>.getComposableValue(defaultValue: T) = observeAsState(defaultValue).value

@Composable
fun LiveData<String?>.getComposableNullableValue(defaultValue: String = "") = observeAsState(defaultValue).value

@Composable
fun LiveData<Float?>.getComposableNullableValue(defaultValue: Float = 0f) = observeAsState(defaultValue).value

@Composable
fun LiveData<Double?>.getComposableNullableValue(defaultValue: Double = 0.0) = observeAsState(defaultValue).value

@Composable
fun LiveData<Int?>.getComposableNullableValue(defaultValue: Int = 0) = observeAsState(defaultValue).value

@Composable
fun LiveData<Boolean?>.getComposableNullableValue(defaultValue: Boolean = false) = observeAsState(defaultValue).value

@Composable
inline fun <reified T : Enum<T>> LiveData<T?>.getComposableNullableValue(defaultValue: T) = observeAsState(defaultValue).value

