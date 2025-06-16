package com.mobilerpgpack.phone.translator

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TranslationEntry::class], version = 1)
@TypeConverters(EngineTypeConverter::class)
abstract class TranslationDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao

    companion object {
        @Volatile private var INSTANCE: TranslationDatabase? = null

        fun getInstance(context: Context): TranslationDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TranslationDatabase::class.java,
                    "translations.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}