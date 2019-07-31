package com.tomergoldst.moviez.data.repository

import com.tomergoldst.moviez.data.local.LocalDataSource
import com.tomergoldst.moviez.data.remote.RemoteDataSource
import com.tomergoldst.moviez.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class Repository(
    private val moviesLocalDataSource: LocalDataSource,
    private val moviesRemoteDataSource: RemoteDataSource
) : RepositoryDataSource {

    override fun getMovies(page: Int): Observable<List<Movie>> {
        // fetch data from remote and update local database on successful response
        moviesRemoteDataSource.getMovies(page)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<Movie>>(){
                override fun onSuccess(movies: List<Movie>) {
                    moviesLocalDataSource.saveMovies(movies)
                        .subscribeOn(Schedulers.io())
                        .doOnError { throwable -> Timber.e(throwable) }
                        .subscribe()                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }
            })

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

}
