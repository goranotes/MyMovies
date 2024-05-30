package com.goranotes.mymovies.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goranotes.mymovies.data.repository.MovieRepository
import com.goranotes.mymovies.data.repository.UiState
import com.goranotes.mymovies.model.MovieList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<MutableList<MovieList>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<MutableList<MovieList>>>
        get() = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun getAllMovieList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if(_query.value.isBlank()){
                repository.getAllMovieList()
                    .catch {
                        _uiState.value = UiState.Error(it.message.toString())
                    }
                    .collect { movieList ->
                        _uiState.value = UiState.Success(movieList.toMutableList())
                    }
            }else{
                searchMovie(_query.value)
            }
        }
    }

    fun searchMovie(query: String) {
        _query.value = query
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.searchMovie(query)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { movieList ->
                    _uiState.value = UiState.Success(movieList.toMutableList())
                }
        }
    }

    fun updateFavoriteMovie(movieId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteMovie(movieId, isFavorite)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getAllMovieList()
                    }
                }
        }
    }
}