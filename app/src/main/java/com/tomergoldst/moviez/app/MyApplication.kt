package com.tomergoldst.moviez.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.tomergoldst.moviez.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Uncomment to debug database (DEBUG ONLY)
        Stetho.initializeWithDefaults(applicationContext)

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModules)
        }

    }

}
