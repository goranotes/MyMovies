package com.goranotes.mymovies

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.goranotes.mymovies.model.FakeMovieListDataSource
import com.goranotes.mymovies.ui.navigation.Screen
import com.goranotes.mymovies.ui.theme.MyMoviesTheme
import kotlinx.coroutines.delay
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyMoviesAppTest{

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MyMoviesTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                MyMoviesApp(navController = navController)
            }
        }
    }

    //---------------------------------------- Positive case ----------------------------------------//
    @Test
    fun navHost_verifyStartDestination() {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(Screen.Home.route, currentRoute)
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_bottomNavigation_working() {
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithStringId(R.string.menu_about).performClick()
        navController.assertCurrentRouteName(Screen.About.route)
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickItem_navigatesToDetailWithData() {
        composeTestRule.onNodeWithTag("MovieList").performScrollToIndex(9)
        composeTestRule.onNodeWithText(FakeMovieListDataSource.dummyMovieList[9].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailMovie.route)
        composeTestRule.onNodeWithText(FakeMovieListDataSource.dummyMovieList[9].title).assertIsDisplayed()
    }

    @Test
    fun navHost_clickItem_navigatesBack() {
        composeTestRule.onNodeWithTag("MovieList").performScrollToIndex(9)
        composeTestRule.onNodeWithText(FakeMovieListDataSource.dummyMovieList[9].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailMovie.route)
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back)).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_HomeScreen_addToFavorite_checkFavoriteMenu() {
        composeTestRule.onNodeWithTag("MovieList").performScrollToIndex(9)
        composeTestRule.onAllNodesWithTag("Favorite")[3].performClick()
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithText(FakeMovieListDataSource.dummyMovieList[9].title).assertIsDisplayed()
    }

    @Test
    fun navHost_DetailScreen_addToFavorite_checkFavoriteMenu() {
        composeTestRule.onNodeWithText(FakeMovieListDataSource.dummyMovieList[1].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailMovie.route)
        composeTestRule.onNodeWithTag("Favorite").performClick()
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back)).performClick()
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithText(FakeMovieListDataSource.dummyMovieList[1].title).assertIsDisplayed()
    }
    //---------------------------------------- Positive case ----------------------------------------//

    //---------------------------------------- Negative case ----------------------------------------//
    @Test
    fun navHost_HomeScreen_addToFavorite_checkFavoriteMenu_error() {
        composeTestRule.onAllNodesWithTag("Favorite")[2].performClick()
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithText(FakeMovieListDataSource.dummyMovieList[2].title).assertIsDisplayed()
    }
    //---------------------------------------- Negative case ----------------------------------------//
}