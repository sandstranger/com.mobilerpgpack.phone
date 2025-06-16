package com.mobilerpgpack.phone.translator

import androidx.room.*

@Dao
interface TranslationDao {

    @Query("SELECT value FROM translations WHERE `key` = :key AND lang = :lang LIMIT 1")
    suspend fun getTranslation(key: String, lang: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntry)

    @Query("SELECT * FROM translations")
    suspend fun getAllTranslations(): Collection<TranslationEntry>

    @Upsert
    suspend fun upsertTranslation(translation: TranslationEntry)

    @Query("DELETE FROM translations WHERE `key` = :key AND lang = :lang")
    suspend fun deleteTranslation(key: String, lang: String)
}