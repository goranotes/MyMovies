package com.goranotes.mymovies.ui.screen.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.goranotes.mymovies.R
import com.goranotes.mymovies.data.repository.UiState
import com.goranotes.mymovies.model.MovieList
import com.goranotes.mymovies.ui.viewmodel.DetailMovieViewModel
import com.goranotes.mymovies.ui.viewmodel.ViewModelFactory

@Composable
fun DetailScreen(
    movieId: Long,
    viewModel: DetailMovieViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
    navigateBack: () -> Unit
){
    viewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getMovieById(movieId)
            }
            is UiState.Success -> {
                val data = uiState.data
                DetailContent(
                    dataItem = data,
                    onBackClick = navigateBack,
                    onUpdateFavorite = {
                        viewModel.updateFavoriteMovie(data.id, !data.isFavorite)
                    }
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun DetailContent(
    dataItem: MovieList,
    onBackClick: () -> Unit,
    onUpdateFavorite: () -> Unit,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Box{
            AsyncImage(
                model = dataItem.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(600.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onBackClick() },
                        tint = Color.White
                    )
                    Icon(
                        imageVector = if (dataItem.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (dataItem.isFavorite) "Favorite Icon" else "Favorite Border Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onUpdateFavorite() }
                            .testTag("Favorite"),
                        tint = Color.White
                    )
                }
            }
        }
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(
                text = dataItem.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                ),
                modifier = Modifier
                    .testTag("title")
            )
            Text(
                text = dataItem.synopsis,
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.rating),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 12.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp),
                        tint = Color(LocalContext.current.getColor(R.color.gold))
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = stringResource(R.string.rating_from, dataItem.rating.toString()),
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.director),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = dataItem.director,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 12.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.writers),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = dataItem.writers,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 12.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.stars),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = dataItem.stars,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 12.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.genres),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = dataItem.genres,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 12.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.duration),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = dataItem.duration,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 12.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.year),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = dataItem.year.toString(),
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteApp() {
    DetailContent(
        MovieList(
            1,
            R.drawable.movie_1,
            "Godzilla x Kong: The New Empire",
            6.5,
            "Two ancient titans, Godzilla and Kong, clash in an epic battle as humans unravel their intertwined origins and connection to Skull Island's mysteries.",
            "Adam Wingard",
            "Terry Rossio . Simon Barrett . Jeremy Slater",
            "Rebecca Hall . Brian Tyree Henry . Dan Stevens",
            "Action . Adventure . Sci-Fi . Thriller",
            "1h 55m",
            2024
        ),
        onBackClick = {},
        onUpdateFavorite = {}
    )
}

