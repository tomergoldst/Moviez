package com.tomergoldst.moviez.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tomergoldst.moviez.data.repository.RepositoryDataSource

@Suppress("UNCHECKED_CAST")
class MainViewModelProvider(private val application: Application,
                            private val repository: RepositoryDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}