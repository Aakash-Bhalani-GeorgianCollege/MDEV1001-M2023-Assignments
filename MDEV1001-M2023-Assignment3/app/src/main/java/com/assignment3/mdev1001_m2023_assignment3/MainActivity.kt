package com.assignment3.mdev1001_m2023_assignment3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assignment3.mdev1001_m2023_assignment3.adapter.MovieAdapter
import com.assignment3.mdev1001_m2023_assignment3.api.ApiService
import com.assignment3.mdev1001_m2023_assignment3.api.RetrofitClient
import com.assignment3.mdev1001_m2023_assignment3.databinding.ActivityMainBinding
import com.assignment3.mdev1001_m2023_assignment3.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var movieAdapter: MovieAdapter
    var movieList = arrayListOf<Movie>()
    lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()
    }

    private fun initView() {
        mBinding.imgSearch.setOnClickListener {
            movieList.clear()
            getMovieList(mBinding.edtSearchView.text.toString())
        }
    }

    private fun getMovieList(movieName:String) {
        GlobalScope.launch(Dispatchers.Main) {
            val moviesApi = RetrofitClient.getInstance().create(ApiService::class.java)

            val response = moviesApi.getMovies(movieName,"6246329e")
            withContext(Dispatchers.Main) {
                movieList.add(response.body()!!)
                movieAdapter = MovieAdapter(this@MainActivity, movieList)
                mBinding.rvMovie.adapter = movieAdapter
            }
        }
    }
}