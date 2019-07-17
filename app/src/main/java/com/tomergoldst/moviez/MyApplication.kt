package com.tomergoldst.moviez

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Uncomment to debug database (DEBUG ONLY)
        Stetho.initializeWithDefaults(applicationContext)

    }

}
