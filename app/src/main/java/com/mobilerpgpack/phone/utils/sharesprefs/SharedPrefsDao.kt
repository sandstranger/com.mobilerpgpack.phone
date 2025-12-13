package com.mobilerpgpack.phone.utils.sharesprefs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
internal interface SharedPrefsDao {
    @Upsert
    suspend fun upsert(entry: SharedPrefsEntry)

    @Query("DELETE FROM ${SharedPrefsDatabase.DATABASE_NAME} WHERE `key` = :key")
    suspend fun delete(key: String)

    @Query("SELECT * FROM ${SharedPrefsDatabase.DATABASE_NAME}")
    suspend fun getAllEntries(): List<SharedPrefsEntry>
}
