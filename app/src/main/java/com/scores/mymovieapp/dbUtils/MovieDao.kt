package com.scores.mymovieapp.dbUtils

import androidx.room.*

@Dao
interface MovieDao {
    @Query("Select * from movies")
    fun getAllMovies(): List<Movie>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)
    @Update
    fun updateMovie(movie: Movie)
    @Delete
    fun deleteMovie(movie: Movie)
    @Query("SELECT * FROM movies WHERE (title == :title and releaseYear == :releaseYear)")
    fun isMovieInDb(title: String, releaseYear: String): Array<Movie>

}