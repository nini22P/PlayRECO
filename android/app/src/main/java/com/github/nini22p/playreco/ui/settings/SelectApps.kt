package com.github.nini22p.playreco.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.github.nini22p.playreco.room.entity.AppInfoEntity
import com.github.nini22p.playreco.utils.getInstalledUserApps
import com.github.nini22p.playreco.viewmodel.UsageViewModel

@Composable
fun SelectApps(viewModel: UsageViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    Box() {
        ListItem(
            modifier = Modifier.clickable { showDialog = true },
            leadingContent = {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Select apps",
                )
            },
            headlineContent = { Text(text = "Select apps") },
        )
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Select apps") },
                text = {
                    UserAppsList(viewModel)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            viewModel.fetchUsage()
                        },
                    ) {
                        Text(
                            "确认"
                        )
                    }
                },
//                dismissButton = {
//                    TextButton(
//                        onClick = {
//                            showDialog = false
//                        }
//                    ) {
//                        Text(
//                            "取消"
//                        )
//                    }
//                }
            )
        }
    }
}

@Composable
fun UserAppsList(viewModel: UsageViewModel) {
    val context = LocalContext.current
    var userApps: List<AppInfoEntity>? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        userApps = getInstalledUserApps(context)
    }

    val selectApps by viewModel.selectApp.observeAsState(emptyList())

    if (userApps != null) {
        LazyColumn {
            items(userApps!!.filter { item -> !viewModel.appInfo.any { app -> app.packageName == item.packageName } }) { item ->
                ListItem(
                    modifier = Modifier.clickable {
                        if (selectApps.any { app -> app.packageName == item.packageName }) {
                            viewModel.removeApp(item.packageName)
                            println("移除应用")
                        } else {
                            viewModel.addApp(item)
                            println("添加应用")
                        }
                    },
                    headlineContent = { item.title?.let { Text(it) } },
                    trailingContent = {
                        Checkbox(
                            checked = selectApps.any { app -> app.packageName == item.packageName },
                            onCheckedChange = {
                                if (selectApps.any { app -> app.packageName == item.packageName }) {
                                    viewModel.removeApp(item.packageName)
                                    println("移除应用")
                                } else {
                                    viewModel.addApp(item)
                                    println("添加应用")
                                }
                            }
                        )
                    },
                )
            }
        }
    }
}