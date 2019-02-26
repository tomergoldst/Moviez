package com.tomergoldst.moviez.data.remote

import com.tomergoldst.moviez.data.DataSource
import com.tomergoldst.moviez.model.DiscoverMoviesResponse
import com.tomergoldst.moviez.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRemoteDataSource(private val discoverMoviesService: DiscoverMoviesService) :
    DataSource {

    override fun getMovies(
        queryParams: Map<String, String>,
        callback: DataSource.LoadMoviesCallback
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

    override fun saveMovies(movies: List<Movie>) {
        // no use for remote data source in this sample app
    }

    override fun getMovieDetails(id: Long, callback: DataSource.LoadMovieCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        // no use for remote data source in this sample app
    }

}