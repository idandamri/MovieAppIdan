package com.scores.mymovieapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scores.mymovieapp.R
import com.scores.mymovieapp.dbUtils.Movie
import com.scores.mymovieapp.interfaces.OnRecyclerViewItemClickListenerScreen
import com.scores.mymovieapp.utilities.Utils
import kotlinx.android.synthetic.main.movie_list_item_layout.view.*

class ListPageAdapter (private var onRecyclerViewItemClickListenerScreen : OnRecyclerViewItemClickListenerScreen):
    RecyclerView.Adapter<ListPageAdapter.MovieViewHolder>() {

    private var items: List<Movie> = ArrayList()

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getItem(position: Int): Movie {
        return items[position]
    }

    fun getItemsList(): List<Movie> {
        return items
    }

    fun setListItems(itemsList: List<Movie>){
        items = itemsList
    }

    class MovieViewHolder(
        itemView: View,
        private var onRecyclerViewItemClickListenerScreen: OnRecyclerViewItemClickListenerScreen)
        : RecyclerView.ViewHolder(itemView){

        fun bind(value: Movie) {
            try {
                itemView.movie_item_name_tv.text = value.title
                itemView.item_genre_ll.removeAllViews()
                Utils.addGenreToLinearLayout(itemView.item_genre_ll, value)
                itemView.movie_item_name_tv.text = value.title

                Glide.with(itemView.context)
                    .load(value.image)
                    .placeholder(R.drawable.ic_baseline_star)
                    .into(itemView.movie_item_iv)
                itemView.setOnClickListener {
                    onRecyclerViewItemClickListenerScreen.onRecyclerViewItemClicked(value)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_item_layout, parent, false)
        return MovieViewHolder(
            view,
            onRecyclerViewItemClickListenerScreen
        )
    }
}