package com.aakash.mdev1001_m2023_assignment2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aakash.mdev1001_m2023_assignment2.entity.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Delete
    suspend fun deleteMovies(movie: Movie)

    @Update
    suspend fun updateMovies(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movies: Movie)

}