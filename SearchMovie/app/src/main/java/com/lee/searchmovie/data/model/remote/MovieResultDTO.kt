package com.lee.searchmovie.data.model.remote

/**
 * 검색 요청에 대한 응답
 * **/
data class MovieResultDTO(
    val display: Int,
    val items: ArrayList<MovieDTO>,
    val lastBuildDate: String,
    val start: Int,
    val total: Int
)