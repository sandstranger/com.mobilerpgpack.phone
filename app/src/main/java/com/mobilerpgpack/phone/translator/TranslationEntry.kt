package com.mobilerpgpack.phone.translator

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mobilerpgpack.phone.engine.EngineTypes

@Entity(
    tableName = "translations",
    indices = [Index(value = ["key", "lang", "engine"], unique = true)]
)
data class TranslationEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val key: String,
    val lang: String,
    val value: String,
    val engine: EngineTypes
)