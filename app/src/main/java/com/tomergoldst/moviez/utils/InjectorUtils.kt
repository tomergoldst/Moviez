package com.tomergoldst.moviez.utils

import android.app.Application
import android.content.Context
import com.tomergoldst.moviez.data.local.DatabaseAccessPoint
import com.tomergoldst.moviez.data.local.MoviesLocalDataSource
import com.tomergoldst.moviez.data.remote.MoviesRemoteDataSource
import com.tomergoldst.moviez.data.repository.Repository
import com.tomergoldst.moviez.data.remote.DiscoverMoviesService
import com.tomergoldst.moviez.data.remote.RetrofitClient
import com.tomergoldst.moviez.ui.MainViewModelProvider
import retrofit2.Retrofit

// Inject classes needed for various Activities and Fragments.
object InjectorUtils {

    fun getMainViewModelProvider(application: Application): MainViewModelProvider {
        return MainViewModelProvider(
            application,
            getRepository(application)
        )
    }

    private fun getRetrofit(): Retrofit {
        return RetrofitClient.retrofit!!
    }

    private fun getRepository(context: Context): Repository {
        val discoverMoviesService = getRetrofit().create(DiscoverMoviesService::class.java)
        val database = DatabaseAccessPoint.getDatabase(context)

        return Repository.getInstance(
            MoviesLocalDataSource.getInstance(getAppExecutors(), database.movieDao()),
            MoviesRemoteDataSource(discoverMoviesService)
        )
    }

    fun getAppExecutors(): AppExecutors {
        return AppExecutors()
    }

}