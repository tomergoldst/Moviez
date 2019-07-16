package com.tomergoldst.moviez.data.remote

import com.tomergoldst.moviez.model.DiscoverMoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRemoteDataSource(private val discoverMoviesService: DiscoverMoviesService) :
    RemoteDataSource {

    override fun getMovies(
        queryParams: Map<String, String>,
        callback: RemoteDataSource.LoadMoviesCallback
    ){
        discoverMoviesService.discoverMovies(queryParams).enqueue(object : Callback<DiscoverMoviesResponse> {
            override fun onFailure(call: Call<DiscoverMoviesResponse>, t: Throwable) {
                callback.onDataNotAvailable()
            }

            override fun onResponse(
                call: Call<DiscoverMoviesResponse>,
                response: Response<DiscoverMoviesResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onMoviesLoaded(response.body()?.results!!)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        })

    }

    override fun getMovieDetails(id: Long, callback: RemoteDataSource.LoadMovieCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}