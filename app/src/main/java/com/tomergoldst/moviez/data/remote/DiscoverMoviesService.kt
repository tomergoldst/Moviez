package com.tomergoldst.moviez.data.remote

import com.tomergoldst.moviez.model.DiscoverMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DiscoverMoviesService {

    @GET("discover/movie")
    fun discoverMovies(@QueryMap params: Map<String, String>): Single<DiscoverMoviesResponse>

}