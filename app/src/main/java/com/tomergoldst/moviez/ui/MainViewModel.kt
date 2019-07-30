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
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class MainViewModel(
    application: Application,
    private val repository: RepositoryDataSource
) :
    AndroidViewModel(application) {

    private val mMoviesMap: MutableMap<Int, List<Movie>> = LinkedHashMap()

    private val _movies: MutableLiveData<List<Movie>> = MutableLiveData()
    val movies: LiveData<List<Movie>> = _movies

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
    private val pagination: PublishSubject<Int> = PublishSubject.create()

    init {
        _movies.value = ArrayList()
        _dataLoading.value = true
        subscribeForData()
    }

    private fun subscribeForData() {
        mCompositeDisposable.add(
            pagination
                .doOnNext { page -> Timber.d("page $page") }
                .flatMap { page -> repository.getMovies(page) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateData, this::onError)
        )

        pagination.onNext(mPage)
    }

    private fun updateData(movies: List<Movie>){
        Timber.d("updateData")

        if (!movies.isNullOrEmpty()) {
            mMoviesMap[mPage] = movies
            _movies.value = getMoviesListFromMap()

            // select first movie on list to load its details on init
            if (_dataLoading.value == true) {
                selectMovie(movies[0].id)
                _dataLoading.value = false
            }
        }
    }

    private fun onError(e: Throwable){
        Timber.e(e)
    }

    private fun getMoviesListFromMap(): List<Movie> {
        val movies = ArrayList<Movie>()
        for (moviesList in mMoviesMap) {
            movies.addAll(moviesList.value)
        }
        return movies
    }

    fun getMoreMovies() {
        pagination.onNext(++mPage)
    }

    fun selectMovie(id: Long) {
        // todo this not efficient
        for (movie in _movies.value!!) {
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