package com.lee.searchmovie.data.model.remote

import androidx.annotation.Keep

/**
 * 검색한 영화의 정보
 * **/
@Keep
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