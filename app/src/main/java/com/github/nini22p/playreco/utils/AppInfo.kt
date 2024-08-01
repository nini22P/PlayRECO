package com.github.nini22p.playreco.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class AppInfo(val title: String?, val icon: Drawable?)

suspend fun getAppInfo(context: Context, packageName: String): AppInfo {
    return withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            // 获取应用标题
            val title = packageManager.getApplicationLabel(applicationInfo).toString()
            // 获取应用图标
            val icon = packageManager.getApplicationIcon(packageName)

            AppInfo(title, icon)
        } catch (e: PackageManager.NameNotFoundException) {
            // 如果找不到应用信息，返回默认值
            AppInfo(null, null)
        }
    }
}

suspend fun getUserInstalledApps(context: Context): List<ApplicationInfo> {
    return withContext(Dispatchers.IO) {
        val pm: PackageManager = context.packageManager
        val allApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        allApps.filter { appInfo ->
            // 过滤掉系统应用和用户更新的系统应用
            val isNonSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 &&
                    (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
            // 仅包含具有启动图标的应用
            isNonSystemApp && pm.getLaunchIntentForPackage(appInfo.packageName) != null
        }
    }
}