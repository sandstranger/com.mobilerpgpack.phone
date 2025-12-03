package com.mobilerpgpack.phone.utils

import java.util.concurrent.CopyOnWriteArrayList

class MulticastAction {
    private val listeners = CopyOnWriteArrayList<() -> Unit>()

    operator fun plusAssign(listener: () -> Unit) {
        listeners.add(listener)
    }

    operator fun minusAssign(listener: () -> Unit) {
        listeners.remove(listener)
    }

    operator fun invoke() {
        listeners.forEach { it() }
    }
}