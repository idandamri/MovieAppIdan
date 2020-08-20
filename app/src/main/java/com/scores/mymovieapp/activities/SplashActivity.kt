package com.scores.mymovieapp.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.scores.mymovieapp.R
import com.scores.mymovieapp.dbUtils.Movie
import com.scores.mymovieapp.interfaces.IcheckDbState
import com.scores.mymovieapp.interfaces.IonRequestCompleted
import com.scores.mymovieapp.objects.MoviesListResponseObject
import com.scores.mymovieapp.utilities.DbManager
import com.scores.mymovieapp.utilities.NetworkMgr
import com.scores.mymovieapp.utilities.Utils

class SplashActivity : AppCompatActivity(), IonRequestCompleted, IcheckDbState {

    private lateinit var splashIv : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setContent(applicationContext)
        setContentView(R.layout.activity_splash)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar?.hide()
        splashIv = findViewById(R.id.splash_iv)
        showGif()
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

    private fun showGif() {
        try {
            Glide.with(Utils.getContext())
                .load("https://media.giphy.com/media/8lKyuiFprZaj2lC3WN/giphy.gif")
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(splashIv)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateListAccordingToDB(isListNotEmpty: Boolean){
        try {
            if(isListNotEmpty){
                navigateToMainActivity()
            }
            else {
                NetworkMgr.request(this, NetworkMgr.MOVIE_URL)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun updateDbWithDataAfterRequestFromServer(data: MoviesListResponseObject) {
        try {
            DbManager.updateDB(data)
            navigateToMainActivity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigateToMainActivity() {
        try {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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