package com.github.nini22p.playreco.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.github.nini22p.playreco.viewmodel.Interval

@Entity(
    tableName = "usage",
    primaryKeys = ["package_name", "date", "interval", "device_id"]
)
data class UsageEntity(
    @ColumnInfo(name = "package_name") val packageName: String,
    val title: String?,
    val date: String,
    @ColumnInfo(name = "total_time") val totalTime: Long,
    val interval: Interval,
    @ColumnInfo(name = "device_id") val deviceId: String
)