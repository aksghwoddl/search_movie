package com.lee.searchmovie.data.model.remote

import androidx.annotation.Keep

/**
 * 검색 요청에 대한 응답
 * **/
@Keep
data class MovieResultDTO(
    val display: Int,
    val items: ArrayList<MovieDTO>,
    val lastBuildDate: String,
    val start: Int,
    val total: Int
)