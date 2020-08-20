package com.scores.mymovieapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.scores.mymovieapp.R
import com.scores.mymovieapp.activities.MainActivity
import com.scores.mymovieapp.dbUtils.Movie
import com.scores.mymovieapp.utilities.Utils
import java.lang.StringBuilder

class MovieDetailsPage : Fragment(), View.OnClickListener {

    private lateinit var yearTv: TextView
    private lateinit var gifIv: ImageView
    private lateinit var mainIv: ImageView
    private lateinit var backIv: ImageView
    private lateinit var titleTv: TextView
    private lateinit var ratingTv: TextView
    private lateinit var genreLl: LinearLayout

    companion object {
        private const val DATA_TAG: String = "movie_data"

        fun newInstance(data: Movie?): MovieDetailsPage {
            val fragment = MovieDetailsPage()
            val args = Bundle()
            if (data != null) {
                args.putSerializable(DATA_TAG, data)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movie_details_page_layout, container, false)
        initViews(view)
        setDataToViews()
        return view
    }

    private fun setDataToViews() {
        try {
            val value = arguments?.getSerializable(DATA_TAG) as Movie
            titleTv.text = value.title
            val sb = StringBuilder()
            sb.append(Utils.getContext().resources.getString(R.string.rating_is))
            sb.append(" ")
            sb.append(value.rating.toString())
            ratingTv.text = sb.toString()
            yearTv.text = value.releaseYear
            val genreTitleTv = TextView(Utils.getContext())
            sb.setLength(0)
            if (value.genre?.size == 1) {
                sb.append(Utils.getContext().resources.getString(R.string.genre_is))
            }
            else if(value.genre?.size!! > 1) {
                sb.append(Utils.getContext().resources.getString(R.string.genre_are))
            }
            sb.append(" ")
            genreTitleTv.text = sb.toString()
            genreTitleTv.setTextColor(Utils.getContext().resources.getColor(R.color.mainTextColor, Utils.getContext().resources.newTheme()))
            genreLl.addView(genreTitleTv)
            Utils.addGenreToLinearLayout(genreLl, value)
            Glide.with(Utils.getContext())
                .load(value.image)
                .placeholder(R.drawable.ic_baseline_star)
                .into(mainIv)
            Glide.with(Utils.getContext())
                .load("https://media.giphy.com/media/8lKyuiFprZaj2lC3WN/giphy.gif")
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerInside()
                .into(gifIv)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initViews(view: View?) {
        mainIv = view?.findViewById(R.id.details_iv)!!
        backIv = view.findViewById(R.id.details_back_iv)!!
        gifIv = view.findViewById(R.id.details_gif_iv)!!
        titleTv = view.findViewById(R.id.details_title_tv)
        ratingTv = view.findViewById(R.id.details_rating_tv)
        genreLl = view.findViewById(R.id.item_genre_ll)
        yearTv = view.findViewById(R.id.details_year_tv)

        backIv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        try {
            if(v?.id == R.id.details_back_iv){
                activity?.onBackPressed()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
