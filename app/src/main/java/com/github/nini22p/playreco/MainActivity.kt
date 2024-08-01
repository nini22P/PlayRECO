package com.github.nini22p.playreco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.nini22p.playreco.ui.AppNavHost
import com.github.nini22p.playreco.ui.NavigationItem
import com.github.nini22p.playreco.ui.Welcome
import com.github.nini22p.playreco.ui.hasUsageStatsPermission
import com.github.nini22p.playreco.ui.home.RefreshButton
import com.github.nini22p.playreco.ui.theme.PlayRECOTheme
import com.github.nini22p.playreco.viewmodel.UsageViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlayRECOTheme {
                val context = LocalContext.current
                var hasPermission by rememberSaveable {
                    mutableStateOf(
                        hasUsageStatsPermission(
                            context
                        )
                    )
                }

                LaunchedEffect(Unit) {
                    while (!hasPermission) {
                        hasPermission = hasUsageStatsPermission(context)
                        delay(1000)
                        println("Check permission")
                    }
                }

                if (hasPermission)
                    PlayRECOApp()
                else
                    Welcome()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayRECOApp() {

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val viewModel: UsageViewModel = viewModel()

    Scaffold(
        topBar = {
            when (currentDestination?.route) {
//                主屏幕
                NavigationItem.Home.route ->
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = { Text(text = stringResource(R.string.app_name)) },
                        actions = {
                            IconButton(onClick = { navController.navigate(NavigationItem.Settings.route) }) {
                                Icon(Icons.Filled.Settings, contentDescription = "Settings")
                            }
                        }
                    )
//                设置
                NavigationItem.Settings.route ->
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        title = { Text(text = "Settings") },
                    )
            }
        },
//        bottomBar = { AppNavigationBar(navController) },
        floatingActionButton = {
            when (currentDestination?.route) {
                NavigationItem.Home.route -> RefreshButton(viewModel)
            }
        },
        content = { padding ->
            AppNavHost(
                modifier = Modifier.padding(padding),
                navController,
                viewModel
            )
        }
    )
}