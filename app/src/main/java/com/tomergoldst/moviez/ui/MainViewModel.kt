package com.tomergoldst.moviez.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.tomergoldst.moviez.R
import com.tomergoldst.moviez.data.DataSource
import com.tomergoldst.moviez.model.Movie
import com.tomergoldst.moviez.data.remote.Constants
import java.lang.RuntimeException
import java.util.*

class MainViewModel(
    application: Application,
    private val repository: DataSource
) :
    AndroidViewModel(application) {

    private val mMovies: MutableLiveData<MutableList<Movie>> = MutableLiveData()
    val selectedMovieTitle: MutableLiveData<String> = MutableLiveData()

    val selectedMovieOverview: MutableLiveData<String> = MutableLiveData()

    val selectedMoviePosterPath: MutableLiveData<String> = MutableLiveData()
    val selectedMovieBackdropPath: MutableLiveData<String> = MutableLiveData()
    val selectedMovieAvgRating: MutableLiveData<Float> = MutableLiveData()
    val selectedMovieReleaseDate: MutableLiveData<Date> = MutableLiveData()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() =_dataLoading

    private var mPage = 1
    private val mContext: Context = getApplication()

    init {
        _dataLoading.value = true
        discoverMovies()
    }

    private fun discoverMovies() {
        repository.getMovies(getQueryParams(), object : DataSource.LoadMoviesCallback{
            override fun onMoviesLoaded(movies: List<Movie>) {
                val newMovies: MutableList<Movie> = ArrayList()
                mMovies.value?.let {
                    newMovies.addAll(it)
                }
                newMovies.addAll(movies)
                mMovies.value = newMovies

                // select first movie on list to load its details on init
                if (_dataLoading.value == true) {
                    selectMovie(newMovies[0].id)
                    _dataLoading.value = false
                }

            }

            override fun onDataNotAvailable() {
                _dataLoading.value = false

            }
        })

    }

    private fun getQueryParams(): MutableMap<String, String> {
        val queryParams: MutableMap<String, String> = HashMap()
        queryParams[Constants.API_KEY] = mContext.getString(R.string.themoviedb_api_key)
        queryParams[Constants.LANGUAGE] = Locale.getDefault().toLanguageTag()
        queryParams[Constants.INCLUDE_ADULT] = false.toString()
        queryParams[Constants.INCLUDE_VIDEO] = false.toString()
        queryParams[Constants.SORT_BY] = "popularity.desc"
        queryParams[Constants.PAGE] = mPage.toString()
        return queryParams
    }

    fun getMovies(): LiveData<MutableList<Movie>> {
        return mMovies
    }

    fun getMoreMovies(){
        mPage++
        discoverMovies()
    }

    fun selectMovie(id: Long){
        // todo this not efficient
        for (movie in mMovies.value!!){
            if (movie.id == id){
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

}