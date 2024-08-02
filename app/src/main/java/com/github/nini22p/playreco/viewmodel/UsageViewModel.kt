package com.github.nini22p.playreco.viewmodel

import android.app.Application
import android.app.usage.UsageStatsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.nini22p.playreco.room.database.UsageDatabase
import com.github.nini22p.playreco.room.entity.AppInfoEntity
import com.github.nini22p.playreco.room.entity.UsageEntity
import com.github.nini22p.playreco.room.repository.AppInfoRepository
import com.github.nini22p.playreco.room.repository.UsageRepository
import com.github.nini22p.playreco.utils.getAppInfo
import com.github.nini22p.playreco.utils.getUsage
import com.github.nini22p.playreco.utils.usageFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private val appInfoRepository: AppInfoRepository
    lateinit var appInfo: List<AppInfoEntity>

    init {
        viewModelScope.launch {
            appInfo = getAppInfo(context)
        }
        val database = UsageDatabase.getDatabase(getApplication())
        usageRepository = UsageRepository(database.usageDao())
        appInfoRepository = AppInfoRepository(database.appInfoDao())
        fetchUsage()
    }

    val interval = MutableLiveData<Interval>().apply { value = Interval.YEARLY }

    val usage: LiveData<List<UsageEntity>>? =
        interval.value?.let { usageRepository.getUsage(it).asLiveData() }

    val selectApp: LiveData<List<AppInfoEntity>> = appInfoRepository.getAppInfo().asLiveData()

    fun clearUsage() {
        viewModelScope.launch {
            usageRepository.clear()
        }
    }

    fun fetchUsage() {
        viewModelScope.launch {
            val dailyUsage = withContext(Dispatchers.IO) { getUsage(context, Interval.DAILY) }
            usageFilter(
                dailyUsage,
                appInfo,
                selectApp.value ?: emptyList(),
            ).forEach { usage: UsageEntity -> usageRepository.upsert(usage) }

            val weeklyUsage =
                withContext(Dispatchers.IO) { getUsage(context, Interval.WEEKLY) }
            usageFilter(
                weeklyUsage,
                appInfo,
                selectApp.value ?: emptyList(),
            ).forEach { usage: UsageEntity -> usageRepository.upsert(usage) }

            val monthlyUsage =
                withContext(Dispatchers.IO) { getUsage(context, Interval.MONTHLY) }
            usageFilter(
                monthlyUsage,
                appInfo,
                selectApp.value ?: emptyList(),
            ).forEach { usage: UsageEntity -> usageRepository.upsert(usage) }

            val yearlyUsage =
                withContext(Dispatchers.IO) { getUsage(context, Interval.YEARLY) }
            usageFilter(
                yearlyUsage,
                appInfo,
                selectApp.value ?: emptyList(),
            ).forEach { usage: UsageEntity -> usageRepository.upsert(usage) }
        }
    }

    fun addApp(appInfo: AppInfoEntity) {
        viewModelScope.launch {
            appInfoRepository.upsert(appInfo)
        }
    }

    fun removeApp(packageName: String) {
        viewModelScope.launch {
            appInfoRepository.delete(packageName)
        }
    }
}
