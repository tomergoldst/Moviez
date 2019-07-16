package com.tomergoldst.moviez.data.repository

import com.tomergoldst.moviez.data.local.MoviesLocalDataSource
import com.tomergoldst.moviez.data.remote.MoviesRemoteDataSource
import com.tomergoldst.moviez.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class Repository private constructor(
    private val moviesLocalDataSource: MoviesLocalDataSource,
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : RepositoryDataSource {

    // todo: add clear disposable
    private val mCompositeDisposable = CompositeDisposable()

    override fun getMovies(queryParams: Map<String, String>, callback: RepositoryDataSource.LoadMoviesCallback) {
        mCompositeDisposable.add(
            moviesRemoteDataSource.getMovies(queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Movie>>() {
                    override fun onSuccess(movies: List<Movie>) {
                        moviesLocalDataSource.saveMovies(movies)
                        callback.onMoviesLoaded(movies)
                    }

                    override fun onError(e: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
        )
    }

    override fun saveMovies(movies: List<Movie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovieDetails(id: Long, callback: RepositoryDataSource.LoadMovieCallback) {
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
