package com.tomergoldst.moviez.data.repository

import com.tomergoldst.moviez.model.Movie

interface RepositoryDataSource {

    interface LoadMoviesCallback {
        fun onMoviesLoaded(movies: List<Movie>)
        fun onDataNotAvailable()
    }

    interface LoadMovieCallback {
        fun onMovieLoaded(movie: Movie)
        fun onDataNotAvailable()
    }

    fun getMovies(queryParams: Map<String, String>, callback: LoadMoviesCallback)

    fun saveMovies(movies: List<Movie>)

    fun getMovieDetails(id: Long, callback: LoadMovieCallback)

    fun clear()

}
