package com.tomergoldst.moviez.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.tomergoldst.moviez.app.GlideApp
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details.view.*
import java.util.*
import com.tomergoldst.moviez.data.remote.Constants
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment : Fragment() {

    private val mModel by sharedViewModel<MainViewModel>()

    companion object {
        val TAG: String = MovieDetailsFragment::class.java.simpleName

        fun newInstance() = MovieDetailsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.tomergoldst.moviez.R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mModel.selectedMovieTitle.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                view.titleTxv.text = it
            }
        })

        mModel.selectedMovieOverview.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                view.overviewTxv.text = it
            }
        })

        mModel.selectedMoviePosterPath.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                loadImage(context!!, posterImv, it)
            }
        })

        mModel.selectedMovieBackdropPath.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                loadImage(context!!, backdropImv, it)
            }
        })

        mModel.selectedMovieAvgRating.observe(this, Observer {
            view.avgRatingTxv.text = getString(com.tomergoldst.moviez.R.string.average_rating_out_of, it.toString(), "10")
        })

        mModel.selectedMovieReleaseDate.observe(this, Observer {
            val calendar = Calendar.getInstance()
            calendar.time = it
            val year = calendar.get(Calendar.YEAR)
            view.releaseYearTxv.text = year.toString()
        })

    }

    private fun loadImage(context: Context, imageView: ImageView, relativePath: String){
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.apply {
            strokeWidth = 5f
            centerRadius = 60f
            start()
        }

        val fullPath = "https://image.tmdb.org/t/p/w${Constants.POSTER_WIDTH}/$relativePath"

        GlideApp.with(context)
            .load(fullPath)
            .placeholder(circularProgressDrawable)
            .into(imageView)
    }

}
