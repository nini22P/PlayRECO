package com.github.nini22p.playreco.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.github.nini22p.playreco.room.entity.AppInfoEntity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getAppInfo(context: Context, packageName: String): AppInfoEntity {
    return withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            // 获取应用标题
            val title = packageManager.getApplicationLabel(applicationInfo).toString()
            // 获取应用图标
            val icon = packageManager.getApplicationIcon(packageName)

            AppInfoEntity(packageName, title)
        } catch (e: PackageManager.NameNotFoundException) {
            // 如果找不到应用信息，返回默认值
            AppInfoEntity(packageName, null)
        }
    }
}

suspend fun getInstalledUserApps(context: Context): List<AppInfoEntity> {
    return withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val apps = mutableListOf<AppInfoEntity>()

        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (app in packages) {
            if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 && packageManager.getLaunchIntentForPackage(
                    app.packageName
                ) != null
            ) {
                val title = packageManager.getApplicationLabel(app).toString()
                val icon = packageManager.getApplicationIcon(app)
                apps.add(AppInfoEntity(app.packageName, title))
            }
        }
        apps.sortedBy { item -> item.title }
    }
}

fun getAppInfo(context: Context): List<AppInfoEntity> {
    val assetsAppInfo = readAppInfoFromAssets(context)
    return assetsAppInfo
}

fun readAppInfoFromAssets(context: Context): List<AppInfoEntity> {
    val assetManager = context.assets
    val inputStream = assetManager.open("app-info.json")
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    return parseAppInfo(jsonString)
}

fun parseAppInfo(jsonString: String): List<AppInfoEntity> {
    return try {
        val gson = Gson()
        val type = object : TypeToken<List<AppInfoEntity>>() {}.type
        gson.fromJson(jsonString, type) ?: emptyList()
    } catch (e: JsonSyntaxException) {
        emptyList()
    }
}
