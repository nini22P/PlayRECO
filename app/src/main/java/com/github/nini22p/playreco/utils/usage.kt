package com.github.nini22p.playreco.utils

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.Context
import android.provider.Settings
import com.github.nini22p.playreco.room.entity.AppInfoEntity
import com.github.nini22p.playreco.room.entity.UsageEntity
import com.github.nini22p.playreco.viewmodel.Interval
import com.github.nini22p.playreco.viewmodel.dateFormat
import com.github.nini22p.playreco.viewmodel.usageTimeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

suspend fun getUsage(context: Context, interval: Interval): List<UsageEntity> {
    return withContext(Dispatchers.IO) {
        val dateFormatter = SimpleDateFormat(dateFormat[interval], Locale.getDefault())
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)

        calendar.set(year - 2, Calendar.JANUARY, 1, 0, 0, 0)
        val beginTime = calendar.timeInMillis

        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59)
        val endTime = calendar.timeInMillis

        val androidId = getAndroidId(context)

        val usage = mutableListOf<UsageEntity>()

        usageTimeMap[interval]?.let { intervalType ->
            val usageStatsList = usageStatsManager.queryUsageStats(
                intervalType,
                beginTime,
                endTime,
            )
            usageStatsList?.forEach { usageStats ->
                val date = dateFormatter.format(Date(usageStats.firstTimeStamp))
                val totalTime = usageStats.totalTimeInForeground
                if (totalTime / 1000 > 0) {
                    val item = UsageEntity(
                        usageStats.packageName,
                        getAppInfo(context, usageStats.packageName).title,
                        date,
                        totalTime,
                        interval,
                        androidId,
                    )
                    usage.add(item)
                }
            }
        }
        usage
    }
}

suspend fun usageFilter(
    usage: List<UsageEntity>,
    appInfo: List<AppInfoEntity>,
    selectApp: List<AppInfoEntity>
): List<UsageEntity> {
    return withContext(Dispatchers.IO) {
        usage.filter { item ->
            selectApp.any { app -> app.packageName == item.packageName } || appInfo.any { app -> app.packageName == item.packageName }
        }
    }
}

@SuppressLint("HardwareIds")
fun getAndroidId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}