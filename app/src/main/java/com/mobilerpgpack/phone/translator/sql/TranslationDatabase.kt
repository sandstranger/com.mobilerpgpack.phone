package com.mobilerpgpack.phone.translator.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobilerpgpack.phone.translator.sql.TranslationEntry

@Database(entities = [TranslationEntry::class], version = 1)
@TypeConverters(EngineTypeConverter::class)
abstract class TranslationDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao

    companion object {
        fun createInstance(context: Context): TranslationDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                TranslationDatabase::class.java,
                "translations.db"
            ).build()
        }
    }
}