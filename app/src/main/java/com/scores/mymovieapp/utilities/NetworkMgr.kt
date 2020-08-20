package com.scores.mymovieapp.utilities

import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.scores.mymovieapp.activities.MainActivity
import com.scores.mymovieapp.interfaces.IonRequestCompleted

class NetworkMgr {

    companion object {
        private var gson: Gson = Gson()
        val MOVIE_URL = "https://api.androidhive.info/json/movies.json"
        val NETWORK_TAG = "network_tag"
        val queue = Volley.newRequestQueue(Utils.getContext())

        fun request(requestSuccessfulListener: IonRequestCompleted, url : String) {
            try {
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    Response.Listener<String> { response ->
                        requestSuccessfulListener.parseJsonString(response)
                    },
                    Response.ErrorListener {
                        Toast.makeText(Utils.getContext(),"Error in request", Toast.LENGTH_SHORT).show()
                    })
                stringRequest.tag = NETWORK_TAG
                queue.add(stringRequest)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun stopAllRequests(){
            queue?.cancelAll(NETWORK_TAG)
        }

        fun getGson(): Gson {
            return gson
        }
    }
}