package com.tomergoldst.moviez.app

import androidx.room.Room
import com.tomergoldst.moviez.data.local.AppDatabase
import com.tomergoldst.moviez.data.local.LocalDataSource
import com.tomergoldst.moviez.data.local.MoviesLocalDataSource
import com.tomergoldst.moviez.data.remote.MoviesRemoteDataSource
import com.tomergoldst.moviez.data.remote.RemoteDataSource
import com.tomergoldst.moviez.data.remote.RetrofitClient
import com.tomergoldst.moviez.data.repository.Repository
import com.tomergoldst.moviez.data.repository.RepositoryDataSource
import com.tomergoldst.moviez.ui.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModules: Module = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().movieDao() }
    single { RetrofitClient.getMoviesService() }
    single<RemoteDataSource> { MoviesRemoteDataSource(get()) }
    single<LocalDataSource> { MoviesLocalDataSource(get()) }
    single<RepositoryDataSource> { Repository(get(), get()) }
    viewModel { MainViewModel(androidApplication(), get()) }
}

