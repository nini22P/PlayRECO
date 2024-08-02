package com.github.nini22p.playreco.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.nini22p.playreco.R
import com.github.nini22p.playreco.ui.Nav

data class MenuItem(val title: String, val icon: ImageVector, val onClick: () -> Unit)

@Composable
fun Menu(navController: NavController) {

    val menuItems = listOf(
        MenuItem(
            title = stringResource(R.string.settings),
            icon = Icons.Filled.Settings,
            onClick = { navController.navigate(Nav.Settings.route) }
        ),
        MenuItem(
            title = stringResource(R.string.about),
            icon = Icons.Filled.Info,
            onClick = { navController.navigate(Nav.About.route) }
        )
    )

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.more))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.widthIn(min = 160.dp)
        ) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.title) },
                    onClick = item.onClick,
                    leadingIcon = { Icon(item.icon, contentDescription = item.title) }
                )
            }
        }
    }
}