package com.mobilerpgpack.phone.utils.sharesprefs

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SharedPrefsDatabase.DATABASE_NAME)
data class SharedPrefsEntry(
    @PrimaryKey
    val key: String,
    var stringValue: String? = null,
    var intValue: Int? = null,
    var booleanValue: Boolean? = null,
    var doubleValue: Double? = null,
    var floatValue: Float? = null
)