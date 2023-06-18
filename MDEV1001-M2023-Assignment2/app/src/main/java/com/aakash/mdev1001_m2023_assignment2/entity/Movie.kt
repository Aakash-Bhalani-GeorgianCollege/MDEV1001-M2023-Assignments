package com.aakash.mdev1001_m2023_assignment2.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val movieID: Int=0,
    var title: String,
    var studio: String,
    var genres: String,
    var directors: String,
    var writers: String,
    var actors: String,
    var year: Int,
    var length: Int,
    var shortDescription: String,
    var mpaRating: String,
    var criticsRating: Double,
    var movieThumbnail: String = ""
)