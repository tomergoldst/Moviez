package com.tomergoldst.moviez.data.local

import com.tomergoldst.moviez.data.remote.Constants
import com.tomergoldst.moviez.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable

class MoviesLocalDataSource(
    private val movieDao: MovieDao
) : LocalDataSource {

    override fun getMovies(queryParams: Map<String, String>): Observable<List<Movie>> {
        val page = queryParams.getValue(Constants.PAGE).toInt()
        return movieDao.getAll((page - 1) * Constants.MOVIES_PER_PAGE, Constants.MOVIES_PER_PAGE)
    }

    override fun getMovieDetails(id: Long): Observable<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveMovies(movies: List<Movie>): Completable {
        return Completable.fromAction { movieDao.insert(movies) }
    }

    override fun clear(): Completable {
        return Completable.fromAction { movieDao.deleteAll() }
    }

    companion object {
        private var INSTANCE: MoviesLocalDataSource? = null

        @JvmStatic
        fun getInstance(movieDao: MovieDao): MoviesLocalDataSource {
            if (INSTANCE == null) {
                synchronized(MoviesLocalDataSource::javaClass) {
                    INSTANCE =
                        MoviesLocalDataSource(movieDao)
                }
            }
            return INSTANCE!!
        }

    }

}