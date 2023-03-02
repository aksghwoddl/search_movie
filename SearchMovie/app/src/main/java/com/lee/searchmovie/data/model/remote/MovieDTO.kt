package com.lee.searchmovie.data.model.remote

/**
 * 검색한 영화의 정보
 * **/
data class MovieDTO(
    val actor: String,
    val director: String,
    val image: String,
    val link: String,
    val pubDate: String,
    val subtitle: String,
    val title: String,
    val userRating: String
)