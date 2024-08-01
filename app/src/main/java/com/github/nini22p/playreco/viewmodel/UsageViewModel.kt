package com.github.nini22p.playreco.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.app.usage.UsageStatsManager
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.nini22p.playreco.room.database.UsageDatabase
import com.github.nini22p.playreco.room.entity.UsageEntity
import com.github.nini22p.playreco.room.repository.UsageRepository
import com.github.nini22p.playreco.utils.getAppInfo
import com.github.nini22p.playreco.utils.getUserInstalledApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class Interval {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
}

val usageTimeMap = mapOf(
    Interval.DAILY to UsageStatsManager.INTERVAL_DAILY,
    Interval.WEEKLY to UsageStatsManager.INTERVAL_WEEKLY,
    Interval.MONTHLY to UsageStatsManager.INTERVAL_MONTHLY,
    Interval.YEARLY to UsageStatsManager.INTERVAL_YEARLY,
)

val dateFormat = mapOf(
    Interval.DAILY to "yyyy-MM-dd",
    Interval.WEEKLY to "yyyy ww",
    Interval.MONTHLY to "yyyy-MM",
    Interval.YEARLY to "yyyy"
)

class UsageViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application
    private val usageRepository: UsageRepository

    init {
        val database = UsageDatabase.getDatabase(getApplication())
        usageRepository = UsageRepository(database.usageDao())
        fetchUsage()
    }

    private val _interval = MutableLiveData<Interval>().apply { value = Interval.YEARLY }
    private val interval: LiveData<Interval> get() = _interval

    val usage: LiveData<List<UsageEntity>>? =
        interval.value?.let { usageRepository.getUsage(it).asLiveData() }

    fun clearUsage() {
        viewModelScope.launch {
            usageRepository.clear()
        }
    }

    fun fetchUsage() {
        viewModelScope.launch {
            val dailyUsage =
                withContext(Dispatchers.IO) { usageFilter(getUsage(context, Interval.DAILY)) }
            dailyUsage.forEach { usage: UsageEntity -> usageRepository.upsert(usage) }

            val weeklyUsage =
                withContext(Dispatchers.IO) { usageFilter(getUsage(context, Interval.WEEKLY)) }
            weeklyUsage.forEach { usage: UsageEntity -> usageRepository.upsert(usage) }

            val monthlyUsage =
                withContext(Dispatchers.IO) { getUsage(context, Interval.MONTHLY) }
            monthlyUsage.forEach { usage: UsageEntity -> usageRepository.upsert(usage) }

            val yearlyUsage =
                withContext(Dispatchers.IO) { getUsage(context, Interval.YEARLY) }
            yearlyUsage.forEach { usage: UsageEntity -> usageRepository.upsert(usage) }
        }
    }

    private suspend fun getUsage(context: Context, interval: Interval): List<UsageEntity> {
        return withContext(Dispatchers.IO) {
            val dateFormatter = SimpleDateFormat(dateFormat[interval], Locale.getDefault())
            val usageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            val beginTime = Calendar.getInstance()
            beginTime.add(Calendar.DAY_OF_YEAR, -99999)

            val endTime = System.currentTimeMillis()

            val usage = mutableListOf<UsageEntity>()

            usageTimeMap[interval]?.let { intervalType ->
                val usageStatsList = usageStatsManager.queryUsageStats(
                    intervalType,
                    beginTime.timeInMillis,
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
                            getAndroidId(),
                        )
                        usage.add(item)
                    }
                }
            }
            usage
        }
    }

    private suspend fun usageFilter(usage: List<UsageEntity>): List<UsageEntity> {
        return withContext(Dispatchers.IO) {
            val userInstalledApps = getUserInstalledApps(context)
            usage
                .filter { item ->
                    userInstalledApps.any { app -> app.packageName == item.packageName }
                }
        }
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}
