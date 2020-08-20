package com.scores.mymovieapp.dbUtils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.Exception

@Entity(tableName = "movies", primaryKeys = ["title","releaseYear"])
class Movie : Serializable{

    @SerializedName("title")
    @ColumnInfo(name = "title") var title: String = ""

    @SerializedName("releaseYear")
    @ColumnInfo(name = "releaseYear") var releaseYear: String = ""

    @SerializedName("rating")
    @ColumnInfo(name = "rating") var rating: Double? = null

    @SerializedName("image")
    @ColumnInfo(name = "image") var image: String = ""

    @SerializedName("genre")
    @TypeConverters(ListConverter::class)
    @ColumnInfo(name = "genreString")var genre: List<String>? = null


    fun getYear(): Int{
        try {
            return releaseYear.toInt()
        }
        catch (e: Exception){
            return -1
        }
    }
}