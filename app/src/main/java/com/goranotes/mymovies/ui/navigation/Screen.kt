package com.goranotes.mymovies.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object About : Screen("about")
    object DetailMovie : Screen("home/{movieId}") {
        fun createRoute(movieId: Long) = "home/$movieId"
    }
}
