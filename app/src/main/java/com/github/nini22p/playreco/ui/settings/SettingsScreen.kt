package com.github.nini22p.playreco.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.github.nini22p.playreco.viewmodel.UsageViewModel

@Composable
fun SettingsScreen(viewModel: UsageViewModel) {

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
            modifier = Modifier.clickable { viewModel.clearUsage() },
            headlineContent = { Text(text = "Clear local data") }
        )
        ListItem(
            modifier = Modifier.clickable {
                openWebsite("https://github.com/nini22P/PlayRECO")
            },
            headlineContent = { Text(text = "GitHub") }
        )
    }

}