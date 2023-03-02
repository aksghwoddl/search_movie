package com.lee.searchmovie.common

/**
 * Network 통신과 관련된 const들을 모아놓은 class
 * **/
class NetworkConst {
    companion object{
        const val BASE_URL = "https://openapi.naver.com/"
        const val SEARCH_MOVIE_URL = "https://openapi.naver.com/v1/search/movie.json"
        const val NAVER_CLIENT_ID = "X-Naver-Client-Id"
        const val NAVER_CLIENT_SECERET_KEY = "X-Naver-Client-Secret"
        const val QUERY = "query"
        const val START = "start"
    }
}