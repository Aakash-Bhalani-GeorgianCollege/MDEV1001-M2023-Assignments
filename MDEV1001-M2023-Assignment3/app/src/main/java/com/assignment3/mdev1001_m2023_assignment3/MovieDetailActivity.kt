package com.assignment3.mdev1001_m2023_assignment3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.assignment3.mdev1001_m2023_assignment3.databinding.ActivityMainBinding
import com.assignment3.mdev1001_m2023_assignment3.databinding.ActivityMovieDetailBinding
import com.assignment3.mdev1001_m2023_assignment3.model.Movie
import com.bumptech.glide.Glide
import com.google.gson.Gson

class MovieDetailActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityMovieDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_movie_detail)
        initView()
    }

    private fun initView() {
        val gson= Gson()
        val movieObj = gson.fromJson<Movie>(intent.getStringExtra("MOVIEOBJ"),Movie::class.java)
        Glide.with(this).load(movieObj.Poster).into(mBinding.imgPoster)
        mBinding.txtMovieName.text = movieObj.Title
        mBinding.txtMovieYear.text = movieObj.Year
        mBinding.txtMoviePlot.text = movieObj.Plot
        mBinding.txtActorsVal.text = movieObj.Actors
        mBinding.txtGenreVal.text = movieObj.Genre
        mBinding.txtReleaseDateVal.text = movieObj.Released
        mBinding.txtIMDBRatingVal.text = movieObj.imdbRating
        mBinding.txtCountryVal.text = movieObj.Country
        mBinding.txtLanguageVal.text = movieObj.Language

        mBinding.imgBack.setOnClickListener {
            finish()
        }
     }
}