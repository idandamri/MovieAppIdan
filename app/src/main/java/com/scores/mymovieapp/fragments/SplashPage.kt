package com.scores.mymovieapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.scores.mymovieapp.R
import com.scores.mymovieapp.activities.MainActivity
import com.scores.mymovieapp.dbUtils.Movie
import com.scores.mymovieapp.interfaces.INavigateToNextScreen
import com.scores.mymovieapp.interfaces.IcheckDbState
import com.scores.mymovieapp.interfaces.IonRequestCompleted
import com.scores.mymovieapp.objects.MoviesListResponseObject
import com.scores.mymovieapp.utilities.DbManager
import com.scores.mymovieapp.utilities.EmovieNavigationType
import com.scores.mymovieapp.utilities.NetworkMgr


class SplashPage : Fragment(), IonRequestCompleted, IcheckDbState {

    private lateinit var splashIv : ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.splash_screen_layout, container, false)
        initViews(view)
        showGif()
        return view
    }

    override fun onResume() {
        try {
            super.onResume()
            updateListOfMoviesFromWebOrDb()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateListOfMoviesFromWebOrDb() {
        try {
            DbManager.startProcess(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateListAccordingToDB(isListNotEmpty: Boolean){
        try {
            if(isListNotEmpty){
                goToNextPage()
            }
            else {
                NetworkMgr.request(this, NetworkMgr.MOVIE_URL)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goToNextPage() {
        try {
            if(activity is INavigateToNextScreen){
                (activity as INavigateToNextScreen).navigateToNextScreen(EmovieNavigationType.FROM_SPLASH_TO_LIST_PAGE, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showGif() {
        try {
            Glide.with(MainActivity.getContext())
                .load("https://media.giphy.com/media/8lKyuiFprZaj2lC3WN/giphy.gif")
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(splashIv)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initViews(view: View?) {
        splashIv = view!!.findViewById(R.id.splash_iv)
    }

    companion object {
        fun newInstance(): SplashPage {
            val fragment = SplashPage()
            val args = Bundle()
            //add arguments if needed
            fragment.arguments = args
            return fragment
        }
    }

    override fun updateDbWithDataAfterRequestFromServer(data: MoviesListResponseObject) {
        try {
            DbManager.updateDB(data)
            goToNextPage()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun parseJsonString(jsonStr: String) {
        try {
            val models: Array<Movie> = NetworkMgr.getGson().fromJson(jsonStr, Array<Movie>::class.java)
            val responseData = MoviesListResponseObject(models)
            updateDbWithDataAfterRequestFromServer(responseData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}