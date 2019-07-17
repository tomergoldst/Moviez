package com.tomergoldst.moviez.data.remote

import com.tomergoldst.moviez.model.Movie
import io.reactivex.Single

interface RemoteDataSource {

    fun getMovies(page: Int) : Single<List<Movie>>

    fun getMovieDetails(id: Long) : Single<Movie>

}
