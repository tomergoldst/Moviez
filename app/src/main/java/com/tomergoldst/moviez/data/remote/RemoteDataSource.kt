package com.tomergoldst.moviez.data.remote

import com.tomergoldst.moviez.model.Movie
import io.reactivex.Single

interface RemoteDataSource {

    fun getMovies(queryParams: Map<String, String>) : Single<List<Movie>>

    fun getMovieDetails(id: Long) : Single<Movie>

}
