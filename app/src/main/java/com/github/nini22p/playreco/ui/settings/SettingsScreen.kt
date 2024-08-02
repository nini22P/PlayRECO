package com.github.nini22p.playreco.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.nini22p.playreco.R
import com.github.nini22p.playreco.viewmodel.UsageViewModel

@Composable
fun SettingsScreen(viewModel: UsageViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.apps),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 60.dp, top = 16.dp, bottom = 8.dp)
        )
        SelectApps(viewModel)
        Text(
            text = stringResource(R.string.data),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 60.dp, top = 16.dp, bottom = 8.dp)
        )
        ListItem(
            modifier = Modifier.clickable { viewModel.clearUsage() },
            leadingContent = {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Clear local data",
                )
            },
            headlineContent = { Text(text = "Clear local data") }
        )
    }

}