package com.goranotes.mymovies.data.repository

import android.util.Log
import com.goranotes.mymovies.model.FakeMovieListDataSource
import com.goranotes.mymovies.model.MovieList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MovieRepository {

    private val movieList = mutableListOf<MovieList>()

    init {
        if (movieList.isEmpty()) {
            FakeMovieListDataSource.dummyMovieList.forEach {
                movieList.add(it)
            }
        }
    }

    fun getAllMovieList(): Flow<MutableList<MovieList>> {
        return flowOf(movieList)
    }

    fun searchMovie(query: String): Flow<MutableList<MovieList>>{
        return flowOf(movieList.filter {
            it.title.contains(query, ignoreCase = true)
        }.toMutableList())
    }

    fun getMovieById(movieId: Long): MovieList {
        return movieList.first {
            it.id == movieId
        }
    }

    fun getFavoriteMovie(): Flow<MutableList<MovieList>> {
        return getAllMovieList()
            .map { movieList ->
                val favoriteList = mutableListOf<MovieList>()
                for (movie in movieList) {
                    if (movie.isFavorite) {
                        favoriteList.add(movie)
                    }
                }
                favoriteList
            }
    }

    fun updateFavoriteMovie(movieId: Long, newFavoriteValue: Boolean): Flow<Boolean> {
        val index = movieList.indexOfFirst { it.id == movieId }
        val result = if (index >= 0) {
            movieList[index] = movieList[index].copy(isFavorite = newFavoriteValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    companion object {
        @Volatile
        private var instance: MovieRepository? = null

        fun getInstance(): MovieRepository =
            instance ?: synchronized(this) {
                MovieRepository().apply {
                    instance = this
                }
            }
    }
}