package com.mobilerpgpack.phone.utils.sharesprefs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.java.KoinJavaComponent.get

@Database(entities = [SharedPrefsEntry::class], version = 1)
internal abstract class SharedPrefsDatabase : RoomDatabase() {

    abstract fun dao(): SharedPrefsDao

    companion object {
        const val DATABASE_NAME = "shared_prefs"
        private const val FULL_DATABASE_NAME = "$DATABASE_NAME.db"

        fun createInstance(): SharedPrefsDatabase {
            val context : Context = get (Context::class.java)
            return Room.databaseBuilder(context.applicationContext,
                SharedPrefsDatabase::class.java,
                FULL_DATABASE_NAME).build()
        }
    }
}