package com.example.ctrlu.data

import androidx.room.*
import com.example.ctrlu.model.DailyUsageEntry

@Dao
interface DailyUsageDao {
    @Query("SELECT * FROM DailyUsageEntry ORDER BY date DESC")
    suspend fun getAll(): List<DailyUsageEntry>

    @Query("SELECT * FROM DailyUsageEntry WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): DailyUsageEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DailyUsageEntry)
}
