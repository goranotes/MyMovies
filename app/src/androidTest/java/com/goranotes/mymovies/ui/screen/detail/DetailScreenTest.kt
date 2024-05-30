package com.goranotes.mymovies.ui.screen.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import com.goranotes.mymovies.R
import com.goranotes.mymovies.model.FakeMovieListDataSource
import com.goranotes.mymovies.onNodeWithStringId
import com.goranotes.mymovies.ui.theme.MyMoviesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailScreenTest{

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp(){
        composeTestRule.setContent {
            MyMoviesTheme {
                DetailScreen(
                    1,
                    navigateBack = {}
                )
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun detailScreen_IsDisplayed(){
        composeTestRule.onNodeWithText(FakeMovieListDataSource.dummyMovieList[0].title).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.rating).performScrollTo()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.rating,
                FakeMovieListDataSource.dummyMovieList[0].rating.toString()
            )
        ).assertIsDisplayed()
    }

    @Test
    fun detailScreen_clickFavorite_working(){
        composeTestRule.onNodeWithContentDescription("Favorite Border Icon").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Favorite").performClick()
        composeTestRule.onNodeWithContentDescription("Favorite Icon").assertIsDisplayed()
    }
}