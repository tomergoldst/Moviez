package com.tomergoldst.moviez.data.repository

import com.tomergoldst.moviez.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable

interface RepositoryDataSource {

    fun getMovies(page: Int): Observable<List<Movie>>

    fun saveMovies(movies: List<Movie>): Completable

    fun getMovieDetails(id: Long): Observable<Movie>

    fun clear()

    fun onDestroy()

}
