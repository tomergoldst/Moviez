package com.tomergoldst.moviez.data.local

import com.tomergoldst.moviez.data.remote.Constants
import com.tomergoldst.moviez.utils.AppExecutors
import com.tomergoldst.moviez.model.Movie

class MoviesLocalDataSource(private val appExecutors: AppExecutors,
                            private val movieDao: MovieDao
) : LocalDataSource {

    override fun getMovies(
        queryParams: Map<String, String>,
        callback: LocalDataSource.LoadMoviesCallback
    ){
        appExecutors.diskIO.execute {
            val page = queryParams.getValue(Constants.PAGE).toInt()
            val movies = movieDao.getAllSync((page - 1) * Constants.MOVIES_PER_PAGE, Constants.MOVIES_PER_PAGE)
            appExecutors.mainThread.execute {
                if (movies.isNullOrEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable()
                } else {
                    // movies are loaded by page, so all should have the same page
                    callback.onMoviesLoaded(movies)
                }
            }
        }

    }

    override fun saveMovies(movies: List<Movie>) {
        appExecutors.diskIO.execute {
            movieDao.insert(movies)
        }
    }

    override fun getMovieDetails(id: Long, callback: LocalDataSource.LoadMovieCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        appExecutors.diskIO.execute {
            movieDao.deleteAll()
        }
    }

    companion object {
        private var INSTANCE: MoviesLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, movieDao: MovieDao): MoviesLocalDataSource {
            if (INSTANCE == null) {
                synchronized(MoviesLocalDataSource::javaClass) {
                    INSTANCE =
                        MoviesLocalDataSource(appExecutors, movieDao)
                }
            }
            return INSTANCE!!
        }

    }

}