package com.example.ctrlu.data
import com.example.ctrlu.model.MetaInfo
import androidx.room.*

@Dao
interface MetaInfoDao {
    @Query("SELECT * FROM MetaInfo WHERE 'key' = :key LIMIT 1")
    suspend fun getByKey(key: String): MetaInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meta: MetaInfo)
}