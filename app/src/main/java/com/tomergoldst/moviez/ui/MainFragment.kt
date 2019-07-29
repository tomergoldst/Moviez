package com.tomergoldst.moviez.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomergoldst.moviez.R
import com.tomergoldst.moviez.model.Movie
import com.tomergoldst.moviez.utils.InjectorUtils
import com.tomergoldst.moviez.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(),
    MoviesPostersRecyclerListAdapter.OnAdapterInteractionListener {

    private lateinit var mModel: MainViewModel
    private lateinit var mAdapter: MoviesPostersRecyclerListAdapter
    private var mListener: OnFragmentInteraction? = null


    companion object {
        val TAG: String = MainFragment::class.java.simpleName

        fun newInstance() = MainFragment()
    }

    // Container Activity must implement this interface
    interface OnFragmentInteraction {
        fun onMovieClicked(movie: Movie)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mModel = activity?.run {
            ViewModelProviders.of(this, InjectorUtils.getMainViewModelProvider(application))
                .get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        mModel.getMovies().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                mAdapter.submitList(it)
            }
        })

        mModel.dataLoading.observe(this, Observer {
            emptyViewGroup.visibility = if (it) View.VISIBLE else View.GONE
            recyclerView.visibility = if (it) View.GONE else View.VISIBLE
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = MoviesPostersRecyclerListAdapter(this)
        recyclerView.apply {

            // show different number of columns for different states
            val spanCount =
                if (UiUtils.isPortrait(context) && !UiUtils.isTablet(context)) 2
                else if (UiUtils.isLandscape(context) && UiUtils.isTablet(context)) 3
                else 4

            layoutManager = GridLayoutManager(context, spanCount)
            addOnScrollListener(object : EndlessRecyclerViewScrollListener(
                layoutManager as GridLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    mModel.getMoreMovies()
                }
            })
            adapter = mAdapter
        }

        emptyViewGroup.visibility = View.VISIBLE

    }

    override fun onItemClicked(movie: Movie) {
        mModel.selectMovie(movie.id)
        mListener!!.onMovieClicked(movie)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as? OnFragmentInteraction
        if (mListener == null) {
            throw ClassCastException("$context must implement OnFragmentInteraction")
        }

    }

}
