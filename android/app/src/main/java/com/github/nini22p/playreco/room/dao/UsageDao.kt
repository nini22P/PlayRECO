package com.github.nini22p.playreco.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.github.nini22p.playreco.room.entity.UsageEntity
import com.github.nini22p.playreco.viewmodel.Interval
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageDao {
    @Upsert
    suspend fun upsert(vararg usage: UsageEntity)

    @Query("SELECT * FROM usage WHERE interval = :interval ORDER BY date DESC, total_time DESC")
    fun getAllUsage(interval: Interval): Flow<List<UsageEntity>>

    @Query("DELETE FROM usage")
    suspend fun clear()
}
