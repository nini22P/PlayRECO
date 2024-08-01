package com.github.nini22p.playreco.room.repository

import androidx.annotation.WorkerThread
import com.github.nini22p.playreco.room.dao.UsageDao
import com.github.nini22p.playreco.room.entity.UsageEntity
import com.github.nini22p.playreco.viewmodel.Interval
import kotlinx.coroutines.flow.Flow

class UsageRepository(private val usageDao: UsageDao) {

    fun getUsage(interval: Interval): Flow<List<UsageEntity>> {
        val usage: Flow<List<UsageEntity>> = usageDao.getAllUsage(interval)
        return usage
    }

    @WorkerThread
    suspend fun upsert(usage: UsageEntity) {
        usageDao.upsert(usage)
    }

    @WorkerThread
    suspend fun clear() {
        usageDao.clear()
    }
}