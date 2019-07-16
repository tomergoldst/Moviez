package com.tomergoldst.moviez.data.local

import com.tomergoldst.moviez.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable

interface LocalDataSource {

    fun getMovies(queryParams: Map<String, String>) : Observable<List<Movie>>

    fun saveMovies(movies: List<Movie>): Completable

    fun getMovieDetails(id: Long) : Observable<Movie>

    fun clear(): Completable

}
