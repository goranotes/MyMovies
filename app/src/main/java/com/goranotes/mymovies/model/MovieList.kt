package com.goranotes.mymovies.model

data class MovieList(
    val id: Long,
    val image: Int,
    val title: String,
    val rating: Double,
    val synopsis: String,
    val director: String,
    val writers: String,
    val stars: String,
    val genres: String,
    val duration: String,
    val year: Long,
    val isFavorite: Boolean = false
)
