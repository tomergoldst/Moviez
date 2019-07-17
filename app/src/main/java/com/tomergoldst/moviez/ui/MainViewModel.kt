package com.tomergoldst.moviez.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tomergoldst.moviez.data.repository.RepositoryDataSource
import com.tomergoldst.moviez.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.lang.RuntimeException
import java.util.*

class MainViewModel(
    application: Application,
    private val repository: RepositoryDataSource
) :
    AndroidViewModel(application) {

    private val mMovies: MutableLiveData<MutableList<Movie>> = MutableLiveData()
    val selectedMovieTitle: MutableLiveData<String> = MutableLiveData()

    val selectedMovieOverview: MutableLiveData<String> = MutableLiveData()

    val selectedMoviePosterPath: MutableLiveData<String> = MutableLiveData()
    val selectedMovieBackdropPath: MutableLiveData<String?> = MutableLiveData()
    val selectedMovieAvgRating: MutableLiveData<Float> = MutableLiveData()
    val selectedMovieReleaseDate: MutableLiveData<Date> = MutableLiveData()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private var mPage = 1
    private val mContext: Context = getApplication()

    private val mCompositeDisposable = CompositeDisposable()

    init {
        _dataLoading.value = true
        discoverMovies()
    }

    private fun discoverMovies() {
        mCompositeDisposable.add(
            repository.getMovies(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Movie>>() {
                    override fun onComplete() {

                    }

                    override fun onNext(movies: List<Movie>) {
                        if (movies.isEmpty()){
                            return
                        }

                        val newMovies: MutableList<Movie> = ArrayList()

                        // add existing movies to list
                        mMovies.value?.let {
                            newMovies.addAll(it)
                        }
                        // add new received movies to list
                        newMovies.addAll(movies)
                        mMovies.value = newMovies

                        // select first movie on list to load its details on init
                        if (_dataLoading.value == true) {
                            selectMovie(newMovies[0].id)
                            _dataLoading.value = false
                        }
                    }

                    override fun onError(e: Throwable) {
                        _dataLoading.value = false
                        Timber.e(e)
                    }
                })
        )
    }

    fun getMovies(): LiveData<MutableList<Movie>> {
        return mMovies
    }

    fun getMoreMovies() {
        mPage++
        discoverMovies()
    }

    fun selectMovie(id: Long) {
        // todo this not efficient
        for (movie in mMovies.value!!) {
            if (movie.id == id) {
                selectedMovieBackdropPath.value = movie.backdropPath
                selectedMoviePosterPath.value = movie.posterPath
                selectedMovieTitle.value = movie.title
                selectedMovieOverview.value = movie.overview
                selectedMovieAvgRating.value = movie.voteAverage
                selectedMovieReleaseDate.value = movie.releaseDate
                return
            }
        }

        throw RuntimeException("Movie not found in movies list")

    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }
}