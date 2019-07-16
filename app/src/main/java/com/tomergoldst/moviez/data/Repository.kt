package com.tomergoldst.moviez.data

import com.tomergoldst.moviez.data.local.LocalDataSource
import com.tomergoldst.moviez.data.local.MoviesLocalDataSource
import com.tomergoldst.moviez.data.remote.MoviesRemoteDataSource
import com.tomergoldst.moviez.data.remote.RemoteDataSource
import com.tomergoldst.moviez.model.Movie

class Repository private constructor(
    private val moviesLocalDataSource: MoviesLocalDataSource,
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : DataSource {

    companion object {

        private val TAG = Repository::class.java.simpleName

        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(
            moviesLocalDataSource: MoviesLocalDataSource,
            moviesRemoteDataSource: MoviesRemoteDataSource
        ):
                Repository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(moviesLocalDataSource, moviesRemoteDataSource).also { INSTANCE = it }
            }

    }

    override fun getMovies(queryParams: Map<String, String>, callback: DataSource.LoadMoviesCallback) {
        moviesRemoteDataSource.getMovies(queryParams, object : RemoteDataSource.LoadMoviesCallback{
            override fun onMoviesLoaded(movies: List<Movie>) {
                moviesLocalDataSource.saveMovies(movies)

                callback.onMoviesLoaded(movies)
            }

            override fun onDataNotAvailable() {
                moviesLocalDataSource.getMovies(queryParams, object : LocalDataSource.LoadMoviesCallback{
                    override fun onMoviesLoaded(movies: List<Movie>) {
                        callback.onMoviesLoaded(movies)
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })
    }

    override fun saveMovies(movies: List<Movie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovieDetails(id: Long, callback: DataSource.LoadMovieCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
