package com.github.nini22p.playreco.room.repository

import androidx.annotation.WorkerThread
import com.github.nini22p.playreco.room.dao.AppInfoDao
import com.github.nini22p.playreco.room.entity.AppInfoEntity
import kotlinx.coroutines.flow.Flow

class AppInfoRepository(private val appInfoDao: AppInfoDao) {

    fun getAppInfo(): Flow<List<AppInfoEntity>> {
        val appInfo: Flow<List<AppInfoEntity>> = appInfoDao.getAppInfo()
        return appInfo
    }

    @WorkerThread
    suspend fun upsert(appInfo: AppInfoEntity) {
        appInfoDao.upsert(appInfo)
    }

    @WorkerThread
    suspend fun delete(packageName: String) {
        appInfoDao.delete(packageName)
    }

    @WorkerThread
    suspend fun clear() {
        appInfoDao.clear()
    }
}