package com.scores.mymovieapp.interfaces

import com.scores.mymovieapp.dbUtils.Movie
import com.scores.mymovieapp.utilities.EmovieNavigationType

interface INavigateToNextScreen {
    fun navigateToNextScreen(type: EmovieNavigationType, data: Movie?)
    fun refreshPage()
}