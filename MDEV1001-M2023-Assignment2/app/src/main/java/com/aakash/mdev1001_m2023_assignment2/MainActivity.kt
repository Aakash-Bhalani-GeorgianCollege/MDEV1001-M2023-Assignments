package com.aakash.mdev1001_m2023_assignment2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.aakash.mdev1001_m2023_assignment2.adapter.MovieAdapter
import com.aakash.mdev1001_m2023_assignment2.databinding.ActivityMainBinding
import com.aakash.mdev1001_m2023_assignment2.db.MovieDatabase
import com.aakash.mdev1001_m2023_assignment2.entity.Movie
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMainBinding
    lateinit var movieAdapter: MovieAdapter
    var savedMovies = ArrayList<Movie>()
    lateinit var database: MovieDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()

        mBinding.btnAddMovie.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddEditMovieActivity::class.java))
        }

    }

    private fun initView() {
        mBinding.rvMovie.layoutManager = GridLayoutManager(this, 2)


        database = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "movie_database"
        ).build()

        movieAdapter = MovieAdapter(this, movieList = savedMovies, database)

    }


    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO) {

            savedMovies = database.movieDao().getAllMovies() as ArrayList<Movie>

            if (savedMovies.isNullOrEmpty()) {
                val movies = parseJsonFileToMovies()
                database.movieDao().insertMovies(movies)
                savedMovies = database.movieDao().getAllMovies() as ArrayList<Movie>
                withContext(Dispatchers.Main) {
                    movieAdapter = MovieAdapter(this@MainActivity, savedMovies,database)
                    mBinding.rvMovie.adapter = movieAdapter
                }
            } else {
                withContext(Dispatchers.Main) {
                    movieAdapter = MovieAdapter(this@MainActivity, savedMovies,database)
                    mBinding.rvMovie.adapter = movieAdapter
                }
            }
        }


    }

    private fun parseJsonFileToMovies(): List<Movie> {
        val json: String = try {
            val inputStream: InputStream = this.assets.open("json/movies.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.defaultCharset())
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }

        val gson = Gson()
        return gson.fromJson(json, Array<Movie>::class.java).toList()
    }
}