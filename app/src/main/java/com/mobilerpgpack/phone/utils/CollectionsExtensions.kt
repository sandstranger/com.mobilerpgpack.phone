package com.mobilerpgpack.phone.utils

fun <T> MutableList<T>.resizeTo(newSize: Int, defaultValue: (Int) -> T) {
    val currentSize = size
    when {
        newSize > currentSize -> {
            for (i in currentSize until newSize) {
                add(defaultValue(i))
            }
        }
        newSize < currentSize -> while (size > newSize) removeAt(lastIndex)
    }
}