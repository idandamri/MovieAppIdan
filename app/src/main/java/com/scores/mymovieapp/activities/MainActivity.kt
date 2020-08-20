package com.scores.mymovieapp.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.scores.mymovieapp.R
import com.scores.mymovieapp.dbUtils.Movie
import com.scores.mymovieapp.fragments.MovieDetailsPage
import com.scores.mymovieapp.fragments.MovieListPage
import com.scores.mymovieapp.fragments.QrCodeReaderFragment
import com.scores.mymovieapp.interfaces.INavigateToNextScreen
import com.scores.mymovieapp.utilities.DbManager
import com.scores.mymovieapp.utilities.EmovieNavigationType
import com.scores.mymovieapp.utilities.NetworkMgr
import com.scores.mymovieapp.utilities.Utils


class MainActivity : AppCompatActivity(), INavigateToNextScreen{

    private lateinit var rootView : ConstraintLayout
    private lateinit var frameLayout : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Utils.setContent(applicationContext)
            setContentView(R.layout.activity_main)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            supportActionBar?.hide()
            rootView = findViewById(R.id.main_activity_cl)
            frameLayout = findViewById(R.id.main_frame_layout)
            supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(frameLayout.id, MovieListPage.newInstance()).commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object{
        private const val DETAILS: String = "DetailsPage"
        private const val QR_CODE: String = "QrCode"
        private val PERMISSION_REQUEST_CODE: Int = 202
        private const val movie_permission_asking_string: String = "You Should approve the permission " +
                "for the camera to enjoy the QR code reader, " +
                "\nplease press \"Allow\".\n\nIf you pressed \"DENY " +
                "& DON\'T ASK AGAIN\" before please press \"CANCLE\" and change the permission " +
                "trough the apps \"Settings\" menu."
    }

    override fun onBackPressed() {
        try {
            if(supportFragmentManager.backStackEntryCount < 1){
                finish()
            }
            else {
                super.onBackPressed()
            }
        } catch (e: Exception) {
            super.onBackPressed()
            e.printStackTrace()
        }
    }

    override fun onStop() {
        try {
            super.onStop()
            NetworkMgr.stopAllRequests()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun navigateToNextScreen(type: EmovieNavigationType, data: Movie?) {
        try {
            when(type) {
                EmovieNavigationType.FROM_DETAILS_TO_QR -> {
                    supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(frameLayout.id, QrCodeReaderFragment.newInstance())
                        .addToBackStack(QR_CODE).commit()
                }
                EmovieNavigationType.FROM_LIST_PAGE_TO_DETAILS -> {
                    supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(frameLayout.id, MovieDetailsPage.newInstance(data))
                        .addToBackStack(DETAILS).commit()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MovieListPage.QR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            handleCallbackFromQrReader()
        }
        else if(requestCode == MovieListPage.QR_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }
    }

    fun handleCallbackFromQrReader() {
        try {
            val scanString = intent.getStringExtra(Utils.DATA_FROM_SCAN)
            val movie = NetworkMgr.getGson().fromJson<Movie>(scanString, Movie::class.java)
            DbManager.addMovieToDb(movie, this)
            refreshPage()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun refreshPage() {
        try {
            val listPage: Fragment = supportFragmentManager.findFragmentById(frameLayout.id)!!
            if (listPage is MovieListPage) {
                listPage.refreshListData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun requestPermission() {
        try {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            when (requestCode) {
                PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        navigateToNextScreen(EmovieNavigationType.FROM_DETAILS_TO_QR, null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel(movie_permission_asking_string,
                                DialogInterface.OnClickListener { _, _ ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermission()
                                    }
                                })
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        try {
            AlertDialog.Builder(this@MainActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRootView(): View {
        return rootView
    }
}