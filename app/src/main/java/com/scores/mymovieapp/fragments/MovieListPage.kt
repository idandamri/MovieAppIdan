package com.scores.mymovieapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.scores.mymovieapp.R
import com.scores.mymovieapp.activities.MainActivity
import com.scores.mymovieapp.activities.QrCodeReaderActivity
import com.scores.mymovieapp.adapters.ListPageAdapter
import com.scores.mymovieapp.dbUtils.Movie
import com.scores.mymovieapp.interfaces.INavigateToNextScreen
import com.scores.mymovieapp.interfaces.OnRecyclerViewItemClickListenerScreen
import com.scores.mymovieapp.utilities.EmovieNavigationType
import com.scores.mymovieapp.utilities.Utils
import java.util.*

class MovieListPage : BaseListFragment() , OnRecyclerViewItemClickListenerScreen,
    View.OnTouchListener {

    private lateinit var btnAdd: ImageView
    private var dX: Float = 0f
    private var dY: Float = 0f
    private var startClickTime: Long = 0

    companion object {
        fun newInstance(): MovieListPage {
            val fragment = MovieListPage()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

        const val MAX_CLICK_DURATION = 300
        const val QR_REQUEST_CODE: Int = 12321
    }

    override fun setAdapter() {
        viewAdapter = ListPageAdapter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.movie_list_page_layout
    }

    override fun getPbLoaderId(): Int {
        return R.id.pbLoader
    }

    override fun initViews(view: View) {
        super.initViews(view)
        btnAdd = view.findViewById(R.id.add_qr_tv)
        btnAdd.isClickable = true
        btnAdd.setOnTouchListener(this)
    }

    override fun handleAdapterWithData() {
        try {
            if (recyclerView.adapter == null) {
                recyclerView.adapter = viewAdapter
            }
            val adapter = viewAdapter as ListPageAdapter
            adapter.setListItems(Utils.getMoviesList()!!)
            recyclerView.adapter?.notifyDataSetChanged()
            hideLoader()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRecyclerViewItemClicked(data: Movie) {
        try {
            openMovieDetailsScreen(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openMovieDetailsScreen(data: Movie) {
        try {
            if(activity is INavigateToNextScreen){
                (activity as INavigateToNextScreen).navigateToNextScreen(EmovieNavigationType.FROM_LIST_PAGE_TO_DETAILS, data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = v?.x!! - event.rawX
                dY = v.y - event.rawY
                startClickTime = Calendar.getInstance().timeInMillis
            }
            MotionEvent.ACTION_UP -> {
                val clickDuration: Long =
                    Calendar.getInstance().timeInMillis - startClickTime
                if (clickDuration < MAX_CLICK_DURATION) {
                    openQrCodeReader()
                }
            }
            MotionEvent.ACTION_MOVE ->
                v?.animate()?.x(event.rawX + dX)
                    ?.y(event.rawY + dY)
                    ?.setDuration(0)
                    ?.start()
            else -> return false
        }
        return true
    }

    private fun openQrCodeReader() {
        try {
            val readerIntent = Intent(MainActivity.getContext(),QrCodeReaderActivity::class.java)
            activity?.startActivityForResult(readerIntent, QR_REQUEST_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun refreshListData() {
        try {
            (viewAdapter as ListPageAdapter).notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}