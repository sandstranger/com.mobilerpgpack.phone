package com.mobilerpgpack.phone.translator.sql

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.models.TranslationType

@Entity(
    tableName = "translations",
    indices = [Index(value = ["key", "lang", "engine","translationModelType"], unique = true)]
)
data class TranslationEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val key: String,
    val lang: String,
    val value: String,
    val engine: EngineTypes,
    val translationModelType : TranslationType
)