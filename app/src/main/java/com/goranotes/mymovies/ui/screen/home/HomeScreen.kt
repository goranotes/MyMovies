package com.goranotes.mymovies.ui.screen.home

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
import com.goranotes.mymovies.ui.screen.favorite.FavoriteContent
import com.goranotes.mymovies.ui.theme.MyMoviesTheme
import com.goranotes.mymovies.ui.viewmodel.HomeViewModel
import com.goranotes.mymovies.ui.viewmodel.ViewModelFactory

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
    navigateToDetail: (Long) -> Unit
){
    val query by viewModel.query
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->

        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllMovieList()
            }
            is UiState.Success -> {
                val data = uiState.data
                HomeContent(
                    movieList = data,
                    navigateToDetail = navigateToDetail,
                    query = query,
                    onQueryChange = viewModel::searchMovie,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    movieList: MutableList<MovieList>,
    query: String,
    onQueryChange: (String) -> Unit,
    onUpdateFavorite: (movieId : Long, isFavorite : Boolean) -> Unit,
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
){
    Box{
        val gridState = rememberLazyGridState()

        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            modifier = modifier
                .background(colorResource(R.color.brink_pink_approx))
        )
        if(movieList.isNotEmpty()){
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(140.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
                    .padding(top = 96.dp)
                    .testTag("MovieList")
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
                        modifier = Modifier
                            .clickable {
                                navigateToDetail(data.id)
                            }
                            .testTag("MovieItem")
                    )
                }
            }
        }else{
            EmptyState(
                icon = Icons.Default.Movie,
                title = stringResource(R.string.empty_state_home),
                body = stringResource(R.string.empty_state_home_detail)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {},
        active = false,
        onActiveChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        placeholder = {
            Text(stringResource(R.string.search_movie))
        },
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
    }
}

@Composable
@Preview(showBackground = true)
fun MovieListItemPreview() {
    MyMoviesTheme {
        HomeContent(
            mutableListOf(),
            "",
            onQueryChange = {},
            onUpdateFavorite = {movieId,isFavorite ->},
            navigateToDetail = {}
        )
    }
}