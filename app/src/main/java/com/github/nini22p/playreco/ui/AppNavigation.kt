package com.github.nini22p.playreco.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.nini22p.playreco.ui.home.HomeScreen
import com.github.nini22p.playreco.ui.settings.SettingsScreen
import com.github.nini22p.playreco.viewmodel.UsageViewModel

enum class Screen {
    HOME,
    SETTINGS,
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object Settings : NavigationItem(Screen.SETTINGS.name)
}

//sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
//    object Home : BottomNavItem("Home", Icons.Default.Home, NavigationItem.Home.route)
//    object Settings :
//        BottomNavItem("Settings", Icons.Default.Settings, NavigationItem.Settings.route)
//}

//val BottomNavItems = listOf(
//    BottomNavItem.Home,
//    BottomNavItem.Settings,
//)

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: UsageViewModel,
    startDestination: String = NavigationItem.Home.route,
) {
    NavHost(navController, startDestination, modifier) {
        composable(
            NavigationItem.Home.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
                )
            },
            exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                )
            },
            popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                )
            },
        ) {
            HomeScreen(viewModel)
        }
        composable(
            NavigationItem.Settings.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                )
            },
            exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
                )
            },
            popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(500)
                )
            },
        ) {
            SettingsScreen(viewModel)
        }
    }
}

//@Composable
//fun AppNavigationBar(navController: NavHostController) {
//    NavigationBar {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//        BottomNavItems.forEach { screen ->
//            NavigationBarItem(
//                icon = { Icon(screen.icon, contentDescription = null) },
//                label = { Text(screen.title) },
//                selected = currentRoute == screen.route,
//                onClick = {
//                    if (currentRoute != screen.route) {
//                        navController.navigate(screen.route) {
//                            popUpTo(navController.graph.startDestinationId)
//                            launchSingleTop = true
//                        }
//                    }
//                }
//            )
//        }
//    }
//}