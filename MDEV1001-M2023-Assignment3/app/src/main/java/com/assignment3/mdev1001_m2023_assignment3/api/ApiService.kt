package com.assignment3.mdev1001_m2023_assignment3.api

import com.assignment3.mdev1001_m2023_assignment3.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{
    @GET("/")
    suspend fun getMovies(@Query("t") movieName: String,@Query("apikey") apiKey:String): Response<Movie>
}