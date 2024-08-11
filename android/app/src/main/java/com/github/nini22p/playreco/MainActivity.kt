package com.github.nini22p.playreco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.nini22p.playreco.ui.Nav
import com.github.nini22p.playreco.ui.NavHost
import com.github.nini22p.playreco.ui.TopBar
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

@Composable
fun PlayRECOApp() {

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val viewModel: UsageViewModel = viewModel()

    Scaffold(
        topBar = {
            TopBar(navController)
        },
//        bottomBar = { AppNavigationBar(navController) },
        floatingActionButton = {
            when (currentDestination?.route) {
                Nav.Home.route -> RefreshButton(viewModel)
            }
        },
        content = { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController,
                viewModel
            )
        }
    )
}