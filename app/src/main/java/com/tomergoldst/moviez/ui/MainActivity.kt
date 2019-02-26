package com.tomergoldst.moviez.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.tomergoldst.moviez.R
import com.tomergoldst.moviez.model.Movie
import com.tomergoldst.moviez.utils.UiUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteraction{

    companion object {
        val TAG: String = MainActivity::class.java.simpleName

        const val SAVED_STATE_HOME_INDICATOR = "SAVED_STATE_HOME_INDICATOR"
    }

    private var mIsTablet: Boolean = false
    private var mIsTabletOnLandscape: Boolean = false
    private var mBackButtonVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tomergoldst.moviez.R.layout.activity_main)

        initToolbar()

        mIsTablet = UiUtils.isTablet(this)
        mIsTabletOnLandscape = mIsTablet && UiUtils.isLandscape(this)

        if (savedInstanceState == null) {
            addMainFragment()
            if (mIsTabletOnLandscape){
                addMovieDetailsFragmentToSecondaryContainer()
            }
        } else {
            setDisplayHomeIndicator(savedInstanceState.getBoolean(SAVED_STATE_HOME_INDICATOR))
        }

        if (getString(R.string.themoviedb_api_key) == "YOUR_THEMOVIEDB_API_KEY_HERE"){
            Snackbar.make(rootLayout, "Missing themoviedb api key", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun addMainFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(com.tomergoldst.moviez.R.id.container, MainFragment.newInstance(), "MainFragment")
            .commit()

    }

    private fun addMovieDetailsFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(com.tomergoldst.moviez.R.id.container, MovieDetailsFragment.newInstance(), "MovieDetailsFragment")
            .addToBackStack(null)
            .commit()

    }

    private fun addMovieDetailsFragmentToSecondaryContainer() {
        supportFragmentManager
            .beginTransaction()
            .add(com.tomergoldst.moviez.R.id.secondaryContainer, MovieDetailsFragment.newInstance(), "MovieDetailsFragment")
            .commit()

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.title = getString(com.tomergoldst.moviez.R.string.app_name)
    }

    override fun onMovieClicked(movie: Movie) {
        if (mIsTabletOnLandscape) {
            return
        }

        setDisplayHomeIndicator(true)

        addMovieDetailsFragment()
    }

    private fun setDisplayHomeIndicator(enable: Boolean) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(enable)
        supportActionBar!!.setDisplayShowHomeEnabled(enable)
        mBackButtonVisible = enable
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(SAVED_STATE_HOME_INDICATOR, mBackButtonVisible)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (mBackButtonVisible) {
            setDisplayHomeIndicator(false)
        }

        super.onBackPressed()

    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }
}
