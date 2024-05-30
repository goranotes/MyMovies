package com.goranotes.mymovies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goranotes.mymovies.data.repository.MovieRepository
import com.goranotes.mymovies.di.Injection

class ViewModelFactory private constructor(private val movieRepository: MovieRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(movieRepository) as T
        }else if (modelClass.isAssignableFrom(DetailMovieViewModel::class.java)) {
            return DetailMovieViewModel(movieRepository) as T
        }else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(movieRepository) as T
        }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }.also { instance = it }
    }
}