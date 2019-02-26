package com.tomergoldst.moviez

import android.app.Application
import com.facebook.stetho.Stetho

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Uncomment to debug database (DEBUG ONLY)
        //Stetho.initializeWithDefaults(applicationContext)

    }

}
