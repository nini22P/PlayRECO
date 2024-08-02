package com.github.nini22p.playreco.ui.about

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.nini22p.playreco.R
import com.github.nini22p.playreco.viewmodel.UsageViewModel

@Composable
fun AboutScreen(viewModel: UsageViewModel) {

    val context = LocalContext.current

    fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ListItem(
            modifier = Modifier.clickable {
                openWebsite("https://github.com/nini22P/PlayRECO")
            },
            leadingContent = {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "GitHub",
                )
            },
            headlineContent = { Text(text = stringResource(R.string.app_name)) },
            supportingContent = { Text(text = getAppVersion(context)) }
        )
        ListItem(
            modifier = Modifier.clickable {
                openWebsite("https://github.com/nini22P/PlayRECO")
            },
            leadingContent = {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "GitHub",
                )
            },
            headlineContent = { Text(text = "GitHub") },
        )
    }

}

fun getAppVersion(context: Context): String {
    val packageManager = context.packageManager
    try {
        val packageInfo: PackageInfo = packageManager.getPackageInfo(context.packageName, 0)

        val versionName = packageInfo.versionName

        return versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        return "Unknown"
    }
}