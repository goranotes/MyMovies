package com.goranotes.mymovies.ui.screen.favorite

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.goranotes.mymovies.R
import com.goranotes.mymovies.data.repository.UiState
import com.goranotes.mymovies.model.MovieList
import com.goranotes.mymovies.ui.components.EmptyState
import com.goranotes.mymovies.ui.components.MovieListItem
import com.goranotes.mymovies.ui.theme.MyMoviesTheme
import com.goranotes.mymovies.ui.viewmodel.FavoriteViewModel
import com.goranotes.mymovies.ui.viewmodel.ViewModelFactory

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
    navigateToDetail: (Long) -> Unit,
){
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->

        when (uiState) {
            is UiState.Loading -> {
                viewModel.getFavoriteMovie()
            }
            is UiState.Success -> {
                val data = uiState.data
                FavoriteContent(
                    movieList = data,
                    navigateToDetail = navigateToDetail,
                    onUpdateFavorite = {movieId, isFavorite ->
                        viewModel.updateFavoriteMovie(movieId, isFavorite)
                    }
                )
            }
            is UiState.Error -> {

            }
        }
    }
}

@Composable
fun FavoriteContent(
    movieList: MutableList<MovieList>,
    onUpdateFavorite: (movieId : Long, isFavorite : Boolean) -> Unit,
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
){
    if(movieList.isNotEmpty()){
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ){
            items(movieList, key = { it.id }) { data ->
                MovieListItem(
                    image = data.image,
                    title = data.title,
                    rating = data.rating,
                    isFavorite = data.isFavorite,
                    onUpdateFavorite = {
                        onUpdateFavorite(data.id, !data.isFavorite)
                    },
                    modifier = Modifier.clickable {
                        navigateToDetail(data.id)
                    }
                )
            }
        }
    }else{
        EmptyState(
            icon = Icons.Default.Favorite,
            title = stringResource(R.string.empty_state_favorite),
            body = stringResource(R.string.empty_state_favorite_detail)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MovieListItemPreview() {
    MyMoviesTheme {
        FavoriteContent(
            mutableListOf(),
            onUpdateFavorite = {movieId,isFavorite ->},
            navigateToDetail = {}
        )
    }
}