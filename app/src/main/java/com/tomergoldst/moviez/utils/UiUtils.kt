package com.tomergoldst.moviez.utils

import android.content.Context
import android.content.res.Configuration
import com.tomergoldst.moviez.R

object UiUtils {

    fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun isTablet(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.isTablet)
    }

}