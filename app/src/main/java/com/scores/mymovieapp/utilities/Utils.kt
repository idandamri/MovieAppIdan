package com.scores.mymovieapp.utilities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.scores.mymovieapp.R
import com.scores.mymovieapp.activities.MainActivity
import com.scores.mymovieapp.dbUtils.Movie
import kotlinx.android.synthetic.main.movie_list_item_layout.view.*


class Utils {
    companion object{
        private const val PERMISSION_REQUEST_CODE = 200
        const val DATA_FROM_SCAN: String = "data_from_scan"

        private fun convertDpToPixel(dp: Float): Float {
            val resources = MainActivity.getContext().resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun getMoviesList(): ArrayList<Movie>? {
            return listOfMovies
        }

        private var listOfMovies: ArrayList<Movie>? = null

        fun setMovieList(list: ArrayList<Movie>){
            try {
                if(listOfMovies.isNullOrEmpty()){
                    listOfMovies = ArrayList()
                }
                listOfMovies = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun addGenreToLinearLayout(itemView: View, value: Movie) {
            try {
                value.genre?.forEach {
                    val tv = TextView(MainActivity.getContext())
                    val spaceView = View(MainActivity.getContext())
                    spaceView.layoutParams = LinearLayout.LayoutParams(
                        convertDpToPixel(2f).toInt(),
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    tv.text = it
                    tv.setBackgroundResource(R.drawable.gray_round_color_bg)
                    tv.setTextColor(Color.parseColor("#000000"))
                    tv.textSize = 10f
                    tv.setPadding(
                        convertDpToPixel(6f).toInt(), convertDpToPixel(2f).toInt(),
                        convertDpToPixel(6f).toInt(), convertDpToPixel(4f).toInt()
                    )
                    itemView.item_genre_ll.addView(tv)
                    itemView.item_genre_ll.addView(spaceView)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun checkAndroidVersionAndPermissionIsGranted(): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission()
            } else {
                // code for lollipop and pre-lollipop devices
                true
            }
        }

        fun checkPermission(): Boolean {
            return ContextCompat.checkSelfPermission(MainActivity.getContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }
    }
}