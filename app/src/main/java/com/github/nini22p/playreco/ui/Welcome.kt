package com.github.nini22p.playreco.ui

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun Welcome() {
    val context = LocalContext.current
    val permissionGranted = rememberSaveable { mutableStateOf(hasUsageStatsPermission(context)) }
    if (!permissionGranted.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    requestUsagePermission(context)
                    Toast.makeText(context, "Please allow usage permission", Toast.LENGTH_LONG)
                        .show()
                }
            ) {
                Text(text = "Request Usage Permission")
            }
        }
    }
}

// 检查权限
@SuppressLint("ServiceCast")
fun hasUsageStatsPermission(context: Context): Boolean {
    val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOpsManager.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
    } else {
        appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
    }
    return mode == AppOpsManager.MODE_ALLOWED
}

// 请求权限
fun requestUsagePermission(context: Context) {
    val intent = Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS)
    intent.addCategory("android.intent.category.DEFAULT")
    intent.data = Uri.parse("package:" + context.packageName)
    context.startActivity(intent)
}