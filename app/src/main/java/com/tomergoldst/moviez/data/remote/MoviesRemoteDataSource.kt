package com.tomergoldst.moviez.data.remote

import com.tomergoldst.moviez.model.Movie
import io.reactivex.Single

class MoviesRemoteDataSource(private val discoverMoviesService: DiscoverMoviesService) :
    RemoteDataSource {

    override fun getMovies(queryParams: Map<String, String>): Single<List<Movie>> {
        return discoverMoviesService.discoverMovies(queryParams).map { r -> r.results }
    }

    override fun getMovieDetails(id: Long): Single<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}