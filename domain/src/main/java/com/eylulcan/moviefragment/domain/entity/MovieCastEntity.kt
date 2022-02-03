package com.eylulcan.moviefragment.domain.entity

data class MovieCastEntity(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String,
    val castId: Int,
    val character: String,
    val creditId: String,
    val order: Int,
)