package com.scores.mymovieapp.interfaces

import com.scores.mymovieapp.dbUtils.Movie

interface OnRecyclerViewItemClickListenerScreen {
    fun onRecyclerViewItemClicked(data: Movie)
}
