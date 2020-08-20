package com.scores.mymovieapp.dbUtils

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scores.mymovieapp.activities.MainActivity
import com.scores.mymovieapp.utilities.Utils

@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class MoviesDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao
    companion object {
        private var INSTANCE: MoviesDatabase? = null

        fun getDataBase(): MoviesDatabase? {
            if (INSTANCE == null){
                synchronized(MoviesDatabase::class){
                    INSTANCE = Room.databaseBuilder(Utils.getContext().applicationContext, MoviesDatabase::class.java, "moviesDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}