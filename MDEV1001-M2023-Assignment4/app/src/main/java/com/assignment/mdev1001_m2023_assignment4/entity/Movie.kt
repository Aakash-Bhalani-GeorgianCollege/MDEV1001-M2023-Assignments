package com.assignment.mdev1001_m2023_assignment4.entity

data class Movie(
    var title: String,
    var studio: String,
    var genres: List<String>,
    var directors: List<String>,
    var writers: List<String>,
    var actors: List<String>,
    var year: Int,
    var length: Int,
    var shortDescription: String,
    var mpaRating: String,
    var criticsRating: Double,
    var movieThumbnail: String
) {
    constructor() : this(
        "", "", emptyList(), emptyList(), emptyList(), emptyList(),
        0, 0, "", "", 0.0, ""
    )
}

