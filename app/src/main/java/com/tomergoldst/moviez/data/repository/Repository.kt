package com.tomergoldst.moviez.data.repository

import com.tomergoldst.moviez.data.local.MoviesLocalDataSource
import com.tomergoldst.moviez.data.remote.MoviesRemoteDataSource
import com.tomergoldst.moviez.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class Repository private constructor(
    private val moviesLocalDataSource: MoviesLocalDataSource,
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : RepositoryDataSource {

    override fun getMovies(page: Int): Observable<List<Movie>> {
        // fetch data from remote and update local database on successful response
        moviesRemoteDataSource.getMovies(page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSuccess { movies ->
                moviesLocalDataSource.saveMovies(movies)
                    .subscribeOn(Schedulers.io())
                    .doOnError { throwable -> Timber.e(throwable) }
                    .subscribe()
            }
            .doOnError{ e -> Timber.e(e)}
            .subscribe()

        // always read from local database, when database is updated, the change will be reflected automatically
        // due to using Observable object together with Room
        return moviesLocalDataSource.getMovies(page)
    }

    override fun getMovieDetails(id: Long): Observable<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveMovies(movies: List<Movie>): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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
                INSTANCE
                    ?: Repository(
                        moviesLocalDataSource,
                        moviesRemoteDataSource
                    ).also { INSTANCE = it }
            }

    }
}
