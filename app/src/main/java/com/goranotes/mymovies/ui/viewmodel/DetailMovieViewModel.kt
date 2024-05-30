package com.goranotes.mymovies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goranotes.mymovies.data.repository.MovieRepository
import com.goranotes.mymovies.data.repository.UiState
import com.goranotes.mymovies.model.MovieList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailMovieViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<MovieList>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<MovieList>>
        get() = _uiState

    fun getMovieById(movieId: Long) {

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getMovieById(movieId))
        }
    }

    fun updateFavoriteMovie(movieId: Long, isFavorite: Boolean) {

       viewModelScope.launch {
            repository.updateFavoriteMovie(movieId, isFavorite)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getMovieById(movieId)
                    }
                }
       }
    }
}