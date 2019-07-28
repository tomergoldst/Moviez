package com.tomergoldst.moviez.data.repository

import com.tomergoldst.moviez.data.local.MoviesLocalDataSource
import com.tomergoldst.moviez.data.remote.MoviesRemoteDataSource
import com.tomergoldst.moviez.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class Repository private constructor(
    private val moviesLocalDataSource: MoviesLocalDataSource,
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : RepositoryDataSource {

    private val mCompositeDisposable = CompositeDisposable()

    override fun getMovies(page: Int): Observable<List<Movie>> {
        val moviesObservable = moviesLocalDataSource.getMovies(page)

        mCompositeDisposable.add(
            moviesRemoteDataSource.getMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Movie>>() {
                    override fun onSuccess(movies: List<Movie>) {
                        moviesLocalDataSource.saveMovies(movies)
                            .subscribeOn(Schedulers.io())
                            .doOnError{throwable -> Timber.e(throwable)}
                            .subscribe()
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                    }
                })
        )

        return moviesObservable
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

    override fun destroy() {
        mCompositeDisposable.dispose()
        INSTANCE = null
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
