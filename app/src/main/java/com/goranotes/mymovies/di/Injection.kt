package com.goranotes.mymovies.di

import com.goranotes.mymovies.data.repository.MovieRepository

object Injection {
    fun provideRepository(): MovieRepository {
        return MovieRepository.getInstance()
    }
}