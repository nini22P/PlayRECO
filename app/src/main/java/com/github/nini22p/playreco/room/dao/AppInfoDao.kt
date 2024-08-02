package com.github.nini22p.playreco.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.github.nini22p.playreco.room.entity.AppInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppInfoDao {
    @Upsert
    suspend fun upsert(vararg appInfo: AppInfoEntity)

    @Query("SELECT * FROM app_info")
    fun getAppInfo(): Flow<List<AppInfoEntity>>

    @Query("DELETE FROM app_info WHERE package_name = :packageName")
    suspend fun delete(packageName: String)

    @Query("DELETE FROM app_info")
    suspend fun clear()
}
