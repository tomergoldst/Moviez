package com.tomergoldst.moviez.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.tomergoldst.moviez.R
import com.tomergoldst.moviez.config.GlideApp
import com.tomergoldst.moviez.data.remote.Constants
import com.tomergoldst.moviez.data.remote.Constants.POSTER_WIDTH
import com.tomergoldst.moviez.model.Movie

class MoviesPostersRecyclerListAdapter(
    val mListener: OnAdapterInteractionListener
) :
    ListAdapter<Movie, MoviesPostersRecyclerListAdapter.MovieViewHolder>(DiffCallback()) {

    companion object {
        val TAG: String = MoviesPostersRecyclerListAdapter::class.java.simpleName

    }

    interface OnAdapterInteractionListener {
        fun onItemClicked(movie: Movie)
    }

    inner class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val moviePosterImv: AppCompatImageView = v.findViewById(R.id.moviePosterImv)

        fun bind(movie: Movie) {
            val context = itemView.context

            itemView.setOnClickListener {
                mListener.onItemClicked(movie)
            }

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.apply {
                strokeWidth = 5f
                centerRadius = 60f
                start()
            }

            val fullPosterPath = "https://image.tmdb.org/t/p/w${Constants.POSTER_WIDTH}/${movie.posterPath}"

            GlideApp.with(context)
                .load(fullPosterPath)
                .placeholder(circularProgressDrawable)
                .into(moviePosterImv)

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesPostersRecyclerListAdapter.MovieViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_poster, parent, false)
        return MovieViewHolder(v)
    }

    override fun onBindViewHolder(holder: MoviesPostersRecyclerListAdapter.MovieViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        holder.bind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return (oldItem.id == newItem.id)
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

}
