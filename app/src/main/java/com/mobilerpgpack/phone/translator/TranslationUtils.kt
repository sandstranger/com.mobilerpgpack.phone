package com.mobilerpgpack.phone.translator

import kotlin.math.roundToInt

fun ByteArray.sanitizeUtf8BytesToString(): String {
    val output = StringBuilder((this.size * 1.5f).roundToInt())
    var i = 0

    while (i < this.size) {
        val b = this[i].toInt() and 0xFF

        val charLen = when {
            b and 0b1000_0000 == 0 -> 1
            b and 0b1110_0000 == 0b1100_0000 -> 2
            b and 0b1111_0000 == 0b1110_0000 -> 3
            b and 0b1111_1000 == 0b1111_0000 -> 4
            else -> 0
        }

        if (charLen == 0 || i + charLen > this.size) {
            output.append(' ')
            i++
            continue
        }

        var valid = true
        for (j in 1 until charLen) {
            if ((this[i + j].toInt() and 0xC0) != 0x80) {
                valid = false
                break
            }
        }

        if (valid) {
            try {
                val str = this.copyOfRange(i, i + charLen).toString(Charsets.UTF_8)
                output.append(str)
            } catch (e: Exception) {
                output.append(' ')
            }
            i += charLen
        } else {
            output.append(' ')
            i++
        }
    }

    return output.toString()
}
