package com.github.nini22p.playreco.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "app_info",
    primaryKeys = ["package_name"]
)
data class AppInfoEntity(
    @ColumnInfo(name = "package_name") val packageName: String,
    val title: String?
)