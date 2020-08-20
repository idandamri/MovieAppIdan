package com.scores.mymovieapp.interfaces

import com.scores.mymovieapp.objects.MoviesListResponseObject
import com.scores.mymovieapp.utilities.NetworkMgr

interface IonRequestCompleted {
    fun updateDbWithDataAfterRequestFromServer(data: MoviesListResponseObject)
    fun parseJsonString(jsonStr : String) {
        try {
            val data = NetworkMgr.getGson().fromJson(jsonStr, MoviesListResponseObject::class.java)
            updateDbWithDataAfterRequestFromServer(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
