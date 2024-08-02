package com.github.nini22p.playreco.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.nini22p.playreco.ui.about.AboutScreen
import com.github.nini22p.playreco.ui.home.HomeScreen
import com.github.nini22p.playreco.ui.settings.SettingsScreen
import com.github.nini22p.playreco.viewmodel.UsageViewModel

enum class Screen {
    HOME,
    SETTINGS,
    ABOUT,
}

sealed class Nav(val route: String) {
    data object Home : Nav(Screen.HOME.name)
    data object Settings : Nav(Screen.SETTINGS.name)
    data object About : Nav(Screen.ABOUT.name)
}

data class Route(
    val route: String,
    val content: @Composable () -> Unit
)

@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: UsageViewModel,
    startDestination: String = Nav.Home.route,
) {
    val routes = listOf(
//        Route(Nav.Home.route, { HomeScreen(viewModel) }),
        Route(route = Nav.Settings.route, content = { SettingsScreen(viewModel) }),
        Route(route = Nav.About.route, content = { AboutScreen(viewModel) })
    )

    NavHost(navController, startDestination, modifier) {
        composable(
            Nav.Home.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(250)
                )
            },
            exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(250)
                )
            },
            popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(250)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(250)
                )
            },
        ) {
            HomeScreen(viewModel)
        }
        routes.forEach { route ->
            composable(
                route.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left, tween(250)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(250)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left, tween(250)
                    )
                },
                popExitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(250)
                    )
                },
            ) {
                route.content()
            }
        }
    }
}