package com.mobilerpgpack.phone.utils.sharesprefs

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SharedPrefsDatabase.DATABASE_NAME)
internal data class SharedPrefsEntry(
    @PrimaryKey
    val key: String,
    var stringValue: String = "",
    var intValue: Int = 0,
    var booleanValue: Boolean = false,
    var doubleValue: Double = 0.0,
    var floatValue: Float = 0f,
    var longValue: Long = 0L
)