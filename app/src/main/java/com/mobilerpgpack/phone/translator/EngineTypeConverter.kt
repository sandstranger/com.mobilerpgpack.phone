package com.mobilerpgpack.phone.translator

import androidx.room.TypeConverter
import com.mobilerpgpack.phone.engine.EngineTypes

class EngineTypeConverter {
    @TypeConverter
    fun fromEngineType(value: EngineTypes): String = value.name

    @TypeConverter
    fun toEngineType(value: String): EngineTypes = EngineTypes.valueOf(value)
}