package com.tomergoldst.moviez.data.remote

import com.tomergoldst.moviez.model.Movie
import io.reactivex.Single
import timber.log.Timber
import java.util.*

class MoviesRemoteDataSource(private val moviesService: MoviesService) :
    RemoteDataSource {

    override fun getMovies(page: Int): Single<List<Movie>> {
        return moviesService.discoverMovies(getQueryParams(page))
            .map { r -> r.results }
    }

    override fun getMovieDetails(id: Long): Single<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getQueryParams(page: Int): MutableMap<String, String> {
        val queryParams: MutableMap<String, String> = HashMap()
        queryParams[Constants.PARAM_API_KEY] = Constants.API_KEY_THE_MOVIE_DB
        queryParams[Constants.PARAM_LANGUAGE] = Locale.getDefault().toLanguageTag()
        queryParams[Constants.PARAM_INCLUDE_ADULT] = false.toString()
        queryParams[Constants.PARAM_INCLUDE_VIDEO] = false.toString()
        queryParams[Constants.PARAM_SORT_BY] = "popularity.desc"
        queryParams[Constants.PARAM_PAGE] = page.toString()
        return queryParams
    }

}