package com.goranotes.mymovies

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.goranotes.mymovies.ui.navigation.NavigationItem
import com.goranotes.mymovies.ui.navigation.Screen
import com.goranotes.mymovies.ui.screen.about.AboutScreen
import com.goranotes.mymovies.ui.screen.detail.DetailScreen
import com.goranotes.mymovies.ui.screen.favorite.FavoriteScreen
import com.goranotes.mymovies.ui.screen.home.HomeScreen
import com.goranotes.mymovies.ui.theme.MyMoviesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMoviesApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailMovie.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ){innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Screen.Home.route){
                HomeScreen(
                    navigateToDetail = { movieId ->
                        navController.navigate(Screen.DetailMovie.createRoute(movieId))
                    }
                )
            }
            composable(Screen.Favorite.route){
                FavoriteScreen(
                    navigateToDetail = { movieId ->
                        navController.navigate(Screen.DetailMovie.createRoute(movieId))
                    }
                )
            }
            composable(Screen.About.route){
                AboutScreen()
            }
            composable(
                route = Screen.DetailMovie.route,
                arguments = listOf(navArgument("movieId") { type = NavType.LongType }),
            ) {
                val id = it.arguments?.getLong("movieId") ?: -1L
                DetailScreen(
                    movieId = id,
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home,
                contentDescription = stringResource(R.string.page_home)
            ),
            NavigationItem(
                title = stringResource(R.string.menu_favorite),
                icon = Icons.Default.Favorite,
                screen = Screen.Favorite,
                contentDescription = stringResource(R.string.page_favorite)
            ),
            NavigationItem(
                title = stringResource(R.string.menu_about),
                icon = Icons.Default.AccountCircle,
                screen = Screen.About,
                contentDescription = stringResource(R.string.page_about)
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.semantics {
                            contentDescription = item.title
                        }
                    )
                },
                label = {
                    Text(item.title)
                        },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JetHeroesAppPreview() {
    MyMoviesTheme {
        MyMoviesApp()
    }
}