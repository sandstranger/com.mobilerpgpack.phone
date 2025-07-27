package com.mobilerpgpack.phone.translator.sql

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.mobilerpgpack.phone.translator.sql.TranslationEntry

@Dao
interface TranslationDao {

    @Query("SELECT value FROM translations WHERE `key` = :key AND lang = :lang LIMIT 1")
    suspend fun getTranslation(key: String, lang: String): String?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntry)

    @Query("SELECT * FROM translations")
    suspend fun getAllTranslations(): List<TranslationEntry>

    @Upsert
    suspend fun upsertTranslation(translation: TranslationEntry)

    @Query("DELETE FROM translations WHERE `key` = :key AND lang = :lang")
    suspend fun deleteTranslation(key: String, lang: String)
}