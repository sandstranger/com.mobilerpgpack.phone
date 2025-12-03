package com.mobilerpgpack.phone.utils

import java.util.concurrent.CopyOnWriteArrayList

class MulticastActionGeneric<T> {
    private val listeners = CopyOnWriteArrayList<(T) -> Unit>()

    operator fun plusAssign(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    operator fun minusAssign(listener: (T) -> Unit) {
        listeners.remove(listener)
    }

    operator fun invoke(param: T) {
        listeners.forEach { it(param) }
    }
}