package com.scores.mymovieapp.utilities

import com.google.android.material.snackbar.Snackbar
import com.scores.mymovieapp.R
import com.scores.mymovieapp.activities.MainActivity
import com.scores.mymovieapp.dbUtils.Movie
import com.scores.mymovieapp.dbUtils.MovieDao
import com.scores.mymovieapp.dbUtils.MoviesDatabase
import com.scores.mymovieapp.interfaces.INavigateToNextScreen
import com.scores.mymovieapp.interfaces.IcheckDbState
import com.scores.mymovieapp.objects.MoviesListResponseObject
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers


class DbManager {

    companion object{

        var dao: MovieDao? = null
        var db: MoviesDatabase? = null

        fun updateDB(data: MoviesListResponseObject) {
            try {
                db = MoviesDatabase.getDataBase()
                dao = db?.movieDao()
                Completable.fromRunnable {
                    data.data.forEach {
                        db?.movieDao()?.insertMovie(it)
                    }
                    //                Log.d("IDAN", "startProcess: " + "list is from network")
                }
                    .subscribeOn(Schedulers.io())
                    .subscribe()

                val sortedList = data.data.toCollection(ArrayList())
                sortedList.sortWith(Comparator { movieA, movieB->
                    movieA.getYear() - movieB.getYear()
                })
                Utils.setMovieList(sortedList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun addMovieToDb(
            data: Movie,
            activity: MainActivity
        ) {
            try {
                db = MoviesDatabase.getDataBase()
                dao = db?.movieDao()
                Completable.fromRunnable {
                    if (db?.movieDao()?.isMovieInDb(data.title, data.releaseYear)!!.isEmpty()) {
                        db?.movieDao()?.insertMovie(data)
                        activity.runOnUiThread {
                            val sortedList = Utils.getMoviesList()
                            sortedList?.add(data)
                            sortedList?.sortWith(Comparator { movieA, movieB ->
                                movieA.getYear() - movieB.getYear()
                            })
                            Utils.setMovieList(sortedList!!)
                            (activity as INavigateToNextScreen).refreshPage()

                        }
                    } else {
                        activity.runOnUiThread {
                            Snackbar.make(activity.getRootView(),
                                activity.resources.getString(R.string.movie_in_db),
                                Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun startProcess(pageRef: IcheckDbState) {
            try {
                Completable.fromAction {
                    db = MoviesDatabase.getDataBase()
                    dao = db?.movieDao()
                    val list = db?.movieDao()?.getAllMovies()
                    var retVal = false
                    if (list != null && list.isNotEmpty()) {
                        retVal = true
                        val sortedList = list.toCollection(ArrayList())
                        sortedList.sortWith(Comparator { movieA, movieB->
                            movieA.getYear() - movieB.getYear()
                        })
                        Utils.setMovieList(sortedList)
    //                    Log.d("IDAN", "startProcess: " + "list is from db")
                    }
                    Thread.sleep(1500)// for the splash effect of loading
                    pageRef.updateListAccordingToDB(retVal)
                }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
