package com.scores.mymovieapp.dbUtils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    @TypeConverter
    fun toArray(listOfString: String): List<String> {
        return Gson().fromJson<List<String>>(
            listOfString,
            object : TypeToken<List<String>>() {}.type
        )
    }

    @TypeConverter
    fun fromArray(listOfString: List<String>): String {
        return Gson().toJson(listOfString)
    }
}